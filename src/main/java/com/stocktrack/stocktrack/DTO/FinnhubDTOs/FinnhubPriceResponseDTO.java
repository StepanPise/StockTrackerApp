package com.stocktrack.stocktrack.DTO.FinnhubDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FinnhubPriceResponseDTO {
    //api example {"c":313.38,"d":-2.84,"dp":-0.8981,"h":316.4,"l":312.17,"o":314.965,"pc":316.22,"t":1783699269}

    @JsonProperty("c")
    private double currentPrice;

}
