package com.android.nejm.data;

import java.util.List;

public class NewKnowledgeInfo {
    public List<TypeItem> types;
    public int total_count;
    public List<NewKnowledgeitem> items;
    public static class NewKnowledgeitem {
        public String id;
        public String title;
        public String thedate;
        public String typename;
        public String is_article;
        public String articleid;
    }

    public static class TypeItem {
        public String id;
        public String typename;
    }
}
