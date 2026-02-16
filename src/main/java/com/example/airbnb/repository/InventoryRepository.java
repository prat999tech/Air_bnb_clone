package com.example.airbnb.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.airbnb.entity.Hotel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.airbnb.entity.Inventory;
import com.example.airbnb.entity.Room;

import jakarta.persistence.LockModeType;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
        void deleteByDateAfterAndRoom(LocalDate date, Room room);

        @Query("SELECT DISTINCT i.hotel FROM Inventory i"
                        + " WHERE i.city = :city"
                        + " AND i.date BETWEEN :startdate AND :enddate"
                        + " AND i.closed = false AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount"
                        + " GROUP BY i.hotel, i.room"
                        + " HAVING COUNT(i.date) = :datecount")
        Page<Hotel> findhotelwithavailableinventory(
                        @Param("city") String city,
                        @Param("startdate") LocalDate startdate,
                        @Param("enddate") LocalDate enddate,
                        @Param("roomsCount") Integer roomsCount,
                        @Param("datecount") Integer datecount,
                        Pageable peagble

        );

        // we are using reserved count while booking service is underprocess
        @Query("SELECT i FROM Inventory i"
                        + " WHERE i.room.id = :roomId"
                        + " AND i.date BETWEEN :startDate AND :endDate"
                        + " AND i.closed = false"
                        + " AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount")
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        List<Inventory> findAndLockAvailableInventory(
                        @Param("roomId") Long roomId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("roomsCount") Integer roomsCount);

        List<Inventory> findByHotelAndDateBetween(Hotel hotel, LocalDate startDate, LocalDate endDate);

}

/*
 * 
 * Imagine a user searching on your website. They say:
 * "I want to find a hotel in New York from Jan 10th to Jan 15th that has at least 2 rooms available."
 * 
 * This query is designed to answer that exact question. It finds all hotels
 * that have at least one specific room type (i.room) available for the entire
 * duration of the user's requested stay.
 * 
 * Let's break it down piece by piece.
 * 
 * SELECT DISTINCT i.hotel FROM Inventory i
 * 
 * FROM Inventory i:
 * "Look at the Inventory table. We'll refer to each row as i."
 * SELECT DISTINCT i.hotel:
 * "From all the matching inventory rows, give me back only the unique Hotel objects. I don't want the same hotel listed multiple times in my final result."
 * WHERE i.city = :city
 * 
 * What it does: Filters the inventory records to only include those in the city
 * the user searched for (e.g., 'New York').
 * : (colon): The colon marks :city as a parameter that will be filled in by the
 * Java method.
 * 
 * AND i.date BETWEEN :startdate AND :enddate
 * 
 * What it does: This is a crucial filter. It narrows down the inventory records
 * to only those that fall within the user's desired check-in and check-out
 * dates.
 * Example: If the user wants Jan 10th to Jan 15th, this will grab all inventory
 * records for Jan 10, 11, 12, 13, and 14. (Note: BETWEEN is inclusive, so you
 * might need to adjust the end date logic in your service).
 * AND i.closed = false
 * 
 * What it does: This is a business rule. It ensures that we only look at
 * inventory for rooms that are currently open for booking. If you manually
 * "close" a room for a specific day (e.g., for maintenance), it won't show up
 * in the search results.
 * Note: As mentioned, this field is missing from your Inventory.java entity.
 * You would need to add private Boolean closed; to your Inventory class for
 * this line to work.
 * AND (i.totalCount - i.bookedCount) >= :roomsCount
 * 
 * What it does: This checks for availability. It calculates the number of
 * available rooms (totalCount - bookedCount) for that specific day and checks
 * if it's greater than or equal to the number of rooms the user requested.
 * Example: If a room type has 10 total rooms and 8 are booked, there are 2
 * available. If the user searched for roomsCount = 2, this condition passes. If
 * they searched for 3, it fails.
 * 
 * GROUP BY i.hotel, i.room
 * 
 * This is the key to the whole query's logic.
 * What it does: It groups all the filtered inventory rows together based on a
 * unique combination of a hotel and a specific room type within that hotel.
 * Analogy: Imagine a big pile of sales receipts. GROUP BY is like creating
 * smaller piles, where each small pile is for
 * "all sales of 'Deluxe King' rooms at the 'Grand Hyatt'". Another pile would
 * be for "all sales of 'Standard Queen' rooms at the 'Grand Hyatt'".
 * HAVING COUNT(i.date) = :datecount
 * 
 * This is the final check that ensures availability for the entire stay.
 * HAVING: This is like a WHERE clause, but it works on the results of a GROUP
 * BY.
 * COUNT(i.date): For each group (e.g., for the 'Deluxe King' room at the 'Grand
 * Hyatt'), this counts how many daily inventory records were found within the
 * date range.
 * = :datecount: This compares that count to the total number of days the user
 * wants to stay. The :datecount parameter would be calculated in your Java code
 * (e.g., endDate.toEpochDay() - startDate.toEpochDay()).
 * Putting it together: If a user wants to stay for 5 nights, this HAVING clause
 * ensures that the specific room type at that hotel was available on all 5 of
 * those nights. If it was only available for 4 out of the 5 nights, the COUNT
 * would be 4, which would not equal the datecount of 5, and that entire group
 * would be discarded.
 * 
 */
