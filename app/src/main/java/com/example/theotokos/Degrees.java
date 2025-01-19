package com.example.theotokos;

public class Degrees {
    private int firstTerm, secondTerm, thirdTerm;

    public Degrees(){

    }
    public Degrees(int firstTerm, int secondTerm, int thirdTerm) {
        this.firstTerm = firstTerm;
        this.secondTerm = secondTerm;
        this.thirdTerm = thirdTerm;
    }

    public int getFirstTerm() {
        return firstTerm;
    }

    public void setFirstTerm(int firstTerm) {
        this.firstTerm = firstTerm;
    }

    public int getSecondTerm() {
        return secondTerm;
    }

    public void setSecondTerm(int secondTerm) {
        this.secondTerm = secondTerm;
    }

    public int getThirdTerm() {
        return thirdTerm;
    }

    public void setThirdTerm(int thirdTerm) {
        this.thirdTerm = thirdTerm;
    }
}
