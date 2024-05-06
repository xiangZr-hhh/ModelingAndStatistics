package com.modeling.model.route;

import lombok.Data;

import java.util.List;

@Data
public class RouteConfig {
    private String path;
    private String component;
    private String redirect;
    private boolean alwaysShow;
    private Meta meta;
    private List<RouteConfig> children;

    // getters and setters
}

@Data
class Meta {
    private String title;
    private String icon;
    private List<String> roles;

    // getters and setters
}