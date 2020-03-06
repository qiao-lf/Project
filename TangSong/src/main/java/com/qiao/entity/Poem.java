package com.qiao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Document(indexName = "tangsong",type = "poem")
public class Poem implements Serializable {

    @Id
    private String id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Keyword)
    private String author;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String content;

    @Field(type = FieldType.Keyword)
    private String href;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String authordes;

    @Field(type = FieldType.Keyword)
    private String origin;

    @Field(type = FieldType.Keyword)
    private String categoryId;


}
