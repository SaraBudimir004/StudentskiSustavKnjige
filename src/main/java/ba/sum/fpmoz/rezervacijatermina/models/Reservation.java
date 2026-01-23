package ba.sum.fpmoz.rezervacijatermina.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "reservations")
@Schema(
        name = "Reservation",
        description = "Predstavlja rezervaciju termina za sobu u čitaonici"
)
public class Reservation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Jedinstveni ID rezervacije")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Korisnik koji je napravio rezervaciju")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @Schema(description = "Soba koja se rezervira")
    private Room room;

    @Column(nullable = false)
    @Schema(description = "Početak rezervacije", example = "2026-01-22T10:00")
    private LocalDateTime startTime;

    @Column(nullable = false)
    @Schema(description = "Kraj rezervacije", example = "2026-01-22T12:00")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Status rezervacije (ACTIVE, CANCELED)")
    private ReservationStatus status;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }


}
