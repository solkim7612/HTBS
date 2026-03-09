package com.htbs.api.entity.flight;

import com.htbs.api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@TableGenerator(name = "flights")
public class Flight extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Long id;

    @Column(name = "flight_name", nullable = false)
    private String name;

    @Column(name = "departure_loc", nullable = false)
    private String departure;

    @Column(name = "arrival_loc", nullable = false)
    private String arrival;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Builder
    private Flight(String name, String departure, String arrival, LocalDateTime departTime, LocalDateTime arrivalTime) {
        this.name=name;
        this.departure=departure;
        this.arrival=arrival;
        this.departTime=departTime;
        this.arrivalTime=arrivalTime;
    }
}
