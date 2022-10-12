package table;

import controller.MySystem;
import controller.Operation;
import state.ResourceCode;

import java.util.ArrayList;
import java.util.List;

public class RCBTable {
    private long RID;
    private ResourceCode state;
    private List<PCBTable> queue;
    /**
     * the rcb belongs to which system
     * to be honest, I must use the variable to implement wake up function
     */
    public MySystem system;

    public RCBTable(long id, MySystem system) {
        this.RID = id;
        this.state = ResourceCode.FREE;
        queue = new ArrayList<>();
        this.system = system;
    }

    public RCBTable(long id) {
        this.RID = id;
    }


    public long getRID() {
        return RID;
    }

    public void release() {
        if (this.queue.isEmpty()) {
            this.state = ResourceCode.FREE;
        } else {
            PCBTable pcb =  this.queue.get(0);
            pcb.occupyResource(RID);
            this.queue.remove(0);
            //wake up the first process
            Operation.wake(pcb, this.system);
        }
    }

    public void removeFromQueue(PCBTable pcb) {
        queue.remove(pcb);
    }

    public ResourceCode getState() {
        return state;
    }

    public void addPCBTableToQueue(PCBTable pcbTable) {
        this.queue.add(pcbTable);
    }

    public void setState(ResourceCode state) {
        this.state = state;
    }

    public String getStateString() {
        if (this.state == ResourceCode.BUSY) {
            return "busy";
        }
        return "free";
    }

}
