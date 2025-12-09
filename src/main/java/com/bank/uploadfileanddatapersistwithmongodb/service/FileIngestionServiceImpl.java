package com.bank.uploadfileanddatapersistwithmongodb.service;

import com.bank.uploadfileanddatapersistwithmongodb.DTO.EmployeeDto;
import com.bank.uploadfileanddatapersistwithmongodb.config.EmployeeMappingProperties;
import com.bank.uploadfileanddatapersistwithmongodb.exception.*;
import com.bank.uploadfileanddatapersistwithmongodb.interfaces.EmployeeService;
import com.bank.uploadfileanddatapersistwithmongodb.interfaces.FileIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileIngestionServiceImpl implements FileIngestionService {
    private final EmployeeMappingProperties mappingProperties;
    private final EmployeeService employeeService;

//    public FileIngestionServiceImpl(EmployeeMappingProperties mappingProperties,
//                                    EmployeeService employeeService) {
//        this.mappingProperties = mappingProperties;
//        this.employeeService = employeeService;
//    }

    @Override
    public int ingestCsv(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileFormatException("CSV file is empty");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            EmployeeMappingProperties.CsvMapping csvMapping = mappingProperties.getCsv();
            String delimiter = csvMapping.getDelimiter();
            boolean hasHeader = csvMapping.isHasHeader();

            String line;
            int lineNumber = 0;
            List<EmployeeDto> employees = new ArrayList<>();

            // Map headerName -> index
            Map<String, Integer> headerIndexMap = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 1 && hasHeader) {
                    String[] headers = line.split(delimiter);
                    for (int i = 0; i < headers.length; i++) {
                        String h = headers[i].trim();
                        headerIndexMap.put(h, i);
                    }
                    // on passe à la ligne suivante (les données)
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] tokens = line.split(delimiter);

                EmployeeDto dto = new EmployeeDto();

                for (EmployeeMappingProperties.CsvColumn column : csvMapping.getColumns()) {
                    Integer colIndex = null;

                    // priorité au header si configuré
                    if (column.getHeader() != null) {
                        colIndex = headerIndexMap.get(column.getHeader());
                    } else {
                        colIndex = column.getIndex();
                    }

//                    if (index >= tokens.length) {
//                        throw new FileProcessingException("Missing column at index " + index +
//                                " for line " + lineNumber);
//                    }
//
//
//
//                    String rawValue = tokens[index].trim();

                    // Si la colonne n'existe pas dans cette ligne → on prend rawValue = null
                    String rawValue = null;
                    if (colIndex != null && colIndex >= 0 && colIndex < tokens.length) {
                        rawValue = tokens[colIndex].trim();
                        if (rawValue.isEmpty()) {
                            rawValue = null; // on traite vide comme "absent"
                        }
                    }

                    // On laisse applyField décider quoi faire (mettre null, 0, etc.)

                    applyField(dto, column.getName(), column.getType(), rawValue, lineNumber);
                }

                employees.add(dto);
            }

            employeeService.saveAll(employees);
            return employees.size();
        } catch (IOException e) {
            throw new FileProcessingException("Error reading CSV file", e);
        }
    }

    @Override
    public int ingestXml(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileFormatException("XML file is empty");
        }

        try {
            EmployeeMappingProperties.XmlMapping xmlMapping = mappingProperties.getXml();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file.getInputStream());
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(xmlMapping.getRecordElement());

            List<EmployeeDto> employees = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) continue;

                Element element = (Element) node;
                EmployeeDto dto = new EmployeeDto();

                for (EmployeeMappingProperties.XmlField field : xmlMapping.getFields()) {
                    String tagName = field.getTag();
                    NodeList values = element.getElementsByTagName(tagName);

//                    if (values.getLength() == 0) {
//                        throw new FileProcessingException("Tag <" + tagName + "> not found in employee index " + i);
//                    }
//                    String rawValue = values.item(0).getTextContent().trim();

                    String rawValue = null;
                    if (values.getLength() > 0) {
                        rawValue = values.item(0).getTextContent();
                        if (rawValue != null) {
                            rawValue = rawValue.trim();
                            if (rawValue.isEmpty()) {
                                rawValue = null;
                            }
                        }
                    }

                    applyField(dto, field.getName(), field.getType(), rawValue, i + 1);
                }

                employees.add(dto);
            }

            employeeService.saveAll(employees);
            return employees.size();
        } catch (Exception e) {
            throw new FileProcessingException("Error reading XML file", e);
        }
    }

    // ================== Helpers ==================

    private void applyField(EmployeeDto dto, String fieldName, String type, String rawValue, int index) {
//        if (rawValue == null || rawValue.isEmpty()) return;

        // Si aucune valeur (colonne / tag manquant ou vide)
        if (rawValue == null) {

            switch (fieldName) {
                case "salary" -> {
                    // Valeur par défaut pour salary si le champ n'est pas dans le XML/CSV
                    if (dto.getSalary() == null) {
                        dto.setSalary(BigDecimal.ZERO);
                    }
                }
                default -> {
                    // ne rien faire => le champ reste null
                }
            }
            return;
        }

        try {
            switch (fieldName) {
                case "id" -> dto.setId(parseLong(rawValue));
                case "firstName" -> dto.setFirstName(rawValue);
                case "lastName" -> dto.setLastName(rawValue);
                case "position" -> dto.setPosition(rawValue);
                case "department" -> dto.setDepartment(rawValue);
                case "hireDate" -> dto.setHireDate(parseLocalDate(rawValue));
                case "salary" -> dto.setSalary(parseDecimal(rawValue));
                default -> {
                    // pour de futurs champs
                }
            }
        } catch (Exception e) {
            throw new FileProcessingException(
                    "Error parsing field '" + fieldName + "' with value '" + rawValue +
                            "' at index " + index + ": " + e.getMessage(), e);
        }
    }

    private Long parseLong(String value) {
        return Long.parseLong(value);
    }

    private LocalDate parseLocalDate(String value) {
        return LocalDate.parse(value); // yyyy-MM-dd
    }

    private BigDecimal parseDecimal(String value) {
        return new BigDecimal(value);
    }
}
