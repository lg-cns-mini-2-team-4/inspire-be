package com.inspire.favorite.ctrl;

import com.inspire.favorite.domain.dto.*;
import com.inspire.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    //  즐겨찾기 추가
    @PostMapping
    public ResponseEntity<FavoriteResponseDTO> addFavorite(
            @RequestBody FavoriteRequestDTO requestDTO) {

        return ResponseEntity.ok(
                favoriteService.addFavorite(requestDTO)
        );
    }

    //  즐겨찾기 목록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteResponseDTO>> getFavorites(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                favoriteService.getFavorites(userId)
        );
    }

    //  즐겨찾기 삭제
    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(
            @RequestParam Long userId,
            @RequestParam String itemCode) {

        favoriteService.removeFavorite(userId, itemCode);
        return ResponseEntity.ok().build();
    }
}