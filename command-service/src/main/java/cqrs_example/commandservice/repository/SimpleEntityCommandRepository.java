package cqrs_example.commandservice.repository;

import cqrs_example.commandservice.model.entity.SimpleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleEntityCommandRepository extends JpaRepository<SimpleEntity, Long> {
}
