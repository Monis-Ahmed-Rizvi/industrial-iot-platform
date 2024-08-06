package com.example.iotplatform.repository;

import com.example.iotplatform.model.NaturalGasProduction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NaturalGasProductionRepository extends MongoRepository<NaturalGasProduction, String> {
    List<NaturalGasProduction> findByDateBetween(LocalDate startDate, LocalDate endDate);
    NaturalGasProduction findByDate(LocalDate date);

    @Query("{'totalProduction': {$gt: ?0}}")
    List<NaturalGasProduction> findByTotalProductionGreaterThan(double production);

    @Query(value="{'date': {$gte: ?0, $lte: ?1}}", sort="{'totalProduction': -1}")
    List<NaturalGasProduction> findTopProductionDays(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(value="{'stateProduction': {$exists: true}}", fields="{'stateProduction': 1, '_id': 0}")
    List<NaturalGasProduction> findAllStateProduction();
}