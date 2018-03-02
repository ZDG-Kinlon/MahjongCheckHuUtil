package cn.mahjong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FindHus {
    private List<List<Integer>> lists;//待测牌组集合的集合
    private int nThreads = 4;//线程池的线程数

    public void setnThreads(int nThreads) {
        this.nThreads = nThreads;
    }

    public FindHus(List<List<Integer>> lists) {
        this.lists = lists;
    }

    /**
     * 获取待测牌组集合的听牌集合
     *
     * @return
     */
    public Map<List<Integer>, List<Integer>> findHu() {
        ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);//创建线程池
        List<FindHu> findHus = new ArrayList<>();//创建对象
        lists.forEach(list -> findHus.add(new FindHu(list)));//遍历每个传入的待测牌组集合，写入对应的对象中
        List<Future<Map<List<Integer>, List<Integer>>>> futures = new ArrayList<>();//创建每个线程返回结果的集合
        try {
            findHus.forEach(findHu -> futures.add(threadPool.submit(findHu)));//递交对象任务到线程池，自动执行call方法，返回结果到集合中
        } finally {
            threadPool.shutdown();//关闭线程池
        }
        Map<List<Integer>, List<Integer>> out = new HashMap<>();//创建返回结果的集合
        futures.forEach(f -> {
            try {
                f.get().forEach(out::put);//遍历传入的待测牌集合，将听牌集合结果写入
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        return out;
    }
}