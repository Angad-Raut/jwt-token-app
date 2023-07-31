package com.projectx.jwt.mysqldb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private Long userMobile;
    private String userEmail;
    private String userPassword;
    private Boolean userStatus;
    @ElementCollection
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private List<String> roles;

    public Users(Users users) {
        this.id=users.id;
        this.userName=users.userName;
        this.userMobile=users.userMobile;
        this.userEmail=users.userEmail;
        this.userPassword=users.userPassword;
        this.userStatus=users.userStatus;
        this.roles=users.roles;
    }
    public List<String> getUserRole() {
        return roles;
    }
}
