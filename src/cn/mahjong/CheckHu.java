package cn.mahjong;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public class CheckHu implements Callable<Map<Integer, Boolean>> {
    private Integer newDeck;
    private List<Integer> checkDeckList;
    private Map<Integer, Long> map;

    public CheckHu(List<Integer> checkDeckList) {
        this.checkDeckList = checkDeckList;
    }

    public CheckHu(List<Integer> checkDeckList, Integer newDeck) {
        this.newDeck = newDeck;
        this.checkDeckList = checkDeckList;
    }

    @Override
    public Map<Integer, Boolean> call() throws Exception {
        return Map.of(newDeck, isHu());
    }

    public boolean isHu() {
        return init.getAsBoolean() && (isQiDuiZi.getAsBoolean() || isGuoShiWuShuang.getAsBoolean() || is3n2.getAsBoolean());
    }

    private BooleanSupplier init = () -> {
        List<Integer> list = new ArrayList<>(this.checkDeckList);
        if (newDeck != null) list.add(newDeck);
        Collections.sort(list);
        this.checkDeckList = list;
        this.map = list.stream().collect(Collectors.groupingBy(integer -> integer, Collectors.counting()));
        return !check4count();
    };

    private boolean check4count() {
        return this.map.values().stream().max(Comparator.comparing(count -> count)).get().intValue() > 4;
    }

    private BooleanSupplier isQiDuiZi = () -> this.map.keySet().size() == 7 && this.map.values().stream().filter(count -> count == 2).count() == 7;

    private BooleanSupplier isGuoShiWuShuang = () -> this.map.keySet().size() == 13 && this.map.keySet().stream().filter(a -> a / 10 == 4 || a % 10 == 9 || a % 10 == 1).count() == 13;

    private BooleanSupplier is3n2 = () -> {
        int count = checkDeckList.size();
        if (count >= 2 && count <= 14 && (count - 2) % 3 == 0) {
            List<Integer> duiDeckList = new ArrayList<>();
            this.map.forEach((a, b) -> {
                if (b > 1) duiDeckList.add(a);
            });
            for (Integer a : duiDeckList) {
                List<Integer> testList = new LinkedList<>(this.checkDeckList);
                testList.remove(a);
                testList.remove(a);
                if (_A(_A(_A(_A(testList)))).size() == 0) return true;
                if (_A(_A(_A(_B(testList)))).size() == 0) return true;
                if (_A(_A(_B(_A(testList)))).size() == 0) return true;
                if (_A(_A(_B(_B(testList)))).size() == 0) return true;
                if (_A(_B(_A(_A(testList)))).size() == 0) return true;
                if (_A(_B(_A(_B(testList)))).size() == 0) return true;
                if (_A(_B(_B(_A(testList)))).size() == 0) return true;
                if (_A(_B(_B(_B(testList)))).size() == 0) return true;
                if (_B(_A(_A(_A(testList)))).size() == 0) return true;
                if (_B(_A(_A(_B(testList)))).size() == 0) return true;
                if (_B(_A(_B(_A(testList)))).size() == 0) return true;
                if (_B(_A(_B(_B(testList)))).size() == 0) return true;
                if (_B(_B(_A(_A(testList)))).size() == 0) return true;
                if (_B(_B(_A(_B(testList)))).size() == 0) return true;
                if (_B(_B(_B(_A(testList)))).size() == 0) return true;
                if (_B(_B(_B(_B(testList)))).size() == 0) return true;
            }
        }
        return false;
    };

    private List<Integer> _A(List<Integer> list) {
        for (Integer integer : list) {
            List<Integer> testList = new LinkedList<>(list);
            if (breakDeckList(testList, integer, integer, integer)) return testList;
        }
        return list;
    }

    private List<Integer> _B(List<Integer> list) {
        List<Integer> tempList = new LinkedList<>(list);
        for (Integer integer : tempList) {
            if (integer / 10 == 4) continue;
            List<Integer> testList = new LinkedList<>(list);
            if (breakDeckList(testList, integer, integer + 1, integer + 2)) return testList;
        }
        return list;
    }

    private boolean breakDeckList(List<Integer> list, Integer i1, Integer i2, Integer i3) {
        return list.remove(i1) && list.remove(i2) && list.remove(i3);
    }
}