package com.example.theotokos;

public class Degrees {
    private Term firstTerm, secondTerm, thirdTerm;

    public Degrees(){

    }
    public Degrees(Term firstTerm, Term secondTerm, Term thirdTerm) {
        this.firstTerm = firstTerm;
        this.secondTerm = secondTerm;
        this.thirdTerm = thirdTerm;
    }

    public Term getFirstTerm() {
        return firstTerm;
    }

    public void setFirstTerm(Term firstTerm) {
        this.firstTerm = firstTerm;
    }

    public Term getSecondTerm() {
        return secondTerm;
    }

    public void setSecondTerm(Term secondTerm) {
        this.secondTerm = secondTerm;
    }

    public Term getThirdTerm() {
        return thirdTerm;
    }

    public void setThirdTerm(Term thirdTerm) {
        this.thirdTerm = thirdTerm;
    }
}
