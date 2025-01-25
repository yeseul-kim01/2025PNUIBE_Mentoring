package pnu.ibe.justice.mentoring.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerUserDTO {

    private String content;

    private String name;

    private Role role;

    private Integer qRId;

}