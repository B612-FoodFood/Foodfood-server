package B612.foodfood.service;

import B612.foodfood.domain.*;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.ErrorCode;
import B612.foodfood.exception.NoDataExistException;
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

    private final MemberDrugRepository memberDrugRepository;
    private final MemberDiseaseRepository memberDiseaseRepository;
    private final AvoidFoodRepository avoidFoodRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private final Long expireTimeMs = 1000 * 60 * 60L;  // 토큰 만료 시간: 1시간

    @Transactional(readOnly = false)
    public Long join(Member member) {

        // 이미 가입된 멤버(동일한 아이디가 존재하는 경우) 회원 가입 불가
        Optional<Member> memberFindByLogInId =
                memberRepository.findByLogInUsername(member.getPersonalInformation().getLogIn().getUsername());

        if (memberFindByLogInId.isPresent()) {
            throw new AppException(MEMBER_ID_DUPLICATED,
                    "오류 발생\n" +
                            "발생위치: MemberService.join(Member member)\n" +
                            "발생원인: 이미 가입된 아이디입니다.");
        }

        memberRepository.save(member);
        return member.getId();
    }

    public Member findMemberById(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "오류 발생\n" +
                            "발생위치: MemberService.findMemberById(Long memberId)\n" +
                            "발생원인: 가입되지 않은 유저입니다.");
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
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "오류 발생\n" +
                            "발생위치: MemberService.findMemberByLogInId(String username)\n" +
                            "발생원인: 가입되지 않은 유저입니다.");
        }
        return findMember.get();
    }

    public Member findMemberByPhoneNumber(String phoneNumber) {
        Optional<Member> findMember = memberRepository.findByPhoneNumber(phoneNumber);
        if (findMember.isEmpty()) {
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "오류 발생\n" +
                            "발생위치: MemberService.findMemberByPhoneNumber(String phoneNumber)\n" +
                            "발생원인: 가입되지 않은 유저입니다.");
        }
        return findMember.get();
    }

    public Member findMemberByEmail(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isEmpty()) {
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "오류 발생\n" +
                            "발생위치: MemberService.findMemberByEmail(String email)\n" +
                            "발생원인: 가입되지 않은 유저입니다.");
        }
        return findMember.get();
    }

    /**
     * 업데이트 로직
     */
    @Transactional(readOnly = false)
    public void updateMemberPassword(Long memberId, String password) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "오류 발생\n" +
                            "발생위치: MemberService.updateMemberPassword(Long memberId, String password)\n" +
                            "발생원인: 가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.updatePassword(password);
    }

    /*@Transactional(readOnly = false)
    public void updateMemberAddress(Long memberId, String city, String street, String zipcode) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "오류 발생\n" +
                            "발생위치: MemberService.updateUserAddress(Long memberId, String city, String street, String zipcode)\n" +
                            "발생원인: 가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.updateAddress(city, street, zipcode);
    }*/

    @Transactional(readOnly = false)
    public void updateMemberPhoneNumber(Long memberId, String phoneNumber) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "오류 발생\n" +
                            "발생위치: MemberService.updateUserPhoneNumber(Long memberId, String phoneNumber)\n" +
                            "발생원인: 가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.updatePhoneNumber(phoneNumber);
    }

    @Transactional(readOnly = false)
    public void updateMemberEmail(Long memberId, String email) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "오류 발생\n" +
                            "발생위치: MemberService.updateUserPhoneNumber(Long memberId, String phoneNumber)\n" +
                            "발생원인: 가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.updateEmail(email);
    }

    @Transactional(readOnly = false)
    public void updateAddAvoidFood(Long memberId, Food avoidFood) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "오류 발생\n" +
                            "발생위치: MemberService.updateUserPhoneNumber(Long memberId, String phoneNumber)\n" +
                            "발생원인: 가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        AvoidFood addedAvoidFood = member.addAvoidFood(avoidFood);
        avoidFoodRepository.save(addedAvoidFood);
    }

    @Transactional(readOnly = false)
    public void updateAddMemberDisease(Long memberId, Disease disease) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "오류 발생\n" +
                            "발생위치: MemberService.updateUserPhoneNumber(Long memberId, String phoneNumber)\n" +
                            "발생원인: 가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        MemberDisease memberDisease = member.addDisease(disease);
        memberDiseaseRepository.save(memberDisease);
    }

    @Transactional(readOnly = false)
    public void updateAddMemberDrug(Long memberId, Drug drug) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            throw new AppException(MEMBER_ID_NOT_FOUND,
                    "오류 발생\n" +
                            "발생위치: MemberService.updateUserPhoneNumber(Long memberId, String phoneNumber)\n" +
                            "발생원인: 가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        MemberDrug memberDrug = member.addDrug(drug);
        memberDrugRepository.save(memberDrug);
    }

    public String login(String username, String password) {
        // 해당 id의 member 존재 안함.
        Member findMember = memberRepository.findByLogInUsername(username)
                .orElseThrow(() -> new AppException(MEMBER_ID_NOT_FOUND,
                        "오류 발생\n" +
                                "발생위치: MemberService.login(String username, String password)\n" +
                                "발생원인: 가입되지 않은 유저입니다."));

        if (!encoder.matches(password, findMember.getPersonalInformation().getLogIn().getPassword())) {
            throw new AppException(INVALID_PASSWORD,
                    "오류 발생\n" +
                            "발생위치: MemberService.login(String username, String password)\n" +
                            "발생원인: 잘못된 비밀번호입니다.");
        }

        // 앞에서 exception 발생 안 한 경우 token 발행
        String token = JwtUtil.createToken(username, secretKey, expireTimeMs);
        return token;
    }
}