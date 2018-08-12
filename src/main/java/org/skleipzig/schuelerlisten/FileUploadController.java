package org.skleipzig.schuelerlisten;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.skleipzig.schuelerauswahl.SchuelerAuswahlRepository;
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
public class FileUploadController {

    public static final int KENNUNG_COLUMN_INDEX = 4;
    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private SchuelerListenRepository schuelerListenRepository;
    @Autowired
    private SchuelerAuswahlRepository schuelerAuswahlRepository;

    @PostMapping("schuelerlisten/upload")
    public void handleFileUpload(@RequestParam("schuelerliste") MultipartFile file,
                    @RequestParam("losverfahrenId") Integer losverfahrenId, HttpServletResponse response) {

        log.info("Empfange Schuelerliste: " + file.getName() + "[" + file.getSize() + " bytes] für Losverfahren mit id="
                        + losverfahrenId);
        try (InputStream inputStream = file.getInputStream()) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            sendDownloadToClient("schuelerliste.xlsx", response, 7500);
            addRandom(bufferedInputStream, response.getOutputStream(), losverfahrenId);
            response.flushBuffer();
        } catch (Exception e) {
            log.error("error processing excel file", e);
        }
    }

    private int addRandom(InputStream inputStream, OutputStream outputStream, Integer losverfahrenId)
                    throws IOException {
        Schuelerliste schuelerliste = new Schuelerliste(losverfahrenId);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        KennungFactory kennungFactory = new KennungFactory();
        Iterator<Row> rowIterator = datatypeSheet.iterator();

        while (rowIterator.hasNext()) {

            Row currentRow = rowIterator.next();
            String klasse = Integer.toString((int) currentRow.getCell(0).getNumericCellValue());
            String kennung = kennungFactory.createKennung(losverfahrenId, 8);
            Cell newCell = currentRow.createCell(KENNUNG_COLUMN_INDEX, CellType.STRING);
            newCell.setCellValue(kennung);
            schuelerliste.add(new Schueler(kennung, klasse));
        }
        workbook.write(outputStream);
        workbook.close();

        Collection<Schuelerliste> schuelerlistenToDelete = schuelerListenRepository
                        .findAllByLosverfahrenId(losverfahrenId);
        log.info("Lösche " + schuelerlistenToDelete.size() + " alte Schülerlisten.");
        schuelerListenRepository.deleteAll(schuelerlistenToDelete);
        List<Schueler> schuelerListe = schuelerlistenToDelete.stream().map(Schuelerliste::getSchuelerListe)
                        .flatMap(Collection::stream).collect(Collectors.toList());
        log.info("Lösche " + schuelerListe.size() + " alte Auswahleinstellungen.");
        schuelerAuswahlRepository.deleteAllBySchueler(schuelerListe);
        schuelerListenRepository.insert(schuelerliste);
        log.info("neue Schülerliste:" + schuelerliste);

        return 7500;
    }

    private void sendDownloadToClient(String fileName, HttpServletResponse response, int downloadSize)
                    throws IOException {
        String mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName + "\""));
        log.info("sending " + fileName);
    }
}