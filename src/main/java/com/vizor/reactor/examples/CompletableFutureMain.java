package com.vizor.reactor.examples;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class CompletableFutureMain {

    private static Random random = new Random();
    private static Executor executor = Executors.newWorkStealingPool(4);

    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> Arrays.asList(1, 2, 3))
                .thenApplyAsync(integers -> integers.stream()
                        .map(integer -> integer + 1)
                        .collect(Collectors.toList()), executor)
                .exceptionally(throwable -> null)
                .thenAccept(integers -> System.out.println(integers));

        while (true) {

        }
    }

    private static String delayedUpperCase(String s) {
        randomSleep();
        return s.toUpperCase();
    }

    private static void randomSleep() {
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            // ...
        }
    }

    private static boolean isUpperCase(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLowerCase(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
