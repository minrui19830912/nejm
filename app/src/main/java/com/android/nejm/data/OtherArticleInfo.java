package com.android.nejm.data;

import java.util.List;

public class OtherArticleInfo {
    public String filtername;
    public String sourcename;
    public List<FilterItem> filter_items;
    public int total_count;
    public List<ArtitleItem> items;
    public List<Specialties> specialties;

    public static class FilterItem {
        public String id;
        public String filtername;
    }

    public class ArtitleItem {
        public String id;
        public String title;
        public String author;
        public String sourcename;
        public String filtername;
        public String postdate;
        public String show_wantsay;
        public String thumb;
        public List<Specialties> specialties;
    }

    public static class Specialties {
        public String classname;
        public String id;
    }
}
