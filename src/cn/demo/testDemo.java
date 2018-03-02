package cn.demo;

import cn.mahjong.CheckHu;
import cn.mahjong.Deck;
import cn.mahjong.FindHu;
import cn.mahjong.FindHus;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class testDemo {

    @Test
public void checkDemo1() {
    try {
        //1.创建待测牌组，可以不按顺序
        List<String> stringList = List.of("一万", "八万", "一万", "六万", "六万", "一万", "七万", "四万", "五万", "二万", "三万", "六万", "六万", "七万");
        //2.将牌组编号化，如果不存在会抛出异常IndexOutOfBoundsException
        List<Integer> integerList = Deck.toDeckNO(stringList);
        //3.初始化对象
        CheckHu checkHu = new CheckHu(integerList);
        //4.调用方法开始判断
        boolean result = checkHu.isHu();
        //5.输出结果，是胡牌=true
        System.out.println(result);
    } catch (IndexOutOfBoundsException e) {
        e.printStackTrace();
    }
}

    @Test
public void checkDemo2() {
    try {
        //1.创建待测牌组，可以不按顺序
        List<String> stringList = List.of("一万", "八万", "一万", "六万", "六万", "一万", "七万", "五万", "二万", "三万", "六万", "六万", "七万");
        //2.创建起到的牌
        String checkDeck = "四万";
        //3.将牌组编号化，如果不存在会抛出异常IndexOutOfBoundsException
        List<Integer> integerList = Deck.toDeckNO(stringList);
        Integer checkDeckNO = Deck.getDeckByNO(checkDeck);
        //4.初始化对象，待测牌组，起到的牌
        CheckHu checkHu = new CheckHu(integerList, checkDeckNO);
        //5.调用方法开始判断
        boolean result = checkHu.isHu();
        //6.输出结果，是胡牌=true
        System.out.println(result);
    } catch (IndexOutOfBoundsException e) {
        e.printStackTrace();
    }
}

@Test
public void findDemo1() {
    try {
        //1.创建待测牌组，可以不按顺序
        List<String> stringList = List.of("一万", "一万", "六万", "六万", "七万", "一万", "二万", "五万", "六万", "三万", "四万", "六万", "八万");
        //2.将牌组编号化，如果不存在会抛出异常IndexOutOfBoundsException
        List<Integer> integerList = Deck.toDeckNO(stringList);
        //3.创建对象
        FindHu findHu = new FindHu(integerList);
        //4.调用方法开始寻找
        List<Integer> result = findHu.findHu();
        //5.转换为字符串牌组，如果不存在会抛出异常IndexOutOfBoundsException
        List<String> out = Deck.toDeckStr(result);
        //6.输出结果
        out.forEach(str -> System.out.print(str + "\t"));
    } catch (IndexOutOfBoundsException e) {
        e.printStackTrace();
    }
}

    @Test
public void findDemo2() {
    try {
        //1.创建待测牌组，可以不按顺序，需要转换为编号牌集合
        List<List<Integer>> lists = List.of(
                //七对子，听1
                Deck.toDeckNO(List.of("一万", "一万", "三万", "三万", "六条", "六条", "五筒", "五筒", "东风", "东风", "南风", "南风", "七万")),
                //国士无双，听13
                Deck.toDeckNO(List.of("一万", "九万", "一筒", "九筒", "一条", "九条", "东风", "西风", "南风", "北风", "红中", "白板", "发财")),
                //13听9
                Deck.toDeckNO(List.of("一万", "一万", "一万", "二万", "三万", "四万", "五万", "六万", "七万", "八万", "九万", "九万", "九万")),
                //13听8
                Deck.toDeckNO(List.of("一万", "一万", "一万", "二万", "三万", "四万", "五万", "六万", "六万", "六万", "六万", "七万", "八万")),
                //10听8
                Deck.toDeckNO(List.of("三万", "三万", "三万", "四万", "五万", "六万", "七万", "八万", "八万", "八万")),
                //13听7
                Deck.toDeckNO(List.of("二万", "二万", "二万", "三万", "四万", "四万", "四万", "四万", "五万", "六万", "七万", "七万", "七万")),
                //10听7
                Deck.toDeckNO(List.of("一万", "一万", "一万", "二万", "三万", "四万", "五万", "六万", "六万", "六万")),
                //10听6
                Deck.toDeckNO(List.of("三万", "三万", "三万", "四万", "四万", "四万", "五万", "六万", "七万", "八万")),
                //7听5
                Deck.toDeckNO(List.of("三万", "三万", "三万", "四万", "五万", "五万", "五万")),
                //10听4
                Deck.toDeckNO(List.of("三万", "三万", "四万", "四万", "五万", "五万", "六万", "六万", "七万", "七万")),
                //7听4
                Deck.toDeckNO(List.of("三万", "三万", "三万", "四万", "四万", "四万", "五万")),
                //4听3
                Deck.toDeckNO(List.of("三万", "三万", "三万", "二万"))
        );
        //2.创建对象，传入待测牌组集合
        FindHus findHus = new FindHus(lists);
        findHus.setnThreads(16);//设置线程池中线程的个数，默认为4
        //3.调用方法开始寻找
        Map<List<Integer>, List<Integer>> result = findHus.findHu();
        //4.对结果进行排序处理，转换为字符串牌组，并输出
        result.forEach((a, b) -> {
            List<String> out1 = Deck.toDeckStr(a);
            List<String> out2 = Deck.toDeckStr(b);
            System.out.print("待测牌：");
            out1.forEach(e -> System.out.print(e + "\t"));
            System.out.print("\n胡牌：");
            out2.forEach(e -> System.out.print(e + "\t"));
            System.out.println("\n");
        });
    } catch (IndexOutOfBoundsException e) {
        e.printStackTrace();
    }
}
}
