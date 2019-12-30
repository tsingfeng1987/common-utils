package com.qing.common.utils;

import org.junit.Test;

public class BMICalculatorTest {

    @Test
    public void testCalculate() {
        BMICalculator bmiCalculator = new BMICalculator();
        int weight = 80;
        double height = 1.67;
        double bmi = bmiCalculator.calculate(weight, height);
        System.out.println(bmi);
    }

    @Test
    public void testASCII() {
        String singleLine = "--";
        String multiLineStart = "/*";
        String multiLineEnd = "*/";
    }
}
