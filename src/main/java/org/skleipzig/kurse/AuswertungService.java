package org.skleipzig.kurse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.skleipzig.losverfahren.Losverfahren;
import org.skleipzig.schuelerauswahl.SchuelerAuswahl;
import org.skleipzig.schuelerlisten.Schueler;
import org.springframework.stereotype.Service;

@Service
public class AuswertungService {
    public Map<String, String> auswertung(Losverfahren losverfahren, List<SchuelerAuswahl> schuelerAuswahlListe,
                    List<Schueler> schuelerListe) {
        Map<String, String> ergebnis = new HashMap<>();

        List<SchuelerAuswahl> auswahlOffen = new ArrayList<>(schuelerAuswahlListe);
        Map<Kurs, AtomicInteger> freiePlaetze = new HashMap<>();
        losverfahren.getKurse().forEach(kurs -> freiePlaetze.put(kurs, new AtomicInteger(kurs.getPlaetze())));

        // Verteilung nach Wünschen
        verteileNachWunsch(auswahlOffen, freiePlaetze, ergebnis, wunschFunktion(1));
        verteileNachWunsch(auswahlOffen, freiePlaetze, ergebnis, wunschFunktion(2));
        verteileNachWunsch(auswahlOffen, freiePlaetze, ergebnis, wunschFunktion(3));
        List<Schueler> schuelerRest = schuelerListe.stream()
                        .filter(schueler -> !ergebnis.containsKey(schueler.getKennung())).collect(Collectors.toList());

        verteileRest(schuelerRest, freiePlaetze, ergebnis);

        return ergebnis;
    }

    private Function<List<SchuelerAuswahl>, Function<String, Optional<SchuelerAuswahl>>> wunschFunktion(int prio) {
        return wuensche -> kursName -> wuensche.stream()
                        .filter(wunsch -> kursName.equals(wunsch.getAuswahl().get(prio - 1))).findFirst();
    }

    private void verteileNachWunsch(List<SchuelerAuswahl> auswahlOffen, Map<Kurs, AtomicInteger> freiePlaetze,
                    Map<String, String> ergebnis,
                    Function<List<SchuelerAuswahl>, Function<String, Optional<SchuelerAuswahl>>> findeWunsch) {
        List<Kurs> kurse = new ArrayList<>(freiePlaetze.keySet());
        Collections.shuffle(auswahlOffen); // zufällige Reihenfolge!

        while (!kurse.isEmpty() && !auswahlOffen.isEmpty() && !freiePlaetze.isEmpty()) {
            Kurs kurs = kurse.get(0);
            Optional<SchuelerAuswahl> erstWunsch = findeWunsch.apply(auswahlOffen).apply(kurs.getName());
            if (erstWunsch.isPresent()) {
                ergebnis.put(erstWunsch.get().getSchueler().getKennung(), kurs.getName());
                int restPlaetze = freiePlaetze.get(kurs).decrementAndGet();
                if (restPlaetze == 0) {
                    kurse.remove(kurs);
                    freiePlaetze.remove(kurs);
                }
                auswahlOffen.remove(erstWunsch.get());
            } else {
                kurse.remove(kurs);
                // Keine Erstwünsche mehr für den Kurs
            }
        }
    }

    private void verteileRest(List<Schueler> schuelerListe, Map<Kurs, AtomicInteger> freiePlaetze,
                    Map<String, String> ergebnis) {
        List<Kurs> kurse = new ArrayList<>(freiePlaetze.keySet());
        Collections.shuffle(schuelerListe); // zufällige Reihenfolge!

        while (!kurse.isEmpty() && !schuelerListe.isEmpty() && !freiePlaetze.isEmpty()) {
            Kurs kurs = kurse.get(0);
            Schueler schueler = schuelerListe.get(0);
            ergebnis.put(schueler.getKennung(), kurs.getName());
            int restPlaetze = freiePlaetze.get(kurs).decrementAndGet();
            if (restPlaetze == 0) {
                kurse.remove(kurs);
                freiePlaetze.remove(kurs);
            }
            schuelerListe.remove(schueler);
        }
    }
}
