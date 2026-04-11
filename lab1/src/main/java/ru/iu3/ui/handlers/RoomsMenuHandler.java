package ru.iu3.ui.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ru.iu3.entity.interfaces.Room;
import ru.iu3.enums.RoomEnum;
import ru.iu3.service.interfaces.RoomService;
import ru.iu3.ui.interfaces.MenuItem;

public class RoomsMenuHandler {
    private static  String ERROR_MSG = "Произошла ошибка: ";

    private  Scanner scanner;
    private  RoomService roomService;
    private  List<MenuItem> items = new ArrayList<>();

    public RoomsMenuHandler(Scanner scanner, RoomService roomService) {
        this.scanner = scanner;
        this.roomService = roomService;
        items.add(new ShowRoomsItem());
        items.add(new AddWorkplaceItem());
        items.add(new AddMeetingRoomItem());
        items.add(new DeleteRoomItem());
        items.add(new BackItem());
    }

    public void run() {
        boolean inRoomsMenu = true;
        while (inRoomsMenu) {
            for (MenuItem item : items) {
                System.out.println(item.getKey() + ". " + item.getLabel());
            }
            try {
                System.out.print("Выберите пункт меню: ");
                int choice = Integer.parseInt(scanner.nextLine());
                MenuItem selected = findItem(choice);
                if (selected != null) {
                    inRoomsMenu = selected.execute();
                } else {
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println(ERROR_MSG + e.getMessage());
            }
        }
    }

    public void showRooms() {
        System.out.println("Cписок комнат:");
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("Список комнат пуст.");
            return;
        }

        System.out.println("ID | Тип | Название | Ставка (руб/час)");
        for (Room room : rooms) {
            if (room.getIsLocked()) {
                System.out.println(room.getId() + " | " + room.getType().getDisplayName() + " | " + room.getName()
                        + " | " + room.getHourlyRate() + " (Запечатана)");
                continue;
            } else {
                System.out.println(room.getId() + " | " + room.getType().getDisplayName() + " | " + room.getName()
                        + " | " + room.getHourlyRate());
            }
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

    private class ShowRoomsItem implements MenuItem {
        @Override
        public int getKey() {
            return 1;
        }

        @Override
        public String getLabel() {
            return "Список комнат";
        }

        @Override
        public boolean execute() {
            showRooms();
            return true;
        }
    }

    private class AddWorkplaceItem implements MenuItem {
        @Override
        public int getKey() {
            return 2;
        }

        @Override
        public String getLabel() {
            return "Добавить рабочее место";
        }

        @Override
        public boolean execute() {
            System.out.println("Введите ID рабочего места:");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("Введите название рабочего места:");
            String name = scanner.nextLine();
            System.out.println("Введите ставку аренды (руб/час):");
            int hourlyRate = Integer.parseInt(scanner.nextLine());
            roomService.addRoom(RoomEnum.WORKPLACE, id, name, hourlyRate);
            return true;
        }
    }

    private class AddMeetingRoomItem implements MenuItem {
        @Override
        public int getKey() {
            return 3;
        }

        @Override
        public String getLabel() {
            return "Добавить переговорную";
        }

        @Override
        public boolean execute() {
            System.out.println("Введите ID переговорной:");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("Введите название переговорной:");
            String name = scanner.nextLine();
            System.out.println("Введите ставку аренды (руб/час):");
            int hourlyRate = Integer.parseInt(scanner.nextLine());
            roomService.addRoom(RoomEnum.MEETING_ROOM, id, name, hourlyRate);
            return true;
        }
    }

    private class DeleteRoomItem implements MenuItem {
        @Override
        public int getKey() {
            return 4;
        }

        @Override
        public String getLabel() {
            return "Запечатать комнату";
        }

        @Override
        public boolean execute() {
            showRooms();
            System.out.println("Введите ID комнаты для запечатывания:");
            int id = Integer.parseInt(scanner.nextLine());
            roomService.lockRoom(id);
            return true;
        }
    }

    private class BackItem implements MenuItem {
        @Override
        public int getKey() {
            return 0;
        }

        @Override
        public String getLabel() {
            return "Назад";
        }

        @Override
        public boolean execute() {
            return false;
        }
    }
}
