package com.qing.common.utils;

public class BMICalculator {
    public double calculate(double weight, double height) {
        double bmi = weight / (height * height);

        return bmi;

    }


}
