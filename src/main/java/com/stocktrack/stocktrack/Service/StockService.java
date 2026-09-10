package com.stocktrack.stocktrack.Service;

import com.stocktrack.stocktrack.DTO.Request.StockRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.StockResponseDTO;
import com.stocktrack.stocktrack.Entity.Stock;
import com.stocktrack.stocktrack.Exception.ResourceNotFoundException;
import com.stocktrack.stocktrack.Mapper.StockMapper;
import com.stocktrack.stocktrack.Repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    private Stock getStockEntityById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock with ID " + id + " does not exist."));
    }

    @Transactional(readOnly = true)
    public List<StockResponseDTO> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(stockMapper::mapToResponseDTO)
                .toList() ;  }

    @Transactional(readOnly = true)
    public StockResponseDTO getStockById(Long id) {
        return stockMapper.mapToResponseDTO(getStockEntityById(id));
    }

    @Transactional
    public StockResponseDTO addStock(StockRequestDTO stockRequestDTO) {
        Stock stock = stockMapper.mapToEntity(stockRequestDTO);
        return stockMapper.mapToResponseDTO(stockRepository.save(stock));
    }

    @Transactional
    public void deleteStockById(Long id) {
        Stock stock = getStockEntityById(id); // better then deleteById (doesnt throw exception when id not found)
        stockRepository.delete(stock);
    }

    @Transactional
    public StockResponseDTO updateStockById(Long id, StockRequestDTO stockRequestDTO) {
        Stock existingStock = getStockEntityById(id);

        existingStock.setName(stockRequestDTO.getName());
        existingStock.setTicker(stockRequestDTO.getTicker());

        return stockMapper.mapToResponseDTO(stockRepository.save(existingStock));
    }

}