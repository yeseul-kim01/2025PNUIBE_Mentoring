package pnu.ibe.justice.mentoring.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "HomeEdits")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class HomeEdit {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seqId;

    @Column()
    private String contentType;

    @Column(columnDefinition = "longtext")
    private String mainHome_content;

}
