package com.greencross.gctemperlib.collection;

/**
 * Created by MobileDoctor on 2017-03-21.
 */

public class EpidemicItem {

    private int dzNum;
    private String dzName;
    private int weekago_1;
    private int weekago_2;

    public double ratio;

    private boolean isNew = false;

    public int getDzNum() {
        return dzNum;
    }

    public void setDzNum(int dzNum) {
        this.dzNum = dzNum;
    }

    public String getDzName() {
        return dzName;
    }

    public void setDzName(String dzName) {
        this.dzName = dzName;
    }

    public int getWeekago_1() {
        return weekago_1;
    }

    public void setWeekago_1(int weekago_1) {
        this.weekago_1 = weekago_1;
    }

    public int getWeekago_2() {
        return weekago_2;
    }

    public void setWeekago_2(int weekago_2) {
        this.weekago_2 = weekago_2;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
