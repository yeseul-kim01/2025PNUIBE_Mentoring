package pnu.ibe.justice.mentoring.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pnu.ibe.justice.mentoring.model.Grade;

@Getter
@Setter
@Entity
@Table(name = "Managers")
@EntityListeners(AuditingEntityListener.class)
public class Manager {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seqId;

    @Column
    private String name;

    @Column
    private String email;

    @Column(columnDefinition = "longtext")
    private String content;


    @Enumerated(EnumType.STRING)
    @NotNull
    private Grade Grade;

}
