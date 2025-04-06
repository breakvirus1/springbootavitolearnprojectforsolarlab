package com.example.avitorest1.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="posts", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class PostEntity {
//    @Version
//    @Column(name = "version")
//    private int version;
//
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;
    @Column(name = "post_name", length = 100, nullable = false)
    private String name;
    @Column(name = "category", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private com.example.avitorest1.enums.CategoryEnum category;
    @Column(name = "description")
    private String description;
    @Column(name = "posted_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;
    @Column(name = "price")
    private int price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_name",nullable = false)
    private AuthorEntity authorName;


}
