package edu.ntnu.idatx1002.status;

import java.time.LocalDate;

/**
 * Represents closed status. If a task is closed,
 * it is marked completed. This gives the task a closed date.
 *
 * @author KOHORT 1.7
 * @version 2021-04-07
 */
public class ClosedStatus extends Status {
    private LocalDate dateClosed; // The date the status was closed.


    public ClosedStatus() {
        this.dateClosed = LocalDate.now();
    }

    /**
     * Returns the closed status of a task.
     *
     * @return false
     */
    @Override
    public Boolean getStatus() {
        return false;
    }

    /**
     * Returns the closed status date.
     *
     * @return closed status date.
     */
    public LocalDate getDateClosed() {
        return dateClosed;
    }

    /**
     * Returns "Closed, ".
     *
     * @return "Closed, "
     */
    @Override
    public String toString(){
        return "Finished: " + dateClosed;
    }
}
