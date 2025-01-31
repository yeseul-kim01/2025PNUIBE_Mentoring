package pnu.ibe.justice.mentoring.model;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
public class ManagerDTO {

    private Integer seqId;

    @NotNull
    private String name;


    private String email;

    private String content;

    @NotNull
    private Grade grade;
}
