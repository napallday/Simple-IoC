package com.jacob.core;

import com.jacob.bean.A;
import com.jacob.bean.B;
import org.junit.Test;

public class TestApplicationContext {

    @Test
    public void test() {
        BeanFactory ac = new ClassPathXmlApplicationContext("/applicationContext.xml");
        A a = (A) ac.getBean("A");
        A a1 = (A) ac.getBean("A");
        B b = (B) ac.getBean("B");
        B b1 = (B) ac.getBean("B");
        System.out.println(a.getB());
        System.out.println("a==a1 : " + (a == a1)); // Singleton
        System.out.println("b==b1 : " + (b == b1)); // Prototype
    }
}