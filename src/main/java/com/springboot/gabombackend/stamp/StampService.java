package com.springboot.gabombackend.stamp;

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