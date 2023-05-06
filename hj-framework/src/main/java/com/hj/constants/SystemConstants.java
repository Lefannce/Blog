package com.hj.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     * 状态正常
     */
    public static final String STATUS_NORMAL = "0";

    /**
     * 友链状态审核通过
     */
    public static final String LINK_STATUS_NORMAL = "0";

    /**
     * 是否为根评论-1
     */
    public static final String ROOT_COMMENT = "-1";


        /**
     * 评论类型为：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 评论类型为：友联评论
     */
    public static final String LINK_COMMENT = "1";

    //menu_type 等于C为菜单
    public static final String MENU = "C";

    //menu_type 等于F为目录
    public static final String BUTTON = "F";
}