package com.android.nejm.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AnnounceRecord {
    public boolean unread;
    //public String

    @Generated(hash = 1619777755)
    public AnnounceRecord(boolean unread) {
        this.unread = unread;
    }

    @Generated(hash = 539284914)
    public AnnounceRecord() {
    }

    public boolean getUnread() {
        return this.unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}
