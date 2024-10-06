package com.btgpactual.fund.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import com.btgpactual.fund.model.Fund;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(MongoTemplate mongoTemplate) {
        return args -> {
            // Utilizamos una consulta vacía para contar todos los documentos en la colección 'fund'
            if (mongoTemplate.count(new Query(), Fund.class) == 0) {
                mongoTemplate.insertAll(List.of(
                        new Fund("FPV_BTG_PACTUAL_RECAUDADORA", new BigDecimal("75000"), "FPV"),
                        new Fund("FPV_BTG_PACTUAL_ECOPETROL", new BigDecimal("125000"), "FPV"),
                        new Fund("DEUDAPRIVADA", new BigDecimal("50000"), "FIC"),
                        new Fund("FDO-ACCIONES", new BigDecimal("250000"), "FIC"),
                        new Fund("FPV_BTG_PACTUAL_DINAMICA", new BigDecimal("100000"), "FPV")
                ));
                System.out.println("Base de datos inicializada con los fondos por defecto.");
            }
        };
    }
}
