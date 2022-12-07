package com.gary.backendv2.model.users.employees;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.json.JsonParseException;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Slf4j
public class MappedSchedule {
    private final Map<DayOfWeek, Pair<LocalTime, LocalTime>> timeTable;

    private MappedSchedule(Map<DayOfWeek, Pair<LocalTime, LocalTime>> workSchedule) {
        this.timeTable = workSchedule;
    }

    public Pair<LocalTime, LocalTime> getWorkingHours(DayOfWeek dayOfWeek) {
        return this.timeTable.get(dayOfWeek);
    }

    @SneakyThrows
    public static MappedSchedule fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (!validJson(json)) {
                throw new JsonParseException(new Throwable("Invalid JSON of work-schedule definition"));
            }

            JsonNode parent = mapper.readTree(json);

            Map<DayOfWeek, Pair<LocalTime, LocalTime>> workSchedule = new LinkedHashMap<>();
            int i = 0;
            for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
                JsonNode node = parent.get(i);
                if (node != null) {
                    JsonNode dayHoursNode = node.get(dayOfWeek.toString());
                    if (dayHoursNode == null) {
                        throw new JsonParseException(new Throwable("Invalid JSON of work-schedule definition"));
                    }

                    JsonNode startNode = dayHoursNode.get("start");
                    JsonNode endNode = dayHoursNode.get("end");
                    if (startNode == null || endNode == null) {
                        throw new JsonParseException(new Throwable("Invalid JSON of work-schedule definition"));
                    }

                    String[] start = startNode.toString().replace("\"", "").split(":");
                    String[] end = endNode.toString().replace("\"", "").split(":");
                    if (start.length != 2 || end.length != 2) {
                        throw new JsonParseException(new Throwable("Invalid JSON of work-schedule definition"));
                    }

                    LocalTime startTime = LocalTime.of(Integer.parseInt(start[0]), Integer.parseInt(start[1]));
                    LocalTime endTime = LocalTime.of(Integer.parseInt(end[0]), Integer.parseInt(end[1]));

                    Pair<LocalTime, LocalTime> workingHours = Pair.of(startTime, endTime);

                    workSchedule.put(dayOfWeek, workingHours);
                }
                i++;
            }

            return new MappedSchedule(workSchedule);
        } catch (Exception e) {
            log.error(e.getCause().getMessage(), e);
        }

        return null;
    }

    private static boolean validJson(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.readTree(json);
        } catch (JacksonException e) {
            return false;
        }

        return true;
    }
}
