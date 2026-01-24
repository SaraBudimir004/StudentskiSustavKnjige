package ba.sum.fpmoz.rezervacijatermina.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public class ChangePassword {

    @Schema(
            description = "Stara lozinka korisnika",
            example = "lozinka123"
    )

    private String oldPassword;

    @Schema(
            description = "Nova lozinka korisnika",
            example = "123456"
    )
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}