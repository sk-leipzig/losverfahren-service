package org.skleipzig.kurse;

import static org.skleipzig.schuelerlisten.FileUploadController.KENNUNG_COLUMN_INDEX;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.skleipzig.losverfahren.Losverfahren;
import org.skleipzig.losverfahren.LosverfahrenRepository;
import org.skleipzig.schuelerauswahl.SchuelerAuswahl;
import org.skleipzig.schuelerauswahl.SchuelerAuswahlRepository;
import org.skleipzig.schuelerlisten.FileUploadController;
import org.skleipzig.schuelerlisten.Schueler;
import org.skleipzig.schuelerlisten.SchuelerListenRepository;
import org.skleipzig.schuelerlisten.Schuelerliste;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@Controller
public class ErgebnisService {

    private static final Logger log = LoggerFactory.getLogger(ErgebnisService.class);

    @Autowired
    private AuswertungService auswertungService;
    @Autowired
    private LosverfahrenRepository losverfahrenRepository;
    @Autowired
    private SchuelerListenRepository schuelerListenRepository;
    @Autowired
    private SchuelerAuswahlRepository schuelerAuswahlRepository;

    @PostMapping("ergebnis/download")
    public void getResult(@RequestParam("schuelerliste") MultipartFile file,
                    @RequestParam("losverfahrenId") Integer losverfahrenId, HttpServletResponse response) {
        log.info("Empfange Schuelerliste: " + file.getName() + "[" + file.getSize() + " bytes] für Losverfahren mit id="
                        + losverfahrenId);
        try (InputStream inputStream = file.getInputStream()) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            sendDownloadToClient("schuelerliste.xlsx", response, 7500);
            fillResults(bufferedInputStream, response.getOutputStream(), losverfahrenId);
            response.flushBuffer();
        } catch (Exception e) {
            log.error("error processing excel file", e);
        }

    }

    private void fillResults(InputStream inputStream, OutputStream outputStream, Integer losverfahrenId)
                    throws IOException {
        Losverfahren losverfahren = losverfahrenRepository.findById(losverfahrenId).orElseThrow(
                        () -> new IllegalStateException("Kein Losverfahren gefunden für id=" + losverfahrenId));
        Collection<Schuelerliste> schuelerlisten = schuelerListenRepository.findAllByLosverfahrenId(losverfahrenId);
        List<Schueler> schuelerliste;
        if (schuelerlisten.size() == 0)
            throw new IllegalStateException("Keine Schülerliste für Losverfahren " + losverfahren.getName());
        else if (schuelerlisten.size() > 1)
            throw new IllegalStateException("Mehr als eine Schüerliste für Losverfahren " + losverfahren.getName());
        else
            schuelerliste = schuelerlisten.iterator().next().getSchuelerListe();
        Collection<SchuelerAuswahl> schuelerAuswahlListe = schuelerAuswahlRepository
                        .findAllByLosverfahrenId(losverfahrenId);

        Map<String, List<String>> schuelerAuswahl = schuelerAuswahlListe.stream().collect(
                        Collectors.toMap(auswahl -> auswahl.getSchueler().getKennung(), SchuelerAuswahl::getAuswahl));
        Map<String, String> auswertung = auswertungService.auswertung(losverfahren, schuelerAuswahlListe,
                        schuelerliste);

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = datatypeSheet.iterator();

        while (rowIterator.hasNext()) {
            Row currentRow = rowIterator.next();
            String kennung = FileUploadController.readStringFromCell(currentRow.getCell(KENNUNG_COLUMN_INDEX));
            log.info("Suche Eintrag für Kennung " + kennung);
            currentRow.createCell(KENNUNG_COLUMN_INDEX + 1, CellType.STRING).setCellValue(auswertung.get(kennung));
            log.info("gewählt: " + auswertung.get(kennung));
            for (int i = 0; i < Optional.ofNullable(schuelerAuswahl.get(kennung)).map(Collection::size)
                            .orElse(0); i++) {
                currentRow.createCell(KENNUNG_COLUMN_INDEX + 2 + i, CellType.STRING)
                                .setCellValue(schuelerAuswahl.get(kennung).get(i));
            }
        }

        Sheet kursSheet = workbook.createSheet("Kurse");
        Row headLine = kursSheet.createRow(0);
        headLine.createCell(0, CellType.STRING).setCellValue("Kursname");
        headLine.createCell(1, CellType.STRING).setCellValue("Kapazität");
        headLine.createCell(2, CellType.STRING).setCellValue("belegt");
        int rowNum = 1;
        for (Kurs kurs : losverfahren.getKurse()) {
            log.info("Füge Kurs " + kurs.getName() + " in Zeile " + rowNum + " hinzu.");
            Row kursRow = kursSheet.createRow(rowNum);
            kursRow.createCell(0, CellType.STRING).setCellValue(kurs.getName());
            kursRow.createCell(1, CellType.NUMERIC).setCellValue(kurs.getPlaetze());
            long count = auswertung.values().stream().filter(kursName -> kurs.getName().equals(kursName)).count();
            kursRow.createCell(2, CellType.NUMERIC).setCellValue(count);
            rowNum++;
        }

        workbook.write(outputStream);
        workbook.close();
    }

    private void sendDownloadToClient(String fileName, HttpServletResponse response, int downloadSize)
                    throws IOException {
        String mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName + "\""));
        log.info("sending " + fileName);
    }

}
