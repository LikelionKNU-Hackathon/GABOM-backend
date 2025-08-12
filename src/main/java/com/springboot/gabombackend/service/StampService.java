package com.springboot.gabombackend.service;

import com.springboot.gabombackend.entity.Stamp;
import com.springboot.gabombackend.repository.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StampService {

    private final StampRepository stampRepository;

    public List<Stamp> getAllStamps() {
        return stampRepository.findAll();
    }

    public List<Stamp> getStampsByCategory(String category) {
        return stampRepository.findByCategory(category);
    }
}