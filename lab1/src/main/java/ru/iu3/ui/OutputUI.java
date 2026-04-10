package ru.iu3.ui;

import ru.iu3.enums.OptionsMenuEnum;
import ru.iu3.ui.constants.UiConstants;

public class OutputUI {

    public void showWelcome() {
        System.out.println(UiConstants.WELCOME_MESSAGE);
    }

    public void showOptions() {
        for (OptionsMenuEnum option : OptionsMenuEnum.values()) {
            System.out.println(option.getKey() + ". " + option.getLabel());
        }
    }

    public void showPrompt() {
        System.out.print(UiConstants.PROMPT_MESSAGE);
    }

    public void showChoice(String choice) {
        System.out.println("Вы выбрали '" + choice + "'");
    }

    public void showInvalidChoice() {
        System.out.println(UiConstants.INVALID_CHOICE);
    }

    public void showGoodbye() {
        System.out.println(UiConstants.GOODBYE_MESSAGE);
    }

    public void showError(String message) {
        System.out.println(UiConstants.ERROR_PREFIX + message);
    }
}
