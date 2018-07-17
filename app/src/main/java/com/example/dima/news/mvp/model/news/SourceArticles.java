package com.example.dima.news.mvp.model.news;

/**
 * Created by Dima on 04.04.2018.
 */

public class SourceArticles {
    private String id;
    private String name;

    public SourceArticles() {
    }

    public SourceArticles(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
