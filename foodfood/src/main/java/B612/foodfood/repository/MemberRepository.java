package B612.foodfood.repository;

import B612.foodfood.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m " +
            "join fetch m.personalInformation p")
    List<Member> findAll();

    @Query("select m from Member m " +
            "join fetch m.personalInformation p " +
            "where m.name = :name")
    List<Member> findByName(@Param("name") String name);

    @Query("select m from Member m " +
            "join fetch m.personalInformation p " +
            "where m.personalInformation.logIn.username = :username")
    Optional<Member> findByLogInUsername(@Param("username") String username);

    @Query("select m from Member m " +
            "join fetch m.personalInformation p " +
            "where m.personalInformation.phoneNumber = :phoneNumber")
    Optional<Member> findByPhoneNumber(String phoneNumber);
}
