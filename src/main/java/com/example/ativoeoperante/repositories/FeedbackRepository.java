package com.example.ativoeoperante.repositories;

import com.example.ativoeoperante.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
}
