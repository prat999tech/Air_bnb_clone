package com.example.airbnb.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import com.example.airbnb.dto.InventoryDto;
import com.example.airbnb.dto.UpdateInventoryRequestDto;
import com.example.airbnb.entity.user;
import com.example.airbnb.exception.Unauthorizedexception;
import com.example.airbnb.exception.resoucenotfoundexception;
import com.example.airbnb.repository.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.airbnb.dto.HotelPrice;
import com.example.airbnb.dto.Hotelsearch;
import com.example.airbnb.entity.Inventory;
import com.example.airbnb.entity.Room;
import com.example.airbnb.repository.HotelMinPriceRepository;
import com.example.airbnb.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j

public class Inventoryserviceimpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void initiliazeroomforayear(Room room) {
        log.info("Initializing inventory for room with id: {}", room.getId());
        // Logic to initialize inventory for the room for a year
        // This could involve creating Inventory entries for each day of the year
        // associated with the given room

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);

        for (LocalDate date = startDate; date.isAfter(endDate); date = date.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .date(date)
                    .bookedCount(0)
                    .totalCount(room.getTotalcount())
                    .surgefactor(room.getBaseprice())
                    .price(room.getBaseprice())
                    .city(room.getHotel().getCity())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);

        }
        log.info("Inventory initialized for room with id: {}", room.getId());

        // this methosd can be used at two place first when we creating a new room and
        // second when creating a new hotel and adding some default rooms to it

    }

    @Override
    @Transactional

    public void deletefutureinventoryofroom(Room room) {
        log.info("Deleting future inventory for room with id: {}", room.getId());
        inventoryRepository.deleteByDateAfterAndRoom(LocalDate.now(), room);
        log.info("Future inventory deleted for room with id: {}", room.getId());
    }

    @Override
    public Page<HotelPrice> searchHotels(Hotelsearch hotelsearchrequest) {
        log.info("Searching hotels in city: {} from {} to {} for {} rooms",
                hotelsearchrequest.getCity(),
                hotelsearchrequest.getStartdate(),
                hotelsearchrequest.getEnddate(),
                hotelsearchrequest.getRoomsCount());

        Pageable peagble = PageRequest.of(hotelsearchrequest.getPage(), hotelsearchrequest.getSize());
        int Datecount = (int) ChronoUnit.DAYS.between(hotelsearchrequest.getStartdate(),
                hotelsearchrequest.getEnddate()) + 1;
        Page<HotelPrice> page = hotelMinPriceRepository.findhotelwithavailableinventory(
                hotelsearchrequest.getCity(),
                hotelsearchrequest.getStartdate(),
                hotelsearchrequest.getEnddate(),
                hotelsearchrequest.getRoomsCount(),
                Datecount,
                peagble);
        log.info("Found {} hotels matching the search criteria", page.getTotalElements());
        return page;

    }

    @Override
    public List<InventoryDto> getAllInventory(long roomId) {
        Room room=roomRepository.findById(roomId).orElseThrow(()-> new resoucenotfoundexception("room not found" + roomId));
        user user=getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) {
            log.warn("user with id: {} is not the owner of the hotel with id: {}", user.getId(), room.getHotel().getId());
            throw new Unauthorizedexception("You are not the owner of this hotel");
        }
        return inventoryRepository.findByRoomOrderByDate(room).stream()
                .map((element)->modelMapper.map(element,
                        InventoryDto.class))
                .collect(Collectors.toList());


    }

    @Override
    public void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto) {
        log.info("updating inventory for room with id: {} for date: {}", roomId, updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate());
        Room room=roomRepository.findById(roomId).orElseThrow(()-> new resoucenotfoundexception("room not found" + roomId));
        user user=getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) {
            log.warn("user with id: {} is not the owner of the hotel with id: {}", user.getId(), room.getHotel().getId());
            throw new Unauthorizedexception("You are not the owner of this hotel");
        }
        inventoryRepository.getInventoryAndLockBeforeUpdate(roomId, updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate());// we are locking the inventory before updating because suppose i user comes and start the booking so in inventory our surgefactor or anyy other fields gets changed then in the same time suppose a admin tries to change the or update the inventory then we have to do a locking
        inventoryRepository.updateInventory(roomId, updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate(),  updateInventoryRequestDto.getClosed(), updateInventoryRequestDto.getSurgeFactor());



    }

    public user getCurrentUser() {

        return (user)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }

}
