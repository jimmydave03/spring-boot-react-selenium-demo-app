package com.example.tests;

import com.example.pages.TaskPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class TaskAppUITest extends BaseTest {

    // Test to verify that the home page loads successfully
    @Test
    void shouldLoadHomePage() {
        driver.get(baseUrl); // Navigate to the base URL
        assertTrue(driver.getPageSource().contains("Task Manager")); // Check if "Task Manager" text is present
    }

    // Test to verify that a task can be created successfully
    @Test
    void shouldCreateTask() throws InterruptedException {
        TaskPage page = new TaskPage(driver);
        page.open(baseUrl); // Open the task page

        page.enterTitle("Buy milk"); // Enter task title
        page.enterDescription("Go to store and buy milk"); // Enter task description
        page.clickCreate(); // Click the create button

        Thread.sleep(800); // Wait for the task to be added
        assertTrue(driver.getPageSource().contains("Buy milk")); // Verify the task is displayed on the page
    }

    // Test to verify that a task is not created when the title is empty
    @Test
    void shouldNotCreateTaskWhenTitleEmpty() throws InterruptedException {
        TaskPage page = new TaskPage(driver);
        page.open(baseUrl); // Open the task page

        int beforeCount = driver.findElements(By.xpath("//button[text()='Delete']")).size(); // Count tasks before creation
        page.enterTitle(""); // Leave the title empty
        page.enterDescription("This should not create" + System.currentTimeMillis()); // Enter a description
        page.clickCreate(); // Attempt to create the task

        Thread.sleep(800); // Wait for the operation to complete
        int afterCount = driver.findElements(By.xpath("//button[text()='Delete']")).size(); // Count tasks after creation
        assertEquals(beforeCount, afterCount, "Task count should not change when title is empty"); // Verify no new task is added
    }

    // Test to verify that a task can be marked as completed
    @Test
    void shouldMarkTaskCompleted() throws InterruptedException {
        TaskPage page = new TaskPage(driver);
        page.open(baseUrl); // Open the task page

        page.enterTitle("Test completion"); // Enter task title
        page.enterDescription("Complete this task"); // Enter task description
        page.clickCreate(); // Create the task

        Thread.sleep(1000); // Wait for the task to be added
        driver.findElement(By.xpath("//button[text()='Done']")).click(); // Click the "Done" button to mark as completed

        Thread.sleep(800); // Wait for the operation to complete
        assertTrue(driver.getPageSource().contains("Undo")); // Verify the "Undo" button is displayed
    }

    // Test to verify that a task can be deleted successfully
    @Test
    void shouldDeleteTask() throws InterruptedException {
        TaskPage page = new TaskPage(driver);
        page.open(baseUrl); // Open the task page

        int beforeCount = driver.findElements(By.xpath("//button[text()='Delete']")).size(); // Count tasks before deletion

        String taskTitle = "Task to delete" + System.currentTimeMillis(); // Generate a unique task title
        page.enterTitle(taskTitle); // Enter task title
        page.enterDescription("Delete this task"); // Enter task description
        page.clickCreate(); // Create the task

        // Wait until the new task is added
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> d.findElements(By.xpath("//button[text()='Delete']")).size() == beforeCount + 1);

        driver.findElement(By.xpath("//div[contains(.,'" + taskTitle + "')]//button[text()='Delete']")).click(); // Delete the task

        // Wait until the task is deleted
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> d.findElements(By.xpath("//button[text()='Delete']")).size() == beforeCount);

        assertEquals(beforeCount, driver.findElements(By.xpath("//button[text()='Delete']")).size()); // Verify the task count is back to the original
    }

    // Test to verify that multiple tasks can be added and deleted in a single test case
    @Test
    void shouldAddAndDeleteMultipleTasks() throws InterruptedException {
        TaskPage page = new TaskPage(driver);
        page.open(baseUrl); // Open the task page

        // Define task titles and descriptions
        String[] taskTitles = {"Task 1", "Task 2", "Task 3"};
        String[] taskDescriptions = {"Description 1", "Description 2", "Description 3"};

        // Add multiple tasks
        for (int i = 0; i < taskTitles.length; i++) {
            page.enterTitle(taskTitles[i]); // Enter task title
            page.enterDescription(taskDescriptions[i]); // Enter task description
            page.clickCreate(); // Create the task
            Thread.sleep(500); // Wait for the task to be added
            assertTrue(driver.getPageSource().contains(taskTitles[i])); // Verify the task is displayed on the page
        }

        // Delete all added tasks
        for (String taskTitle : taskTitles) {
            driver.findElement(By.xpath("//div[contains(.,'" + taskTitle + "')]//button[text()='Delete']")).click(); // Delete the task
            Thread.sleep(500); // Wait for the task to be deleted
            assertFalse(driver.getPageSource().contains(taskTitle)); // Verify the task is no longer displayed on the page
        }
    }
}