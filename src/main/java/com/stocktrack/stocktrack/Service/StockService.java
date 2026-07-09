package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.Model.Stock;
import com.stocktrack.stocktrack.Repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getStockById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock with ID " + id + " does not exist."));
    }

    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public void deleteStockById(Long id) {
        Stock stock = getStockById(id); // better then deleteById (doesnt throw exception when id not found)
        stockRepository.delete(stock);
    }

    public Stock updateStockById(Long id, Stock updatedStock) {
        Stock existingStock = getStockById(id);

        existingStock.setName(updatedStock.getName());
        existingStock.setTicker(updatedStock.getTicker());

        return stockRepository.save(existingStock);
    }

}