package com.jm.codewars;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Examples {

    public static void main(String[] args) {
        System.out.println(yearsToCenturies(2020)+" siglo(s) en " + 2020 + " año(s)");
        System.out.println(scramble("hdsasdellsdasooawwadwdwawrl", "helloworld"));
        System.out.println(duplicateCount("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        System.out.println(getMiddle("test"));
        System.out.println(persistence(999));
        System.out.println(getCount("abracadabra"));
        System.out.println(digital_root(789));
        System.out.println(Arrays.toString(solution("Pares")));
        System.out.println(findShort("Frase mas corta tres"));
        System.out.println(longToIP(2147483467L));
        System.out.println(repeated(new int[]{1, 2, 3, 5, 5, 6, 7, 8, 9, 0, 1}));
    }

    public static int yearsToCenturies(int years) {
        return (int) Math.ceil((double) years / 100d);
    }

    public static boolean scramble(String str1, String str2) {
        HashMap<String, Integer> values = new HashMap<>();
        Stream.of(str1.split("")).forEach(a -> values.merge(a, 1, Integer::sum));
        Stream.of(str2.split("")).forEach(a -> values.merge(a, -1, Integer::sum));
        return values.values().stream().noneMatch(v -> v < 0);
    }

    public static String getMiddle(String word) {
        return word.length() % 2 > 0 ? String.valueOf(word.charAt((word.length() / 2))) : word.substring((int) Math.ceil(word.length() / 2d) - 1, (int) Math.ceil(word.length() / 2d) + 1);
    }

    public static int duplicateCount(String text) {
        HashMap<String, Integer> repeated = new HashMap<>();
        Stream.of(text.toLowerCase().split("")).forEach(val -> repeated.merge(val, 1, Integer::sum));
        return (int) repeated.values().stream().filter(v -> v > 1).count();
    }

    public static int getCount(String str) {
        return Stream.of(str.split("")).map(v -> v.matches("[aeiou]") ? 1 : 0).reduce(Integer::sum).get();
    }

    public static int persistence(long n) {
        String val = String.valueOf(n);
        AtomicInteger counter = new AtomicInteger(0);
        while (val.length() > 1) {
            val = String.valueOf(Stream.of(val.split("")).map(Integer::parseInt).reduce((a, b) -> a * b).get());
            counter.getAndIncrement();
        }

        return counter.get();
    }

    public static int digital_root(int n) {
        String val = String.valueOf(n);
        while (val.length() > 1) {
            val = String.valueOf(Stream.of(val.split("")).map(Integer::parseInt).reduce(Integer::sum).get());
        }

        return Integer.parseInt(val);
    }

    public static String[] solution(String s) {
        return (s + (s.length() % 2 > 0 ? "_" : "")).split("(?<=\\G..)");
    }

    public static String longToIP(long ip) {
        return Stream.of(String.format("%32s", Long.toBinaryString(ip)).replace(" ", "0")
                .split("(?<=\\G........)"))
                .map(v -> String.valueOf(Integer.parseInt(v, 2)))
                .collect(Collectors.joining("."));
    }

    public static int findShort(String s) {
        return Stream.of(s.split(" ")).map(String::length).sorted().findFirst().get();
    }

    public static Integer repeated(int[] values) {
        HashMap<Integer, Integer> valuesRepeated = new HashMap<>();

        for (int value : values) {
            if (valuesRepeated.merge(value, 1, Integer::sum) > 1)
                return value;
        }

        return -1;
    }
}
