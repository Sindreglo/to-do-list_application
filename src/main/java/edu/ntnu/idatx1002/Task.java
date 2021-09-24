package edu.ntnu.idatx1002;

import edu.ntnu.idatx1002.priority.LowPriority;
import edu.ntnu.idatx1002.priority.Priority;
import edu.ntnu.idatx1002.status.OpenStatus;
import edu.ntnu.idatx1002.status.Status;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a Task. All task consists of a name, description and a creation date.
 * Category, start date and priority are optional attributes which further improves
 * sorting of tasks.
 *
 * @author KOHORT 1.7
 * @version 2020-04-07
 */
public class Task implements Serializable {
    private String taskName; // name of a task.
    private String taskDescription; // description of a task.
    private String category; // The category a task belongs to.
    private LocalDate creationDate; // The date a task was created.
    private LocalDate startDate; // Starting date of a task.
    private Status status; // Checks if the task is still active, or completed.
    private Priority priority; // The priority of a task.

    public Task(String taskName, String taskDescription, String category, LocalDate startDate) throws IllegalArgumentException{
        if (taskName == null||taskName.isEmpty()){
            throw new IllegalArgumentException("Task name can not be null");
        }else{
            this.taskName = taskName;
        }

        this.taskDescription = taskDescription;
        this.category = category;
        this.creationDate = LocalDate.now();
        this.startDate = startDate;
        this.status = new OpenStatus();
        this.priority = new LowPriority();
    }
    public Task(String taskName, String taskDescription, String category) throws IllegalArgumentException{
        if (taskName == null||taskName.isEmpty()){
            throw new IllegalArgumentException("Task name can not be null");
        }else{
            this.taskName = taskName;
        }

        this.taskDescription = taskDescription;
        this.category = category;
        this.creationDate = LocalDate.now();
        this.startDate = this.creationDate;
        this.status = new OpenStatus();
        this.priority = new LowPriority();
    }
    public Task(String taskName, String taskDescription, String category, LocalDate startDate, Priority priority) throws IllegalArgumentException{
        if (taskName == null||taskName.isEmpty()){
            throw new IllegalArgumentException("Task name can not be null");
        }else{
            this.taskName = taskName;
        }

        this.taskDescription = taskDescription;
        this.category = category;
        this.creationDate = LocalDate.now();
        this.startDate = startDate;
        this.status = new OpenStatus();
        this.priority = priority;
    }
    public Task(String taskName, String taskDescription, String category, Priority priority) throws IllegalArgumentException{
        if (taskName == null||taskName.isEmpty()){
            throw new IllegalArgumentException("Task name can not be null");
        }else{
            this.taskName = taskName;
        }

        this.taskDescription = taskDescription;
        this.category = category;
        this.creationDate = LocalDate.now();
        this.startDate = this.creationDate;
        this.status = new OpenStatus();
        this.priority = priority;
    }

    /**
     * When editing a task, taskName, taskDescription, category, startDate and priority will be overwritten.
     * If a parameters has not been edited, it will be overwritten variable identical to it's older version.
     * If a parameter has been edited, it will be overwritten with a new variable.
     *
     * @param taskName String new task name
     * @param taskDescription String new task description
     * @param category String new category
     * @param startDate LocalDate new start date
     * @param priority Priority new priority
     * @throws IllegalArgumentException Throws illegal argument exception if the task does not contain a name.
     */
    public void editTask(String taskName, String taskDescription, String category, LocalDate startDate, Priority priority) throws IllegalArgumentException {
        if (taskName == null||taskName.isEmpty()){
            throw new IllegalArgumentException("Task name can not be null");
        }else{
            this.taskName = taskName;
        }

        this.taskDescription = taskDescription;
        this.category = category;
        this.startDate = startDate;
        this.priority = priority;
    }

    public String getTaskName() {
        return taskName;
    }

    /**
     * @return String task description in one line
     */
    public String getTaskDescription() {
        String description = taskDescription.replace("\n", " ");
       return description;
    }
    public String getTaskDescriptionFull() {
        return taskDescription;
    }
    public String getCategory() {
        return category;
    }
    public Priority getPriority() {
        return priority;
    }
    public Status getStatus() {
        return status;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setStartDate(LocalDate date){
        this.startDate = date;
    }
    /**
     * @return String taks name max length 15
     */
    public String shortTaskName(){
        String originalTaskName = taskName;
        String shortTaskName = (originalTaskName.length() > 15)? originalTaskName.substring(0, 15) + "...": originalTaskName;
        return shortTaskName;
    }
    public String shortTaskDescription() {
        String originalDescription = taskDescription;
        String shortDescription = (originalDescription.length() > 10)? originalDescription.substring(0, 10) + "...": originalDescription;
        return "test" + shortDescription;
    }


    /**
     * ToString method return a string containing all information about the task
     *
     * @return informational string of the task.
     */
    @Override
    public String toString(){
        return "Task Name: " + shortTaskName() + "     Description: " + shortTaskDescription()
                + "     Category: " + category + "     Priority: " + priority.getPriorityName()
                + "     Start date: " + startDate + "     Status: " + status.toString();
    }
}
