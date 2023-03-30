package com.hj.domain.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkVo {

    private Long id;

    //网站地址
    private String address;

    private String description;

    private String logo;

    private String name;
}
