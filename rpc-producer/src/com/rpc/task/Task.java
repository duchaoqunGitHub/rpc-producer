package com.rpc.task;

import com.rpc.producer.RpcProducer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

/**
 * @日期: 2019-05-16 14:11
 * @作者: 杜超群
 * @描述:
 */
public class Task implements Runnable {

    private Socket socket;

    public Task(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String message = null;
        ObjectInputStream in = null;
        ObjectOutputStream outputStream = null;
        try {
            System.out.println("开始读取数据");
            //服务端为 先读写后输出，流的顺序为 先获取输入流,再获取输出流，否则会造成阻塞
            in = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            //分别读取 类名,方法,参数类型,参数
            Class clazzInterfaceName = (Class) in.readObject();
            System.out.println("调用对象类-------------------" + clazzInterfaceName);

            String methodName = (String) in.readObject();
            System.out.println("调用方法---------------------" + methodName);

            Class[] parameTypes = (Class[]) in.readObject();
            System.out.println("方法参数类型-----------------" + parameTypes[0]);

            Object[] parames = (Object[]) in.readObject();
            System.out.println("方法参数---------------------" + parames[0]);

            //获取具体对象
            Object object = findService(clazzInterfaceName);
            if (object == null) {
                outputStream.writeObject("服务未发现");
                outputStream.flush();
            } else {
                //获取Class对象
                Class clazz = object.getClass();
                //根据方法名，参数类型获取方法对象
                Method method = clazz.getDeclaredMethod(methodName, parameTypes);
                //利用java反射机制调用对象具体方法
                Object resultObj = method.invoke(object, parames);
                //返回结果给客户端
                outputStream.writeObject(resultObj);
            }
            //关闭socket
            socket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private Object findService(Class clazz) {
        List<Object> list = RpcProducer.serviceRegisterList;
        for (Object service : list) {
            if (clazz.isAssignableFrom(service.getClass())) {
                return service;
            }
        }
        return null;
    }
}
