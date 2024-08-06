package com.example.iotplatform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "natural_gas_production")
public class NaturalGasProduction {
    @Id
    private String id;
    private LocalDate date;
    private double totalProduction;
    private Map<String, Double> stateProduction;
    private double usProduction;
    private double offshoreProduction;
    private double averageDailyProduction;
    private String highestProducingState;
    private double highestStateProduction;
    private String lowestProducingState;
    private double lowestStateProduction;

    public void calculateDerivedFields() {
        if (stateProduction != null && !stateProduction.isEmpty()) {
            averageDailyProduction = totalProduction / stateProduction.size();
            
            Map.Entry<String, Double> maxEntry = stateProduction.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
            
            Map.Entry<String, Double> minEntry = stateProduction.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElse(null);
            
            if (maxEntry != null) {
                highestProducingState = maxEntry.getKey();
                highestStateProduction = maxEntry.getValue();
            }
            
            if (minEntry != null) {
                lowestProducingState = minEntry.getKey();
                lowestStateProduction = minEntry.getValue();
            }
        }
    }
}