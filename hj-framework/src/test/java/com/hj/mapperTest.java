package com.hj;

import com.hj.mapper.ArticleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class mapperTest {
    public static void main(String[] args) {
        love();
    }

    static void love(){
        System.out.println("爱你");
        love();
    }
}
