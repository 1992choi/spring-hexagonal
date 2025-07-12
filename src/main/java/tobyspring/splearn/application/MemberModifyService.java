package tobyspring.splearn.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.provided.MemberFinder;
import tobyspring.splearn.application.provided.MemberRegister;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.*;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {
    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        checkDuplicationEmail(registerRequest);

        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    @Override
    public Member activate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.activate();

        /*
            memberRepository.save(member)를 명시적으로 사용하는 이유

            - 단순히 return member;만 해도 메서드 레벨에서는 문제 없어 보일 수 있으나,
              Spring Data JPA의 save()는 실제로 persist 또는 merge 동작을 트리거하며,
              이때 Entity lifecycle 이벤트 또는 Domain Event(@DomainEvents) 처리가 함께 이루어질 수 있다.
            - 특히 Event 발행 등의 부수 효과를 기대하는 경우에는 save()를 명시적으로 호출하는 습관을 들이는 것이 바람직하다.
            - Spring Data Repository는 JpaRepository를 포함하고 있으며, save()는 그 핵심 메서드 중 하나로, 저장 외에 이벤트 트리거의 역할도 한다.
         */
        // return member;
        return memberRepository.save(member);
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클릭해서 등록을 완료해주세요.");
    }

    private void checkDuplicationEmail(MemberRegisterRequest registerRequest) {
        if (memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()) {
            throw new DuplicateEmailException("이미 사용중인 이메일입니다: " + registerRequest.email());
        }
    }

}
