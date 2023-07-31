package com.projectx.jwt.elasticsearch.service;

import com.projectx.jwt.elasticsearch.entity.RefreshToken;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

@Service
public class TokenDetailsSearchQuery {

    @Autowired
    private ElasticsearchRestTemplate template;

    private final String INDEX_NAME = "tokendetails";
    private final String TOKEN_ID_FIELD="tokenid";
    private final String USER_ID_FIELD="userid";


    public RefreshToken findByTokenId(String tokenId) {
        RefreshToken refreshToken = null;
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery(TOKEN_ID_FIELD,tokenId)).build();
        SearchHits<RefreshToken> refreshTokenList = template.search(searchQuery, RefreshToken.class,IndexCoordinates.of(INDEX_NAME));
        if (refreshTokenList.hasSearchHits()) {
            for (SearchHit<RefreshToken> data:refreshTokenList.getSearchHits()) {
                refreshToken = data.getContent();
                break;
            }
        }
        return refreshToken;
    }

    public RefreshToken getRefreshTokenByUserId(Long userId) {
        RefreshToken refreshToken = null;
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery(USER_ID_FIELD,userId)).build();
        SearchHits<RefreshToken> refreshTokenList = template.search(searchQuery,RefreshToken.class,IndexCoordinates.of(INDEX_NAME));
        if (refreshTokenList.hasSearchHits()) {
            for (SearchHit<RefreshToken> data:refreshTokenList.getSearchHits()) {
                refreshToken = data.getContent();
                break;
            }
        }
        return refreshToken;
    }

}
