package com.android.nejm.data;

public class VideoDetail {
    public Item item;
    public String logo;
    //public RelatedArticle equivalent;
    public String isfav;

    public static class Item {
        public String title;
        public String outlink;
        public String outtitle;
        public String video_url;
        public String thumb;
        public String postdate;
        public String content;
        public String author;
    }

    public static class RelatedArticle {
        public String id;
        public String title;
    }
}
