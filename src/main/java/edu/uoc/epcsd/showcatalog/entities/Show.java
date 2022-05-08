package edu.uoc.epcsd.showcatalog.entities;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@ToString
@Getter
@Setter
@Data
@Table(name = "tbl_shows")
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "onsaledate", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date onSaleDate;

    @Column(name = "status", nullable = true)
    private String status;

    @OneToMany(cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.PERSIST})
    private List<Performance> performances;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE})
    @JoinTable(
            name = "show_categories",
            joinColumns = @JoinColumn(name = "id_show"),
            inverseJoinColumns = @JoinColumn(name = "id_category")
    )
    private Set<Category> categories = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return Float.compare(show.price, price) == 0 && duration == show.duration && capacity == show.capacity
                && Objects.equals(id, show.id) && Objects.equals(name, show.name) && Objects.equals(description, show.description)
                && Objects.equals(image, show.image) && Objects.equals(onSaleDate, show.onSaleDate) && Objects.equals(status, show.status)
                && Objects.equals(performances, show.performances) && Objects.equals(categories, show.categories);
    }
}
