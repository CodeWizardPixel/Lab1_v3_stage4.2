package ru.iu3.ui.constants;

public class UiConstants {
    public static final String WELCOME_MESSAGE = "--- Добро пожаловать в чудный консольный коворкинг! ---";
    public static final String GOODBYE_MESSAGE = "Споки-ноки :3c ~";
    public static final String PROMPT_MESSAGE = "Выберите пункт меню: ";
    public static final String INVALID_CHOICE = "Неверный выбор. Пожалуйста, попробуйте снова.";
    public static final String ERROR_PREFIX = "Произошла ошибка: ";

    public static final String ROOMS_LIST_TITLE = "Cписок комнат:";
    public static final String ROOMS_EMPTY = "Список комнат пуст.";
    public static final String ROOMS_HEADER = "ID | Тип | Название | Ставка (руб/час)";
    public static final String ROOM_LOCKED_SUFFIX = " (Запечатана)";
    public static final String PROMPT_ROOM_TYPE = "Введите тип комнаты:";
    public static final String PROMPT_ROOM_ID = "Введите ID комнаты:";
    public static final String PROMPT_ROOM_NAME = "Введите название комнаты:";
    public static final String PROMPT_ROOM_RATE = "Введите ставку аренды (руб/час):";
    public static final String PROMPT_ROOM_LOCK = "Введите ID комнаты для запечатывания:";

    public static final String PASSES_LIST_TITLE = "Cписок пропусков:";
    public static final String PASSES_EMPTY = "Список пропусков пуст.";
    public static final String PASSES_HEADER = "ID | Владелец | Активен";
    public static final String PROMPT_PASS_ID = "Введите ID пропуска:";
    public static final String PROMPT_PASS_HOLDER = "Введите имя владельца пропуска:";
    public static final String PROMPT_PASS_DEACTIVATE = "Введите ID пропуска для деактивации:";
    public static final String PROMPT_PASS_ACCESS = "Введите ID пропуска для просмотра доступа:";
    public static final String PASS_INACTIVE = "Пропуск неактивен. Доступ к комнатам отсутствует.";
    public static final String ROOM_ACCESS_PREFIX = "Доступ к комнате: ";

    public static final String BOOKINGS_LIST_TITLE = "Список бронирований:";
    public static final String BOOKINGS_EMPTY = "Список бронирований пуст.";
    public static final String BOOKINGS_HEADER = "ID | ID Комнаты | Время начала | Время окончания | ID Пропуска";
    public static final String PROMPT_BOOKING_ROOM_ID = "Введите ID комнаты:";
    public static final String PROMPT_BOOKING_MONTH = "Введите месяц (1-12):";
    public static final String PROMPT_BOOKING_DAY = "Введите день месяца:";
    public static final String PROMPT_BOOKING_START_TIME = "Введите время начала (HH:mm):";
    public static final String PROMPT_BOOKING_END_TIME = "Введите время окончания (HH:mm):";
    public static final String PROMPT_BOOKING_PASS_ID = "Введите ID пропуска:";
    public static final String BOOKING_CREATED = "Бронирование создано. Стоимость: ";
    public static final String BOOKING_CREATED_SUFFIX = " руб.";
    public static final String PROMPT_BOOKING_CANCEL = "Введите ID бронирования для отмены:";
}
