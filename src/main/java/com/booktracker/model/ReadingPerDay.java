package com.booktracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="reading_per_day")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadingPerDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private boolean read;
    private LocalDate date;
    private Integer pagesRead;
}
