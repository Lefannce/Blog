package com.hj.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    //主键@TableId
    private Long id;
    //头像
    private String avatar;

    //创建时间
    private Date createTime;

    //邮箱
    private String email;

    //昵称
    private String nickName;

    //手机号
    private String phonenumber;

    //用户性别（0男，1女，2未知）
    private String sex;

    //账号状态（0正常 1停用）
    private String status;
    //更新人
    private Long updateBy;

    //更新时间
    private Date updateTime;

    //用户名
    private String userName;
}
