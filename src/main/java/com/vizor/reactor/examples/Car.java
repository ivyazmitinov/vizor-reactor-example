package com.vizor.reactor.examples;

class Car {
    int id;
    int manufacturerId;
    String model;
    int year;
    Float rating;

    public Car(int id, int manufacturerId, String model, int year) {
        this.id = id;
        this.manufacturerId = manufacturerId;
        this.model = model;
        this.year = year;
    }

    public Car(int id, int manufacturerId, String model, int year, float rating) {
        this.id = id;
        this.manufacturerId = manufacturerId;
        this.model = model;
        this.year = year;
        this.rating = rating;
    }

    void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", manufacturerId=" + manufacturerId +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", rating=" + rating +
                '}';
    }
}