package com.noom.interview.fullstack.sleep.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Duration;

public class CustomDurationSerializer extends JsonSerializer<Duration> {
    @Override
    public void serialize(Duration duration, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        int hours = duration.toHoursPart();
        int minutes = duration.toMinutesPart();
        gen.writeString(hours + "h" + minutes + "m");
    }
}
