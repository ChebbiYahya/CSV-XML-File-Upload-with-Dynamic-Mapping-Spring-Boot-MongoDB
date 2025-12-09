package com.bank.uploadfileanddatapersistwithmongodb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

//EmployeeMappingProperties : Le traducteur des YAML

//@Component : Spring va créer un bean de cette classe.
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeMappingProperties {
    private CsvMapping csv;
    private XmlMapping xml;

//    méthode est appelée après la création du bean.
    @PostConstruct
    public void load() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ClassLoader cl = getClass().getClassLoader();

        // Chargement CSV
        try (InputStream csvIs = cl.getResourceAsStream("mapping/employees-csv.yml")) {
            if (csvIs == null) {
                throw new IllegalStateException("employees-csv.yml not found in resources");
            }
            this.csv = mapper.readValue(csvIs, CsvMapping.class);
        }

        // Chargement XML
        try (InputStream xmlIs = cl.getResourceAsStream("mapping/employees-xml.yml")) {
            if (xmlIs == null) {
                throw new IllegalStateException("employees-xml.yml not found in resources");
            }
            this.xml = mapper.readValue(xmlIs, XmlMapping.class);
        }
    }

    // ====== Inner classes ======

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CsvMapping {
        private String delimiter;
        private boolean hasHeader;
        private List<CsvColumn> columns;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CsvColumn {
        private String name; // nom logique (DTO)
        private Integer index; // optionnel
        private String header; // nom de la colonne dans le CSV
        private String type;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class XmlMapping {
        private String rootElement;
        private String recordElement;
        private List<XmlField> fields;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class XmlField {
        private String name;
        private String tag;
        private String type;
    }
}
