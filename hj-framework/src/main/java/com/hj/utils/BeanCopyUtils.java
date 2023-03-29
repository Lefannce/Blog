package com.hj.utils;

import com.hj.domain.VO.HotArticleListVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    /**
     * 单个对象拷贝
     * @param source
     * @param clazz
     * @return
     * @param <V>
     */
    //传入泛型class 的V,返回也是V
    public static <V> V copyBean(Object source,Class<V> clazz) {
        //创建目标对象(创建传入的类型)
        V result = null;
        try {
            //通过反射创建传入的对象类型(空参对象)
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }

    /**
     * 多个对象拷贝,
     * 传入的集合对象泛型O,需要返回的类型V,
     * 使用stream.map方法拷贝并收为list对象
     * @param list
     * @param clazz
     * @return
     * @param <O>
     * @param <V>
     */
    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))//o代表list的参数
                .collect(Collectors.toList());
    }


}
