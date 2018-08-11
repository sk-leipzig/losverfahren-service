package org.skleipzig.schuelerlisten;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test mit Spring Beans und MockMVC für das Hochladen und Abfragen von
 * hochgeladenen Daten.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ImportRestControllerIntegrationTest {
    private static final String REQUEST_URL = "/schuelerlisten";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void postExcelFile() throws Exception {
        byte[] fileBytes = Files.readAllBytes(Paths.get(this.getClass().getResource("/schuelerliste.xlsx").toURI()));
        MockMultipartFile file = new MockMultipartFile("schuelerliste", fileBytes);

        mockMvc.perform(fileUpload(REQUEST_URL).file(file)).andExpect(status().isOk());

        // .andExpect(jsonPath("$.powerSlices[0].deliveryQuantity", is(2400.0)))
        // .andExpect(jsonPath("$.powerSlices[0].power", is(100)))
        // .andExpect(jsonPath("$.powerSlices[0].powerUnit", is("UNIT_KWH_PER_HOUR")))
        // .andExpect(jsonPath("$.powerSlices[0].deliveryQuantityUnit",
        // is("UNIT_KWH")));
    }
}
