package com.htbs.api.entity.hotel;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hotel_roomtype")
public class Roomtype {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomtype_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    @Column(name = "roomtype_grade", nullable = false)
    private RoomGrade grade;

    @Column(name = "default_price", nullable = false)
    private BigDecimal price;

    @Column(name = "roomtype_capacity", nullable = false)
    private int capacity;

    @Builder
    private Roomtype(Hotel hotel, RoomGrade grade, BigDecimal price, int capacity) {
        this.hotel = hotel;
        this.grade = grade;
        this.price = price;
        this.capacity = capacity;
    }
}
