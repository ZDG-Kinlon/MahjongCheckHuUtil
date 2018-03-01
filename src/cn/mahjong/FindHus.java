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
    private List<List<Integer>> lists;
    private int nThreads = 4;

    public FindHus(List<List<Integer>> lists) {
        this.lists = lists;
    }

    public Map<List<Integer>, List<Integer>> findHu() {
        ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);

        List<FindHu> findHus = new ArrayList<>();
        lists.forEach(list -> findHus.add(new FindHu(list)));
        List<Future<Map<List<Integer>, List<Integer>>>> futures = new ArrayList<>();

        try {
            findHus.forEach(findHu -> futures.add(threadPool.submit(findHu)));
        } finally {
            threadPool.shutdown();
        }
        Map<List<Integer>, List<Integer>> out = new HashMap<>();
        futures.forEach(f -> {
            try {
                f.get().forEach(out::put);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        return out;
    }

    public void setnThreads(int nThreads) {
        this.nThreads = nThreads;
    }
}


