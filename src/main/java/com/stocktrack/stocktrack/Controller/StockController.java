package com.stocktrack.stocktrack.Controller;

import com.stocktrack.stocktrack.DTO.StockResponseDTO;
import com.stocktrack.stocktrack.Model.Stock;
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

    @GetMapping
    public List<StockResponseDTO> getAllStocks(){
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public StockResponseDTO getStockById(@PathVariable Long id){
        return stockService.getStockById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockResponseDTO addStock(@Valid @RequestBody Stock stock){
        return stockService.addStock(stock);
    }

    @PutMapping("/{id}")
    public StockResponseDTO updateStockById(@PathVariable Long id, @Valid @RequestBody Stock stock){
        return stockService.updateStockById(id, stock);    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStockById(@PathVariable Long id){
        stockService.deleteStockById(id);
    }
}
