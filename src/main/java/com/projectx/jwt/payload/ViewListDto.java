package com.projectx.jwt.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewListDto {
    private Integer srNo;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long userMobile;
    private List<String> userRoles;
}
