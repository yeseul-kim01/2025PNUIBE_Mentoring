package pnu.ibe.justice.mentoring.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class SystemUserDTO {

    private Integer Id;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String password;

    @NotNull
    private Role role;

}
