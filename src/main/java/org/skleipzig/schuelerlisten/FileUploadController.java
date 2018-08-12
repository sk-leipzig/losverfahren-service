package org.skleipzig.schuelerlisten;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private SchuelerListenRepository schuelerListenRepository;

    @PostMapping("/schuelerlisten/upload")
    public void handleFileUpload(@RequestParam("schuelerliste") MultipartFile file,
                    @RequestParam("losverfahrenId") String losverfahrenId, HttpServletResponse response) {

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

    private int addRandom(InputStream inputStream, OutputStream outputStream, String losverfahrenId)
                    throws IOException {
        Schuelerliste schuelerliste = new Schuelerliste(losverfahrenId);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        KennungFactory kennungFactory = new KennungFactory();
        Iterator<Row> rowIterator = datatypeSheet.iterator();

        while (rowIterator.hasNext()) {

            Row currentRow = rowIterator.next();
            String klasse = currentRow.getCell(2).getStringCellValue();
            String kennung = kennungFactory.createKennung(10);
            Cell newCell = currentRow.createCell(3, CellType.STRING);
            newCell.setCellValue(kennung);
            Schueler schueler = new Schueler(kennung, klasse);
            schueler.choose(1, "1");
            schuelerliste.add(schueler);
        }
        workbook.write(outputStream);
        workbook.close();

        schuelerListenRepository.findAllByLosverfahrenId(losverfahrenId);
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