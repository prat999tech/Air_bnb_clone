package com.example.airbnb.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.airbnb.entity.Hotel;
import com.example.airbnb.entity.HotelMinPrice;
import com.example.airbnb.entity.Inventory;
import com.example.airbnb.repository.HotelMinPriceRepository;
import com.example.airbnb.repository.HotelRepository;
import com.example.airbnb.repository.InventoryRepository;
import com.example.airbnb.stratergy.PricingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service

public class PricingUpdateService {
    // schedular method to update pricing every hour or day can be added here,
    // update the inventory and hotelminprice tables accordingly

    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService pricingService;

    @Scheduled(cron = "0 0 * * * ?") // Runs every hour
    public void updatePricing() {
        // Logic to update pricing
        log.info("Starting pricing update for all hotels");
        int page = 0;
        int batchSize = 100;
        while (true) {
            log.info("Processing page {} with batch size {}", page, batchSize);
            Page<Hotel> hotelsPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
            if (hotelsPage.isEmpty()) {
                break;
            }
            hotelsPage.getContent().forEach(hotel -> updateHotelPrices(hotel));
            page++;
            log.info("Finished processing page {} with batch size {}", page, batchSize);

        }
        log.info("Completed pricing update for all hotels");

    }

    private void updateHotelPrices(Hotel hotel) {
        log.info("Updating pricing for hotel with id: {}", hotel.getId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);
        List<Inventory> inventories = inventoryRepository.findByHotelAndDateBetween(hotel, startDate, endDate);

        updateInventoryPrices(inventories);
        updateHotelMinPrice(hotel, inventories, startDate, endDate);

    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventories, LocalDate startDate, LocalDate endDate) {
        log.info("Updating minimum prices for hotel with id: {}", hotel.getId());
        Map<LocalDate, BigDecimal> dailyMinPrices = inventories.stream()
                .collect(Collectors.groupingBy(
                        Inventory::getDate,
                        Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().orElse(BigDecimal.ZERO)));
        log.info("Daily minimum prices calculated for hotel with id: {}", hotel.getId());

        // Prepare HotelPrice entities in bulk
        log.info("Preparing HotelPrice entities in bulk for hotel with id: {}", hotel.getId());
        List<HotelMinPrice> hotelPrices = new ArrayList<>();
        dailyMinPrices.forEach((date, price) -> {
            HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel, date)
                    .orElse(new HotelMinPrice(hotel, date));
            hotelPrice.setPrice(price);
            hotelPrices.add(hotelPrice);
        });

        // Save all HotelPrice entities in bulk
        hotelMinPriceRepository.saveAll(hotelPrices);
        log.info("Saved HotelPrice entities in bulk for hotel with id: {}", hotel.getId());
    }

    private void updateInventoryPrices(List<Inventory> inventories) {
        log.info("Updating inventory prices for {} inventories", inventories.size());
        inventories.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPrice(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventories);
        log.info("Inventory prices updated for {} inventories", inventories.size());
    }

}
