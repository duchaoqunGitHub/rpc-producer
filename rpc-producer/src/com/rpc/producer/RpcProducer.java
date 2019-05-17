package com.rpc.producer;

import com.rpc.task.Task;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @日期: 2019-05-16 13:54
 * @作者: 杜超群
 * @描述:
 */
public class RpcProducer {

    public static ServerSocket SERVER_SOCKET = null;
    static {
        try {
            SERVER_SOCKET = new ServerSocket(9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 已注册的服务
     */
    public static List<Object> serviceRegisterList = new ArrayList<>();

    public void registerService(Object o){
        serviceRegisterList.add(o);
    }

    /**
     * 发布服务
     * @param o
     */
    public void publicService(Object o) throws IOException {
        System.out.println("服务端Socket连接已开启");
        //注册服务到列表
        registerService(o);
       while (true){
           //阻塞等待客户端连接
           Socket socket = SERVER_SOCKET.accept();
           System.out.println("开始处理socket连接");
           //开启线程单独执行
           new Thread(new Task(socket)).start();
       }

    }
}
