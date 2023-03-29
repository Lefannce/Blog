package com.hj.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotArticleListVo {

     private Long id;

    //标题
    private String title;

    //访问量
    private Long viewCount;

}
