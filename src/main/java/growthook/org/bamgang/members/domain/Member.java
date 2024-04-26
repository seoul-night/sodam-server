package growthook.org.bamgang.members.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name="userid")
    private int userId;

    @Column(name="nickname")
    private String nickName;

    private int exp;

    @Column(name = "walkedday")
    private int walkedDay;


    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWalkedDay() {
        return walkedDay;
    }

    public void setWalkedDay(int walkedDay) {
        this.walkedDay = walkedDay;
    }
}
