package com.example.theotokos;

import androidx.annotation.Keep;

import java.io.Serializable;
@Keep
public class Term implements Serializable {
    private float hymns, taks, agbya, coptic, attencance;

    public Term() {
        hymns = taks = agbya = coptic = 0;
    }

    public Term(int hymns, int taks, int agbya, int coptic, int attencance) {
        this.hymns = hymns;
        this.taks = taks;
        this.agbya = agbya;
        this.coptic = coptic;
        this.attencance = attencance;
    }
    public float getResult(){ return hymns + taks + agbya + coptic + attencance; }

    public float getHymns() {
        return hymns;
    }

    public void setHymns(int hymns) {
        this.hymns = hymns;
    }

    public float getTaks() {
        return taks;
    }

    public void setTaks(int taks) {
        this.taks = taks;
    }

    public float getAgbya() {
        return agbya;
    }

    public void setAgbya(int agbya) {
        this.agbya = agbya;
    }

    public float getCoptic() {
        return coptic;
    }

    public void setCoptic(int coptic) {
        this.coptic = coptic;
    }

    public float getAttencance() {
        return attencance;
    }

    public void setAttencance(int attencance) {
        this.attencance = attencance;
    }
}
