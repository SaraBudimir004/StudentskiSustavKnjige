package ba.sum.fpmoz.rezervacijatermina.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ReservationDTO {
    @Schema(
            description = "ID sobe koja se rezervira",
            example = "1"
    )
    private Long roomId;
    @Schema(
            description = "Datum rezervacije (format: yyyy-MM-dd)",
            example = "2026-01-24"
    )
    private String date;

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}

