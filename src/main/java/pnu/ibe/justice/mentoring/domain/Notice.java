package pnu.ibe.justice.mentoring.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Notices")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Notice {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seqId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    @Column(columnDefinition = "tinyint", length = 1)
    private Boolean isPopup;

    @Column(columnDefinition = "tinyint", length = 1)
    private Boolean isMust;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NoticeFile> noticeFiles;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "users_id", nullable = true)
    private User users;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
