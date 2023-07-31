package com.projectx.jwt.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "tokendetails")
public class RefreshToken {
    @Id
    private String id;
    @Field(type = FieldType.Text, name = "tokenid")
    private String tokenid;
    @Field(type = FieldType.Date, name = "expirydate")
    private Date expirydate;
    @Field(type = FieldType.Text, name = "accesstoken")
    private String accesstoken;
    @Field(type = FieldType.Text, name = "refreshtoken")
    private String refreshtoken;
    @Field(type = FieldType.Long, name = "userid")
    private Long userid;
}
