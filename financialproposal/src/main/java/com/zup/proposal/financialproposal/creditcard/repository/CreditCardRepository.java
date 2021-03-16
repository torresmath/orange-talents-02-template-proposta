package com.zup.proposal.financialproposal.creditcard.repository;

import com.zup.proposal.financialproposal.creditcard.model.BlockStatus;
import com.zup.proposal.financialproposal.creditcard.model.CreditCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    Page<CreditCard> findByBlocksStatus(BlockStatus blockStatus, Pageable pageable);
    Page<CreditCard> findByTripsNotificationTimestampIsNullAndTripsEndDateGreaterThan(LocalDate today, Pageable pageable);
}
