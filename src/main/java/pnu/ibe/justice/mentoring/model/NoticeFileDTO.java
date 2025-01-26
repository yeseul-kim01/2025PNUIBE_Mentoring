package pnu.ibe.justice.mentoring.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoticeFileDTO {

    private Integer seqId;

    @Size(max = 255)
    private String fileSrc;

    private UUID uuid;

    private String filename;

    private Integer userSeqId;

    private Integer notice;

}
