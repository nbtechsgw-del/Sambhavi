package com.expensetracker.repository;

import com.expensetracker.model.AppUser;
import com.expensetracker.model.FinancialTransaction;
import com.expensetracker.model.TransactionType;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<FinancialTransaction, Long> {
  List<FinancialTransaction> findTop8ByUserOrderByTransactionDateDescCreatedDateDesc(AppUser user);
  List<FinancialTransaction> findByUserOrderByTransactionDateDescCreatedDateDesc(AppUser user);

  @Query("""
      select t from FinancialTransaction t
      where t.user = :user
        and (:fromDate is null or t.transactionDate >= :fromDate)
        and (:toDate is null or t.transactionDate <= :toDate)
        and (:categoryId is null or t.category.id = :categoryId)
        and (:type is null or t.type = :type)
        and (:search is null or lower(t.description) like lower(concat('%', :search, '%')) or lower(t.category.name) like lower(concat('%', :search, '%')))
      order by t.transactionDate desc, t.createdDate desc
      """)
  List<FinancialTransaction> search(
      @Param("user") AppUser user,
      @Param("fromDate") LocalDate fromDate,
      @Param("toDate") LocalDate toDate,
      @Param("categoryId") Long categoryId,
      @Param("type") TransactionType type,
      @Param("search") String search);
}
