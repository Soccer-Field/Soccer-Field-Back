package com.community.back.domain.field.domain.repository;

import com.community.back.domain.field.domain.Field;
import com.community.back.domain.field.domain.FieldStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    List<Field> findByStatus(FieldStatus status);

    List<Field> findByNameContainingOrAddressContaining(String name, String address);
}
