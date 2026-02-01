package com.example.airbnb.stratergy;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.airbnb.entity.Inventory;

@Service
public class PricingService {
    public BigDecimal calculateDynamicPrice(Inventory inventory) {
        // 1. Core
        PricingStrategy pricingStrategy = new BasePriceStrategy();

        // 2. Layer 1 wraps Core
        pricingStrategy = new SurgePricingStrategy(pricingStrategy);

        // 3. Layer 2 wraps Layer 1
        pricingStrategy = new OccupancyPricingStrategy(pricingStrategy);

        // 4. Layer 3 wraps Layer 2
        pricingStrategy = new HolidayPricingStrategy(pricingStrategy);

        // 5. Layer 4 wraps Layer 3 (This is the outermost layer)
        pricingStrategy = new UrgencyPricingStrategy(pricingStrategy);

        // Execute!
        return pricingStrategy.calculatePrice(inventory);
    }

}

/*
 * Phase 1: wrapping the Gift (Construction)
 * Inside PricingService.java, lines 13-17 execute first. This builds the chain.
 * 
 * Line 13: PricingStrategy pricingStrategy = new BasePriceStrategy();
 * 
 * Result: You have a Base object.
 * Chain: [Base]
 * Line 14: pricingStrategy = new SurgePricingStrategy(pricingStrategy);
 * 
 * Result: You created a Surge object and put [Base] inside it.
 * Chain: [Surge] -> [Base]
 * Line 15: pricingStrategy = new OccupancyPricingStrategy(pricingStrategy);
 * 
 * Result: You created an Occupancy object and put the previous chain inside it.
 * Chain: [Occupancy] -> [Surge] -> [Base]
 * Line 16: pricingStrategy = new HolidayPricingStrategy(pricingStrategy);
 * 
 * Result: You created a Holiday object and put the previous chain inside it.
 * Chain: [Holiday] -> [Occupancy] -> [Surge] -> [Base]
 * Line 17: pricingStrategy = new UrgencyPricingStrategy(pricingStrategy);
 * 
 * Result: You created an Urgency object (the outermost layer) and put the whole
 * previous chain inside.
 * Final Chain: [Urgency] -> [Holiday] -> [Occupancy] -> [Surge] -> [Base]
 * Phase 2: Drilling Down (The Call Stack)
 * Now line 18 executes: return pricingStrategy.calculatePrice(inventory);
 * 
 * This triggers a chain reaction of method calls going down (or "in") to the
 * center.
 * 
 * 1. Call calculatePrice on UrgencyPricingStrategy
 * 
 * Line 18 (Urgency.java): BigDecimal price=wrapped.calculatePrice(inventory);
 * Action: It pauses here and calls the next strategy in the chain (Holiday).
 * 2. Call calculatePrice on HolidayPricingStrategy
 * 
 * Line 19 (Holiday.java): BigDecimal price=wrapped.calculatePrice(inventory);
 * Action: It pauses here and calls the next strategy (Occupancy).
 * 3. Call calculatePrice on OccupancyPricingStrategy
 * 
 * Line 18 (Occupancy.java): BigDecimal price =
 * wrapped.calculatePrice(inventory);
 * Action: It pauses here and calls the next strategy (Surge).
 * 4. Call calculatePrice on SurgePricingStrategy
 * 
 * Line 18 (Surge.java): BigDecimal price=wrapped.calculatePrice(inventory);
 * Action: It pauses here and calls the next strategy (Base).
 * 5. Call calculatePrice on BasePriceStrategy
 * 
 * Line 15 (Base.java): return inventory.getRoom().getBaseprice();
 * Action: This is the bottom. It returns 100 (example value).
 * Phase 3: Bubbling Up (The Calculation)
 * Now the functions unpause and finish their logic, moving up (or "out") from
 * the center.
 * 
 * 6. Back in SurgePricingStrategy
 * 
 * Line 18: price is set to 100.
 * Line 19: return price.multiply(inventory.getSurgefactor());
 * Calc: 100 * 1.1 = 110.
 * Action: Returns 110 to the caller.
 * 7. Back in OccupancyPricingStrategy
 * 
 * Line 18: price is set to 110.
 * Line 19: double occupancyRate... (Calculates rate).
 * Line 20: if(occupancyRate>0.8) (Assume yes).
 * Line 21: price=price.multiply(...1.25);
 * Calc: 110 * 1.25 = 137.5.
 * Line 22: return price; (Returns 137.5).
 * 8. Back in HolidayPricingStrategy
 * 
 * Line 19: price is set to 137.5.
 * Line 20: boolean isHoliday=true; (Assume yes).
 * Line 21: if(isHoliday) starts.
 * Line 22: price=price.multiply(...1.20);
 * Calc: 137.5 * 1.2 = 165.
 * Line 23: return price; (Returns 165).
 * 9. Back in UrgencyPricingStrategy
 * 
 * Line 18: price is set to 165.
 * Line 19: LocalDate today... (Gets date).
 * Line 20: if(!inventory.getDate()...) (Assume date is soon).
 * Line 21: price=price.multiply(...1.15);
 * Calc: 165 * 1.15 = 189.75.
 * Line 22: return price; (Returns 189.75).
 * 10. Back in PricingService
 * 
 * Line 18: The calculatePrice method finally finishes and returns 189.75 to
 * whatever called the service.
 * Gemini 3 Pro (Preview) • 1x
 */
