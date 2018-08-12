package org.skleipzig;

import org.skleipzig.kurse.Kurs;
import org.skleipzig.losverfahren.Losverfahren;
import org.skleipzig.losverfahren.LosverfahrenRepository;
import org.skleipzig.schuelerauswahl.SchuelerAuswahlRepository;
import org.skleipzig.schuelerlisten.SchuelerListenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class LosverfahrenManagerApplication implements CommandLineRunner {

    @Autowired
    private RepositoryRestConfiguration repositoryRestConfiguration;
    @Autowired
    private LosverfahrenRepository losverfahrenRepository;
    @Autowired
    private SchuelerListenRepository schuelerListenRepository;
    @Autowired
    private SchuelerAuswahlRepository schuelerAuswahlRepository;

    public static void main(String[] args) {
        SpringApplication.run(LosverfahrenManagerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        repositoryRestConfiguration.exposeIdsFor(Losverfahren.class, Kurs.class);
        /*
        losverfahrenRepository.deleteAll();
        schuelerListenRepository.deleteAll();
        */
        schuelerAuswahlRepository.deleteAll();
    }
}
