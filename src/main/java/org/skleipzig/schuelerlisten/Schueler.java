package org.skleipzig.schuelerlisten;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.skleipzig.kurse.Kurs;

/**
 * Ein Teilnehmer an einem Losverfahren. Speichert die Auswahl, die der
 * Teilnehmer für das Losverfahren getroffen hat.
 *
 * @author Stefan
 */
public class Schueler implements Serializable {
    private String kennung;
    private String klasse;

    public Schueler() {
    }

    public Schueler(String kennung, String klasse) {
        this();
        this.kennung = kennung;
        this.klasse = klasse;
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

    @Override
    public String toString() {
        return "Schueler [kennung=" + kennung + ", klasse=" + klasse + "]";
    }
}
