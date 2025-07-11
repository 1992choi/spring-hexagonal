package tobyspring.splearn.application.required;

import org.springframework.data.repository.Repository;
import tobyspring.splearn.domain.Email;
import tobyspring.splearn.domain.Member;

import java.util.Optional;

/**
 * 회원 정보를 저장하거나 조회한다
 */
public interface MemberRepository extends Repository<Member, Long> { // 특이사항 : JpaRepository 대신 Repository 사용
    Member save(Member member);

    Optional<Member> findByEmail(Email email);
}
