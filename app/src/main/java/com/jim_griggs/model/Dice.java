package com.jim_griggs.model;

import java.io.Serializable;

/**
 * Created by TDEVJWG on 3/16/2016.
 */
public class Dice implements Serializable{
    private int dieNum;
    private int dieType;

    public Dice (int dieNum, int dieType) {
        this.dieNum = dieNum;
        this.dieType = dieType;
    }

    public void setDieNum(int dieNum) { this.dieNum = dieNum; }
    public int getDieNum() { return this.dieNum; }

    public void setDieType(int dieType) { this.dieType = dieType; }
    public int getDieType() { return this.dieType; }
}
