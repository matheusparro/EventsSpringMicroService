package com.eventostec.api.controller;

import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventDetailDTO;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestParam("title") String title,
                                             @RequestParam(value = "description", required = false) String description,
                                             @RequestParam("date") Long date,
                                             @RequestParam("city") String city,
                                             @RequestParam("state") String state,
                                             @RequestParam("remote") Boolean remote,
                                             @RequestParam("eventUrl") String eventUrl,
                                             @RequestParam(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(eventService.createEvent(new EventRequestDTO(title, description, date, city, state, remote, eventUrl, image)));
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEvents( @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue ="10") int size){
       List<EventResponseDTO> allEvents = eventService.getUpcomingEvents(page, size);
       return ResponseEntity.ok(allEvents);

    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> filterEvents(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue ="10") int size,
                                                               @RequestParam(defaultValue = "") String title,
                                                               @RequestParam(defaultValue = "") String city,
                                                               @RequestParam(defaultValue = "") String uf,
                                                               @RequestParam(defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                               @RequestParam(defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate){

        List<EventResponseDTO> events = eventService.getFilteredEvents(page, size, title, city, uf, startDate, endDate);

        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailDTO> getEventDetails(@PathVariable("eventId") String eventId){
        return ResponseEntity.ok(eventService.getEventDetails(eventId));
    }
}
