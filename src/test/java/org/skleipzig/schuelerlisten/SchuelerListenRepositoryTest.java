package org.skleipzig.schuelerlisten;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureDataMongo
@EnableConfigurationProperties
public class SchuelerListenRepositoryTest {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private SchuelerListenRepository schuelerListenRepository;

    @Before
    public void setup() throws Exception {
        mongoTemplate.dropCollection(Schuelerliste.class);
    }

    @After
    public void tearDown() throws Exception {
        mongoTemplate.dropCollection(Schuelerliste.class);
    }

    @Test
    public void findBySchuelerlisteKennung() {
        Schuelerliste schuelerliste1 = new Schuelerliste(1);
        schuelerliste1.add(new Schueler("123456789", "5a"));
        Schuelerliste schuelerliste2 = new Schuelerliste(2);
        schuelerliste2.add(new Schueler("987654321", "5b"));

        schuelerListenRepository.insert(Arrays.asList(schuelerliste1, schuelerliste2));
        Schuelerliste schuelerliste3 = schuelerListenRepository.findBySchuelerListeKennung("123456789");
        assertThat(schuelerliste3.getLosverfahrenId(), Matchers.equalTo(1));
    }

    @Test
    public void dontFindSchuelerlisteForInvalidKennung() {
        Schuelerliste schuelerliste1 = new Schuelerliste(1);
        schuelerliste1.add(new Schueler("123456789", "5a"));
        Schuelerliste schuelerliste2 = new Schuelerliste(2);
        schuelerliste2.add(new Schueler("987654321", "5b"));

        schuelerListenRepository.insert(Arrays.asList(schuelerliste1, schuelerliste2));
        Schuelerliste schuelerliste3 = schuelerListenRepository.findBySchuelerListeKennung("6");
        assertThat(schuelerliste3, Matchers.nullValue());
    }

    @Test
    public void findByLosverfahrenId() {
        Schuelerliste schuelerliste1 = new Schuelerliste(1);
        Schuelerliste schuelerliste2 = new Schuelerliste(2);
        Schuelerliste schuelerliste3 = new Schuelerliste(2);
        Schuelerliste schuelerliste4 = new Schuelerliste(4);

        schuelerListenRepository.insert(Arrays.asList(schuelerliste1, schuelerliste2, schuelerliste3, schuelerliste4));
        Collection<Schuelerliste> schuelerliste5 = schuelerListenRepository.findAllByLosverfahrenId(2);
        assertThat(schuelerliste5.size(), Matchers.equalTo(2));
        assertThat(schuelerliste5, Matchers.everyItem(Matchers.hasProperty("losverfahrenId", Matchers.equalTo(2))));
    }
}