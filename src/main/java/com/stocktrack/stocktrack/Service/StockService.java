package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.StockResponseDTO;
import com.stocktrack.stocktrack.Mapper.StockMapper;
import com.stocktrack.stocktrack.Model.Stock;
import com.stocktrack.stocktrack.Repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    private Stock getStockEntityById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock with ID " + id + " does not exist."));
    }

    public List<StockResponseDTO> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(StockMapper::mapToResponseDTO)
                .collect(Collectors.toList());    }

    public StockResponseDTO getStockById(Long id) {
        return StockMapper.mapToResponseDTO(getStockEntityById(id));
    }

    public StockResponseDTO addStock(Stock stock) {
        return StockMapper.mapToResponseDTO(stockRepository.save(stock));
    }

    public void deleteStockById(Long id) {
        Stock stock = getStockEntityById(id); // better then deleteById (doesnt throw exception when id not found)
        stockRepository.delete(stock);
    }

    public StockResponseDTO updateStockById(Long id, Stock updatedStock) {
        Stock existingStock = getStockEntityById(id);

        existingStock.setName(updatedStock.getName());
        existingStock.setTicker(updatedStock.getTicker());

        return StockMapper.mapToResponseDTO(stockRepository.save(existingStock));
    }

}