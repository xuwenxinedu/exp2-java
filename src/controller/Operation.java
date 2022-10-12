/**
 * @author:xuwenxin
 */
package controller;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.REUtil;
import state.ProcessCode;
import state.ResourceCode;
import stationary.Program;
import table.PCBTable;
import table.RCBTable;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * As its name, it is the operation between
 * system, PCB and RCB
 */
public class Operation {

    /**
     *Process request resources, pcb request rcb
     *only one thread can use the code in the same time
     */
    synchronized public static boolean requestResource(PCBTable pcb, RCBTable rcb) {
        if (ResourceCode.BUSY == rcb.getState()) {
            rcb.addPCBTableToQueue(pcb);
            return false;
        } else {
            rcb.setState(ResourceCode.BUSY);
            pcb.occupyResource(rcb.getRID());
            return true;
        }
    }

    /**
     * PCB request all resources it needs.
     * @param pcb which pcb requests
     * @param system which system does the pcb in
     * @return success or not
     */
    public static boolean requestResource(PCBTable pcb, MySystem system) {
        if (pcb.getResourceIds().isEmpty()) {
            return true;
        }

        Set<Long> tempSet = new HashSet<>();
        if (!pcb.getOccupiedResourceIds().isEmpty()) {
            for (long rid : pcb.getOccupiedResourceIds()) {
                tempSet.add(rid);
            }
        }

        //require its resources all it needs
        int l = pcb.getResourceIds().size();
        for (int i = 0; i < l; ++i) {
            long rid = pcb.getResourceIds().get(i);
            if (tempSet.contains(rid)) {
                continue;
            }
            // At first, I use -1 to judge whether there is this rid.
            //But now, I have judged it before code comes here.
            RCBTable rcb = system.resources.getOrDefault(rid, new RCBTable(-1));
//            if (-1 == rcb.getRID()) {
//                System.out.println("what you want: " + rid);
//                System.out.println("but we only have:");
//                for (Long integer : system.getResources().keySet()) {
//                    System.out.print(integer + " ");
//                }
//                System.out.println();
//                System.out.println("kill this process!");
//                deleteProcess(pcb, system);
//                break;
//            }
            /**
             * if not true: cannot fetch the resource, blocked and stop
             */
            if(!requestResource(pcb, rcb)) {
                if (ProcessCode.RUNNING == pcb.getStateCode()) {
                    system.blockRunning();
                    pcb.setStateBlocked(system.blocked);
                    system.blocked.add(pcb);
                    return false;
                }
                pcb.setStateBlocked(system.blocked);
                system.blocked.add(pcb);
                return false;
            }
        }
        system.scheduler();
        return true;
    }

    /**
     * create a process and assign it name and priority
     * @param id pic
     * @param name process's name
     * @param priority its priority
     * @param system which system it is in
     */
    public static void createProcessWithPriorityAndName(long id, String name, int priority , MySystem system) {
        Program program = new Program();
        program.setPriority(priority);
        PCBTable newProcess = new PCBTable(id, program);
        newProcess.setName(name);
        newProcess.setStateReady(system.ready);
        system.ready.add(newProcess);
        system.scheduler();
    }


    //create a process
    public static void createProcess(long id, MySystem system) {
        Program program = new Program();
        System.out.println("will u assign the memory or resources?(y/n)");
        Scanner scanner = new Scanner(System.in);
        String temp = scanner.nextLine();
        while (true) {
            if (temp.equals("y")) {
                program.assignProgramNeed();
                break;
            } else if (temp.equals("n")) {
                break;
            } else {
                System.out.println("please type 'y' or 'n'");
                temp = scanner.nextLine();
            }
        }
        PCBTable newProcess = null;
        if (system.occupyMemory(program.memorySize)) {
            // attention, the priority default value is 0, directly ready
            newProcess = new PCBTable(id, program);
            newProcess.setStateReady(system.ready);
            system.ready.add(newProcess);
            system.scheduler();
        } else {
            System.out.println("create process failed because space is not enough!");
            return;
        }

        //require its resources all it needs
//        boolean isGetAll = requestResource(newProcess, system);

        //after it got all resources all it needs
        //into ready queue
//        if (isGetAll) {
//            newProcess.setStateReady(system.ready);
//        }
    }


    /**
     * Kill a process
     * @param pcb the process's pcb
     * @param system
     */
    public static void deleteProcess(PCBTable pcb, MySystem system) {

        if (null == pcb) return;
        //if it is running
        if (pcb.getPID() == system.getRunning().getPID()) {
            system.blockRunning();
            system.scheduler();
        }
        //get the code to delete pcb from the corresponding queue
        ProcessCode code = pcb.getStateCode();
        if (ProcessCode.BLOCKED == code) {
            system.removeProcessFromBlockedQueue(pcb);
        } else if (ProcessCode.READY == code) {
            system.removeProcessFromReadyQueue(pcb);
        }

        //release the resources the process has occupied
        Set<Long> tempSet = new HashSet<>();
        for (Long occupiedResourceId : pcb.getOccupiedResourceIds()) {
            system.releaseResource(occupiedResourceId);
            tempSet.add(occupiedResourceId);
        }

        //delete the pcb from the queue which blocked it
        for (Long resourceId : pcb.getResourceIds()) {
            //if the id in set, pass it
            if (tempSet.contains(resourceId)) {
                continue;
            }
            //or, delete the pcb from resources' queue
            system.removeProcessFromResourceQueue(resourceId, pcb);
        }

        pcb.setStateExit();

        //release the memory that the process has occupied
        system.releaseMemory(pcb.getMemory());
        //for relaxing my mind
        system.scheduler();
    }

    /**
     * Release the resource
     * @param system
     * @param rid its rid
     */
    public static void releaseResourceFromRunningByRID(MySystem system, long rid) {
        boolean temp = false;
        for (long id : system.getRunning().getOccupiedResourceIds()) {
            //make sure the rcb is occupied by the running process
            if (id == rid) {
                temp = true;
                system.getRunning().releaseResource(rid);
                system.releaseResource(rid);
                break;
            }
        }
        if (!temp) {
            System.out.println("Running process doesn't occupy the resource u want to release");
        }
    }

    /**
     * Just wake up the blocked process
     * @param pcb its PCB
     * @param system where it is
     */
    public static void wake(PCBTable pcb, MySystem system) {
        if (requestResource(pcb, system)) {
            //get out from blocked
            system.blocked.remove(pcb);
            //into ready
            pcb.setStateReady(system.ready);
            system.ready.add(pcb);
            //dispatch
            system.scheduler();
        }
    }

}
