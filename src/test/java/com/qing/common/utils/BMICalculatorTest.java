package com.qing.common.utils;

import org.junit.Test;

public class BMICalculatorTest {

    @Test
    public void testCalculate() {
        BMICalculator bmiCalculator = new BMICalculator();
        double bmi = bmiCalculator.calculate(78, 1.66);
        System.out.println(bmi);
    }
}
