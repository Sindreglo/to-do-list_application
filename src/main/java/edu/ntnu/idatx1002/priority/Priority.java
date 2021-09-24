package edu.ntnu.idatx1002.priority;

import java.io.Serializable;

/**
 * Represents the priority of a task. The priority can
 * be low, medium or high, meaning the task is either
 * low prioritized, medium prioritized or high prioritized.
 *  @author KOHORT 1.7
 *  @version 2020-04-07
 */
public abstract class Priority implements Serializable {


    public Priority() {}

    public abstract String getPriorityName();
}
