package com.jm.codewars;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Examples {

    public static void main(String[] args) {
        System.out.println(yearsToCenturies(2020) + " siglo(s) en " + 2020 + " año(s)");
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

    /**
     * This method search a text equality inside of another text like SCRAMBLE !!!
     *
     * @param str1 unknown text
     * @param str2 searched word
     * @return if all same words and same quantity from second string exists in first string
     */
    public static boolean scramble(String str1, String str2) {
        HashMap<String, Integer> values = new HashMap<>();
        Stream.of(str1.split("")).forEach(a -> values.merge(a, 1, Integer::sum));
        Stream.of(str2.split("")).forEach(a -> values.merge(a, -1, Integer::sum));
        return values.values().stream().noneMatch(v -> v < 0);
    }

    /**
     * Find a pair middle chars in a text
     *
     * @param word is the resource
     * @return a pair middle chars
     */
    public static String getMiddle(String word) {
        return word.length() % 2 > 0 ? String.valueOf(word.charAt((word.length() / 2))) : word.substring((int) Math.ceil(word.length() / 2d) - 1, (int) Math.ceil(word.length() / 2d) + 1);
    }

    /**
     * This method is used to find how many words are repeated
     *
     * @param text is the resource to search
     * @return how many words are repeated
     */
    public static int duplicateCount(String text) {
        HashMap<String, Integer> repeated = new HashMap<>();
        Stream.of(text.toLowerCase().split("")).forEach(val -> repeated.merge(val, 1, Integer::sum));
        return (int) repeated.values().stream().filter(v -> v > 1).count();
    }

    /**
     * This method get how many times a vocals are in a text
     *
     * @param str is a text to search
     * @return a number of how many times a vocals are in a text
     */
    public static int getCount(String str) {
        return Stream.of(str.split("")).map(v -> v.matches("[aeiou]") ? 1 : 0).reduce(Integer::sum).get();
    }

    /**
     * This method solve a quantity of multiply all separated decimal numbers on a number recursively
     *
     * <blockquote><table cellpadding=1 cellspacing=0 summary="Split examples showing regex and result">
     * <tr><th>Input</th><th>Process</th><th>Output</th></tr>
     * <tr><td>236</td><td>{@code (2 * 3 * 6) = [36] (3 * 6) = [18] (1 * 8) = [8] }</td><td>{@code 3}</td></tr>
     * </table></blockquote>
     *
     * <h3> How many times a decimal values need be multiplied to find a root number</h3>
     *
     * @param n a number to find a digital root
     * @return how many times a decimal values in a number can be multiplied recursively
     */
    public static int persistence(long n) {
        String val = String.valueOf(n);
        AtomicInteger counter = new AtomicInteger(0);
        while (val.length() > 1) {
            val = String.valueOf(Stream.of(val.split("")).map(Integer::parseInt).reduce((a, b) -> a * b).get());
            counter.getAndIncrement();
        }

        return counter.get();
    }

    /**
     * This value sum all separated decimal numbers on a number recursively
     *
     * <blockquote><table cellpadding=1 cellspacing=0 summary="Split examples showing regex and result">
     * <tr><th>Input</th><th>Process</th></tr>
     * <tr><td>123456</td><td>{@code (1 + 2 + 3 + 4 + 5 + 6) = [21] (2 + 1) = [3] }</td></tr>
     * </table></blockquote>
     *
     * @param n a number to find a digital root
     * @return digital root from a number
     */
    public static int digital_root(int n) {
        String val = String.valueOf(n);
        while (val.length() > 1) {
            val = String.valueOf(Stream.of(val.split("")).map(Integer::parseInt).reduce(Integer::sum).get());
        }

        return Integer.parseInt(val);
    }


    /**
     * This method return a char pair in array
     *
     * @param s a text to evaluate
     * @return an array with pair of chars
     */
    public static String[] solution(String s) {
        return (s + (s.length() % 2 > 0 ? "_" : "")).split("(?<=\\G..)");
    }

    /**
     * This method convert a number in a 4 byte and format each one to a single decimal
     * and format them to a ip schema
     *
     * @param ip is a 32 bits number maxim
     * @return an IP
     */
    public static String longToIP(long ip) {
        return Stream.of(String.format("%32s", Long.toBinaryString(ip)).replace(" ", "0")
                .split("(?<=\\G........)"))
                .map(v -> String.valueOf(Integer.parseInt(v, 2)))
                .collect(Collectors.joining("."));
    }

    /**
     * This method find a shortest word in a text
     *
     * @param s a text to find the shortest word
     * @return a shortest word size in a text
     */
    public static int findShort(String s) {
        return Stream.of(s.split(" ")).map(String::length).sorted().findFirst().get();
    }

    /**
     * Find a first number repeated in the array
     * or else return -1
     *
     * @param values is an int array
     * @return a first number repeated in the array
     */
    public static Integer repeated(int[] values) {
        HashMap<Integer, Integer> valuesRepeated = new HashMap<>();

        for (int value : values) {
            if (valuesRepeated.merge(value, 1, Integer::sum) > 1)
                return value;
        }

        return -1;
    }
}
