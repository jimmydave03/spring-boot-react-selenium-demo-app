package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TaskPage {
    private final WebDriver driver;

    public TaskPage(WebDriver driver) {
        this.driver = driver;
    }

    private By titleInput = By.cssSelector("[data-testid='task-title']");
    private By descInput = By.cssSelector("[data-testid='task-desc']");
    private By createBtn  = By.cssSelector("[data-testid='create-task-btn']");

    public void open(String url) {
        driver.get(url);
    }

    public void enterTitle(String title) {
        driver.findElement(titleInput).clear();
        driver.findElement(titleInput).sendKeys(title);
    }

    public void enterDescription(String description) {
        driver.findElement(descInput).clear();
        driver.findElement(descInput).sendKeys(description);
    }

    public void clickCreate() {
        driver.findElement(createBtn).click();
    }
}
