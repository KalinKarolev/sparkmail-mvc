package app.repository;

import java.util.List;
import java.util.UUID;

import app.model.Email;
import app.model.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {
    List<Email> findAllByStatus(EmailStatus status);
}
