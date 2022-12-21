package com.gary.backendv2.model.users.employees;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.utils.Utils;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Slf4j
public class MappedSchedule {
    private final Map<DayOfWeek, Pair<LocalTime, LocalTime>> timeTable = new LinkedHashMap<>();


    public Pair<LocalTime, LocalTime> getWorkingHours(DayOfWeek dayOfWeek) {
        return this.timeTable.get(dayOfWeek);
    }

    @SneakyThrows
    public static MappedSchedule fromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MappedSchedule.class, new Utils.ScheduleDeserializer());
        mapper.registerModule(module);

        try {
            mapper.readValue(json, MappedSchedule.class);
        } catch (RuntimeException e) {
            throw new HttpException(HttpStatus.BAD_REQUEST, e.getCause().getMessage());
        }

        return mapper.readValue(json, MappedSchedule.class);
    }

}
