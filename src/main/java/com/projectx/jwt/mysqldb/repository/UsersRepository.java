package com.projectx.jwt.mysqldb.repository;

import com.projectx.jwt.mysqldb.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
    @Query(value = "select * from users where user_mobile=:mobile",nativeQuery = true)
    Users findUsersByMobile(@Param("mobile") Long mobile);
    @Query(value = "select * from users where user_email=:email",nativeQuery = true)
    Users findUsersByEmail(@Param("email") String email);
    @Query(value = "select * from users where id=:userId",nativeQuery = true)
    Users findByUserId(@Param("userId") Long userId);
    Boolean existsByUserEmail(String email);
    Boolean existsByUserMobile(Long mobile);
}
