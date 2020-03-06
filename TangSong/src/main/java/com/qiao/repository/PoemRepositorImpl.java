package com.qiao.repository;

import com.qiao.entity.Poem;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Component
public class PoemRepositorImpl {
    @Autowired
    private PoemRepositor poemRepositor;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public void clearAll(){
        System.out.println("开始清除所有");
        poemRepositor.deleteAll();
        System.out.println("清除所有ES成功");
    }

    public void addAll(List<Poem> list){
        System.out.println("开始添加所有");
            elasticsearchTemplate.putMapping(Poem.class);
        list.forEach(l->{
            poemRepositor.save(l);
        });
        System.out.println("添加所有索引成功");
    }
    public List<Poem> findAllByPage(int page, int size) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("tangsong")
                .withTypes("poem")
                .withQuery(matchAllQuery())
                .withPageable(PageRequest.of(page, size))
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, Poem.class);
    }
    public List<Poem> findAllByTrem(String msg,int page,int size) {
        //高亮字段
        System.out.println(msg);

        List<Poem> pos = new ArrayList<>();
        HighlightBuilder.Field field = new HighlightBuilder.Field("*")
                .requireFieldMatch(false)//关闭检索字段匹配
                .preTags("<span class='text-danger'>")
                .postTags("</span>");

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("tangsong")
                .withTypes("poem")
                .withQuery(QueryBuilders.queryStringQuery(msg).field("name").field("content").field("authordes"))//设置查询条件
                //.withSort(new FieldSortBuilder("price").order(SortOrder.DESC))//执行排序条件
                .withPageable(PageRequest.of(page, size))
                .withHighlightFields(field)
                .build();

        AggregatedPage<Poem> Poems = elasticsearchTemplate.queryForPage(searchQuery, Poem.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<Poem> poems = new ArrayList<>();

                //根据searchResponse获取hits
                SearchHits hits = searchResponse.getHits();
                //获取检索结果hits
                SearchHit[] searchHits = hits.getHits();
                //遍历
                for (SearchHit searchHit : searchHits) {
                    Poem poem = new Poem();
                    //获取原始记录
                    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                    //获取高亮map
                    Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();

                    poem.setId(sourceAsMap.get("id").toString());

                    poem.setName(sourceAsMap.get("name").toString());
                    if (highlightFields.containsKey("name")) {
                        poem.setName(highlightFields.get("name").fragments()[0].toString());
                    }

                    poem.setAuthor(sourceAsMap.get("author").toString());
                    poem.setType(sourceAsMap.get("type").toString());

                    poem.setContent(sourceAsMap.get("content").toString());
                    if (highlightFields.containsKey("content")) {
                        poem.setContent(highlightFields.get("content").fragments()[0].toString());
                    }
                    if (sourceAsMap.get("href").equals("")){
                        poem.setHref(sourceAsMap.get("href").toString());
                    }

                    poem.setAuthordes(sourceAsMap.get("authordes").toString());
                    if (highlightFields.containsKey("authordes")) {
                        poem.setAuthordes(highlightFields.get("authordes").fragments()[0].toString());
                    }
                    if (sourceAsMap.get("origin").equals("")){
                        poem.setOrigin(sourceAsMap.get("origin").toString());
                    }
                    if (sourceAsMap.get("categoryId").equals("")){
                        poem.setCategoryId(sourceAsMap.get("categoryId").toString());
                    }


                    poems.add(poem);
                }
                return new AggregatedPageImpl<T>((List<T>) poems);
            }
        });

        for (Poem poem : Poems) {
            pos.add(poem);
        }
        return pos;
    }

}
