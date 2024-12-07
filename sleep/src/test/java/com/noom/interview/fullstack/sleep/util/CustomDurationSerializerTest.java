package com.noom.interview.fullstack.sleep.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Duration;

import static com.noom.interview.fullstack.sleep.SleepApplication.UNIT_TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles(UNIT_TEST_PROFILE)
public class CustomDurationSerializerTest {
    @Test
    public void testCustomDurationSerializer() throws IOException {
        Duration duration = Duration.ofMinutes(150L);
        Writer jsonWriter = new StringWriter();
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();

        CustomDurationSerializer customDurationSerializer = new CustomDurationSerializer();
        customDurationSerializer.serialize(duration, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertEquals("\"2h30m\"", jsonWriter.toString());
    }
}
