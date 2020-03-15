package com.jacob.core;

import com.jacob.config.Bean;
import com.jacob.config.Property;
import com.jacob.config.parsing.ConfigurationManager;
import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过ClassPath查找配置文件，对IoC容器进行初始化
 */
public class ClassPathXmlApplicationContext implements BeanFactory {
    // 存放配置文件信息
    private Map<String, Bean> config;
    // 存放bean对象的IoC容器
    private Map<String, Object> context = new HashMap<>();

    /**
     * 将配置文件中设置为单例的bean对象创建好放入IoC容器中
     * @param path
     */
    public ClassPathXmlApplicationContext(String path) {
        // 读取配置文件中bean的信息
        config = ConfigurationManager.getBeanConfig(path);
        // 遍历初始化bean
        if (config != null) {
            for (Map.Entry<String, Bean> e : config.entrySet()) {
                // 获取bean信息
                String beanName = e.getKey();
                Bean bean = e.getValue();
                // 如果设置成单例的才创建好bean对象放进IoC容器中
                if (bean.getScope().equals(Bean.SINGLETON)) {
                    Object beanObj = createBeanByConfig(bean);
                    context.put(beanName, beanObj);
                }
            }
        }
    }

    /**
     * 反射创建bean对象，并放入IoC容器
     * @param bean
     * @return
     */
    private Object createBeanByConfig(Bean bean) {
        // 根据bean信息创建对象
        Class clazz = null;
        Object beanObj = null;
        try {
            clazz = Class.forName(bean.getClassName());
            // 创建bean对象
            beanObj = clazz.newInstance();
            // 获取bean对象中的property配置
            List<Property> properties = bean.getProperties();
            // 遍历bean对象中的property配置,并将对应的value或者ref注入到bean对象中
            for (Property prop : properties) {
                Map<String, Object> params = new HashMap<>();
                // 基本类型
                if (prop.getValue() != null) {
                    params.put(prop.getName(), prop.getValue());
                    // 将value值注入到bean对象中
                    BeanUtils.populate(beanObj, params);
                }
                // 引用类型
                else if (prop.getRef() != null) {
                    Object ref = context.get(prop.getRef());
                    // 如果依赖对象还未被加载则递归创建依赖的对象
                    if (ref == null) {
                        // 先传入当前bean所依赖对象的bean对象
                        ref = createBeanByConfig(config.get(prop.getRef()));
                    }
                    params.put(prop.getName(), ref);
                    // 将ref对象注入bean对象中
                    BeanUtils.populate(beanObj, params);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建" + bean.getClassName() + "对象失败");
        }
        return beanObj;
    }

    @Override
    public Object getBean(String name) {
        Bean bean = config.get(name);
        Object beanObj = null;
        if (bean.getScope().equals(Bean.SINGLETON)) {
            // 如果将创建bean设置成单例则在容器中找
            beanObj = context.get(name);
        } else if (bean.getScope().equals(Bean.PROTOTYPE)) {
            // 如果是prototype则新创建一个对象
            beanObj = createBeanByConfig(bean);
        }
        return beanObj;
    }
}
