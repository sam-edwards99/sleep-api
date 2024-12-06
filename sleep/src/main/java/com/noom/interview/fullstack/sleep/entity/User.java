package com.noom.interview.fullstack.sleep.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "sleeper")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
