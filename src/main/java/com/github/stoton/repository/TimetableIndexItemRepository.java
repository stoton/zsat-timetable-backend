package com.github.stoton.repository;

import com.github.stoton.domain.TimetableIndexItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableIndexItemRepository extends JpaRepository<TimetableIndexItem, Long> {

    TimetableIndexItem findFirstByName(String name);

    TimetableIndexItem findFirstByTeacherID(String teacherId);
}
