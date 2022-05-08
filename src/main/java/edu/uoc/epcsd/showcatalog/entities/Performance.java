package edu.uoc.epcsd.showcatalog.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@ToString
@Getter
@Setter
@Data
@Table(name = "tbl_performances")
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = true)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/YYYY")
    //@Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/YYYY")
    private SimpleDateFormat simpleDateFormat;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:MM")
    @Column(name = "time", nullable = true)
    @DateTimeFormat(pattern = "HH:MM")
    private String time;

    @Column(name = "streamingurl", nullable = true)
    private String streamingUrl;

    @Column(name = "remainingseats", nullable = true)
    private Integer remainingSeats;

    @Column(name = "status", nullable = true)
    private String status;

    @ManyToOne(cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.PERSIST})
    private Show show;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Performance that = (Performance) o;
        return Objects.equals(id, that.id) && Objects.equals(simpleDateFormat, that.simpleDateFormat) && Objects.equals(time, that.time) && Objects.equals(streamingUrl, that.streamingUrl) && Objects.equals(remainingSeats, that.remainingSeats) && Objects.equals(status, that.status) && Objects.equals(show, that.show);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, simpleDateFormat, time, streamingUrl, remainingSeats, status, show);
    }
}
