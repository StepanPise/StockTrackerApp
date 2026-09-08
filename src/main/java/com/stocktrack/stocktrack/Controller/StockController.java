package com.stocktrack.stocktrack.Controller;

import com.stocktrack.stocktrack.DTO.Request.StockRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.StockResponseDTO;
import com.stocktrack.stocktrack.Entity.Stock;
import com.stocktrack.stocktrack.Service.MarketDataService;
import com.stocktrack.stocktrack.Service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;
    private final MarketDataService marketDataService;

    @GetMapping
    public List<StockResponseDTO> getAllStocks(){
        return stockService.getAllStocks();
    }

    @GetMapping("/price/{ticker}")
    public double givePrice(@PathVariable String ticker){
        return marketDataService.getCurrentPrice(ticker);
    }


    @GetMapping("/{id}")
    public StockResponseDTO getStockById(@PathVariable Long id){
        return stockService.getStockById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockResponseDTO addStock(@Valid @RequestBody StockRequestDTO stockRequestDTO){
        return stockService.addStock(stockRequestDTO);
    }

    @PutMapping("/{id}")
    public StockResponseDTO updateStockById(@PathVariable Long id, @Valid @RequestBody StockRequestDTO stockRequestDTO){
        return stockService.updateStockById(id, stockRequestDTO);    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStockById(@PathVariable Long id){
        stockService.deleteStockById(id);
    }


}
