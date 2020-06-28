package com.gridnine.testing;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import com.gridnine.testing.repository.FlightBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.gridnine.testing.util.FlightUtil.*;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();
        flights.forEach(System.out::println);
        System.out.println("=======================");
        Map<Flight, List<Segment>> map = flights.stream().collect(Collectors.toMap(Function.identity(), Flight::getSegments));

        //1.	вылет до текущего момента времени
        filterByPredicate(map, s -> isBetween(s.getDepartureDate(), LocalDateTime.now(),
                null, false, false)).forEach(System.out::println);
        //2.	имеются сегменты с датой прилёта раньше даты вылета
        System.out.println("+++++++++++++++++");

        filterByPredicate(map, s -> !s.getArrivalDate().isBefore(s.getDepartureDate())).forEach(System.out::println);

        //3.	общее время, проведённое на земле превышает два часа (время на земле — это интервал между прилётом одного сегмента и вылетом следующего за ним)
        System.out.println("**********************");

        filterByFunction(map, s -> (int) Duration.between(s.getDepartureDate(), s.getArrivalDate()).toHours(), 2).forEach(System.out::println);

    }


}
