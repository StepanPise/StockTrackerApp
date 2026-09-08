package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.Request.StockRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.StockResponseDTO;
import com.stocktrack.stocktrack.Mapper.StockMapper;
import com.stocktrack.stocktrack.Entity.Stock;
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

    public StockResponseDTO addStock(StockRequestDTO stockRequestDTO) {
        Stock stock = StockMapper.mapToEntity(stockRequestDTO);
        return StockMapper.mapToResponseDTO(stockRepository.save(stock));
    }

    public void deleteStockById(Long id) {
        Stock stock = getStockEntityById(id); // better then deleteById (doesnt throw exception when id not found)
        stockRepository.delete(stock);
    }

    public StockResponseDTO updateStockById(Long id, StockRequestDTO stockRequestDTO) {
        Stock existingStock = getStockEntityById(id);

        existingStock.setName(stockRequestDTO.getName());
        existingStock.setTicker(stockRequestDTO.getTicker());

        return StockMapper.mapToResponseDTO(stockRepository.save(existingStock));
    }

}