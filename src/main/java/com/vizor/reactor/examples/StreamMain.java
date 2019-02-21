package com.vizor.reactor.examples;

import java.util.stream.Stream;

public class StreamMain {

    public static void main(String[] args) {
        Stream.of(1, 2, 3, 4)
                .parallel()
                .map(integer -> {
                    System.out.println(Thread.currentThread().getName());
                    return integer + 1;
                })
                .forEach(System.out::println);

        System.out.println("finished");
    }
}
