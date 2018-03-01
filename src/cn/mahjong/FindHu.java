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
    private List<Integer> integerList;

    public FindHu(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public List<Integer> findHu() {
        List<CheckHu> checkHuList = new ArrayList<>();
        List<Integer> allDeck = Deck.DECK_NO;
        allDeck.forEach(integer -> {
            checkHuList.add(new CheckHu(this.integerList, integer));
        });
        ExecutorService threadPool = Executors.newFixedThreadPool(allDeck.size());
        List<Future<Map<Integer, Boolean>>> futures = new ArrayList<>();
        try {
            checkHuList.forEach(checkHu -> futures.add(threadPool.submit(checkHu)));
        } finally {
            threadPool.shutdown();
        }
        List<Integer> out = new ArrayList<>();
        futures.forEach(f -> {
            try {
                f.get().forEach((a, b) -> {
                    if (b) out.add(a);
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        return out;
    }

    @Override
    public Map<List<Integer>, List<Integer>> call() throws Exception {
        return Map.of(this.integerList, findHu());
    }
}