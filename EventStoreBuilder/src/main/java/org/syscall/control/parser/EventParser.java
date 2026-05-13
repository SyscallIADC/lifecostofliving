package org.syscall.control.parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class EventParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
    .ofPattern("yyyyMMdd").withZone(ZoneId.of("UTC"));

    public static ParsedEvent parse(String rawJson, String topic) {
        JsonObject json = JsonParser.parseString(rawJson).getAsJsonObject();

        String ss = json.get("ss").getAsString();
        String ts = json.get("ts").getAsString();

        String folderDate = DATE_FORMATTER.format(Instant.parse(ts));
        return new ParsedEvent(topic, ss, folderDate, rawJson);
    }

}
