package com.htbs.api.entity.hotel;

import com.htbs.api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hotels")
public class Hotel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long id;

    @Column(name = "hotel_name", nullable = false)
    private String name;

    @Column(name = "hotel_location", nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "hotel_grade", nullable = false)
    private HotelGrade grade;

    @Builder
    private Hotel(String name, String location, HotelGrade grade) {
        this.name = name;
        this.location = location;
        this.grade = grade;
    }
}
