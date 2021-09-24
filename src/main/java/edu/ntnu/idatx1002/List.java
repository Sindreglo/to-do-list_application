package edu.ntnu.idatx1002;

import edu.ntnu.idatx1002.priority.Priority;
import edu.ntnu.idatx1002.status.OpenStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a list of Task's. A list can be sorted by priority or dates.
 * Lists containing only chosen categories or priorities can be returned to the user.
 * As an example may all tasks containing the category "Work" be returned as one list.
 *
 * @author KOHORT 1.7
 * @version 2021-04-07
 */
public class List implements Serializable {
    private String listName; // The name of a list.
    private ArrayList<Task> taskList; // The list of tasks.

    /**
     * Creates an instance of List. A list must have a name to be created.
     *
     * @param name The name of the list.
     * @throws IllegalArgumentException Throws illegal argument exception if the list does not have a name.
     */
    public List(String name)throws IllegalArgumentException{
        if (name == null||name.isEmpty()){
            throw new IllegalArgumentException("List name can not be null");
        }else{
            this.listName = name;
        }
        this.taskList = new ArrayList<>();
    }

    public String getListName() {
        return listName;
    }
    public void setListName(String listName) {
        this.listName = listName;
    }
    public ArrayList<Task> getTaskList() {
        return taskList;
    }
    public void addTask(Task newTask) {
        if (!taskList.contains(newTask)) {
            this.taskList.add(newTask);
        } // Skal vi ha en illegalargumentexception her???????
    }
    public void removeTask(Task oldTask)throws IllegalArgumentException{
        if(this.taskList.remove(oldTask)){
        }else{
            throw new IllegalArgumentException("Object not in list or could not be removed.");
        }
    }

    /**
     * Returns all tasks of a given priority.
     *
     * @param p priority High, medium or low.
     * @return List of all tasks which have not expired of the given priority
     */
    public ArrayList<Task> getTasksByPriority(Priority p){
        ArrayList<Task> priority = new ArrayList<>();
        for (Task t:taskList) {
            if(t.getPriority().getPriorityName().equals(p.getPriorityName())&&t.getStatus().getStatus()) {
                priority.add(t);
            }
        }
        return priority;
    }

    /**
     * Returns all tasks of a given category
     *
     * @param category name of a category
     * @return List of all tasks which have not expired of a given category
     */
    public ArrayList<Task> getTasksByCategory(String category){
        ArrayList<Task> tasksByCategory = new ArrayList<>();
        for (Task t:taskList){
            if (t.getCategory().equalsIgnoreCase(category)&&t.getStatus().getStatus()){
                tasksByCategory.add(t);
            }
        }
        return tasksByCategory;
    }
    public ArrayList<Task> getTodaysTasks(){
        ArrayList<Task> todaysTasks = new ArrayList<>();
        for (Task t : taskList){
            if ((t.getStartDate().equals(LocalDate.now())||((t.getStartDate().isBefore(LocalDate.now())))&&!(t.getStatus().getStatus()))){
                todaysTasks.add(t);
            }
        }
        return todaysTasks;
    }
    public ArrayList<Task> getUpcomingTasks(){
        ArrayList<Task> upcomingTasks = new ArrayList<>();
        for (Task t : taskList){
            if (t.getStartDate().isAfter(LocalDate.now())&&t.getStatus().getStatus()){
                upcomingTasks.add(t);
            }
        }
        return upcomingTasks;
    }
    public ArrayList<Task> getTaskByDay(LocalDate day){
        ArrayList<Task> tasksByDay = new ArrayList<>();
        for (Task t:taskList){
            if (t.getStartDate().equals(day)){
                tasksByDay.add(t);
            }
        }
        return tasksByDay;
    }

    @Override
    public String toString(){
        String s = "";
        for (Task t :taskList) {
            s += t.toString() +"\n";
        }
        return listName + "\n" + s;
    }
}
