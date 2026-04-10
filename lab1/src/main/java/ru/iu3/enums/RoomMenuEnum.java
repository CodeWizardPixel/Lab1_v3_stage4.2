package ru.iu3.enums;

public enum RoomMenuEnum {
    SHOW_ALL(1, "Показать все комнаты"),
    ADD(2, "Добавить комнату"),
    LOCK(3, "Запечатать комнату"),
    EXIT(0, "Выход");

    private int id;
    private String displayName;

    RoomMenuEnum(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static RoomMenuEnum findByKey(int key) {
        for (RoomMenuEnum option : RoomMenuEnum.values()) {
            if (option.getId() == key) {
                return option;
            }
        }
        return null;
    }
}
