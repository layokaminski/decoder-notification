package com.ead.notification.DTOs;

import com.ead.notification.enums.NotificationStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NotificationDTO {

    @NotNull
    private NotificationStatus notificationStatus;
}
