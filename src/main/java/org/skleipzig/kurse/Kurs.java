package org.skleipzig.kurse;

import java.util.Arrays;

import org.springframework.data.annotation.Id;

public class Kurs {
    @Id
    private String id;
    private String name;
    private int[] klassenstufen;
    private int plaetze;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getplaetze() {
        return plaetze;
    }

    public void setplaetze(int plaetze) {
        this.plaetze = plaetze;
    }

    @Override
    public String toString() {
        return "Kurs [id=" + id + ", name=" + name + ", klassenstufen=" + Arrays.toString(klassenstufen) + ", plaetze="
                        + plaetze + "]";
    }

}
