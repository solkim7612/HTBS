package com.htbs.api.entity.coupon;

import com.htbs.api.entity.BaseEntity;
import com.htbs.api.entity.payment.Payment;
import com.htbs.api.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "coupon_history",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_issue_limit",
                        columnNames = {"coupon_id", "user_id"}
                )
        }
)
public class History extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_status", nullable = false)
    private CouponStatus status;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Builder
    private History(User user, Coupon coupon, Payment payment, CouponStatus status, LocalDateTime issuedAt, LocalDateTime usedAt) {
        this.user = user;
        this.coupon = coupon;
        this.payment = payment;
        this.status = CouponStatus.UNUSED;
        this.issuedAt = issuedAt;
        this.usedAt = usedAt;
    }
}
