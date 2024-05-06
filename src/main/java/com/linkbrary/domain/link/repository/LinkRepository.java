package com.linkbrary.domain.link.repository;


import com.linkbrary.domain.link.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    boolean existsByUrl(String url);
    Optional<Link> findByUrl(String url);
}
