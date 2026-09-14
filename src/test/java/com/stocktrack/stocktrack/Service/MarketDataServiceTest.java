package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.FinnhubDTOs.FinnhubCompanyProfileResponseDTO;
import com.stocktrack.stocktrack.DTO.FinnhubDTOs.FinnhubPriceResponseDTO;
import com.stocktrack.stocktrack.Exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarketDataServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MarketDataService marketDataService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(marketDataService, "apiKey", "test-token");
        ReflectionTestUtils.setField(marketDataService, "priceApiUrl", "https://api.example.com/price");
        ReflectionTestUtils.setField(marketDataService, "profileApiUrl", "https://api.example.com/name");
    }

    @Test
    @DisplayName("getCurrentPrice: Should return price when API responds correctly")
    void getCurrentPrice_ValidTicker_ReturnsPrice() {
        String ticker = "AAPL";
        String expectedUrl = "https://api.example.com/price?symbol=AAPL&token=test-token";

        FinnhubPriceResponseDTO mockResponse = new FinnhubPriceResponseDTO();
        mockResponse.setCurrentPrice(150.50);

        when(restTemplate.getForObject(expectedUrl, FinnhubPriceResponseDTO.class)).thenReturn(mockResponse);

        double actualPrice = marketDataService.getCurrentPrice(ticker);

        assertEquals(150.50, actualPrice);

        verify(restTemplate, times(1)).getForObject(expectedUrl, FinnhubPriceResponseDTO.class);
    }

    @Test
    @DisplayName("getCurrentPrice: Should throw exception when price is zero")
    void getCurrentPrice_InvalidTickerOrZeroPrice_ThrowsException() {
        String ticker = "INVALID";
        String expectedUrl = "https://api.example.com/price?symbol=INVALID&token=test-token";

        FinnhubPriceResponseDTO mockResponse = new FinnhubPriceResponseDTO();
        mockResponse.setCurrentPrice(0.0);

        when(restTemplate.getForObject(expectedUrl, FinnhubPriceResponseDTO.class)).thenReturn(mockResponse);

        assertThrows(
                ResourceNotFoundException.class,
                () -> marketDataService.getCurrentPrice(ticker)
        );
    }

    @Test
    @DisplayName("getCompanyProfile: Should return company profile when API responds correctly")
    void getCompanyProfile_ValidTicker_ReturnsProfile() {
        String ticker = "MSFT";
        String expectedUrl = "https://api.example.com/name?symbol=MSFT&token=test-token";

        FinnhubCompanyProfileResponseDTO mockResponse = new FinnhubCompanyProfileResponseDTO();
        mockResponse.setCompanyName("Microsoft Corporation");
        mockResponse.setLogo("https://example.com/microsoft.png");

        when(restTemplate.getForObject(expectedUrl, FinnhubCompanyProfileResponseDTO.class)).thenReturn(mockResponse);

        FinnhubCompanyProfileResponseDTO actualProfile = marketDataService.getCompanyProfile(ticker);

        assertEquals("Microsoft Corporation", actualProfile.getCompanyName());
        assertEquals("https://example.com/microsoft.png", actualProfile.getLogo());

        verify(restTemplate, times(1)).getForObject(expectedUrl, FinnhubCompanyProfileResponseDTO.class);
    }

    @Test
    @DisplayName("getCompanyProfile: Should throw exception when API returns null")
    void getCompanyProfile_InvalidTicker_ThrowsException() {
        String ticker = "INVALID";
        String expectedUrl = "https://api.example.com/name?symbol=INVALID&token=test-token";

        when(restTemplate.getForObject(expectedUrl, FinnhubCompanyProfileResponseDTO.class)).thenReturn(null);

        assertThrows(
                ResourceNotFoundException.class,
                () -> marketDataService.getCompanyProfile(ticker)
        );
    }
}