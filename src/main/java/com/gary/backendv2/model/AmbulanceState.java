package com.gary.backendv2.model;

import com.gary.backendv2.model.enums.AmbulanceStateType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@Entity
public class AmbulanceState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stateId;

    @Enumerated(EnumType.STRING)
    private AmbulanceStateType stateType;

    @Embedded
    private TimeWindow stateTimeWindow;


    @Embeddable
    @Getter
    @Setter
    public static class TimeWindow {
        protected TimeWindow() {}

        public TimeWindow(LocalDateTime start, LocalDateTime end) {
            this.startTime = start;
            this.endTime = end;
        }

        public static TimeWindow days(int numberOfDays) {
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.plusDays(numberOfDays);

            return new TimeWindow(start, end);
        }

        public static TimeWindow hours(int numberOfHours) {
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.plusHours(numberOfHours);

            return new TimeWindow(start, end);
        }

        public static TimeWindow minutes(int numberOfMinutes) {
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.plusMinutes(numberOfMinutes);

            return new TimeWindow(start, end);
        }

        public static TimeWindow startingFrom(LocalDateTime startingDate) {
            return new TimeWindow(startingDate, null);
        }

        public static TimeWindow startingFrom(LocalDateTime startingDate, int duration, Unit unit) {
            unit = Objects.requireNonNull(unit, "Duration unit cannot be null");

            LocalDateTime end;

            switch (unit) {
                case MINUTES -> end = startingDate.plusMinutes(duration);
                case HOURS ->  end = startingDate.plusHours(duration);
                case DAYS -> end = startingDate.plusDays(duration);
                case WEEKS -> end = startingDate.plusWeeks(duration);
                default -> throw new RuntimeException("Undefined duration unit");
            }

            return new TimeWindow(startingDate, end);
        }

        public static TimeWindow fixed(LocalDateTime start, LocalDateTime end) {
            return new TimeWindow(start, end);
        }

        public static TimeWindow fixed(LocalDate start, LocalDate end) {
            LocalDateTime starting = start.atTime(LocalTime.now());
            LocalDateTime ending = end.atTime(LocalTime.now());

            return new TimeWindow(starting, ending);
        }

        private LocalDateTime startTime;
        private LocalDateTime endTime;


        private enum Unit {
            MINUTES, HOURS, DAYS, WEEKS
        }
    }
}
