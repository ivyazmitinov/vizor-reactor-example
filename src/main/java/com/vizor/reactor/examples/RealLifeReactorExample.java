package com.vizor.reactor.examples;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RealLifeReactorExample {

    public static void main(String[] args) {
        Holder h = new Holder();
        cars()
                .flatMap(c -> Flux.fromIterable(IntStream.rangeClosed(1, 100)
                        .mapToObj(v -> c)
                        .collect(Collectors.toList())))
                .subscribeOn(Schedulers.elastic())
                .doOnNext(car -> System.out.printf("[%d] %s generated on %s%n", System.currentTimeMillis(), car, Thread.currentThread().getName()))
                .flatMap(car -> rating(car.manufacturerId)
                        .map(r -> {
                            car.setRating(r);
                            return car;
                        }).subscribeOn(Schedulers.parallel()))
                .flatMap(c -> Flux.just(c).doOnNext(c2 -> h.incAndSout()).subscribeOn(Schedulers.parallel()))
                .collectList()
                .subscribe(RealLifeReactorExample::printCars);

        System.out.println("Start waiting...");
        while (true) {

        }
    }

    private static void printCars(List<Car> cars) {
        System.out.printf("[%d] Subscription on %s%n", System.currentTimeMillis(), Thread.currentThread().getName());
        System.out.println("Cars are:");
        cars.forEach(System.out::println);
    }

    private static Mono<Float> rating(Integer manufacturerId) {

        return Mono.fromCallable(() -> {
            // Long running blocking code
            System.out.printf("[%d] Fetching rating on %s%n", System.currentTimeMillis(), Thread.currentThread().getName());
            simulateDelay();
            switch (manufacturerId) {
                case 2:
                    return 4f;
                case 3:
                    return 4.1f;
                case 7:
                    return 4.2f;
                default:
                    return 5f;
            }
        }).onErrorReturn(-1f);
    }

    private static Flux<Car> cars() {
        return Flux.fromIterable(() -> {
            System.out.printf("[%d] Fetching cars on %s%n", System.currentTimeMillis(), Thread.currentThread().getName());
            simulateDelay();
            return Arrays.asList(new Car(1, 3, "Fiesta", 2017),
                    new Car(2, 7, "Camry", 2014),
                    new Car(3, 2, "M2", 2008)
            ).iterator();
        });
    }

    private static void simulateDelay() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class Holder {
        int c;

        public void incAndSout() {
            System.out.println(c);
            c++;
        }
    }
}
