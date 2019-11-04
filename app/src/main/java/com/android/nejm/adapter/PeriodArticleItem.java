package com.android.nejm.adapter;

import java.util.List;

public class PeriodArticleItem {
    public String month;
    public String year;
    public List<ArticleItem> articleItems;

    public static class ArticleItem {
        public String id;
        public String title;
        public String thedate;
        public String cover;
    }
}
