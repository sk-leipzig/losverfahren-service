package org.skleipzig.schuelerauswahl;

import org.skleipzig.kurse.Kurs;
import org.skleipzig.schuelerlisten.Schueler;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchuelerAuswahl {
    @Id
    private Schueler schueler;
    private List<String> auswahl;

    public SchuelerAuswahl() {
        this.auswahl = new ArrayList<>(3);
    }

    public SchuelerAuswahl(Schueler schueler) {
        this();
        this.schueler = schueler;
    }

    /**
     * Setzt eine neue Auswahl.
     * @param prio 1. Wunsch, 2. Wunsch, etc.
     * @param kurs Kurswunsch
     * @return <code>true</code> wenn sich durch die Auswahl etwas geändert hat.
     */
    public boolean choose(int prio, Kurs kurs)
    {
        return !kurs.getName().equals(auswahl.set(prio, kurs.getName()));
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
}
