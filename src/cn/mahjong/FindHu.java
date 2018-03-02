package cn.mahjong;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FindHu implements Callable<Map<List<Integer>, List<Integer>>> {
    private List<Integer> integerList;//待寻找的牌组集合

    public FindHu(List<Integer> integerList) {
        this.integerList = integerList;
    }

    /**
     * 获取听牌集合
     * @return
     */
    public List<Integer> findHu() {
        List<CheckHu> checkHuList = new ArrayList<>();//创建对象集合
        List<Integer> allDeck = Deck.DECK_NO;//获取所有的牌的编号
        allDeck.forEach(integer -> checkHuList.add(new CheckHu(this.integerList, integer)));//遍历所有牌的编号，传入对象中
        ExecutorService threadPool = Executors.newFixedThreadPool(allDeck.size());//创建线程池，每张牌为一个线程
        List<Future<Map<Integer, Boolean>>> futures = new ArrayList<>();//创建线程执行结果的集合
        try {
            checkHuList.forEach(checkHu -> futures.add(threadPool.submit(checkHu)));//递交对象任务到线程池，自动调用call方法，返回结果到集合中
        } finally {
            threadPool.shutdown();//关闭线程池
        }
        List<Integer> out = new ArrayList<>();//创建返回结果的集合
        futures.forEach(f -> {
            try {
                f.get().forEach((a, b) -> {
                    if (b) out.add(a);//遍历每一个线程的结果，将结果为true对应的牌的编号传入返回集合
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        return out;//返回结果
    }

    /**
     * 多线程遍历多个待测牌的方法
     * @return
     * @throws Exception
     */
    @Override
    public Map<List<Integer>, List<Integer>> call() throws Exception {
        return Map.of(this.integerList, findHu());//key=待测牌集合,value=听牌集合
    }
}