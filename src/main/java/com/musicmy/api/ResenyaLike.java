package com.musicmy.api;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.musicmy.service.ResenyaLikeService;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/likes")
public class ResenyaLike {

    @Autowired
    private ResenyaLikeService likeService;

    @PostMapping("/like")
    public ResponseEntity<?> like(@RequestParam Long usuarioId, @RequestParam Long resenyaId) {
        boolean liked = likeService.likeResenya(usuarioId, resenyaId);
        return ResponseEntity.ok(Map.of("liked", liked));
    }

    @DeleteMapping("/unlike")
    public ResponseEntity<?> unlike(@RequestParam Long usuarioId, @RequestParam Long resenyaId) {
        boolean unliked = likeService.unlikeResenya(usuarioId, resenyaId);
        return ResponseEntity.ok(Map.of("unliked", unliked));
    }

    @GetMapping("/count")
    public ResponseEntity<?> countLikes(@RequestParam Long resenyaId) {
        long count = likeService.countLikes(resenyaId);
        return ResponseEntity.ok(Map.of("likes", count));
    }

    @GetMapping("/check")
    public ResponseEntity<?> hasUserLiked(@RequestParam Long usuarioId, @RequestParam Long resenyaId) {
        boolean liked = likeService.hasUserLiked(usuarioId, resenyaId);
        return ResponseEntity.ok(Map.of("liked", liked));
    }
}
