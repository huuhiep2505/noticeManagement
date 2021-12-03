package hiepnh.noticemanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "notice")
public class NoticeEntity extends BaseEntity<String> {
    private static final long serialVersionUID = 344881855277285582L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "number_view")
    private Integer numberOfView;
    @ManyToOne
    @JoinColumn(name = "author")
    @JsonIgnore
    private UserEntity userEntity;
    @Column(name = "is_active")
    private Boolean isActive;
    @OneToMany(cascade= {CascadeType.ALL,CascadeType.PERSIST},fetch = FetchType.EAGER)
    @JoinColumn(name = "notice_id", referencedColumnName = "id")
    private List<AttachFileEntity> attachFiles;

}
