package pnu.ibe.justice.mentoring.model;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    /*'''GUEST("ROLE_GUEST"),
    ADMIN("ROLE_ADMIN"),
    MANAGER,
    MENTO,
    MENTEE,
    MEMBER,
    GUEST,-->*/

    ROOT("ROLE_ROOT","최상위 관리자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    MANAGER("ROLE_MANAGER", "매니저"),
    MENTO("ROLE_MENTO", "멘토"),
    MENTEE("ROLE_MENTEE", "멘티"),
    MEMBER("ROLE_MEMBER", "일반 사용자"),
    GUEST("ROLE_GUEST", "게스트");
    private final String key;
    private final String title;
}
