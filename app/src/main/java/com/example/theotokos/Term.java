package com.example.theotokos;

public class Term {
    private int hymns, taks, agbya, coptic, attencance;

    public Term() {
    }

    public Term(int hymns, int taks, int agbya, int coptic, int attencance) {
        this.hymns = hymns;
        this.taks = taks;
        this.agbya = agbya;
        this.coptic = coptic;
        this.attencance = attencance;
    }

    public int getHymns() {
        return hymns;
    }

    public void setHymns(int hymns) {
        this.hymns = hymns;
    }

    public int getTaks() {
        return taks;
    }

    public void setTaks(int taks) {
        this.taks = taks;
    }

    public int getAgbya() {
        return agbya;
    }

    public void setAgbya(int agbya) {
        this.agbya = agbya;
    }

    public int getCoptic() {
        return coptic;
    }

    public void setCoptic(int coptic) {
        this.coptic = coptic;
    }

    public int getAttencance() {
        return attencance;
    }

    public void setAttencance(int attencance) {
        this.attencance = attencance;
    }
}
