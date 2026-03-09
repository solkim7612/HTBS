package com.htbs.api.entity.book;

import com.htbs.api.entity.BaseEntity;
import com.htbs.api.entity.user.User;
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
@Table(name = "books")
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "order_id", unique = true, nullable = false)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated
    @Column(name = "book_type", nullable = false)
    private BookType type;

    @Column(name = "stock_id", nullable = false)
    private Long stockId;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_status", nullable = false)
    private BookStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDate startdate;

    @Column(name = "end_date", nullable = false)
    private LocalDate enddate;

    @Column(name = "headcount", nullable = false)
    private int headcount;

    @Column(name = "book_price", nullable = false)
    private BigDecimal price;

    @Builder
    private Book(String orderId, User user, BookType type, Long stockId, BookStatus status, LocalDate startdate, LocalDate enddate, int headcount, BigDecimal price) {
        this.orderId = orderId;
        this.user = user;
        this.type = type;
        this.stockId = stockId;
        this.status = status;
        this.startdate = startdate;
        this.enddate = enddate;
        this.headcount = headcount;
        this.price = price;
    }
}
