package com.zup.proposal.financialproposal.client.analysis.response;

import com.zup.proposal.financialproposal.proposal.model.ProposalStatus;

public enum AnalysisResult {

    SEM_RESTRICAO(ProposalStatus.ELEGIVEL),
    COM_RESTICAO(ProposalStatus.NAO_ELEGIVEL);

    private final ProposalStatus proposalStatus;

    AnalysisResult(ProposalStatus proposalStatus) { this.proposalStatus = proposalStatus; }

    public ProposalStatus getProposalStatus() { return proposalStatus; }
}
