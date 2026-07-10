package com.stocktrack.stocktrack.DTO.FinnhubDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinnhubNameResponseDTO {

    @JsonProperty("name")
    private String companyName;
}