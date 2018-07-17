package com.example.dima.news.mvp.model.news;

import java.util.List;

/**
 * Created by Dima on 03.04.2018.
 */

public class News {

    private String status;
    private Integer totalResults;
    private List<Article> articles;

    public News() {
    }

    public News(String status, Integer totalResults, List<Article> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
