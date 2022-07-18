package bandwagon.bandwagonback.domain;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_infos")
@Getter @Setter
@TypeDefs({
        @TypeDef(name = "list-array", typeClass = ListArrayType.class),
        @TypeDef(name = "json", typeClass = JsonType.class)
})
public class UserInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_info_id")
    private Long Id;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private List<UserPerformance> userPerformances = new ArrayList<>();
}
