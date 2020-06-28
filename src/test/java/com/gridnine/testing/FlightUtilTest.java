package com.gridnine.testing;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import com.gridnine.testing.util.FlightUtil;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.gridnine.testing.util.FlightUtil.filterByFunction;
import static com.gridnine.testing.util.FlightUtil.isBetween;
import static org.junit.Assert.*;

public class FlightUtilTest {
    List<Flight> flights;
    Map<Flight, List<Segment>> map;

    @Before
    public void initTest() {
        flights = Arrays.asList(
                new Flight(Arrays.asList(//вылет до текущего момента времени
                        new Segment(LocalDateTime.of(2020, Month.JANUARY, 10, 10, 00, 00),
                                LocalDateTime.of(2020, Month.JANUARY, 10, 11, 00, 00)),
                        new Segment(LocalDateTime.of(2020, Month.JANUARY, 10, 11, 30, 00),
                                LocalDateTime.of(2020, Month.JANUARY, 10, 15, 00, 00))
                )),
                new Flight(Arrays.asList(//имеются сегменты с датой прилёта раньше даты вылета
                        new Segment(LocalDateTime.of(2020, Month.FEBRUARY, 10, 10, 00, 00),
                                LocalDateTime.of(2020, Month.FEBRUARY, 10, 11, 00, 00)),
                        new Segment(LocalDateTime.of(2020, Month.FEBRUARY, 10, 12, 30, 00),
                                LocalDateTime.of(2020, Month.FEBRUARY, 10, 11, 30, 00))
                )),
                new Flight(Arrays.asList(//общее время, проведённое на земле превышает два часа
                        new Segment(LocalDateTime.of(2020, Month.JUNE, 10, 10, 00, 00),
                                LocalDateTime.of(2020, Month.JUNE, 10, 11, 00, 00)),
                        new Segment(LocalDateTime.of(2020, Month.JUNE, 28, 11, 30, 00),
                                LocalDateTime.of(2020, Month.JUNE, 28, 15, 00, 00))
                )),
                new Flight(Arrays.asList(//вылет после текущего времени
                        new Segment(LocalDateTime.of(2020, Month.JUNE, 30, 10, 00, 00),
                                LocalDateTime.of(2020, Month.JUNE, 30, 11, 00, 00))
                ))
        );
        map = flights.stream().collect(Collectors.toMap(Function.identity(), Flight::getSegments));
    }

    @Test
    public void filterByPredicateTest() {
        List<Flight> newFlights = FlightUtil.filterByPredicate(map, s -> FlightUtil.isBetween(s.getDepartureDate(), LocalDateTime.now(), null, false, false));
        assertEquals(1, newFlights.size());
        assertEquals(newFlights.get(0), flights.get(3));
    }

    @Test
    public void filterByFunctionTest() {
        List<Flight> newFlights = filterByFunction(map, s -> (int) Duration.between(s.getDepartureDate(), s.getArrivalDate()).toHours(), 2);
        assertEquals(2, newFlights.size());
        assertEquals(newFlights.get(0), flights.get(1));
    }

    @Test
    public void isBetweenTest() {
        Boolean testValue;
        LocalDateTime value = LocalDateTime.now().minusDays(1);
        testValue = isBetween(value, LocalDateTime.now(), null, false, false);
        assertFalse(testValue);

        testValue = isBetween(value, null, null, false, false);
        assertTrue(testValue);

        testValue = isBetween(LocalDateTime.now(), LocalDateTime.now(), null, false, false);
        assertFalse(testValue);

        testValue = isBetween(LocalDateTime.now(), LocalDateTime.now(), null, true, false);
        assertTrue(testValue);

        testValue = isBetween(LocalDateTime.now(), null, LocalDateTime.now(), false, false);
        assertFalse(testValue);

        testValue = isBetween(LocalDateTime.now(), null, LocalDateTime.now(), false, true);
        assertTrue(testValue);
    }
}