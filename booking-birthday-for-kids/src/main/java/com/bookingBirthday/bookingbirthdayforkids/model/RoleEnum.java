package com.bookingBirthday.bookingbirthdayforkids.model;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN(1),
    HOST(2),
    CUSTOMER(3);

    private final int value;

    RoleEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
