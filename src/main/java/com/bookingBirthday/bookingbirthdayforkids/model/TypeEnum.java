package com.bookingBirthday.bookingbirthdayforkids.model;

public enum TypeEnum {
    FOOD(1),
    DECORATION(2);

    private final int value;

    TypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
