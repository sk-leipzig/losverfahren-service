package org.skleipzig.schuelerlisten;

import java.util.HashMap;
import java.util.Map;

import org.skleipzig.kurse.Kurs;

/**
 * Ein Teilnehmer an einem Losverfahren. Speichert die Auswahl, die der
 * Teilnehmer für das Losverfahren getroffen hat.
 *
 * @author Stefan
 */
public class Schueler {
    private String kennung;
    private String klasse;
    /**
     * Als Schlüssel wird die Priorität (1 = Erste Wahl, 2 = Zweite Wahl, usw.)
     * und als Wert wird {@link Kurs#getId()} verwendet.
     */
    private Map<Integer, String> auswahl;

    public Schueler() {
        auswahl = new HashMap<>();
    }

    public Schueler(String kennung, String klasse) {
        this();
        this.kennung = kennung;
        this.klasse = klasse;
    }

    public void choose(int prio, String kursId) {
        auswahl.put(prio, kursId);
    }

    public String getKennung() {
        return kennung;
    }

    public void setKennung(String kennung) {
        this.kennung = kennung;
    }

    public String getKlasse() {
        return klasse;
    }

    public void setKlasse(String klasse) {
        this.klasse = klasse;
    }

    public Map<Integer, String> getAuswahl() {
        return auswahl;
    }

    public void setAuswahl(Map<Integer, String> auswahl) {
        this.auswahl = auswahl;
    }

    @Override
    public String toString() {
        return "Schueler [kennung=" + kennung + ", klasse=" + klasse + ", auswahl=" + auswahl + "]";
    }
}
