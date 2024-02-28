package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface PackageRepository extends JpaRepository<Package, Long> {
        boolean existsByPackageName (String packageName);

}
