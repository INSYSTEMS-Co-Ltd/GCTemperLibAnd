package com.greencross.gctemperlib.greencare.base.value;

/**
 * Created by mrsohn on 2017. 2. 28..
 */

public class TypeDataSet {

    public enum Period {
        PERIOD_DAY
        , PERIOD_WEEK
        , PERIOD_MONTH
        , PERIOD_YEAR
        , PERIOD_PRAGNANT        // 체중차트 임신여부
    }

    public enum Type {
        TYPE_CALORY
        , TYPE_STEP
    }

    public enum EatState {
        TYPE_ALL
        , TYPE_BEFORE
        , TYPE_AFTER
    }

    public enum Currency {
        PENNY(1), NICKLE(5), DIME(10), QUARTER(25);
        private int value;

        private Currency(int value) {
            this.value = value;
        }
    };
}
