package codesquad.domain.qna;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByWriter(String writer);
    Page<Question> findAllByDeleted(boolean bool, Pageable pageable);
}
