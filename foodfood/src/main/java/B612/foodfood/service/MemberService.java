package B612.foodfood.service;

import B612.foodfood.domain.*;
import B612.foodfood.exception.AppException;
import B612.foodfood.repository.*;
import B612.foodfood.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static B612.foodfood.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    @Value("${jwt.token.expireTimeMs}")
    private long expireTimeMs;  // 토큰 만료 시간: 1시간

    @Transactional(readOnly = false)
    public Long join(Member member) {
        // 이미 가입된 멤버(동일한 아이디가 존재하는 경우) 회원 가입 불가
        Optional<Member> memberFindByLogInId =
                memberRepository.findByLogInUsername(member.getPersonalInformation().getLogIn().getUsername());

        if (memberFindByLogInId.isPresent()) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.join(Member member)\n" +
                    "발생원인: 이미 가입된 아이디입니다.");

            throw new AppException(MEMBER_ID_DUPLICATED, "이미 가입된 아이디입니다.");
        }

        memberRepository.save(member);
        return member.getId();
    }

    public Member findMemberById(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.findMemberById(Long memberId)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");

            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "가입되지 않은 유저입니다.");
        }

        return member.get();
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public List<Member> findMembersByName(String name) {
        return memberRepository.findByName(name);
    }

    public Member findMemberByLogInUsername(String username) {
        Optional<Member> findMember = memberRepository.findByLogInUsername(username);
        if (findMember.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.findMemberByLogInId(String username)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "가입되지 않은 유저입니다.");
        }
        return findMember.get();
    }

    public Member findMemberByPhoneNumber(String phoneNumber) {
        Optional<Member> findMember = memberRepository.findByPhoneNumber(phoneNumber);
        if (findMember.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.findMemberByPhoneNumber(String phoneNumber)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "가입되지 않은 유저입니다.");
        }
        return findMember.get();
    }

    /**
     * 업데이트 로직
     */
    @Transactional(readOnly = false)
    public void updateAchieveBodyGoal(Long memberId, AchieveBodyGoal achieveBodyGoal) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.updateAchieveBodyGoal(Long memberId, AchieveBodyGoal achieveBodyGoal)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.updateAchieveBodyGoal(achieveBodyGoal);
    }

    @Transactional(readOnly = false)
    public void updateMemberPassword(Long memberId, String password) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.updateMemberPassword(Long memberId, String password)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.updatePassword(password);
    }

    @Transactional(readOnly = false)
    public void updateMemberPhoneNumber(Long memberId, String phoneNumber) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.updateUserPhoneNumber(Long memberId, String phoneNumber)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.updatePhoneNumber(phoneNumber);
    }


    @Transactional(readOnly = false)
    public void updateAddAvoidIngredient(Long memberId, Ingredient ingredient) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.updateAddAvoidIngredient(Long memberId, Ingredient ingredient)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.addAvoidIngredient(ingredient);
    }

    @Transactional(readOnly = false)
    public void updateAddDisease(Long memberId, Disease disease) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.updateAddMemberDisease(Long memberId, Disease disease)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.addDisease(disease);
    }

    @Transactional(readOnly = false)
    public void updateAddDrug(Long memberId, Drug drug) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.updateAddMemberDrug(Long memberId, Drug drug)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.addDrug(drug);
    }

    @Transactional(readOnly = false)
    public void updateAddBodyComposition(Long memberId, BodyComposition bodyComposition) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.updateAddMemberDrug(Long memberId, Drug drug)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.addBodyComposition(bodyComposition);
    }

    public String login(String username, String password) {
        // 해당 id의 member 존재 안함.
        Optional<Member> findMember = memberRepository.findByLogInUsername(username);

        if (findMember.isEmpty()) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.login(String username, String password)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        if (!encoder.matches(password, member.getPersonalInformation().getLogIn().getPassword())) {
            log.error("오류 발생\n" +
                    "발생위치: MemberService.login(String username, String password)\n" +
                    "발생원인: 잘못된 비밀번호입니다.");
            throw new AppException(INVALID_PASSWORD,
                    "잘못된 비밀번호입니다.");
        }

        // 앞에서 exception 발생 안 한 경우 token 발행
        String token = JwtUtil.createToken(username, secretKey, expireTimeMs);
        return token;
    }
}