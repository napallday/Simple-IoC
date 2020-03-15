package com.jacob.config;

import java.util.ArrayList;
import java.util.List;

/**
 * bean标签的对应类
 */
public class Bean {
    public static final String SINGLETON = "singleton";
    public static final String PROTOTYPE = "prototype";

    private String name;
    private String className;
    // 默认是单例的
    private String scope = SINGLETON;
    private List<Property> properties = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", scope='" + scope + '\'' +
                ", properties=" + properties +
                '}';
    }
}
