package com.tommannson.bodystats.model.reminding

import com.tommannson.bodystats.infrastructure.notifications.notificationorganizer.NotificationInvoker

enum class ReminderType {
    Weight,
    Measurements,
    Composition
}

val ReminderType.text
    get() = when (this) {
        ReminderType.Weight -> "Przypomnienie o ważeniu"
        ReminderType.Measurements -> "Przypomnienie o pomiarach ciała"
        ReminderType.Composition -> "Przypomnienie o składzie ciała"
    }

val ReminderType.notificationId
    get() = when (this) {
        ReminderType.Weight -> NotificationInvoker.WEIGHT_NOTIFICATION_ID
        ReminderType.Measurements -> NotificationInvoker.MEASUREMENTS_NOTIFICATION_ID
        ReminderType.Composition -> NotificationInvoker.COMPOSITION_NOTIFICATION_ID
    }
