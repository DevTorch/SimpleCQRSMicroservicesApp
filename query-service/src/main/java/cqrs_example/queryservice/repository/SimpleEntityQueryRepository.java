package cqrs_example.queryservice.repository;

import cqrs_example.queryservice.model.entity.SimpleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimpleEntityQueryRepository extends JpaRepository<SimpleEntity, Long> {
}
