package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.FinnhubDTOs.FinnhubCompanyProfileResponseDTO;
import com.stocktrack.stocktrack.DTO.Request.AlertRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.AlertResponseDTO;
import com.stocktrack.stocktrack.Entity.Alert;
import com.stocktrack.stocktrack.Entity.Stock;
import com.stocktrack.stocktrack.Entity.User;
import com.stocktrack.stocktrack.Mapper.AlertMapper;
import com.stocktrack.stocktrack.Repository.AlertRepository;
import com.stocktrack.stocktrack.Repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private MarketDataService marketDataService;

    @Mock
    private AlertMapper alertMapper;

    @InjectMocks
    private AlertService alertService;

    @Test
    @DisplayName("getAlertById: Should return alert when current user is the owner")
    void getAlertById_UserIsOwner_ReturnsAlertResponseDTO() {
        User currentUser = new User();
        currentUser.setId(1L);

        Alert alert = new Alert();
        alert.setId(10L);
        alert.setUser(currentUser);

        AlertResponseDTO expectedDto = new AlertResponseDTO();
        expectedDto.setId(10L);

        when(alertRepository.findById(10L)).thenReturn(Optional.of(alert));
        when(alertMapper.mapToResponseDTO(alert)).thenReturn(expectedDto);

        AlertResponseDTO result = alertService.getAlertById(10L, currentUser);

        assertNotNull(result);
        assertEquals(10L, result.getId());

        verify(alertRepository, times(1)).findById(10L);
    }

    @Test
    @DisplayName("getAlertById: Should throw AccessDeniedException when user is not the owner")
    void getAlertById_UserIsNotOwner_ThrowsAccessDeniedException() {
        User currentUser = new User();
        currentUser.setId(1L);

        User differentUser = new User();
        differentUser.setId(2L);

        Alert alert = new Alert();
        alert.setId(10L);
        alert.setUser(differentUser);

        when(alertRepository.findById(10L)).thenReturn(Optional.of(alert));

        assertThrows(
                AccessDeniedException.class,
                () -> alertService.getAlertById(10L, currentUser)
        );

        verifyNoInteractions(alertMapper);
    }

    @Test
    @DisplayName("addAlert: Should fetch company profile and save alert when stock is new")
    void addAlert_StockDoesNotExist_CreatesStockAndSavesAlert() {
        User currentUser = new User();
        currentUser.setId(1L);

        AlertRequestDTO requestDto = new AlertRequestDTO();
        requestDto.setTicker("AAPL");

        FinnhubCompanyProfileResponseDTO profile = new FinnhubCompanyProfileResponseDTO();
        profile.setCompanyName("Apple Inc.");
        profile.setLogo("https://example.com/apple.png");

        Stock newStock = new Stock();
        newStock.setTicker("AAPL");
        newStock.setName("Apple Inc.");
        newStock.setLogoUrl("https://example.com/apple.png");

        Alert mappedAlert = new Alert();
        Alert savedAlert = new Alert();
        AlertResponseDTO expectedDto = new AlertResponseDTO();

        when(stockRepository.findByTicker("AAPL")).thenReturn(Optional.empty());
        when(marketDataService.getCompanyProfile("AAPL")).thenReturn(profile);
        when(stockRepository.save(any(Stock.class))).thenReturn(newStock);
        when(alertMapper.mapToEntity(requestDto, currentUser, newStock)).thenReturn(mappedAlert);
        when(alertRepository.save(mappedAlert)).thenReturn(savedAlert);
        when(alertMapper.mapToResponseDTO(savedAlert)).thenReturn(expectedDto);

        AlertResponseDTO result = alertService.addAlert(requestDto, currentUser);

        assertNotNull(result);

        verify(marketDataService, times(1)).getCompanyProfile("AAPL");
        verify(stockRepository, times(1)).save(any(Stock.class));
        verify(alertRepository, times(1)).save(mappedAlert);
    }
}