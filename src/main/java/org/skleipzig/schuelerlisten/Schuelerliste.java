package org.skleipzig.schuelerlisten;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;

public class Schuelerliste {
    @Id
    private String id;
    private Integer losverfahrenId;
    private List<Schueler> schuelerListe;

    public Schuelerliste() {
        schuelerListe = new ArrayList<>();
    }

    public Schuelerliste(Integer losverfahrenId) {
        this();
        this.losverfahrenId = losverfahrenId;
    }

    public void add(Schueler schueler) {
        schuelerListe.add(schueler);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLosverfahrenId() {
        return losverfahrenId;
    }

    public void setLosverfahrenId(int losverfahrenId) {
        this.losverfahrenId = losverfahrenId;
    }

    public List<Schueler> getSchuelerListe() {
        return schuelerListe;
    }

    public void setSchuelerListe(List<Schueler> schuelerListe) {
        this.schuelerListe = schuelerListe;
    }

    @Override
    public String toString() {
        return "Schuelerliste [id=" + id + ", losverfahrenId=" + losverfahrenId + ", schuelerListe=\n"
                        + schuelerListe.stream().map(Schueler::toString).collect(Collectors.joining("\n", "\n", ""))
                        + "]";
    }
}
