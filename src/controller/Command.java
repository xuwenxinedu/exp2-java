package controller;

import table.PCBTable;
import table.RCBTable;

/**
 * command open to user
 */
public class Command {

    private MySystem system;

    public Command(MySystem system) {
        this.system = system;
    }

    /**
     * only the running one can request by your order
     * the running one request rid
     * @param rid the rid of your rcb(what you want)
     */
    public void request(long rid) {
        if (this.system.getRunning().getName().equals("init")) {
            System.out.println("No running process now, please create a new one.");
            return;
        }
        RCBTable rcbTable = this.system.resources.getOrDefault(rid, null);
        if (null == rcbTable) {
            System.out.println("We don't have " + rid + " resource");
            System.out.println("If you want to see what we have please type in \"show resource\"");
            return;
        }
        system.getRunning().addResource(rid);
        if (!Operation.requestResource(system.getRunning(), system)) {
            system.scheduler();
        }
    }

    /**
     * create a new process with priority and name
     * @param priority the priority of the new process
     * @param name the name of the process
     */
    public void create(String name, int priority) {
        Operation.createProcessWithPriorityAndName(MySystem.getNextPID(), name, priority, this.system);
    }

    /**
     * make the running process release the resource which id is rid
     * @param rid the rid of rcb
     */
    public void release(long rid) {
        Operation.releaseResourceFromRunningByRID(system, rid);
    }

    /**
     * intimate the timeout by your order
     */
    public void timeout() {
        this.system.timeOutDispatch();
    }

    /**
     * kill a process by its name
     * @param name the process' name
     */
    public void kill(String name) {
        //u really can add a map <name, pcb>, but I don't want to change my code either.
        if (system.getRunning().getName().equals(name)) {
            Operation.deleteProcess(system.getRunning(), system);
        }
        PCBTable pcb = null;
        boolean temp = false;
        for (PCBTable table : system.ready) {
            if (table.getName().equals(name)) {
                pcb = table;
                temp = true;
                break;
            }
        }
        if (temp) {
            Operation.deleteProcess(pcb, system);
            return;
        }
        for (PCBTable table : system.blocked) {
            if (table.getName().equals(name)) {
                pcb = table;
                break;
            }
        }
        Operation.deleteProcess(pcb, system);
    }


    /**
     * show system's resource and their state
     */
    public void showResource() {
        this.system.showResource();
    }

    /**
     * show system's ready queue
     */
    public void showReadyQueue() {
        this.system.showReadyQueue();
    }

    /**
     * show system's blocked queue
     */
    public void showBlockedQueue() {
        this.system.showBlockedQueue();
    }

    /**
     * show running
     */
    public void showRunning() {
        this.system.showRunning();
    }

    public void afterAction() {
        String name = this.system.getRunning().getName();
        System.out.println("The running process: " + name);
    }
}
