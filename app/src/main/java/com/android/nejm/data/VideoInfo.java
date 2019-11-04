package com.android.nejm.data;

import java.util.List;

public class VideoInfo {
    public List<TypeItem> types;
    public int total_count;
    public List<Videoitem> items;
    public static class Videoitem {
        public String id;
        public String title;
        public String postdate;
        public String typename;
        public String thumb;
    }

    public static class TypeItem {
        public String id;
        public String typename;
    }
}
