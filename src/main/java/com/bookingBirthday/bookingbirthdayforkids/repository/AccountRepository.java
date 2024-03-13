package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsAccountByEmail(String email);
    boolean existsAccountByUsername(String userName);
    boolean existsAccountByEmailAndUsername(String email, String userName);
//    List<Account> findAllByIsActiveIsTrue();

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsername(String username);

    Optional<Account> findAccountById(Long id);
}
