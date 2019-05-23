package com.android.nejm.data;

import java.util.List;

public class AnnounceMessage {
    public List<MessageItem> items;

    public static class MessageItem {
        public String id;
        public String title;
        public String postdate;
        public boolean read;
    }
}
