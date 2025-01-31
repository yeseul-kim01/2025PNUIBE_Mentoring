package pnu.ibe.justice.mentoring.model;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {

    MANAGER("GRADE_MANAGER", "매니저"),
    PROF("GRADE_PROF","교수님");

    private final String key;
    private final String Value;



}
