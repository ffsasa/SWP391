package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import com.bookingBirthday.bookingbirthdayforkids.model.RoleEnum;
import com.bookingBirthday.bookingbirthdayforkids.model.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsAccountByEmail(String email);
    boolean existsAccountByUsername(String userName);
    boolean existsAccountByEmailAndUsername(String email, String userName);
//    List<Account> findAllByIsActiveIsTrue();

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsername(String username);

    List<Account> findAllByRoleId(Long id);
}
