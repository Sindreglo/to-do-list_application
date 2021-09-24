package edu.ntnu.idatx1002;

import edu.ntnu.idatx1002.status.ClosedStatus;
import edu.ntnu.idatx1002.status.OpenStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import edu.ntnu.idatx1002.priority.*;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    public List listFiller(){
        List testList = new List("a");
        testList.addTask(new Task("A", "A", "A", LocalDate.now(), new LowPriority()));
        testList.addTask(new Task("B", "B", "B", LocalDate.now(), new MediumPriority()));
        testList.addTask(new Task("C", "C", "C", LocalDate.now(), new HighPriority()));
        testList.addTask(new Task("D", "D", "D", LocalDate.now(), new MediumPriority()));
        testList.addTask(new Task("E", "E", "E", LocalDate.now()));
        return testList;
    }

    @Test
    @DisplayName("Throws IllegalArgumentException when list name is empty or null.")
    public void throwExceptionWhenListNameIsEmptyOrNull(){
        assertThrows(IllegalArgumentException.class, ()-> new List(""));
        assertThrows(IllegalArgumentException.class, ()-> new List(null));
    }
    @Test
    @DisplayName("Does not throw exception when list name is not empty or null.")
    public void doesNotThrowWhenListNameIsNotNullOrEmpty(){
        assertDoesNotThrow(()-> new List("Name"));
    }

    @Test
    @DisplayName("Does not throw exception when a task is successfully removed.")
    public void doesNotThrowWhenTaskRemovedSuccess(){
        List testList = new List("a");
        Task testTask = new Task("A", "B", "C", LocalDate.now());
        testList.addTask(testTask);
        assertDoesNotThrow(()-> testList.removeTask(testTask));
    }

    @Test
    @DisplayName("Throws exception when task did not get removed.")
    public void throwsWhenTaskRemovedUnsuccessfully(){
        List testList = new List("a");
        Task testTask = new Task("A", "B", "C", LocalDate.now());
        assertThrows(IllegalArgumentException.class, ()-> testList.removeTask(testTask));
    }

    @Test
    @DisplayName("Sort by medium priority, checks if all medium priority elements have been added to return list.")
    public void assertGetByPriorityMediumSuccess(){
        List testList = listFiller();
        ArrayList<Task> priorityTestList = new ArrayList<>();
        priorityTestList.add(testList.getTaskList().get(1));
        priorityTestList.add(testList.getTaskList().get(3));
        ArrayList<Task> sortList = testList.getTasksByPriority(new MediumPriority());
        assertEquals(priorityTestList.get(0), sortList.get(0));
        assertEquals(priorityTestList.get(1), sortList.get(1));
    }

    @Test
    @DisplayName("Sort by low priority, checks if all low priority elements have been added to return list.")
    public void assertGetByPriorityLowSuccess(){
        List testList = listFiller();
        ArrayList<Task> priorityTestList = new ArrayList<>();
        priorityTestList.add(testList.getTaskList().get(0));
        priorityTestList.add(testList.getTaskList().get(4));
        ArrayList<Task> sortList = testList.getTasksByPriority(new LowPriority());
        assertEquals(priorityTestList.get(0), sortList.get(0));
        assertEquals(priorityTestList.get(1), sortList.get(1));
    }

    @Test
    @DisplayName("Sort by high priority, checks if all high priority elements have been added to return list.")
    public void assertGetByPriorityHighSuccess(){
        List testList = listFiller();
        ArrayList<Task> priorityTestList = new ArrayList<>();
        priorityTestList.add(testList.getTaskList().get(2));
        ArrayList<Task> sortList = testList.getTasksByPriority(new HighPriority());
        assertEquals(priorityTestList.get(0), sortList.get(0));
    }

    @Test
    @DisplayName("Sort by category, checks if all elements in category have been added to return list.")
    public void assertGetByCategorySuccess(){
        List testList = listFiller();
        ArrayList<Task> priorityTestList = new ArrayList<>();
        priorityTestList.add(testList.getTaskList().get(0));
        ArrayList<Task> sortList = testList.getTasksByCategory("a");
        assertEquals(priorityTestList.get(0), sortList.get(0));
    }
    @Test
    @DisplayName("Check if all tasks for today and tasks that has expired and still are open are added to a list")
    public void assertGetTodaysTasks(){
        List testList = new List("Today");
        Task task1 = new Task("A", "A", "A", LocalDate.now(), new LowPriority());
        Task task2 = new Task("B", "B", "B", LocalDate.of(2021, 4, 10), new MediumPriority());
        Task task3 = new Task("C", "C", "C", LocalDate.of(2021, 4, 8), new HighPriority());
        task2.setStatus(new ClosedStatus());
        testList.addTask(task1);
        testList.addTask(task2);
        testList.addTask(task3);
        assertEquals(testList.getTodaysTasks().size(), 2);
    }
    @Test
    @DisplayName("Check if all upcoming tasks which are not closed are added to a list")
    public void assertGetUpcomingTasks(){
        List testList = new List("Upcoming");
        Task task1 = new Task("A", "A", "A", LocalDate.now(), new LowPriority());
        Task task2 = new Task("B", "B", "B", LocalDate.of(2028, 4, 10), new MediumPriority());
        Task task3 = new Task("C", "C", "C", LocalDate.of(2028, 4, 8), new HighPriority());
        task2.setStatus(new ClosedStatus());
        testList.addTask(task1);
        testList.addTask(task2);
        testList.addTask(task3);
        assertEquals(testList.getTodaysTasks().size(), 1);
    }
}