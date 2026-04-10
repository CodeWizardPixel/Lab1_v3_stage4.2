package ru.iu3.ui.handlers;

import java.util.List;

import ru.iu3.entity.Booking;
import ru.iu3.entity.Pass;
import ru.iu3.entity.interfaces.Room;
import ru.iu3.service.interfaces.BookingService;
import ru.iu3.service.interfaces.PassService;
import ru.iu3.service.interfaces.RoomService;
import ru.iu3.ui.constants.UiConstants;

public class DisplayHelper {
    private PassService passService;
    private RoomService roomService;
    private BookingService bookingService;

    public DisplayHelper(PassService passService, RoomService roomService, BookingService bookingService) {
        this.passService = passService;
        this.roomService = roomService;
        this.bookingService = bookingService;
    }

    public void showRooms() {
        System.out.println(UiConstants.ROOMS_LIST_TITLE);
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println(UiConstants.ROOMS_EMPTY);
            return;
        }

        System.out.println(UiConstants.ROOMS_HEADER);
        for (Room room : rooms) {
            if (room.getIsLocked()) {
                System.out.println(room.getId() + " | " + room.getType().getDisplayName() + " | " + room.getName()
                        + " | " + room.getHourlyRate() + UiConstants.ROOM_LOCKED_SUFFIX);
                continue;
            } else {
                System.out.println(room.getId() + " | " + room.getType().getDisplayName() + " | " + room.getName()
                        + " | " + room.getHourlyRate());
            }
        }
    }

    public void showPasses() {
        System.out.println(UiConstants.PASSES_LIST_TITLE);
        List<Pass> passes = passService.getAllPasses();
        if (passes.isEmpty()) {
            System.out.println(UiConstants.PASSES_EMPTY);
            return;
        }

        System.out.println(UiConstants.PASSES_HEADER);
        for (Pass pass : passes) {
            System.out.println(pass.getId() + " | " + pass.getHolderName() + " | " + pass.isActive());
        }
    }

    public void showBookings() {
        System.out.println(UiConstants.BOOKINGS_LIST_TITLE);
        List<Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            System.out.println(UiConstants.BOOKINGS_EMPTY);
            return;
        }

        System.out.println(UiConstants.BOOKINGS_HEADER);
        for (Booking booking : bookings) {
            System.out.println(booking.getId() + " | " + booking.getRoomId() + " | "
                    + booking.getStartTime() + " | " + booking.getEndTime() + " | " + booking.getPassId());
        }
    }
}
