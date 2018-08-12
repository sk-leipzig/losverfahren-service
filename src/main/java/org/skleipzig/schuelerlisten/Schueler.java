package org.skleipzig.schuelerlisten;

import java.io.Serializable;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((kennung == null) ? 0 : kennung.hashCode());
        result = prime * result + ((klasse == null) ? 0 : klasse.hashCode());
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
        Schueler other = (Schueler) obj;
        if (kennung == null) {
            if (other.kennung != null)
                return false;
        } else if (!kennung.equals(other.kennung))
            return false;
        if (klasse == null) {
            if (other.klasse != null)
                return false;
        } else if (!klasse.equals(other.klasse))
            return false;
        return true;
    }
}
