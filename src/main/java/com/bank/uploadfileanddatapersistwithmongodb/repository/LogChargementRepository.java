package com.bank.uploadfileanddatapersistwithmongodb.repository;

import com.bank.uploadfileanddatapersistwithmongodb.entity.LogChargement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogChargementRepository extends MongoRepository<LogChargement, String> {
}
