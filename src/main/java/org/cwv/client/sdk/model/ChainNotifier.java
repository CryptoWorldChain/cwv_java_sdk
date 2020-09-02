package org.cwv.client.sdk.model;

public interface ChainNotifier<T extends ContractCallResponse> {
    

    void sendHash(String hash);

    void sendResult(T result);

    void error(ChainResult cr);
}
