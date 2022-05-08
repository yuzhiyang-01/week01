package com.yu.week01;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class Week01Classloader extends ClassLoader{

    public static void main(String[] args) throws Exception {
// 相关参数
        final String className = "Hello";
        final String methodName = "hello";
        // 创建类加载器
        ClassLoader classLoader = new Week01Classloader();
        // 加载相应的类
        Class<?> clazz = classLoader.loadClass(className);
        // 看看里面有些什么方法
        for (Method m : clazz.getDeclaredMethods()) {
            System.out.println(clazz.getSimpleName() + "." + m.getName());
        }
        // 创建对象
        Object instance = clazz.getDeclaredConstructor().newInstance();
        // 调用实例方法
        Method method = clazz.getMethod(methodName);
        method.invoke(instance);
    }
    @Override
    protected Class<?> findClass(String name) {
        // 获取resource下的文件
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(name + ".xlass");
            // 获取字节
        int available = 0;
        try {
            // 获取字节长度
            available = resourceAsStream.available();
            byte[] bytes = new byte[available];
            //将输入流的读取字节
            resourceAsStream.read(bytes);

            byte[] decode = decode(bytes);
            return defineClass (name,decode,0,decode.length);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                resourceAsStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            return null;
    }

    // 解码
    private static byte[] decode(byte[] byteArray) {
        byte[] targetArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            targetArray[i] = (byte) (255 - byteArray[i]);
        }
        return targetArray;
    }
}
