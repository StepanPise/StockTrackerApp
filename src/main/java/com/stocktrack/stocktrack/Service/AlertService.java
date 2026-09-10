package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.Request.AlertRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.AlertResponseDTO;
import com.stocktrack.stocktrack.Exception.ResourceNotFoundException;
import com.stocktrack.stocktrack.Mapper.AlertMapper;
import com.stocktrack.stocktrack.Entity.Alert;
import com.stocktrack.stocktrack.Entity.Stock;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Repository.AlertRepository;
import com.stocktrack.stocktrack.Repository.StockRepository;
import com.stocktrack.stocktrack.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // HELPER METHODS

    private Alert getAlertEntityById(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert with ID " + id + " does not exist."));
    }

    private User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " does not exist."));
    }

    private Stock getOrCreateStock(String ticker) {
        return stockRepository.findByTicker(ticker)
                .orElseGet(() -> {
                    String companyName = marketDataService.getStockName(ticker);
                    Stock newStock = new Stock();
                    newStock.setTicker(ticker);
                    newStock.setName(companyName);
                    return stockRepository.save(newStock);
                });
    }

    // BUISNESS LOGIC

    @Transactional(readOnly = true)
    public List<AlertResponseDTO> getAllAlerts() {
        return alertRepository.findAll().stream()
                .map(alertMapper::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AlertResponseDTO getAlertById(Long id) {
        Alert alert = getAlertEntityById(id);
        return alertMapper.mapToResponseDTO(alert);
    }

    @Transactional
    public AlertResponseDTO addAlert(AlertRequestDTO requestDto, User user) {
        Stock stock = getOrCreateStock(requestDto.getTicker());

        Alert alertToSave = alertMapper.mapToEntity(requestDto, user, stock);
        Alert savedAlert = alertRepository.save(alertToSave);

        return alertMapper.mapToResponseDTO(savedAlert);
    }

    @Transactional
    public AlertResponseDTO updateAlertById(Long id, AlertRequestDTO requestDto) {
        Alert existingAlert = getAlertEntityById(id);

        User user = getUserEntity(requestDto.getUserId());
        Stock stock = getOrCreateStock(requestDto.getTicker());

        existingAlert.setName(requestDto.getName());
        existingAlert.setTargetPrice(requestDto.getTargetPrice());
        existingAlert.setConditionType(requestDto.getConditionType());
        existingAlert.setUser(user);
        existingAlert.setStock(stock);

        Alert savedAlert = alertRepository.save(existingAlert);
        return alertMapper.mapToResponseDTO(savedAlert);
    }

    @Transactional
    public void deleteAlertById(Long id) {
        Alert alert = getAlertEntityById(id);
        alertRepository.delete(alert);
    }
}
