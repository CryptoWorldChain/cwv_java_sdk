package org.cwv.client.sdk.model;

import org.cwv.client.sdk.model.contract.AbsContractCallResp;

public interface TxNotifier<T extends AbsContractCallResp> {
    
    void sendHash(String hash);

    void sendResult(T result);

    void error(T error);
    
}
