package com.example.iotplatform.service;

import com.example.iotplatform.exception.CustomException;
import com.example.iotplatform.model.NaturalGasProduction;
import com.example.iotplatform.repository.NaturalGasProductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NaturalGasProductionService {

    private final NaturalGasProductionRepository repository;

    @Autowired
    public NaturalGasProductionService(NaturalGasProductionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void importCsvData(String filePath) {
        if (!Files.exists(Paths.get(filePath))) {
            throw new CustomException("File not found: " + filePath);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                try {
                    NaturalGasProduction production = parseProductionData(line);
                    production.calculateDerivedFields();
                    repository.save(production);
                } catch (Exception e) {
                    throw new CustomException("Error parsing line " + lineNumber + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new CustomException("Error reading file: " + e.getMessage());
        }
    }

    private NaturalGasProduction parseProductionData(String line) {
        String[] values = line.split(",");
        if (values.length < 20) {
            throw new CustomException("Invalid data format: expected at least 20 values");
        }

        LocalDate date;
        try {
            date = LocalDate.parse("01 " + values[0], DateTimeFormatter.ofPattern("dd MMM yyyy"));
        } catch (DateTimeParseException e) {
            throw new CustomException("Invalid date format: " + values[0]);
        }

        double usProduction;
        try {
            usProduction = Double.parseDouble(values[1]);
        } catch (NumberFormatException e) {
            throw new CustomException("Invalid US production value: " + values[1]);
        }

        Map<String, Double> stateProduction = new HashMap<>();
        double totalProduction = 0;
        for (int i = 2; i < values.length - 1; i++) {
            String state = getStateNameFromIndex(i);
            try {
                double production = Double.parseDouble(values[i]);
                stateProduction.put(state, production);
                totalProduction += production;
            } catch (NumberFormatException e) {
                throw new CustomException("Invalid production value for " + state + ": " + values[i]);
            }
        }

        double offshoreProduction;
        try {
            offshoreProduction = Double.parseDouble(values[values.length - 1]);
            totalProduction += offshoreProduction;
        } catch (NumberFormatException e) {
            throw new CustomException("Invalid offshore production value: " + values[values.length - 1]);
        }

        NaturalGasProduction production = new NaturalGasProduction();
        production.setDate(date);
        production.setUsProduction(usProduction);
        production.setStateProduction(stateProduction);
        production.setOffshoreProduction(offshoreProduction);
        production.setTotalProduction(totalProduction);
        return production;
    }

    private String getStateNameFromIndex(int index) {
        String[] states = {"Alaska", "Arkansas", "California", "Colorado", "Kansas", "Louisiana", "Montana", "North Dakota", "New Mexico", "Ohio", "Oklahoma", "Pennsylvania", "Texas", "Utah", "West Virginia", "Wyoming"};
        return states[index - 2];
    }

    public List<NaturalGasProduction> getProductionByDateRange(LocalDate startDate, LocalDate endDate) {
        return repository.findByDateBetween(startDate, endDate);
    }

    public NaturalGasProduction getProductionByDate(LocalDate date) {
        return repository.findByDate(date);
    }

    public List<NaturalGasProduction> getTopProductionDays(LocalDate startDate, LocalDate endDate, int limit) {
        return repository.findTopProductionDays(startDate, endDate, PageRequest.of(0, limit));
    }

    public Map<String, Double> getTotalProductionByState() {
        List<NaturalGasProduction> allProductions = repository.findAllStateProduction();
        Map<String, Double> totalByState = new HashMap<>();

        for (NaturalGasProduction production : allProductions) {
            production.getStateProduction().forEach((state, amount) -> 
                totalByState.merge(state, amount, Double::sum));
        }

        return totalByState;
    }
}