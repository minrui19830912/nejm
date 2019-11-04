package com.android.nejm.data;

import java.util.List;

public class ArticleDetail {
    public String title;
    public String thumb;
    public String postdate;
    public String show_wantsay;
    public String author;
    public List<Specialties> specialties;

    public static class Specialties {
        public String id;
        public String classname;
    }
}
