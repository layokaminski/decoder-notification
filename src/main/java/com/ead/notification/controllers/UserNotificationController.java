package com.ead.notification.controllers;

import com.ead.notification.DTOs.NotificationDTO;
import com.ead.notification.models.NotificationModel;
import com.ead.notification.services.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserNotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/users/{userId}/notifications")
    public ResponseEntity<Page<NotificationModel>> getAllNotificationsByUser(
            @PageableDefault(page = 0, size = 10, sort = "notificationId", direction = Sort.Direction.DESC)
            Pageable pageable,
            @PathVariable UUID userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.findAllNotificationsByUser(userId, pageable));
    }

    @PutMapping("users/{userId}/notifications/{notificationId}")
    public ResponseEntity<Object> updateNotification(
            @PathVariable UUID userId,
            @PathVariable UUID notificationId,
            @RequestBody @Valid NotificationDTO notificationDTO
    ) {
        log.debug("PUT updateNotification notificationDTO received {} ", notificationDTO.toString());

        Optional<NotificationModel> notificationModelOptional = notificationService
                .findByNotificationIdAndUserId(notificationId, userId);

        if (!notificationModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }

        var notificationModel = notificationModelOptional.get();

        BeanUtils.copyProperties(notificationDTO, notificationModel);

        log.debug("PUT updateNotification notificationId saved {} ", notificationModel.getNotificationId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificationService.saveNotification(notificationModel));
    }
}
