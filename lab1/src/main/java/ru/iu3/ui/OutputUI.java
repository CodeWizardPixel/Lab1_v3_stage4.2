package ru.iu3.ui;

import java.util.List;

import ru.iu3.ui.interfaces.MenuItem;

public class OutputUI {

    public void showWelcome() {
        System.out.println("--- Добро пожаловать в чудный консольный коворкинг! ---");
    }

    public void showOptions(List<MenuItem> items) {
        for (MenuItem item : items) {
            System.out.println(item.getKey() + ". " + item.getLabel());
        }
    }

    public void showPrompt() {
        System.out.print("Выберите пункт меню: ");
    }

    public void showChoice(String choice) {
        System.out.println("Вы выбрали '" + choice + "'");
    }

    public void showInvalidChoice() {
        System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
    }

    public void showGoodbye() {
        System.out.println("Споки-ноки :3c ~");
    }

    public void showError(String message) {
        System.out.println("Произошла ошибка: " + message);
    }
}
