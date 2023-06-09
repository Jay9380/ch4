package com.fastcampus.ch4.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class PageHandlerTest {
    @Test
    public void test(){
        PageHandler ph = new PageHandler(250, 1);
        ph.print();
        System.out.println(ph);
    }

    @Test
    public void test1(){
        PageHandler ph = new PageHandler(255, 25);
        ph.print();
        System.out.println("ph ==>" + ph);

    }

}