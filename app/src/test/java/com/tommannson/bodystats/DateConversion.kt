package com.tommannson.bodystats

import org.junit.Test
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

class DateConversion {

    @Test
    fun `test datetimeConversion`() {

        val triggerTime = LocalDateTime.now()
        var zone = ZoneId.systemDefault();
        var rules = zone.getRules()
        var instant = Instant.now();

        var offset = rules.getOffset(instant);
        val cutMs = triggerTime.withNano(0)
        val time =  cutMs.toEpochSecond(offset)

        zone = ZoneId.systemDefault();
        rules = zone.getRules()
        instant = Instant.now();
        offset = rules.getOffset(instant);

        val restoredTime =  LocalDateTime.ofEpochSecond(time, 0, offset)

        assert(cutMs == restoredTime)
    }
}