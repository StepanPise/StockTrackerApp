package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.Request.AlertRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.AlertResponseDTO;
import com.stocktrack.stocktrack.Mapper.AlertMapper;
import com.stocktrack.stocktrack.Entity.Alert;
import com.stocktrack.stocktrack.Entity.Stock;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Repository.AlertRepository;
import com.stocktrack.stocktrack.Repository.StockRepository;
import com.stocktrack.stocktrack.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final MarketDataService marketDataService;
    private final AlertMapper alertMapper;
    
//use LOMBOK insted
//    public AlertService(AlertRepository alertRepository) {
//        this.alertRepository = alertRepository;
//    }

    private Alert getAlertEntityById(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert with ID " + id + " does not exist."));
    }

    public List<AlertResponseDTO> getAllAlerts() {
        return alertRepository.findAll().stream()
                .map(alertMapper::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public AlertResponseDTO getAlertById(Long id) {
        Alert alert = getAlertEntityById(id);
        return alertMapper.mapToResponseDTO(alert);
    }

    public AlertResponseDTO addAlert(AlertRequestDTO requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User with ID" + requestDto.getUserId() + " doesnt exist."));
        Stock stock = stockRepository.findByTicker(requestDto.getTicker())
                .orElseGet(() -> {

            //if it doesnt fall, code continues and Saves the new Stock to DB
            String companyName = marketDataService.getStockName(requestDto.getTicker());

            Stock newStock = new Stock();
            newStock.setTicker(requestDto.getTicker());
            newStock.setName(companyName);
            return stockRepository.save(newStock);
        });

        Alert alertToSave = alertMapper.mapToEntity(requestDto, user, stock);
        Alert savedAlert = alertRepository.save(alertToSave);

        return alertMapper.mapToResponseDTO(savedAlert);    }

    public void deleteAlertById(Long id) {
        Alert alert = getAlertEntityById(id);
        alertRepository.delete(alert);
    }

    public AlertResponseDTO updateAlertById(Long id, AlertRequestDTO requestDto) {
        Alert existingAlert = getAlertEntityById(id);

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User with ID" + requestDto.getUserId() + " doesnt exist."));
        Stock stock = stockRepository.findByTicker(requestDto.getTicker())
                .orElseGet(() -> {
                    String companyName = marketDataService.getStockName(requestDto.getTicker());
                    Stock newStock = new Stock();
                    newStock.setTicker(requestDto.getTicker());
                    newStock.setName(companyName);
                    return stockRepository.save(newStock);
                });

        existingAlert.setName(requestDto.getName());
        existingAlert.setTargetPrice(requestDto.getTargetPrice());
        existingAlert.setConditionType(requestDto.getConditionType());
        existingAlert.setUser(user);
        existingAlert.setStock(stock);

        Alert savedAlert = alertRepository.save(existingAlert);
        return alertMapper.mapToResponseDTO(savedAlert);
    }
}
