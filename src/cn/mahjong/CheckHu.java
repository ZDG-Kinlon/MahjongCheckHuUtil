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
    private Integer newDeck;//待测牌
    private List<Integer> checkDeckList;//待测牌集合
    private Map<Integer, Long> map;//每张牌的数量

    public CheckHu(List<Integer> checkDeckList) {
        this.checkDeckList = checkDeckList;
    }

    public CheckHu(List<Integer> checkDeckList, Integer newDeck) {
        this.newDeck = newDeck;
        this.checkDeckList = checkDeckList;
    }

    /**
     * 多线程返回值方法
     *
     * @return
     * @throws Exception
     */
    @Override
    public Map<Integer, Boolean> call() throws Exception {
        return Map.of(newDeck, isHu());
    }

    /**
     * 根据待测牌集合判断是否为胡牌牌型
     *
     * @return
     */
    public boolean isHu() {
        return init.getAsBoolean() && (isQiDuiZi.getAsBoolean() || isGuoShiWuShuang.getAsBoolean() || is3n2.getAsBoolean());
    }

    //初始化方法
    private BooleanSupplier init = () -> {
        List<Integer> list = new ArrayList<>(this.checkDeckList);
        if (newDeck != null) list.add(newDeck);//如果是另一个待测牌存在，添加进待测集合中
        Collections.sort(list);//必须排序，链牌拆解的时候需要从小到大遍历
        this.checkDeckList = list;
        this.map = list.stream().collect(Collectors.groupingBy(integer -> integer, Collectors.counting()));//使用流统计每张牌的数量
        return !check4count();//判断单张牌是否超过4张
    };

    /**
     * 判断单张牌是否超过4张
     *
     * @return
     */
    private boolean check4count() {
        return this.map.values().stream().max(Comparator.comparing(count -> count)).get().intValue() > 4;//使用流遍历出单张牌最多的张数
    }

    //使用流判断是否为七对子的Lambda表达式，先判断去重复后的牌的数量是否为7张，然后过滤每张牌的个数是为2张的结果的个数是否为7张，
    private BooleanSupplier isQiDuiZi = () -> this.map.keySet().size() == 7 && this.map.values().stream().filter(count -> count == 2).count() == 7;

    //使用流判断是否为国士无双的Lambda表达式，先判断去重复后的牌的数量是否为13张，然后过滤每张牌必须为40以上的杂牌，或者40以下的边牌的结果的个数是否为13张
    private BooleanSupplier isGuoShiWuShuang = () -> this.map.keySet().size() == 13 && this.map.keySet().stream().filter(a -> a / 10 == 4 || a % 10 == 9 || a % 10 == 1).count() == 13;

    //使用流判断是否为3n+2牌型的Lambda表达式
    private BooleanSupplier is3n2 = () -> {
        int count = checkDeckList.size();//判断待测牌的张数是否满足3n+2，且必须在2至14张之内
        if (count >= 2 && count <= 14 && (count - 2) % 3 == 0) {
            List<Integer> duiDeckList = new ArrayList<>();//使用流遍历找出可能为将牌(2)的所有可能，不是单张牌就添加
            this.map.forEach((a, b) -> {
                if (b > 1) duiDeckList.add(a);
            });
            for (Integer a : duiDeckList) {//遍历将牌
                List<Integer> testList = new LinkedList<>(this.checkDeckList);
                testList.remove(a);//去除两张将牌，然后根据刻牌和链牌进行3n拆解
                testList.remove(a);
                if (_A(_A(_A(_A(testList)))).size() == 0) return true;//刻 刻 刻 刻
                if (_A(_A(_A(_B(testList)))).size() == 0) return true;//链 刻 刻 刻
                if (_A(_A(_B(_A(testList)))).size() == 0) return true;//刻 链 刻 刻
                if (_A(_A(_B(_B(testList)))).size() == 0) return true;//链 链 刻 刻
                if (_A(_B(_A(_A(testList)))).size() == 0) return true;//刻 刻 链 刻
                if (_A(_B(_A(_B(testList)))).size() == 0) return true;//链 刻 链 刻
                if (_A(_B(_B(_A(testList)))).size() == 0) return true;//刻 链 链 刻
                if (_A(_B(_B(_B(testList)))).size() == 0) return true;//链 链 链 刻
                if (_B(_A(_A(_A(testList)))).size() == 0) return true;//刻 刻 刻 链
                if (_B(_A(_A(_B(testList)))).size() == 0) return true;//链 刻 刻 链
                if (_B(_A(_B(_A(testList)))).size() == 0) return true;//刻 链 刻 链
                if (_B(_A(_B(_B(testList)))).size() == 0) return true;//链 链 刻 链
                if (_B(_B(_A(_A(testList)))).size() == 0) return true;//刻 刻 链 链
                if (_B(_B(_A(_B(testList)))).size() == 0) return true;//链 刻 链 链
                if (_B(_B(_B(_A(testList)))).size() == 0) return true;//刻 链 链 链
                if (_B(_B(_B(_B(testList)))).size() == 0) return true;//链 链 链 链
            }
        }
        return false;//最多只有4组3n组合，如果都尝试过没有能拆解完毕，则不满足3n+2牌型
    };

    /**
     * 刻牌拆解方法
     *
     * @param list
     * @return
     */
    private List<Integer> _A(List<Integer> list) {
        for (Integer integer : list) {//遍历每张牌
            List<Integer> testList = new LinkedList<>(list);//创建测试牌集合
            if (breakDeckList(testList, integer, integer, integer)) return testList;//尝试拆解，如果拆解成功，返回拆解后的牌集合
        }
        return list;//拆解失败，返回原先传入的待测牌组
    }

    /**
     * 链牌拆解方法
     *
     * @param list
     * @return
     */
    private List<Integer> _B(List<Integer> list) {
        for (Integer integer : list) {//遍历每张牌
            if (integer / 10 == 4) continue;//杂牌无法构成链牌
            List<Integer> testList = new LinkedList<>(list);//创建测试牌集合
            if (breakDeckList(testList, integer, integer + 1, integer + 2)) return testList;//尝试拆解，如果拆解成功，返回拆解后的牌集合
        }
        return list;
    }

    /**
     * 拆解牌的方法
     *
     * @param list
     * @param i1
     * @param i2
     * @param i3
     * @return
     */
    private boolean breakDeckList(List<Integer> list, Integer i1, Integer i2, Integer i3) {
        return list.remove(i1) && list.remove(i2) && list.remove(i3);//尝试删除传入的三张牌，有一张删除失败返回false
    }
}