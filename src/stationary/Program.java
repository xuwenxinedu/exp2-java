package stationary;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * class program, we can use program to create a PCB(process)
 */

public class Program {
    public long memorySize;
    private List<Long> resourceIds;
    int priority;

    /**
     * assign the parameter that u may need
     */
    public void assignProgramNeed() {
        System.out.println("how much memory you need?");
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        memorySize = n;
        System.out.println("input the priority");
        priority = scanner.nextInt();
        System.out.println("how many kinds of resources you need?");
        n = scanner.nextInt();
        long temp;
        for (int i = 0; i < n; ++i) {
            temp = scanner.nextInt();
            resourceIds.add(temp);
        }
    }

    /**
     * constructor
     * Default all zero or empty list.
     * Be careful the arraylist rather than LinkedList.
     * The list.get() method id faster.
     * We often request the resource it needs.
     */
    public Program() {
        priority = 0;
        memorySize = 0;
        resourceIds = new ArrayList<>();
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }

    public List<Long> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<Long> resourceIds) {
        this.resourceIds = resourceIds;
    }
}
