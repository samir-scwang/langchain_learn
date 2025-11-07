package com.sicheng.langchain4j03bootintegration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedisSetGet() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        // 写入数据
        ops.set("test:key", "hello redis");

        // 读取数据
        String value = ops.get("test:key");

        // 断言测试
        assertThat(value).isEqualTo("hello redis");
        System.out.println("从Redis获取到的值：" + value);
    }
}

