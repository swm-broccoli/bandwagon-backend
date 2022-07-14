package bandwagon.bandwagonback.domain;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Getter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_infos")
@Getter
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class UserInfo {

    @Id @GeneratedValue
    @Column(name = "user_info_id")
    private Long Id;

    private int age;

    @Type(type = "list-array")
    @Column(columnDefinition = "text[]")
    private List<String> position;
    @Type(type = "list-array")
    @Column(columnDefinition = "text[]")
    private List<String> area;
    @Type(type = "list-array")
    @Column(columnDefinition = "text[]")
    private List<String> genre;

    @Column(columnDefinition="TEXT")
    private String description;
    @Column(columnDefinition="TEXT")
    private String avatar_url;

    @OneToOne(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private User user;
}
