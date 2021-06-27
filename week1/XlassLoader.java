package geektime.spring.hello.hellospring.classLoader;

import java.io.*;
import java.lang.reflect.Method;

/**
 * 自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供。
 */
public class XlassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        String className = "Hello";
        String methodName = "hello";
        XlassLoader xlassLoader = new XlassLoader();
        Class clazz = xlassLoader.loadClass(className);
        Method method = clazz.getMethod(methodName);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        method.invoke(instance);
    }


    @Override
    public Class<?> findClass(String name) {

        File file = null;
        InputStream inputStream = null;
        try {
            //文件的路径
            file = new File("/Users/chengwenping/Downloads/hello-spring/src/main/java/geektime/spring/hello/hellospring/classLoader/Hello.xlass");
            inputStream = new FileInputStream(file);
            int length = inputStream.available();
            byte[] byteArray = new byte[length];
            inputStream.read(byteArray);
            //转换
            byte[] classBytes = decode(byteArray);
            //通知底层定义
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(inputStream);

        }

        return null;
    }

    private static byte[] decode(byte[] byteArray){
        byte[] targetArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length;i ++){
            targetArray[i] = (byte) (255 -byteArray[i]);
        }
        return targetArray;
    }

    private static void close(Closeable res){
        if(null != res){
            try{
                res.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
