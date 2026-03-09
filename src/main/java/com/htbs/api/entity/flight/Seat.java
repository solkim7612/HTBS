package com.htbs.api.entity.flight;

import com.htbs.api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "flight_seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_flight_grade",
                        columnNames = {"flight_id", "seat_grade"}
                )
        }
)
public class Seat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_grade", nullable = false)
    private SeatGrade grade;

    @Column(name = "seat_price", nullable = false)
    private BigDecimal price;

    @Column(name = "max_stock", nullable = false)
    private int max;

    @Column(name = "sold_stock", nullable = false)
    private int sold;

    @Builder
    private Seat(Flight flight, SeatGrade grade, BigDecimal price, int max, int sold) {
        this.flight = flight;
        this.grade = grade;
        this.price = price;
        this.max = max;
        this.sold = 0;
    }

}
