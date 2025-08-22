package com.springboot.gabombackend.store;

import com.springboot.gabombackend.store.dto.StoreDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 가게 검색
    @GetMapping("/search")
    public List<Store> searchStores(@RequestParam String keyword) {
        return storeService.searchStores(keyword);
    }

    // 가게 상세 조회 (최대 방문자 & 내 방문 수 포함)
    @GetMapping("/{storeId}")
    public StoreDetailResponse getStoreDetail(@PathVariable Long storeId) {
        return storeService.getStoreDetail(storeId);
    }
}