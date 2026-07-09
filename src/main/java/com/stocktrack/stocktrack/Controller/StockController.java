package com.stocktrack.stocktrack.Controller;

import com.stocktrack.stocktrack.Model.Stock;
import com.stocktrack.stocktrack.Service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    @GetMapping
    public List<Stock> getAllStocks(){
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public Stock getStockById(@PathVariable Long id){
        return stockService.getStockById(id);
    }

    @PostMapping
    public Stock addStock(@Valid @RequestBody Stock stock){
        return stockService.addStock(stock);
    }

    @PutMapping("/{id}")
    public Stock updateStockById(@PathVariable Long id, @Valid @RequestBody Stock stock){
        return stockService.updateStockById(id, stock);    }

    @DeleteMapping("/{id}")
    public void deleteStockById(@PathVariable Long id){
        stockService.deleteStockById(id);
    }
}
