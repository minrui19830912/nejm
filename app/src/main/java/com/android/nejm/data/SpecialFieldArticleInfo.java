package com.android.nejm.data;

import java.util.List;

public class SpecialFieldArticleInfo {
    public List<Classes> classes;
    public int total_count;
    public List<ArtitleItem> items;
    public List<Specialties> specialties;

    public static class Classes {
        public String id;
        public String classname;
        public String icon;
    }

    public class ArtitleItem {
        public String id;
        public String title;
        public String author;
        public String sourcename;
        public String typename;
        public String postdate;
        public String show_wantsay;
        public String thumb;
        public String intro;
        public List<Specialties> specialties;
    }

    public static class Specialties {
        public String classname;
        public String id;
    }
}
