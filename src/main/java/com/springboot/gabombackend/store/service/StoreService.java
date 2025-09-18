package com.springboot.gabombackend.store.service;

import com.springboot.gabombackend.store.entity.Store;
import com.springboot.gabombackend.store.repository.StoreRepository;
import com.springboot.gabombackend.store.repository.VisitRepository;
import com.springboot.gabombackend.store.dto.StoreDetailResponse;
import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;

    public List<Store> searchStores(String keyword) {
        return storeRepository.searchByKeyword(keyword);
    }

    public StoreDetailResponse getStoreDetail(Long storeId) {
        // 가게 찾기
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        // 로그인 유저 가져오기 (loginId 기반)
        String loginId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 내 방문 수
        int myVisitCount = visitRepository.findMyVisitCount(currentUser, store);

        // top visitor
        StoreDetailResponse.VisitorInfo topVisitorDto = null;
        var topVisitorList = visitRepository.findTopVisitors(store, PageRequest.of(0, 1));
        if (!topVisitorList.isEmpty()) {
            Object[] row = topVisitorList.get(0);
            User topUser = (User) row[0];
            Long total = (Long) row[1];

            topVisitorDto = StoreDetailResponse.VisitorInfo.builder()
                    .nickname(topUser.getNickname())
                    .visitCount(total.intValue())
                    .build();
        }

        // 내 방문 DTO
        StoreDetailResponse.VisitorInfo myVisitDto = StoreDetailResponse.VisitorInfo.builder()
                .nickname(currentUser.getNickname())
                .visitCount(myVisitCount)
                .build();

        // 최종 반환
        return StoreDetailResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .category(store.getCategory())
                .openingHours(store.getOpeningHours())
                .address(store.getAddress())
                .topVisitor(topVisitorDto)
                .myVisit(myVisitDto)
                .build();
    }
}
