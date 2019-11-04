package com.android.nejm.data;

import java.util.List;

public class PeriodArticleDeatailInfo {
    public Item item;
    public List<Items0> items_0;
    public List<Items0> items_5;
    public List<Items0> items_6;
    public List<Items0> wantsay;
    public Video video;

    public static class Item {
        public String id;
        public String title;
        public String cover;
    }

    public static class Items0 {
        public String id;
        public String title;
    }

    public static class Video {
        public String id;
        public String title;
        public String thumb;
    }
}
