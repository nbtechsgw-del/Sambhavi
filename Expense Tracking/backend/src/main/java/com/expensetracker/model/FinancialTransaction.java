package com.expensetracker.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class FinancialTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private AppUser user;

  @ManyToOne(optional = false)
  private Category category;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransactionType type;

  @Column(length = 500)
  private String description;

  @Column(nullable = false)
  private LocalDate transactionDate;

  @Column(nullable = false)
  private LocalDateTime createdDate = LocalDateTime.now();

  private boolean recurring;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public AppUser getUser() { return user; }
  public void setUser(AppUser user) { this.user = user; }
  public Category getCategory() { return category; }
  public void setCategory(Category category) { this.category = category; }
  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public TransactionType getType() { return type; }
  public void setType(TransactionType type) { this.type = type; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public LocalDate getTransactionDate() { return transactionDate; }
  public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
  public LocalDateTime getCreatedDate() { return createdDate; }
  public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
  public boolean isRecurring() { return recurring; }
  public void setRecurring(boolean recurring) { this.recurring = recurring; }
}
