package com.vizor.reactor.examples;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class RealLifeCompletableFutureExample {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        List<Car> join = cars().thenCompose(cars -> {
            List<CompletionStage<Car>> updatedCars = cars
                    .stream()
                    .map(car -> rating(car.manufacturerId)
                            .thenApply(r -> {
                                car.setRating(r);
                                return car;
                            }))
                    .collect(Collectors.toList());
            CompletableFuture<Void> done = CompletableFuture
                    .allOf(updatedCars.toArray(new CompletableFuture[updatedCars.size()]));
            return done.thenApply(v -> updatedCars.stream().map(CompletionStage::toCompletableFuture)
                    .map(CompletableFuture::join).collect(Collectors.toList()));
        }).whenComplete((cars, th) -> {
            if (th == null) {
                cars.forEach(System.out::println);
            } else {
                throw new RuntimeException(th);
            }
        }).toCompletableFuture().join();

        long end = System.currentTimeMillis();

        System.out.println("Took " + (end - start) + " ms.");
        while (true) {

        }
    }

    static CompletionStage<Float> rating(int manufacturer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                simulateDelay();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            switch (manufacturer) {
                case 2:
                    return 4f;
                case 3:
                    return 4.1f;
                case 7:
                    return 4.2f;
                default:
                    return 5f;
            }
        }).exceptionally(th -> -1f);
    }

    public static CompletionStage<List<Car>> cars() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                simulateDelay();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Arrays.asList(new Car(1, 3, "Fiesta", 2017),
                    new Car(2, 7, "Camry", 2014),
                    new Car(3, 2, "M2", 2008));
        });
    }

    private static void simulateDelay() throws InterruptedException {
        Thread.sleep(5000);
    }
}