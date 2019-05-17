package com.rpc.service;

/**
 * @日期: 2019-05-16 14:40
 * @作者: 杜超群
 * @描述:
 */
public class UserServiceImpl implements UserService {

    @Override
    public String test(int num) {
        return "rpc调用成功:" + num;
    }
}
