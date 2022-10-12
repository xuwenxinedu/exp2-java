package table;

import state.ProcessCode;
import state.ProcessState;
import stationary.Program;

import java.util.LinkedList;
import java.util.List;

public class PCBTable{
    private long PID;
    private long memory;
    private List<Long> resourceIds;//all process need
    private List<Long> occupiedResourceIds;//the process has occupied
    private ProcessState state;
    private PCBTable father;
    private List<PCBTable> sons;
    private int priority;

    public String name; //I really don't know why the variable name exists

    public PCBTable(long PID, Program program) {
        this.PID = PID;
        this.priority = program.getPriority();
        this.resourceIds = program.getResourceIds();
        this.memory = program.getMemorySize();
        this.state = new ProcessState();
        this.state.setCode(ProcessCode.NEW);
        this.occupiedResourceIds = new LinkedList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addResource(long rid) {
        this.resourceIds.add(rid);
    }

    public void occupyResource(long rid) {
        this.occupiedResourceIds.add(rid);
    }

    public List<Long> getResourceIds() {
        return resourceIds;
    }

    public List<Long> getOccupiedResourceIds() {
        return occupiedResourceIds;
    }

    public void setStateRunning() {
        this.state.setCode(ProcessCode.RUNNING);
        this.state.setQueue(null);
    }

    public ProcessCode getStateCode() {
        return this.state.getCode();
    }

    public void setStateExit() {
        this.state.setCode(ProcessCode.EXIT);
    }

    public void setStateBlocked(List<PCBTable> q) {
        this.state.setQueue(q);
        this.state.setCode(ProcessCode.BLOCKED);
    }

    public void setStateReady(List<PCBTable> q) {
        this.state.setQueue(q);
        this.state.setCode(ProcessCode.READY);
    }

    public void releaseResource(Long rid) {
        this.resourceIds.remove(rid);
        this.occupiedResourceIds.remove(rid);
    }

    public long getMemory() {
        return memory;
    }

    public int getPriority() {
        return priority;
    }

    public long getPID() {
        return PID;
    }

    public String getName() {
        return this.name;
    }
}
