package ba.sum.fpmoz.rezervacijatermina.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "rooms")
@Schema(name = "Room", description = "Predstavlja sobu u čitaonici")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Jedinstveni ID sobe")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Ime ili broj sobe", example = "Soba 101")
    private String name;

    @Column(nullable = false)
    @Schema(description = "Kapacitet sobe", example = "4")
    private Integer capacity;

    @Column
    @Schema(description = "Lokacija sobe u čitaonici", example = "Prvi kat, lijevo")
    private String location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
