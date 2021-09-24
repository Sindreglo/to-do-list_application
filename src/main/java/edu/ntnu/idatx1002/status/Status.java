package edu.ntnu.idatx1002.status;

import java.io.Serializable;

/**
 * Represents the status of a task. The status can
 * either be open or closed, meaning the task is either
 * still active or marked done.
 *
 * @author KOHORT 1.7
 * @version 2021-04-07
 */
public abstract class Status implements Serializable {


    public Status() {}

    /**
     * Returns the status of a task, meaning it's
     * opened or closed.
     *
     * @return true or false
     */
    public abstract Boolean getStatus();

    /**
     * Returns "Open" or "Closed"
     *
     * @return String "Open" or "Closed"
     */
    public abstract String toString();
}
