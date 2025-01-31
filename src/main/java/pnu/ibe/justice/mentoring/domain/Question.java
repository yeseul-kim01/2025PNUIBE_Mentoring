package pnu.ibe.justice.mentoring.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Table(name = "Questions")
@EntityListeners(AuditingEntityListener.class)
public class Question {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seqId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "longtext")
    private String content;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<QuestionFile> questionFiles;

    //변경사항//
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Answer> answers;


    //변경사항//
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id", nullable = false)
    private User users;


    //변경사항//
    @Column
    private Integer mFId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}