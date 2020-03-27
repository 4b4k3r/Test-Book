package com.jm.codewars;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Examples2 {

    public static void main(String[] args) {
        System.out.println(mix("Are they here", "yes, they are here"));
    }

    public static String mix(String s1, String s2) {

        HashMap<String, Integer> s1Hash = new HashMap<>();
        HashMap<String, Integer> s2Hash = new HashMap<>();
        HashMap<String, Integer> s3Hash = new HashMap<>();
        HashMap<String, Integer> s4Hash = new HashMap<>();

        Stream.of((s1 + s2).replaceAll("[^A-Za-z]", "").split("")).forEach(v -> s3Hash.merge(v, 1, Integer::sum));
        String used = s3Hash.keySet().stream().map(v -> s3Hash.get(v) > 1 ? v : "").collect(Collectors.joining());

        Stream.of(s1.replaceAll("[^" + used + "]", "").split("")).forEach(v -> s1Hash.merge(v + "/1", 1, Integer::sum));
        Stream.of(s2.replaceAll("[^" + used + "]", "").split("")).forEach(v -> s2Hash.merge(v + "/2", 1, Integer::sum));

//        List<String> = new LinkedList<>().addAll(s1Hash.)
        return null;
    }
}
