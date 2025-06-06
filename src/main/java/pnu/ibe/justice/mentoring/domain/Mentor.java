package pnu.ibe.justice.mentoring.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Mentors")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Mentor {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seqId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer minMent;

    @Column(nullable = false)
    private Integer maxMent;

    @Column(columnDefinition = "longtext")
    private String content;

    @Column(nullable = false)
    private String team;

    @Column
    private Integer mFId;

    @Column
    private Integer status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id", nullable = false)
    private User users;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL , orphanRemoval = true )
    private Set<MentorFile> mentorFiles;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}