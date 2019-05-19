package com.android.nejm.data;

import java.util.List;

public class RoleBean {
    public String roleid;
    public String name;
    public String division;
    public String parent_id;
    public String hospital;
    public String jobname;
    public String identity;
    public String company;
    public List<DPTS> dpts;
    public List<IdentityInfo> doctorJobs;
    public List<IdentityInfo> teacherJobs;
    public List<IdentityInfo> teacherIdentity;
    public List<IdentityInfo> otherIdentity;
    public List<IdentityInfo> roleArray;

    public static class DPTS {
        public String id;
        public String title;
        public List<SubListItem> sublist;
    }

    public static class SubListItem {
        public String id;
        public String title;
    }

    public static class IdentityInfo {
        public String id;
        public String name;

        public IdentityInfo(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
