package com.example.my_theatre.utils;


import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 基于UUID构造分布式ID
 */
@Component
public class OrderIdUtil {

    public static String  getUUID()
    {
        return  UUID.randomUUID().toString().replace("-", "");
    }


}
/**
 * todo
 * 使用UUID之后，无法将订单id作为索引来提高数据库检索效率，后续尝试更改
 * 美团LEAF技术方案：
 * 参考源码：
 * https://github.com/Meituan-Dianping/Leaf
 */
