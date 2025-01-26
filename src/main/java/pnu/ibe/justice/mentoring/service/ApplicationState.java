package pnu.ibe.justice.mentoring.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class ApplicationState {
    private boolean mentorOpenStatus;
    private boolean menteeOpenStatus;

}