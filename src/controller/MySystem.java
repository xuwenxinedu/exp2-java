/**
 * @author:xuwenxin
 */
package controller;

import stationary.Program;
import table.PCBTable;
import table.RCBTable;

import java.util.*;

/**
 * a part of operating system,
 * only can manage PCB, RCB and dispatch process
 */
public class MySystem {
//    private final long MAX_MEMORY = 10000;
    private long remainMemory = 10000;
    public List<PCBTable> ready;
    public List<PCBTable> blocked;
    public Map<Long, RCBTable> resources;
    private PCBTable running;
    /**
     * Create PID and RID with static variable
     */
    static long INCREMENT_PID = -1;
    static long INCREMENT_RID = -1;

    /**
     * Initiate variables and initiate system
     */
    public MySystem() {
        //because we often delete the first node from the queue,
        //we use LinkedList, it is more efficient
        ready = new LinkedList<>();
        blocked = new LinkedList<>();
        resources = new HashMap<>();
        this.running = null;
        this.initiateSystem();
    }

    /***
     * Find next running process
     * @return the index of next running process
     */
    public int indexOfNextRunning() {
        int index = 0;
        int maxPriority = -1;
        int readySize = ready.size();
        for (int i = 0; i < readySize; ++i) {
            //only can '>'
            // '>=' is wrong, because time first
            if (ready.get(i).getPriority() > maxPriority) {
                index = i;
                maxPriority = ready.get(i).getPriority();
            }
        }
        return index;
    }

    /**
     * change running's state to see if it still has the
     * highest priority
     */
    public void runningToReady() {
        this.running.setStateReady(this.ready);
        this.ready.add(this.running);
    }

    /**
     * scheduler method, dispatch process
     */
    public void scheduler() {
        //when we initiate the system, there is no running process
        //when we block the running process
        if (null == this.running) {
            int index = this.indexOfNextRunning();
            this.running = this.ready.get(index);
            this.ready.remove(index);
            this.running.setStateRunning();
            return;
        }
        if (null == this.ready) {
            Operation.createProcess(MySystem.getNextPID(), this);
            this.scheduler();
            return;
        }
        if (this.ready.isEmpty()) {
            Operation.createProcess(MySystem.getNextPID(), this);
            this.scheduler();
            return;
        }
        this.runningToReady();
        //we can use heap or priority queue to make the operation simpler
        //but, I have written so many codes.
        //Therefore, I don't want to change my code now.
        int index = this.indexOfNextRunning();
        //when the max priority smaller than the running's priority, return
        if (this.ready.get(index).getPriority() <= this.running.getPriority()) {
            this.ready.remove(this.running);
            return;
        }
        this.running = this.ready.get(index);
        this.ready.remove(index);

        //if there are indeed enough resource
        if (Operation.requestResource(this.running, this)) {
            this.running.setStateRunning();
        } else {
            this.blocked.add(this.running);
            this.timeOutDispatch();
        }
    }

    /**
     * Intimate timeout dispatch
     */
    public void timeOutDispatch() {
        //timeout -> ready
        PCBTable table = this.running;

        int index = this.indexOfNextRunning();
        this.running = this.ready.get(index);
        this.ready.remove(index);

        table.setStateReady(this.ready);
        this.ready.add(table);

        //if there are indeed enough resource
        if (Operation.requestResource(this.running, this)) {
            this.running.setStateRunning();
        } else {
            this.blocked.add(this.running);
            this.running.setStateBlocked(this.blocked);
            this.running = null;
            this.scheduler();
        }

    }


    /**
     * useless,when you kill a process, you'll get the memory back
     * @param mem
     */
    public void releaseMemory(long mem) {
        remainMemory += mem;
    }

    public boolean occupyMemory(long mem) {
        remainMemory -= mem;
        if (remainMemory < 0) {
            remainMemory += mem;
            return false;
        }
        return true;
    }

    public Map<Long, RCBTable> getResources() {
        return resources;
    }

    //delete pcb from ready queue
    public void removeProcessFromReadyQueue(PCBTable pcb) {
        ready.remove(pcb);
    }

    //delete pcb from blocked queue
    public void removeProcessFromBlockedQueue(PCBTable pcb) {
        ready.remove(pcb);
    }

    //release the resource
    public void releaseResource(long rid) {
        this.resources.get(rid).release();
    }

    //as its name
    public void removeProcessFromResourceQueue(Long resourceId, PCBTable pcb) {
        this.resources.get(resourceId).removeFromQueue(pcb);
    }

    /**
     *
     * @return the running process's PCB
     */
    public PCBTable getRunning() {
        return running;
    }

    /**
     * Initiate system, set some resource and useless process
     */
    public void initiateSystem() {
        //init process
        Operation.createProcessWithPriorityAndName(getNextPID(), "init", 0, this);
        //scheduler();
        //ten resources
        for (int i = 0; i < 5; ++i) {
            long tempRID = getNextRID();
            this.resources.put(tempRID, new RCBTable(tempRID, this));
        }
    }

    /**
     * generate next pid we will use
     * @return next pid
     */
    public static Long getNextPID() {
        INCREMENT_PID += 1;
        return INCREMENT_PID;
    }

    /**
     * generate next rid we will use
     * @return next rid
     */
    public static Long getNextRID() {
        INCREMENT_RID += 1;
        return INCREMENT_RID;
    }

    /**
     * show resources and their state
     */
    public void showResource() {
        System.out.println("RID\tState");
        for (Long rid : this.resources.keySet()) {
            RCBTable rcb = this.resources.get(rid);
            System.out.println(rcb.getRID() + "\t" + rcb.getStateString());
        }
    }

    /**
     * show the process' information in queue
     */
    public void showReadyQueue() {
        System.out.println("Below is the ready queue");
        System.out.println("PID\tProcess Name\tPriority");
        for (PCBTable table : this.ready) {
            System.out.println(table.getPID() + "\t" +
                    table.getName() + "\t" +
                    table.getPriority());
        }
    }

    /**
     * display blocked queue
     */
    public void showBlockedQueue() {
        System.out.println("Below is the blocked queue");
        System.out.println("PID\tProcess Name\tPriority");
        for (PCBTable table : this.blocked) {
            System.out.println(table.getPID() + "\t" +
                    table.getName() + "\t" +
                    table.getPriority());
        }
    }

    /**
     * show running process' information
     */
    public void showRunning() {
        System.out.println("PID\tProcess Name\tPriority");
        PCBTable table = this.running;
        System.out.println(table.getPID() + "\t" +
                table.getName() + "\t" +
                table.getPriority());
    }

    /**
     * make running = null and scheduler
     */
    public void blockRunning() {
        this.running = null;
        this.scheduler();
    }
}
