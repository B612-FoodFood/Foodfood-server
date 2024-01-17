package B612.foodfood.repository;

import B612.foodfood.domain.Meal;
import B612.foodfood.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MealRepository {

    private final EntityManager em;

    public void save(Meal meal) {
        em.persist(meal);
    }

    public Optional<Meal> findByDate(LocalDate date) {
        return em.createQuery("select m from Meal m " +
                        "join fetch m.member mm " +
                        "where m.date = :date", Meal.class)
                .setParameter("date", date)
                .getResultList()
                .stream()
                .findAny();
    }

    public Optional<Meal> findById(Long mealId) {
        return Optional.ofNullable(em.find(Meal.class, mealId));
    }

    public List<Meal> findAll() {
        return em.createQuery("select m from Meal m " +
                        "join fetch m.member mm", Meal.class)
                .getResultList();
    }

    /*
    public List<Meal> findByMember(Member member) {
        return em.createQuery("select m from Meal m " +
                        "join fetch m.member mm " +
                        "where mm.id = :memberId", Meal.class)
                .setParameter("memberId", member.getId())
                .getResultList();
    }
    */

}