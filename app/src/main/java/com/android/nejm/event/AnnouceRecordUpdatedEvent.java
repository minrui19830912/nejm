package com.android.nejm.event;

public class AnnouceRecordUpdatedEvent {
    public boolean hasUnread;

    public AnnouceRecordUpdatedEvent(boolean hasUnread) {
        this.hasUnread = hasUnread;
    }
}
