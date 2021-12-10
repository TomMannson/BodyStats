package com.tommannson.bodystats

import com.jakewharton.threetenabp.AndroidThreeTen
import org.junit.Test
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

class DateConversion {

    @Test
    fun test() {

        val triggerTime = LocalDateTime.now()
        var zone = ZoneId.systemDefault();
        var rules = zone.getRules()
        var instant = Instant.now();

        var offset = rules.getOffset(instant);
        val time =  triggerTime.toEpochSecond(offset)

        zone = ZoneId.systemDefault();
        rules = zone.getRules()
        instant = Instant.now();
        offset = rules.getOffset(instant);

        val restoredTime =  LocalDateTime.ofEpochSecond(time, 0, offset)

    }
}