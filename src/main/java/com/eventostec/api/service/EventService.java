package com.eventostec.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventDetailDTO;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    AddressService addressService;

    @Autowired
    EventRepository eventRepository;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    private CouponService cupomService;



    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = eventRepository.findUpcomingEvents(new Date(),pageable);
        return getEventResponseDTOS(events);
    }

    private List<EventResponseDTO> getEventResponseDTOS(Page<Event> events) {
        return events.stream().map(event -> new EventResponseDTO(event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getRemote(),
                event.getEventUrl(),
                event.getImgUrl())).collect(Collectors.toList());
    }

    public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String uf, Date startDate, Date endDate) {
        title = title.isEmpty() ?  "" : title;
        city = city.isEmpty() ? "" : city;
        uf = uf.isEmpty() ?  "" : uf;
        startDate = (startDate != null ) ? startDate : new Date(0);
        endDate = (endDate != null ) ? endDate : new Date();

        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = eventRepository.findFilteredEvents(title, city, uf, startDate, endDate, pageable);
        return getEventResponseDTOS(events);
    }

    public EventDetailDTO getEventDetails(String eventId) {
        Event event = eventRepository.findById(UUID.fromString(eventId)).orElseThrow();
        List<Coupon> couponsFinded = this.cupomService.getCouponsByEvent(UUID.fromString(eventId), new Date());
        //FOORMATAR TODOS COUPONSFINDED PARA O DTO DO CUPOM USANDO STREAM ETC
        List<EventDetailDTO.CouponDTO> coupons = couponsFinded.stream().map(coupon -> new EventDetailDTO.CouponDTO(coupon.getId(),coupon.getDiscount(),coupon.getValid())).collect(Collectors.toList());
        return new EventDetailDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getUf() : "",
                event.getImgUrl(),
                event.getEventUrl(),
                coupons);

    }
    public Event createEvent(EventRequestDTO data) {
        String imgUrl = null;

        if (data.image() != null) {
            imgUrl = this.uploadImage(data.image());
        }
        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setDate(new Date(data.date()));
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());
        eventRepository.save(newEvent);

        if(!newEvent.getRemote()){
            this.addressService.createAddress(data,newEvent);
        }
        return newEvent;
    }

    private String uploadImage(MultipartFile multipartFile) {
        String fileName = UUID.randomUUID()+ " - " + multipartFile.getOriginalFilename();
        try {
            File file = this.convertMultiPartToFile(multipartFile);
            s3Client.putObject(bucketName,fileName,file);
            file.delete();
            return s3Client.getUrl(bucketName,fileName).toExternalForm();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }


}

