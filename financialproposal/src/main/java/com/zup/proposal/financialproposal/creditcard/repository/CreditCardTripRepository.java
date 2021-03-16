package com.zup.proposal.financialproposal.creditcard.repository;

import com.zup.proposal.financialproposal.creditcard.model.CreditCardTrip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CreditCardTripRepository extends JpaRepository<CreditCardTrip, Long> {

    Page<CreditCardTrip> findByNotificationTimestampIsNullAndEndDateGreaterThan(LocalDate today, Pageable pageable);
}
