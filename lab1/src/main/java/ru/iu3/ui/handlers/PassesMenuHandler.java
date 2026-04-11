package ru.iu3.ui.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ru.iu3.entity.Pass;
import ru.iu3.entity.interfaces.Room;
import ru.iu3.service.interfaces.BookingService;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.ui.interfaces.MenuItem;

public class PassesMenuHandler {
    private static  String ERROR_MSG = "Произошла ошибка: ";

    private  Scanner scanner;
    private  PassService passService;
    private  BookingService bookingService;
    private  List<MenuItem> items = new ArrayList<>();

    public PassesMenuHandler(Scanner scanner, PassService passService, BookingService bookingService) {
        this.scanner = scanner;
        this.passService = passService;
        this.bookingService = bookingService;
        items.add(new ShowPassesItem());
        items.add(new IssuePassItem());
        items.add(new DeactivatePassItem());
        items.add(new ShowRoomsForPassItem());
        items.add(new BackItem());
    }

    public void run() {
        boolean inPassesMenu = true;
        while (inPassesMenu) {
            for (MenuItem item : items) {
                System.out.println(item.getKey() + ". " + item.getLabel());
            }
            try {
                System.out.print("Выберите пункт меню: ");
                int choice = Integer.parseInt(scanner.nextLine());
                MenuItem selected = findItem(choice);
                if (selected != null) {
                    inPassesMenu = selected.execute();
                } else {
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println(ERROR_MSG + e.getMessage());
            }
        }
    }

    public void showPasses() {
        System.out.println("Cписок пропусков:");
        for (Pass pass : passService.getAllPasses()) {
            System.out.println(
                    "ID: " + pass.getId() + ", Владелец: " + pass.getHolderName() + ", Активен: " + pass.isActive());
        }
    }

    private MenuItem findItem(int key) {
        for (MenuItem item : items) {
            if (item.getKey() == key) {
                return item;
            }
        }
        return null;
    }

    private class ShowPassesItem implements MenuItem {
        @Override
        public int getKey() { return 1; }
        @Override
        public String getLabel() { return "Список пропусков"; }
        @Override
        public boolean execute() { showPasses(); return true; }
    }

    private class IssuePassItem implements MenuItem {
        @Override
        public int getKey() { return 2; }
        @Override
        public String getLabel() { return "Добавить пропуск"; }
        @Override
        public boolean execute() {
            showPasses();
            System.out.println("Введите ID пропуска:");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("Введите имя владельца пропуска:");
            String holderName = scanner.nextLine();
            passService.issuePass(id, holderName);
            return true;
        }
    }

    private class DeactivatePassItem implements MenuItem {
        @Override
        public int getKey() { return 3; }
        @Override
        public String getLabel() { return "Удалить пропуск"; }
        @Override
        public boolean execute() {
            showPasses();
            System.out.println("Введите ID пропуска для деактивации:");
            int id = Integer.parseInt(scanner.nextLine());
            passService.deactivatePass(id);
            return true;
        }
    }

    private class ShowRoomsForPassItem implements MenuItem {
        @Override
        public int getKey() { return 4; }
        @Override
        public String getLabel() { return "Помещения для пропуска"; }
        @Override
        public boolean execute() {
            System.out.println("Введите ID пропуска:");
            int passId = Integer.parseInt(scanner.nextLine());
            List<Room> rooms = bookingService.getRoomsForPass(passId);
            if (rooms.isEmpty()) {
                System.out.println("Для этого пропуска нет активных бронирований.");
                return true;
            }
            System.out.println("Помещения, к которым есть доступ по этому пропуску:");
            for (Room room : rooms) {
                System.out.println(room.getId() + " | " + room.getType().getDisplayName() + " | " + room.getName());
            }
            return true;
        }
    }

    private class BackItem implements MenuItem {
        @Override
        public int getKey() { return 0; }
        @Override
        public String getLabel() { return "Назад"; }
        @Override
        public boolean execute() { return false; }
    }
}
