package com.htbs.api.entity.hotel;

import com.htbs.api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "hotel_rooms",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_room_date",
                        columnNames = {"roomtype_id", "stay_date"}
                )
        }
)
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomtype_id", nullable = false)
    private Roomtype roomtype;

    @Column(name = "stay_date", nullable = false)
    private LocalDate date;

    @Column(name = "peak_price")
    private BigDecimal peakPrice;

    @Column(name = "max_stock", nullable = false)
    private int max;

    @Column(name = "sold_stock", nullable = false)
    private int sold;

    @Builder
    private Room(Roomtype roomtype, LocalDate date, BigDecimal peakPrice, int max, int sold) {
        this.roomtype = roomtype;
        this.date = date;
        this.peakPrice = peakPrice;
        this.max = max;
        this.sold = 0;
    }

}
