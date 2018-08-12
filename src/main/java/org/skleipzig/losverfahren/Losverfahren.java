package org.skleipzig.losverfahren;

import java.util.List;

import org.skleipzig.kurse.Kurs;
import org.springframework.data.annotation.Id;

public class Losverfahren {
    @Id
    private Integer id;
    private String name;
    private boolean aktiv;
    private List<Kurs> kurse;

    public Losverfahren() {
    }

    public Losverfahren(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Kurs> getKurse() {
        return kurse;
    }

    public void setKurse(List<Kurs> kurse) {
        this.kurse = kurse;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    @Override
    public String toString() {
        return "Losverfahren [id=" + id + ", name=" + name + ", aktiv=" + aktiv + ", kurse=" + kurse + "]";
    }

}
