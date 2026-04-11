package ru.iu3.ui.handlers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ru.iu3.entity.Booking;
import ru.iu3.service.interfaces.BookingService;
import ru.iu3.ui.interfaces.MenuItem;

public class BookingsMenuHandler {
    private static  String ERROR_MSG = "Произошла ошибка: ";
    // Пул констант (coool!) Перенести все строковые константы (выводы) в interface!
    private  Scanner scanner;
    private  BookingService bookingService;
    private  RoomsMenuHandler roomsMenuHandler;
    private  PassesMenuHandler passesMenuHandler;
    private  List<MenuItem> items = new ArrayList<>();

    public BookingsMenuHandler(Scanner scanner, BookingService bookingService,
            RoomsMenuHandler roomsMenuHandler, PassesMenuHandler passesMenuHandler) {
        this.scanner = scanner;
        this.bookingService = bookingService;
        this.roomsMenuHandler = roomsMenuHandler;
        this.passesMenuHandler = passesMenuHandler;
        items.add(new ShowBookingsItem());
        items.add(new AddBookingItem());
        items.add(new CancelBookingItem());
        items.add(new BackItem());
    }

    public void run() {
        boolean inBookingsMenu = true;
        while (inBookingsMenu) {
            for (MenuItem item : items) {
                System.out.println(item.getKey() + ". " + item.getLabel());
            }
            try {
                System.out.print("Выберите пункт меню: ");
                int choice = Integer.parseInt(scanner.nextLine());
                MenuItem selected = findItem(choice);
                if (selected != null) {
                    inBookingsMenu = selected.execute();
                } else {
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println(ERROR_MSG + e.getMessage());
            }
        }
    }

    public void showRooms() {
        roomsMenuHandler.showRooms();
    }

    public void showPasses() {
        passesMenuHandler.showPasses();
    }

    private void showBookings() {
        for (Booking booking : bookingService.getAllBookings()) {
            System.out.println("ID: " + booking.getId() + ", ID Комнаты: " + booking.getRoomId() + ", Время: c "
                    + booking.getStartTime() + " по " + booking.getEndTime() + ", ID Пропуска: " + booking.getPassId());
        }
    }

    private void addBooking() {
        System.out.println("Введите ID комнаты:");
        int roomId = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите месяц (1-12):");
        int month = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите день месяца:");
        int day = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите время начала (HH:mm):");
        LocalTime startTime = LocalTime.parse(scanner.nextLine());
        System.out.println("Введите время окончания (HH:mm):");
        LocalTime endTime = LocalTime.parse(scanner.nextLine());

        int year = LocalDate.now().getYear();
        LocalDate date = LocalDate.of(year, month, day);
        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
        System.out.println("Введите ID пропуска:");
        int passId = Integer.parseInt(scanner.nextLine());
        double cost = bookingService.createBooking(roomId, passId, startDateTime, endDateTime);
        System.out.println("Бронирование создано. Стоимость: " + cost + " руб.");
    }

    private void cancelBooking() {
        System.out.println("Введите ID бронирования для отмены:");
        int id = Integer.parseInt(scanner.nextLine());
        bookingService.cancelBooking(id);
    }

    private MenuItem findItem(int key) {
        for (MenuItem item : items) {
            if (item.getKey() == key) {
                return item;
            }
        }
        return null;
    }

    private class ShowBookingsItem implements MenuItem {
        @Override
        public int getKey() { return 1; }
        @Override
        public String getLabel() { return "Список бронирований"; }
        @Override
        public boolean execute() { showBookings(); return true; }
    }

    private class AddBookingItem implements MenuItem {
        @Override
        public int getKey() { return 2; }
        @Override
        public String getLabel() { return "Добавить бронирование"; }
        @Override
        public boolean execute() {
            roomsMenuHandler.showRooms();
            passesMenuHandler.showPasses();
            addBooking();
            return true;
        }
    }

    private class CancelBookingItem implements MenuItem {
        @Override
        public int getKey() { return 3; }
        @Override
        public String getLabel() { return "Удалить бронирование"; }
        @Override
        public boolean execute() {
            showBookings();
            cancelBooking();
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
