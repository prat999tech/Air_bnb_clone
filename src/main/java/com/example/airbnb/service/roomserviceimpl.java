package com.example.airbnb.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.airbnb.dto.RoomDto;
import com.example.airbnb.entity.Hotel;
import com.example.airbnb.entity.Room;
import com.example.airbnb.exception.resoucenotfoundexception;
import com.example.airbnb.repository.HotelRepository;
import com.example.airbnb.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j

public class roomserviceimpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    /**
     * The ModelMapper instance is declared here as a final field to be injected by
     * Spring's
     * dependency injection mechanism. While the @Bean in the MapperConfig class is
     * responsible
     * for creating the ModelMapper object and adding it to the Spring application
     * context (the "toolbox"),
     * this line is how our service class "asks for" that specific tool. Spring sees
     * this request
     * and automatically provides the single, shared ModelMapper bean to this class
     * upon its creation,
     * allowing us to use it for mapping operations.
     */

    @Override
    @Transactional
    public RoomDto createNewRoom(long hotelId, RoomDto roomDto) {
        log.info("attempting to create new room in hotel with id: {}", hotelId);
        // Check if hotel exists as for creating room hotel must exist
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new resoucenotfoundexception("Hotel not found with id: " + hotelId));
        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);
        log.info("room created with id: {}", room.getId());
        if (hotel.getActive()) {
            inventoryService.initiliazeroomforayear(room);
        }
        return modelMapper.map(room, RoomDto.class);

    }

    @Override
    @Transactional
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("attempting to get all rooms in hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new resoucenotfoundexception("Hotel not found with id: " + hotelId));

        return hotel.getRooms()
                .stream()
                .map(room -> modelMapper.map(room, RoomDto.class)).toList();

        // why we have used stream here?
        /*
         * 
         * 
         * he Simple Explanation: An Office Assistant
         * Imagine you have a pile of 100 paper documents (List<Document>) and you need
         * to get the "Title" from each one and put those titles into a new list.
         * 
         * The Old Way (Without Streams - A for loop):
         * 
         * You do everything yourself, step-by-step.
         * 
         * You get an empty box (new ArrayList<String>()).
         * You pick up the first document from the pile.
         * You read its title.
         * You write the title on a new piece of paper.
         * You put that new paper in your empty box.
         * You repeat steps 2-5 for all 100 documents.
         * When you're done, you have a box full of titles.
         * This is reliable, but it's a lot of manual work.
         * 
         * The New Way (With Streams - Your Smart Assistant):
         * 
         * You hire a smart assistant (a Stream) and just give instructions.
         * 
         * You tell the assistant: "Take this pile of documents (.stream())."
         * You give one simple rule:
         * "For each document, just give me its title (.map(doc -> doc.getTitle()))."
         * You give a final instruction:
         * "Put all the titles you get into a new list for me (.toList())."
         * The assistant handles all the details of picking up, processing, and
         * collecting. You just declare what you want, not how to do it.
         * 
         * 
         * 
         * 
         */

        /*
         * alternate way:-
         * 
         * 
         * // 1. Get the original list of Room entities from the hotel.
         * List<Room> rooms = hotel.getRooms();
         * 
         * // 2. Create a new, empty list to hold the results (the DTOs).
         * List<RoomDto> roomDtos = new ArrayList<>();
         * 
         * // 3. Loop through each 'Room' in the original list.
         * for (Room room : rooms) {
         * // 4. For each room, map it to a RoomDto.
         * RoomDto dto = modelMapper.map(room, RoomDto.class);
         * 
         * // 5. Add the newly created DTO to your results list.
         * roomDtos.add(dto);
         * }
         * 
         * // 6. Return the final list of DTOs.
         * return roomDtos;
         * 
         */

    }

    @Override
    @Transactional
    public RoomDto getRoomById(Long roomId) {
        log.info("attempting to get room by id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new resoucenotfoundexception("Room not found with id: " + roomId));
        return modelMapper.map(room, RoomDto.class);

    }

    @Override
    @Transactional
    public void deleteRoomById(Long roomId) {
        log.info("attempting to delete room by id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new resoucenotfoundexception("Room not found with id: " + roomId));
        roomRepository.delete(room);
        log.info("room deleted with id: {}", roomId);
        // todo: delete all future inventories associated with this room
        inventoryService.deletefutureinventoryofroom(room);
    }

}
