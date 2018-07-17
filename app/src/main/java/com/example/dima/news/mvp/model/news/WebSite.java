package com.example.dima.news.mvp.model.news;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dima on 30.03.2018.
 */

public class WebSite {
    private String status;
    private List<SourceNews> sources;
    private Map<String, Object> additionalProperties = new HashMap<>();
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public WebSite(String status, List<SourceNews> sources) {
        this.status = status;
        this.sources = sources;
    }

    public List<SourceNews> getSources() {
        return sources;
    }
    public void setSources(List<SourceNews> sources) {
        this.sources = sources;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
