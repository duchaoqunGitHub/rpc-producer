package com.rpc.test;

import com.rpc.producer.RpcProducer;
import com.rpc.service.UserService;
import com.rpc.service.UserServiceImpl;

import java.io.IOException;

/**
 * @日期: 2019-05-16 14:42
 * @作者: 杜超群
 * @描述:
 */
public class Test {
    public static void main(String[] args) throws IOException {
        UserService userService = new UserServiceImpl();
        RpcProducer producer = new RpcProducer();
        producer.publicService(userService);
    }
}
