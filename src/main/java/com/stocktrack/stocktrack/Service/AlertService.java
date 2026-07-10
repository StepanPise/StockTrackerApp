package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.AlertRequestDTO;
import com.stocktrack.stocktrack.DTO.AlertResponseDTO;
import com.stocktrack.stocktrack.Mapper.AlertMapper;
import com.stocktrack.stocktrack.Model.Alert;
import com.stocktrack.stocktrack.Model.Stock;
import com.stocktrack.stocktrack.Model.User;
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
                .map(AlertMapper::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public AlertResponseDTO getAlertById(Long id) {
        Alert alert = getAlertEntityById(id);
        return AlertMapper.mapToResponseDTO(alert);
    }

    public AlertResponseDTO addAlert(AlertRequestDTO requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User with ID" + requestDto.getUserId() + " doesnt exist."));
        Stock stock = stockRepository.findById(requestDto.getStockId())
                .orElseThrow(() -> new RuntimeException("Stock with ID " + requestDto.getStockId() + " doesnt exist."));

        Alert alertToSave = AlertMapper.mapToEntity(requestDto, user, stock);
        Alert savedAlert = alertRepository.save(alertToSave);

        return AlertMapper.mapToResponseDTO(savedAlert);    }

    public void deleteAlertById(Long id) {
        Alert alert = getAlertEntityById(id);
        alertRepository.delete(alert);
    }

    public AlertResponseDTO updateAlertById(Long id, AlertRequestDTO requestDto) {
        Alert existingAlert = getAlertEntityById(id);

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User with ID" + requestDto.getUserId() + " doesnt exist."));
        Stock stock = stockRepository.findById(requestDto.getStockId())
                .orElseThrow(() -> new RuntimeException("Stock with ID " + requestDto.getStockId() + " doesnt exist."));

        existingAlert.setName(requestDto.getName());
        existingAlert.setTargetPrice(requestDto.getTargetPrice());
        existingAlert.setConditionType(requestDto.getConditionType());
        existingAlert.setUser(user);
        existingAlert.setStock(stock);

        Alert savedAlert = alertRepository.save(existingAlert);
        return AlertMapper.mapToResponseDTO(savedAlert);
    }
}
