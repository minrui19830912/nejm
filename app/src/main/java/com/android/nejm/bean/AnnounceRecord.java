package com.android.nejm.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class AnnounceRecord {

    private Long id;

    public boolean read;
    @Id
    public String msgId;

    @Generated(hash = 1660040171)
    public AnnounceRecord(Long id, boolean read, String msgId) {
        this.id = id;
        this.read = read;
        this.msgId = msgId;
    }

    @Generated(hash = 539284914)
    public AnnounceRecord() {
    }
    
    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getRead() {
        return this.read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
