package com.android.nejm.data;

import java.util.List;

public class RelatedArticle {
    public String id;
    public String title;
    public String author;
    public String sourcename;
    public List<Specialties> specialties;
    public String typename;
    public String filtername;
    public String show_wantsay;
    public String postdate;
    public String thumb;

    public static class Specialties {
        public String id;
        public String classname;
    }
}
