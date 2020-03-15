package com.jacob.core;

public interface BeanFactory {
    /**
     * 根据name返回bean对象
     * @return Bean实例
     */
    Object getBean(String name);
}
