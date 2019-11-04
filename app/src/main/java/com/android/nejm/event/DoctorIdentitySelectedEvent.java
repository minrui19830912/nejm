package com.android.nejm.event;

public class DoctorIdentitySelectedEvent {
    public int options1;
    public int options2;

    public DoctorIdentitySelectedEvent(int options1, int options2) {
        this.options1 = options1;
        this.options2 = options2;
    }
}
