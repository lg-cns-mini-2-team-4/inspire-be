package com.inspire.user_service.favorite.ctrl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inspire.user_service.favorite.domain.dto.FavoriteRequestDTO;
import com.inspire.user_service.favorite.domain.dto.FavoriteResponseDTO;
import com.inspire.user_service.favorite.service.FavoriteService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/add")
    public ResponseEntity<String> add(
            @RequestHeader("X-User-Id") Long userId, 
            @RequestBody FavoriteRequestDTO request) {
        favoriteService.add(userId, request);
        return ResponseEntity.ok("즐겨찾기에 추가되었습니다.");
    }

    // 2. 즐겨찾기 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<FavoriteResponseDTO>> list(
            @RequestHeader("X-User-Id") Long userId) {
        List<FavoriteResponseDTO> favorites = favoriteService.list(userId);
        return ResponseEntity.ok(favorites);
    }

    // 3. 즐겨찾기 삭제
    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(
            @RequestHeader("X-User-Id") Long userId, 
            @RequestBody FavoriteRequestDTO request) {
        favoriteService.remove(userId, request);
        return ResponseEntity.ok("즐겨찾기에서 삭제되었습니다.");
    }
}
