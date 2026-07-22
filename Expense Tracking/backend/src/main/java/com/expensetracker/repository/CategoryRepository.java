package com.expensetracker.repository;

import com.expensetracker.model.AppUser;
import com.expensetracker.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findByUserOrderByTypeAscNameAsc(AppUser user);
  Optional<Category> findByIdAndUser(Long id, AppUser user);
}
