package com.zup.proposal.financialproposal.wallet.controller.response;

import com.zup.proposal.financialproposal.wallet.model.Provider;
import com.zup.proposal.financialproposal.wallet.model.Wallet;
import com.zup.proposal.financialproposal.wallet.model.WalletStatus;

public class WalletResponse {
    private final Long id;

    private final Provider provider;

    private final WalletStatus status;

    public WalletResponse(Wallet wallet) {
        this.id = wallet.getId();
        this.provider = wallet.getProvider();
        this.status = wallet.getStatus();
    }

    public Long getId() { return id; }

    public Provider getProvider() { return provider; }

    public WalletStatus getStatus() { return status; }
}
