package com.projectx.jwt.elasticsearch.repository;

import com.projectx.jwt.elasticsearch.entity.RefreshToken;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends ElasticsearchRepository<RefreshToken,String> {

}
