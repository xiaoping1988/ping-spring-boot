package com.ping.sample.controller;

import com.ping.sample.PingApplication;
import com.ping.sample.entity.User;
import com.ping.sample.vo.ResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

/**
 * Created by liujiangping on 2017/8/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PingApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String PREFIX_URL = "/api/user";

    @Test
    public void testAddUser() {
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setUser_name("系统管理员" + i);
            user.setUser_account("admin" + i);
            user.setUser_age(new Random().nextInt(100));
            user.setUser_city("北京");
            HttpEntity<User> he = new HttpEntity<>(user);
            ResponseVO responseVO = this.restTemplate.postForObject(PREFIX_URL + "/add", he, ResponseVO.class);
            logger.info(responseVO.toString());
        }
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUser_id(1L);
        user.setUser_name("系统管理员22222");
        user.setUser_account("admin2222");
        user.setUser_age(new Random().nextInt(100));
        user.setUser_city("上海");
        HttpEntity<User> he = new HttpEntity<>(user);
        ResponseEntity<ResponseVO> res = this.restTemplate.exchange(PREFIX_URL + "/update", HttpMethod.PUT, he, ResponseVO.class);
        logger.info(res.getBody().toString());
    }

    @Test
    public void testGetByUserId() {
        String json = this.restTemplate.getForObject(PREFIX_URL + "/1", String.class);
        logger.info(json);
    }

    @Test
    public void testDeleteByUserId() {
        ResponseEntity<ResponseVO> res = this.restTemplate.exchange(PREFIX_URL + "/delete/1", HttpMethod.DELETE, null, ResponseVO.class);
        logger.info(res.getBody().toString());
    }

    @Test
    public void testListAll() {
        ResponseVO users = this.restTemplate.getForObject(PREFIX_URL + "/all", ResponseVO.class);
        logger.info(users.toString());

        ResponseVO users2 = this.restTemplate.getForObject(PREFIX_URL + "/all?user_name=2&user_account=2", ResponseVO.class);
        logger.info(users2.toString());
    }

    @Test
    public void testListPage() {
        ResponseVO users = this.restTemplate.getForObject(PREFIX_URL + "/1/10", ResponseVO.class);
        logger.info(users.toString());

        ResponseVO users2 = this.restTemplate.getForObject(PREFIX_URL + "/1/10?user_name=2&user_account=2", ResponseVO.class);
        logger.info(users2.toString());
    }

    @Test
    public void testCountGroupByAge() {
        ResponseVO responseVO = this.restTemplate.getForObject(PREFIX_URL + "/count-group-by-age", ResponseVO.class);
        logger.info(responseVO.toString());
    }
}
