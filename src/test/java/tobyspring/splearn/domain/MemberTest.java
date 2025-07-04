package tobyspring.splearn.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void createMember() {
        var member = new Member("toby@splearn.app", "Toby", "secret");

        Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

}