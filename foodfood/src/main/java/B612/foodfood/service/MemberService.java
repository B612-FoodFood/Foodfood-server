package B612.foodfood.service;

import B612.foodfood.domain.Meal;
import B612.foodfood.domain.Member;
import B612.foodfood.exception.DataSaveException;
import B612.foodfood.exception.NoDataExistException;
import B612.foodfood.repository.MealRepository;
import B612.foodfood.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MealRepository mealRepository;

    @Transactional(readOnly = false)
    public Long join(Member member) throws DataSaveException {

        // 이미 가입된 멤버(동일한 아이디가 존재하는 경우) 회원 가입 불가
        Optional<Member> memberFindByLogInId = memberRepository.findByLogInId(member.getPersonalInformation().getLogIn().getLogin_id());
        if (memberFindByLogInId.isPresent()) {
            throw new DataSaveException("오류 발생\n" +
                    "발생위치: MemberService.join(Member member)\n" +
                    "발생원인: 이미 가입된 아이디입니다.");
        }

        /**
         * 구현중...
         * 아이디 비번을 해싱 처리해서 db에 저장 해야한다. hs256 알고리즘 사용하기
         */

        memberRepository.save(member);
        return member.getId();
    }

    public Member findMemberById(Long memberId) throws NoDataExistException {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new NoDataExistException("오류 발생\n" +
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

    public Member findMemberByLogInId(String login_id) throws NoDataExistException {
        Optional<Member> findMember = memberRepository.findByLogInId(login_id);
        if (findMember.isEmpty()) {
            throw new NoDataExistException("오류 발생\n" +
                    "발생위치: MemberService.findMemberByLogInId(String login_id)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
        }
        return findMember.get();
    }

    public Member findMemberByPhoneNumber(String phoneNumber) throws NoDataExistException {
        Optional<Member> findMember = memberRepository.findByPhoneNumber(phoneNumber);
        if (findMember.isEmpty()) {
            throw new NoDataExistException("오류 발생\n" +
                    "발생위치: MemberService.findMemberByPhoneNumber(String phoneNumber)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
        }
        return findMember.get();
    }

    public Member findMemberByEmail(String email) throws NoDataExistException {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isEmpty()) {
            throw new NoDataExistException("오류 발생\n" +
                    "발생위치: MemberService.findMemberByEmail(String email)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
        }
        return findMember.get();
    }

    /**
     * 업데이트 로직
     */
    @Transactional(readOnly = false)
    public void updateMemberPassword(Long memberId, String password) throws NoDataExistException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            throw new NoDataExistException("오류 발생\n" +
                    "발생위치: MemberService.updateMemberPassword(Long memberId, String password)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.updatePassword(password);
    }

    @Transactional(readOnly = false)
    public void updateMemberAddress(Long memberId, String city, String street, String zipcode) throws NoDataExistException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            throw new NoDataExistException("오류 발생\n" +
                    "발생위치: MemberService.updateUserAddress(Long memberId, String city, String street, String zipcode)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.updateAddress(city, street, zipcode);
    }

    @Transactional(readOnly = false)
    public void updateMemberPhoneNumber(Long memberId, String phoneNumber) throws NoDataExistException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            throw new NoDataExistException("오류 발생\n" +
                    "발생위치: MemberService.updateUserPhoneNumber(Long memberId, String phoneNumber)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.updatePhoneNumber(phoneNumber);
    }

    @Transactional(readOnly = false)
    public void updateMemberEmail(Long memberId, String email) throws NoDataExistException {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (findMember.isEmpty()) {
            throw new NoDataExistException("오류 발생\n" +
                    "발생위치: MemberService.updateUserPhoneNumber(Long memberId, String phoneNumber)\n" +
                    "발생원인: 가입되지 않은 유저입니다.");
        }

        Member member = findMember.get();
        member.updateEmail(email);
    }
}