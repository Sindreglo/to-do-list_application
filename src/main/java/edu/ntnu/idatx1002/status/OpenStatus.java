package edu.ntnu.idatx1002.status;

/**
 *Represents a open status.
 *
 * @author KOHORT 1.7
 * @version 2021-04-07
 */
public class OpenStatus extends Status {

    public OpenStatus() { }


    /**
     * Returns the open status of a task.
     *
     * @return true
     */
    @Override
    public Boolean getStatus() {
        return true;
    }

    /**
     * Returns "Open".
     *
     * @return "Open"
     */
    @Override
    public String toString() {
        return "To do";
    }
}
