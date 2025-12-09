package com.bank.uploadfileanddatapersistwithmongodb.interfaces;

import org.springframework.web.multipart.MultipartFile;

//Service qui sait ingérer des fichiers CSV ou XML.
//Il renvoie un int = nombre de lignes enregistrées.

public interface FileIngestionService {
    int ingestCsv(MultipartFile file);

    int ingestXml(MultipartFile file);
}
