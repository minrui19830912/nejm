package com.android.nejm.bean;

import com.android.nejm.data.RelatedArticle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class DownloadRecord {
    @Id
    public String articleId;
    public String title;
    public String author;
    public String sourcename;
    public String typename;
    public String filtername;
    public String show_wantsay;
    public String postdate;
    public String thumb;
    public String classname;

    public String filePath;

    public DownloadRecord(RelatedArticle article) {
        this.articleId = article.id;
        this.title = article.title;
        this.author = article.author;
        this.sourcename = article.sourcename;
        this.typename = article.typename;
        this.filtername = article.filtername;
        this.show_wantsay = article.show_wantsay;
        this.postdate = article.postdate;
        this.thumb = article.thumb;
        this.classname = "";
        if(article.specialties != null && article.specialties.size() > 0) {
            this.classname = article.specialties.get(0).classname;
        }
    }

    @Generated(hash = 970552819)
    public DownloadRecord(String articleId, String title, String author,
            String sourcename, String typename, String filtername,
            String show_wantsay, String postdate, String thumb, String classname,
            String filePath) {
        this.articleId = articleId;
        this.title = title;
        this.author = author;
        this.sourcename = sourcename;
        this.typename = typename;
        this.filtername = filtername;
        this.show_wantsay = show_wantsay;
        this.postdate = postdate;
        this.thumb = thumb;
        this.classname = classname;
        this.filePath = filePath;
    }

    @Generated(hash = 155491740)
    public DownloadRecord() {
    }

    public String getFilePath() {
        return this.filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getArticleId() {
        return this.articleId;
    }
    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
    public String getPostdate() {
        return this.postdate;
    }
    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }
    public String getThumb() {
        return this.thumb;
    }
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSourcename() {
        return this.sourcename;
    }

    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    public String getTypename() {
        return this.typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getFiltername() {
        return this.filtername;
    }

    public void setFiltername(String filtername) {
        this.filtername = filtername;
    }

    public String getShow_wantsay() {
        return this.show_wantsay;
    }

    public void setShow_wantsay(String show_wantsay) {
        this.show_wantsay = show_wantsay;
    }

    public String getClassname() {
        return this.classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }
}
