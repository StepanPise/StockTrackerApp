package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.FinnhubDTOs.FinnhubPriceResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MarketDataService {

    @Value("${finnhub.api.key}")
    private String apiKey;
    @Value("${finnhub.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public double getCurrentPrice(String ticker) {
        String url = apiUrl + "?symbol=" + ticker + "&token=" + apiKey;

        FinnhubPriceResponseDTO response = restTemplate.getForObject(url, FinnhubPriceResponseDTO.class);

        if (response != null && response.getCurrentPrice() > 0) {
            return response.getCurrentPrice();
        } else {
            throw new RuntimeException("Invalid ticker: " + ticker);
        }

    }

    public String getStockName(String ticker) {
        String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" + "?symbol=" + ticker + "&token=" + apiKey;

    }

}
