package com.qing.common.utils;

import java.math.BigDecimal;

public class BMICalculator {
/*    过轻：低于18.5

    正常：18.5-23.9

    过重：24-27

    肥胖：28-32

    非常肥胖, 高于32*/
    public double calculate(double weight, double height) {

        double bmi = weight / (height * height);
        bmi = BigDecimal.valueOf(bmi).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return bmi;

    }


}
