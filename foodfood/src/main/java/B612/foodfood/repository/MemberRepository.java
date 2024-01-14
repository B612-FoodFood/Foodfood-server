package B612.foodfood.repository;

import B612.foodfood.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository{
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m " +
                        "join fetch m.personalInformation p ", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m " +
                        "join fetch m.personalInformation p " +
                        "where m.name =: name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    public Optional<Member> findByLogInId(String login_id) {
        return em.createQuery("select m from Member m " +
                        "join fetch m.personalInformation p " +
                        "where m.personalInformation.logIn.login_id = :id", Member.class)
                .setParameter("id", login_id)
                .getResultStream()
                .findAny();
    }

    public Optional<Member> findByPhoneNumber(String phoneNumber) {
        return em.createQuery("select m from Member m " +
                "join fetch m.personalInformation p " +
                "where m.personalInformation.phoneNumber = :phoneNumber", Member.class)
                .setParameter("phoneNumber",phoneNumber)
                .getResultStream()
                .findAny();
    }

    public Optional<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m " +
                "join fetch m.personalInformation p " +
                "where m.personalInformation.email = :email", Member.class)
                .setParameter("email",email)
                .getResultStream()
                .findAny();
    }

}
