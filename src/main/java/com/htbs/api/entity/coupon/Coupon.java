package com.htbs.api.entity.coupon;

import com.htbs.api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupons")
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "event_name", nullable = false)
    private String name;

    @Column(name = "coupon_price", nullable = false)
    private BigDecimal price;

    @Column(name = "max_coupon", nullable = false)
    private int max;

    @Column(name = "issued_coupon", nullable = false)
    private int issued;

    @Column(name = "valid_until", nullable = false)
    private LocalDateTime valid;

    @Builder
    private Coupon(String name, BigDecimal price, int max, int issued, LocalDateTime valid) {
        this.name = name;
        this.price = price;
        this.max = max;
        this.issued = 0;
        this.valid = valid;
    }
}
