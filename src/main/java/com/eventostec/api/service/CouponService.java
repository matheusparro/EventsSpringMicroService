package com.eventostec.api.service;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.domain.coupon.CouponResponseDTO;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.repositories.CouponRepository;
import com.eventostec.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository eventRepository;
    public CouponResponseDTO addCouponToEvent(UUID eventId, CouponRequestDTO coupon) {
        Event eventFound = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        Coupon newCoupon = new Coupon();
        newCoupon.setEvent(eventFound);
        newCoupon.setDiscount(coupon.discount());
        newCoupon.setCode(coupon.code());
        newCoupon.setValid(new Date(coupon.valid()* 1000L));
        couponRepository.save(newCoupon);
        CouponResponseDTO couponResponseDTO = new CouponResponseDTO(newCoupon.getId(),newCoupon.getDiscount(),newCoupon.getValid());
        return couponResponseDTO;
    }

    public List<Coupon> getCouponsByEvent(UUID eventId, Date dateCheck) {
        return couponRepository.findByEventIdAndValidAfter(eventId,dateCheck);
    }
}
