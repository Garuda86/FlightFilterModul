package com.gridnine.testing.util;


import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class FlightUtil {

    public static List<Flight> filterByPredicate(Map<Flight, List<Segment>> map, Predicate<Segment> filter) {


        return map.entrySet()
                .stream()
                .filter(e -> e.getValue().stream().anyMatch(filter)) //s -> !s.getArrivalDate().isBefore(s.getDepartureDate())))
                .map(Entry::getKey)
                .collect(Collectors.toList());

    }

    public static List<Flight> filterByFunction (Map<Flight, List<Segment>> map, ToIntFunction<Segment> filter, int duration) {
        return map.entrySet()
                .stream()
                .filter(e -> e.getValue().stream().mapToInt(filter)
                        .sum() <= duration)
                .map(Entry::getKey)
                .collect(Collectors.toList());
    }


    public static <T extends Comparable<T>> boolean isBetween(T value, T start, T end, boolean includeStart, boolean includeEnd) {
        Objects.requireNonNull(value);
        return (start == null || (includeStart ? value.compareTo(start) >= 0 : value.compareTo(start) > 0))
                && (end == null || (includeEnd ? value.compareTo(end) <= 0 : value.compareTo(end) < 0));
    }

    public static boolean isTwoHourDuration(LocalDateTime start, LocalDateTime end, int duration) {
        return (end.isAfter(start)) && Duration.between(start, end).toHours() <= duration;
    }
}
