package com.bookingBirthday.bookingbirthdayforkids.model;

public enum InquiryStatus {
    PENDING(1),
    APPROVED(2),
    REJECTED(3);
    private final int value;

    InquiryStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
