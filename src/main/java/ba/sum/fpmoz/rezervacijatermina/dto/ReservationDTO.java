package ba.sum.fpmoz.rezervacijatermina.dto;

public class ReservationDTO {
    private Long roomId;
    private String date; // format "yyyy-MM-dd"

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}

