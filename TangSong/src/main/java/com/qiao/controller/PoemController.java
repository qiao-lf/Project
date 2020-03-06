package com.qiao.controller;

import com.qiao.entity.Poem;
import com.qiao.service.PoemService;
import com.qiao.util.UIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("poems")
public class PoemController {
    @Autowired
    private PoemService poemService;

    @ResponseBody
    @RequestMapping("Show")
    public Map<String,Object> show(Integer rows, Integer page,Boolean _search,String searchField,String searchString,String searchOper){
        Map<String,Object> map =  new HashMap<>();
        List<Poem> lists = null;
        Integer counts = null;
        if(_search){
            //模糊查询
            counts = poemService.findMHC(searchField,searchString,searchOper);
            Integer sta=rows*(page-1);
            lists = poemService.findMH(searchField,searchString,searchOper,sta,rows);
            map.put("_search",_search);
            map.put("searchField",searchField);
            map.put("searchString",searchString);
            map.put("searchOper",searchOper);
        }else{//查询全部
            counts = poemService.findCounts();
            Integer sta=rows*(page-1);//   不包含
            lists = poemService.findByPage(sta, rows);
        }
        Integer    totalPage = counts%rows==0?counts/rows:counts/rows+1;
        map.put("rows",lists);
        map.put("total",totalPage);
        map.put("page",page);
        map.put("records",counts);
        return map;
    }

    @ResponseBody
    @RequestMapping("cz")
    public void caozuo(String oper,Poem poem,String id){
        if(oper.equals("add")){
            //进行添加操作
            System.out.println(poem);
            String eid= UIDUtils.getUUID32();
            poem.setId(eid);
            System.out.println(poem);
            poemService.add(poem);
            System.out.println("添加成功");
        }
        if(oper.equals("del")){
            poemService.delete(id);
            System.out.println("删除成功");
        }
        if(oper.equals("edit")){
            poemService.update(poem);
            System.out.println("修改成功");
        }

    }
}
