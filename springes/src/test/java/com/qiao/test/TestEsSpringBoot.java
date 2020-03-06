package com.qiao.test;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.qiao.EsApplication;
import com.qiao.dao.BookRepositor;
import com.qiao.entity.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = EsApplication.class)
@RunWith(SpringRunner.class)
public class TestEsSpringBoot {
    @Autowired
    private BookRepositor bookRepositor;







    @Test
    public void t6(){
        /*List<Book> bo = bookRepositor.findByNameAndAutor("小美的故事", "小日月");
        bo.forEach(b-> System.out.println(b));*/
//        List<Book> byUpdowndateBefore = bookRepositor.findByPriceBefore(10000.00);
//        byUpdowndateBefore.forEach(b-> System.out.println(b));

        List<Book> fbnl = bookRepositor.findByNameLike("小");
        fbnl.forEach(f-> System.out.println(f));

    }


    //排序
    @Test
    public void t5(){
        Iterable<Book> updowndate = bookRepositor.findAll(Sort.by(Sort.Order.desc("updowndate")));
        updowndate.forEach(book -> System.out.println(book));
    }

    @Test
    public void t4(){
        Iterable<Book> all = bookRepositor.findAll();

        all.forEach(a-> System.out.println(a));

        System.out.println("--------******-------");

        Optional<Book> byId = bookRepositor.findById("1");
        System.out.println(byId);
    }

    @Test
    public void t3(){
        Book b=new Book();
        b.setId("2");
        bookRepositor.delete(b);
    }


    //数据存在则修改数据 数据不存在则添加数据
    @Test
    public void t2(){

//        Book book=new Book("2","小明的故事",
//                500.00,new Date(),
//                "这是一本关于小明的故事，里面记载了各种小" +
//                        "明经历过的事情或者与小明有关的事情","小日月");
//        bookRepositor.save(book);
//        Book book2=new Book("2","小美的镜子",
//                500.00,new Date(),
//                "这是一本关于小明的故事，里面记载了各种小" +
//                        "明经历过的事情或者与小明有关的事情","小日月");
        Book book2=new Book("3","小黑的代码",
                100.00,new Date(),
                "关于小黑的代码经历","小日月");
        Book book3=new Book("4","小李的自述",
                100.00,new Date(),
                "关于小李的亲身经历的事情","小李");
        Book book4=new Book("5","老王翻墙的故事",
                1003.00,new Date(),
                "关于老王如何翻墙，以及在派出所交代的事情","老王");
        bookRepositor.save(book2);bookRepositor.save(book3);bookRepositor.save(book4);
    }
    @Test
    public void t1(){
        Book book=new Book("1","小明的故事",
                500.00,new Date(),
                "这是一本关于小明的故事，里面记载了各种小" +
                        "明经历过的事情或者与小明有关的事情","小日月");
        bookRepositor.save(book);
    }



}
