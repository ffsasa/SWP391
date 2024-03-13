package com.bookingBirthday.bookingbirthdayforkids.repository;


import com.bookingBirthday.bookingbirthdayforkids.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


}
