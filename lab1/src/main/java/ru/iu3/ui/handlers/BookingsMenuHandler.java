package ru.iu3.ui.handlers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

import ru.iu3.enums.BookingsMenuEnum;
import ru.iu3.service.interfaces.BookingService;
import ru.iu3.ui.constants.UiConstants;

public class BookingsMenuHandler {
    private static String ERROR_MSG = UiConstants.ERROR_PREFIX;

    private Scanner scanner;
    private BookingService bookingService;
    private DisplayHelper displayHelper;

    public BookingsMenuHandler(Scanner scanner, BookingService bookingService,
            RoomsMenuHandler roomsMenuHandler, PassesMenuHandler passesMenuHandler, DisplayHelper displayHelper) {
        this.scanner = scanner;
        this.bookingService = bookingService;
        this.displayHelper = displayHelper;
    }

    public void run() {
        boolean inBookingsMenu = true;
        while (inBookingsMenu) {
            for (BookingsMenuEnum item : BookingsMenuEnum.values()) {
                System.out.println(item.getId() + ". " + item.getDisplayName());
            }
            try {
                System.out.print(UiConstants.PROMPT_MESSAGE);
                int choice = Integer.parseInt(scanner.nextLine());
                BookingsMenuEnum selected = BookingsMenuEnum.findByKey(choice);

                switch (selected) {
                    case SHOW_ALL:
                        displayHelper.showBookings();
                        break;
                    case ADD:
                        displayHelper.showRooms();
                        displayHelper.showPasses();
                        addBooking();
                        break;
                    case CANCEL:
                        displayHelper.showBookings();
                        cancelBooking();
                        break;
                    case EXIT:
                        inBookingsMenu = false;
                        break;
                    default:
                        System.out.println(UiConstants.INVALID_CHOICE);
                }

            } catch (Exception e) {
                System.out.println(ERROR_MSG + e.getMessage());
            }
        }
    }

    private void addBooking() {
        System.out.println(UiConstants.PROMPT_BOOKING_ROOM_ID);
        int roomId = Integer.parseInt(scanner.nextLine());
        System.out.println(UiConstants.PROMPT_BOOKING_MONTH);
        int month = Integer.parseInt(scanner.nextLine());
        System.out.println(UiConstants.PROMPT_BOOKING_DAY);
        int day = Integer.parseInt(scanner.nextLine());
        System.out.println(UiConstants.PROMPT_BOOKING_START_TIME);
        LocalTime startTime = LocalTime.parse(scanner.nextLine());
        System.out.println(UiConstants.PROMPT_BOOKING_END_TIME);
        LocalTime endTime = LocalTime.parse(scanner.nextLine());

        int year = LocalDate.now().getYear();
        LocalDate date = LocalDate.of(year, month, day);
        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
        System.out.println(UiConstants.PROMPT_BOOKING_PASS_ID);
        int passId = Integer.parseInt(scanner.nextLine());
        double cost = bookingService.createBooking(roomId, passId, startDateTime, endDateTime);
        System.out.println(UiConstants.BOOKING_CREATED + cost + UiConstants.BOOKING_CREATED_SUFFIX);
    }

    private void cancelBooking() {
        System.out.println(UiConstants.PROMPT_BOOKING_CANCEL);
        int id = Integer.parseInt(scanner.nextLine());
        bookingService.cancelBooking(id);
    }
}
