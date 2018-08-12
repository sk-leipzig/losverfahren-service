package org.skleipzig.kurse;

import java.util.Arrays;

public class Kurs {
    private String name;
    private int[] klassenstufen;
    private int plaetze;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getKlassenstufen() {
        return klassenstufen;
    }

    public void setKlassenstufen(int[] klassenstufen) {
        this.klassenstufen = klassenstufen;
    }

    public int getPlaetze() {
        return plaetze;
    }

    public void setPlaetze(int plaetze) {
        this.plaetze = plaetze;
    }

    @Override
    public String toString() {
        return "Kurs [name=" + name + ", klassenstufen=" + Arrays.toString(klassenstufen) + ", plaetze=" + plaetze
                        + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Kurs other = (Kurs) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
