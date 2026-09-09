package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.FinnhubDTOs.FinnhubNameResponseDTO;
import com.stocktrack.stocktrack.DTO.FinnhubDTOs.FinnhubPriceResponseDTO;
import com.stocktrack.stocktrack.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MarketDataService {

    @Value("${finnhub.api.key}")
    private String apiKey;
    @Value("${finnhub.api.url}")
    private String priceApiUrl;
    @Value("${finnhub.api.profile.url}")
    private String nameApiUrl;

    private final RestTemplate restTemplate;

    public double getCurrentPrice(String ticker) {
        String url = priceApiUrl + "?symbol=" + ticker + "&token=" + apiKey;

        FinnhubPriceResponseDTO response = restTemplate.getForObject(url, FinnhubPriceResponseDTO.class);

        if (response != null && response.getCurrentPrice() > 0) {
            return response.getCurrentPrice();
        } else {
            throw new ResourceNotFoundException("Invalid ticker: " + ticker);
        }

    }

    public String getStockName(String ticker) {
        String url = nameApiUrl + "?symbol=" + ticker + "&token=" + apiKey;

        FinnhubNameResponseDTO response = restTemplate.getForObject(url, FinnhubNameResponseDTO.class);

        if (response != null) {
            return response.getCompanyName();
        } else {
            throw new ResourceNotFoundException("Invalid ticker: " + ticker);
        }
    }

}
