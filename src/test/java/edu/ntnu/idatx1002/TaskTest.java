package edu.ntnu.idatx1002;

import edu.ntnu.idatx1002.priority.LowPriority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    @DisplayName("Throws IllegalArgumentException when a taskname is empty or null.")
    public void throwExceptionWhenTaskNameIsEmptyOrNull(){
        assertThrows(IllegalArgumentException.class, ()-> new Task("", "Test", "Test", LocalDate.now()));
        assertThrows(IllegalArgumentException.class, ()-> new Task(null, "Test", "Test", LocalDate.now()));
        assertThrows(IllegalArgumentException.class, ()-> new Task("", "Test", "Test"));
        assertThrows(IllegalArgumentException.class, ()-> new Task(null, "Test", "Test"));
        assertThrows(IllegalArgumentException.class, ()-> new Task("", "Test", "Test", LocalDate.now(), new LowPriority()));
        assertThrows(IllegalArgumentException.class, ()-> new Task(null, "Test", "Test", LocalDate.now(), new LowPriority()));
        assertThrows(IllegalArgumentException.class, ()-> new Task("", "Test", "Test", new LowPriority()));
        assertThrows(IllegalArgumentException.class, ()-> new Task(null, "Test", "Test", new LowPriority()));
    }
    @Test
    @DisplayName("Does not throw exception when a taskname is not empty or null.")
    public void doesNotThrowWhenTaskNameIsNotNullOrEmpty(){
        assertDoesNotThrow(()-> new Task("A", "B", "C", LocalDate.now()));
        assertDoesNotThrow(()-> new Task("A", "B", "C"));
    }
}