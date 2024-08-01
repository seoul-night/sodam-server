package growthook.org.bamgang.members.domain;

import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name="nick_name")
    private String nickName;

    @Column(name="password")
    private String password;

    @Column(name="exp")
    private Integer exp;

    @Column(name="walked_day")
    private Integer walkedDay;

    @Column(name="finished_count")
    private Integer finishedCount;

    @Column(name="picked_count")
    private Integer pickedCount;

    @Column(name="profile")
    private String profile;

    @Column(name="email")
    private String email;

    @Column(name="friend_list")
    private Integer[] friendList;
}
