package org.skleipzig.schuelerlisten;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@Controller
public class FileUploadController {

    @PostMapping("/schuelerlisten")
    public String handleFileUpload(@RequestParam("schuelerliste") MultipartFile file, HttpServletResponse response) {

        System.out.println("handleFileUpload: " + file.getName() + "[" + file.getSize() + "]");
        try (InputStream inputStream = file.getInputStream()) {
            sendDownloadToClient("schuelerliste.xlsx", response, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "localhost:8080/";
    }

    private void addRandom(InputStream inputStream, OutputStream outputStream) {
        Random random = new Random();
        try {

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = datatypeSheet.iterator();

            while (rowIterator.hasNext()) {

                Row currentRow = rowIterator.next();
                Cell newCell = currentRow.createCell(2, CellType.NUMERIC);
                newCell.setCellValue(random.nextInt(99999999));
            }

            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendDownloadToClient(String fileName, HttpServletResponse response, InputStream inputStream)
                    throws IOException {
        String mimeType = "application/vnd.ms-excel";
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName + "\""));
        response.setContentLength(inputStream.available());
        addRandom(inputStream, response.getOutputStream());
    }
}