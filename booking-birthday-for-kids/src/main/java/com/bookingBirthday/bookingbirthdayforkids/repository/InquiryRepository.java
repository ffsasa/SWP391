package com.bookingBirthday.bookingbirthdayforkids.repository;

import com.bookingBirthday.bookingbirthdayforkids.model.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findAllByIsActiveIsTrue();
}
