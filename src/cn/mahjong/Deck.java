package cn.mahjong;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    public static final List<Integer> DECK_NO = List.of(
            11, 12, 13, 14, 15, 16, 17, 18, 19,
            21, 22, 23, 24, 25, 26, 27, 28, 29,
            31, 32, 33, 34, 35, 36, 37, 38, 39,
            41, 42, 43, 44, 45, 46, 47
    );

    private static final List<String> DECK_STR = List.of(
            "一万", "二万", "三万", "四万", "五万", "六万", "七万", "八万", "九万",
            "一筒", "二筒", "三筒", "四筒", "五筒", "六筒", "七筒", "八筒", "九筒",
            "一条", "二条", "三条", "四条", "五条", "六条", "七条", "八条", "九条",
            "东风", "西风", "南风", "北风", "红中", "白板", "发财"
    );

    public static String getDeckByStr(int num) throws IndexOutOfBoundsException {
        return DECK_STR.get(DECK_NO.indexOf(num));
    }

    public static Integer getDeckByNO(String str) throws IndexOutOfBoundsException {
        return DECK_NO.get(DECK_STR.indexOf(str));
    }

    public static List<Integer> toDeckNO(List<String> stringList) throws IndexOutOfBoundsException {
        List<Integer> out = new ArrayList<>();
        stringList.forEach(s -> out.add(getDeckByNO(s)));
        return out;
    }

    public static  List<String> toDeckStr(List<Integer> intList) throws IndexOutOfBoundsException {
        List<String> out = new ArrayList<>();
        intList.forEach(s -> out.add(getDeckByStr(s)));
        return out;
    }
}
