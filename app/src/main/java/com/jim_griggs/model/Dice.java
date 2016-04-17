package com.jim_griggs.model;

import java.io.Serializable;

/**
 * Created by TDEVJWG on 3/16/2016.
 */
public class Dice implements Serializable{
    private int mDieNum;
    private int mDieType;

    public Dice (int dieNum, int dieType) {
        this.mDieNum = dieNum;
        this.mDieType = dieType;
    }

    public void setDieNum(int dieNum) { this.mDieNum = dieNum; }
    public int getDieNum() { return this.mDieNum; }

    public void setDieType(int dieType) { this.mDieType = dieType; }
    public int getDieType() { return this.mDieType; }

    public int getTakeTen(){
        int dieValue = (mDieType/2)+1;
        return dieValue * mDieNum;
    }
}
