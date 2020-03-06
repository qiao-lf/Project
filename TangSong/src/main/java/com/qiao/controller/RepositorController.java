package com.qiao.controller;

import com.qiao.entity.Poem;
import com.qiao.repository.PoemRepositorImpl;
import com.qiao.service.PoemService;
import com.qiao.util.UIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("repos")
public class RepositorController {
    @Autowired
    private PoemRepositorImpl poemRepositorImpl;
    @Autowired
    private PoemService poemService;
    @Autowired
    private RedisTemplate redisTemplate;


    @ResponseBody
    @RequestMapping("clear")
    public void clearAll(){
        poemRepositorImpl.clearAll();
    }

    @RequestMapping("addAll")
    @ResponseBody
    public void addAll(){
        List<Poem> all = poemService.findAll();
        poemRepositorImpl.addAll(all);
    }

    @ResponseBody
    @RequestMapping("showAll")
    public List<Poem> show(String msg){
        //System.out.println("kaishi");
        List<Poem> poems =null;
        if(msg==null||msg.equals("")){
            System.out.println("开始查询全部");
            poems= poemRepositorImpl.findAllByPage(0, 10);
            poems.forEach(a-> System.out.println(a));
        }else{
            //此时name有效  输入的模糊数据有效
            System.out.println("开始模糊查询");
            try {
                poems = poemRepositorImpl.findAllByTrem(msg, 0, 10);
                poems.forEach(a -> System.out.println(a));
                //开始吧msg数据存入redis缓存中
                //设置key的序列化   //string  list  set   zset
                redisTemplate.setKeySerializer(new StringRedisSerializer());  //字符串序列化方式
                HashOperations<String,String,Double> hp = redisTemplate.opsForHash();
                Map<String, Double> msd = hp.entries("poem");
                if(msd.isEmpty()){
                    Map<String,Double> mps=new HashMap<>();
                    mps.put(msg,0.1);
                    hp.putAll("poem",mps);
                }else{
                    if(msd.containsKey(msg)){
                        //key存在
                        double adb = msd.get(msg);
                        msd.put(msg,adb+0.1);
                    }else{
                        //key不存在
                        msd.put(msg,0.1);
                    }
                    //覆盖内存中的poem
                    hp.putAll("poem",msd);
                }
            }catch(Exception e){
                System.out.println("数据有误,查询失败");
            }
        }
        return poems;
    }
    @ResponseBody
    @RequestMapping("showMh")
    public Map<String,Double> showAllMh(){
        //设置key的序列化   //string  list  set   zset
        redisTemplate.setKeySerializer(new StringRedisSerializer());  //字符串序列化方式
        HashOperations<String,String,Double> hp = redisTemplate.opsForHash();
        Map<String, Double> msd = hp.entries("poem");
        return msd;
    }

    @ResponseBody
    @RequestMapping("addtoci")
    public String addtoword(String msg, HttpServletRequest request){
        System.out.println(msg);//把词取出来
        /**
         * 使用IO流  读word.txt中的数据,   再删除word.txt    chuachuang'ji      */
        String realPath = request.getSession().getServletContext().getRealPath("/word.txt");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(realPath,true));
            out.write(msg+"\r\n"); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        }catch(IOException e){
            System.out.println("写入失败");
            throw new RuntimeException("添加失败");
        }

        return "addSc";

    }
    @ResponseBody
    @RequestMapping("del")
    public void del(String num,HttpServletRequest request){
        System.out.println(num);
        //获取流
        String realPath = request.getSession().getServletContext().getRealPath("/word.txt");
        try{
            List<String> list = new ArrayList<>();
            //读  数据
            File filename = new File(realPath);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader);
            String line = null;
            line = br.readLine();
            while (line != null) {
                list.add(line);
                line = br.readLine(); // 一次读入一行数据
            }

            BufferedWriter out = new BufferedWriter(new FileWriter(realPath));
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(num)) {
                    continue;
                }else{
                    out.write(list.get(i)+"\r\n"); // \r\n即为换行
                }
            }
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @ResponseBody
    @RequestMapping("showzd")
    public Map<String,String> showZD(HttpServletRequest request){
        Map<String,String> map=new HashMap<>();
        String realPath = request.getSession().getServletContext().getRealPath("/word.txt");
        try{
            //真实路径
            //获得真实路径后获取IO流  读取数据 存入map中
            File filename = new File(realPath);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader);
            String line = null;
            line = br.readLine();
            while (line != null) {
                map.put(UIDUtils.getUUID32(),line);
                line = br.readLine(); // 一次读入一行数据
            }
            br.close();
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return map;
    }

}
