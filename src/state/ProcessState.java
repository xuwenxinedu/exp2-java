package state;

import table.PCBTable;

import java.util.*;

/**
 * process state contains code and queue
 */
public class ProcessState {
    private ProcessCode code;
    private List<PCBTable> queue;

    public ProcessState() {
        code = ProcessCode.NEW;
        queue = null;
    }

    public ProcessCode getCode() {
        return code;
    }

    public void setCode(ProcessCode code) {
        this.code = code;
    }

    public List<PCBTable> getQueue() {
        return queue;
    }

    public void setQueue(List<PCBTable> queue) {
        this.queue = queue;
    }
}
