package com.android.nejm.data;

import java.util.List;

public class HomeBean {
    public List<Banner> banner;
    public List<Classes> classes;
    public List<Filter> filter_1;
    public List<Filter> filter_2;
    public List<Weekly> weekly;
    public List<Videos> videos;

    public static class Banner {
        public String title;
        public String intro;
        public String pic;
        public String articleid;
    }

    public static class Classes {
        public String id;
        public String classname;
        public String icon;
        public String xcx_icon_se;
    }

    public static class Filter {
        public String id;
        public String filtername;
    }

    public static class Weekly {
        public String id;
        public String title;
        public String thedate;
        public String cover;
    }

    public static class Videos {
        public String id;
        public String title;
        public String postdate;
        public String thumb;
    }
}
