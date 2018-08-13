package org.skleipzig.schuelerauswahl;

import java.util.ArrayList;
import java.util.List;

import org.skleipzig.schuelerlisten.Schueler;
import org.springframework.data.annotation.Id;

public class SchuelerAuswahl {
    @Id
    private Schueler schueler;
    private Integer losverfahrenId;
    private List<String> auswahl;

    public SchuelerAuswahl() {
        this.auswahl = new ArrayList<>(3);
    }

    public Schueler getSchueler() {
        return schueler;
    }

    public void setSchueler(Schueler schueler) {
        this.schueler = schueler;
    }

    public List<String> getAuswahl() {
        return auswahl;
    }

    public void setAuswahl(List<String> auswahl) {
        this.auswahl = auswahl;
    }

    public Integer getLosverfahrenId() {
        return losverfahrenId;
    }

    public void setLosverfahrenId(Integer losverfahrenId) {
        this.losverfahrenId = losverfahrenId;
    }
}
