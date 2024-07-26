package com.caiwei.ojcodesandbox;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SampleComputer {
    public static void main(String[] args) {
        // 获取当前工作目录
        String userDir = System.getProperty("user.dir");
        // 打印当前工作目录
        System.out.println("当前工作目录是: " + userDir);
    }

    @Test
    void getUrl() {
        String code = ResourceUtil.readStr("testCode.simpleCompteArgs/Main.java", StandardCharsets.UTF_8);
        System.out.println(code);
    }

    @Test
    void list() {
        ArrayList<String> list = new ArrayList<>();
        System.out.println(list.isEmpty());
    }

    @Test
    void future() {
        // 创建一个单线程的线程池
        ExecutorService executorService = Executors.newSingleThreadExecutor();


        try {

            // 提交任务并获取Future以便控制任务
            Future<Void> futureTask = (Future<Void>) executorService.submit(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    while (true) {
                        System.out.println("watch , i = " + (i++));
                    }
                }
            });
            // 这里模拟主函数的执行
            int i = 500;
            while ((--i) > 0) {
                System.out.println("main");
            }

            // 主函数执行完毕后，尝试取消并发执行的任务
            futureTask.cancel(true); // true表示如果任务正在运行，则尝试中断它
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池
            executorService.shutdown();
        }
    }


    @Test
    void thread() {
/*        new Thread(()->{
            int j = 0;
            while( j <500){
                System.out.println("second j =" + (j++));
            }
        }).start();*/
        int sum = 0;
        int[] arr = new int[3];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        for (int i : arr) {
            sum += i;
        }
        System.out.println("sum = " + sum);
    }

}
