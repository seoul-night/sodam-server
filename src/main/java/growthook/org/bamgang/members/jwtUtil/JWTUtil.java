package growthook.org.bamgang.members.jwtUtil;

import growthook.org.bamgang.members.dto.token.MemberToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTUtil {
    @Value("${kakao-key}")
    private String secretKeyPlain;

    private final long EXPIRATION_SECONDS = 60 * 60 * 24 *365;	//24시간

    //application.properties에 등록된 변수
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKeyPlain.getBytes());
    }

    // 토큰 생성
    public String generateToken(MemberToken member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_SECONDS * 1000);

        return Jwts.builder()
                .claim("id", member.getId())
                .claim("nickName", member.getNickName())
                .claim("profileName", member.getProfile())
                .setExpiration(expiration)
                .signWith(getSecretKey())
                .compact();
    }

    // 토큰 유효성 검사
    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰으로 부터 ID 조회
    public int getMemberIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        int memberId = claims.get("memberId", Integer.class);
        return memberId;
    }
}
