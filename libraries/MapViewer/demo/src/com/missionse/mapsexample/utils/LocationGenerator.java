package com.missionse.mapsexample.utils;

import java.util.Random;

public class LocationGenerator {

    private static Random sRandom = new Random(1986);

    public static void main(String[] args) {
        System.out.println("[");
        for (int i = 1; i < 200; i++) {
            System.out.println("{ \"lat\" : " + random(39.656126, 40.253722) + ", \"lng\" : " + random(-75.669708, -74.334869) + " },");
        }
        System.out.println("{ \"lat\" : " + random(39.656126, 40.253722) + ", \"lng\" : " + random(-75.669708, -74.334869) + " }");
        System.out.println("]");
    }

    private static double random(double min, double max) {
        return sRandom.nextDouble() * (max - min) + min;
    }
}
