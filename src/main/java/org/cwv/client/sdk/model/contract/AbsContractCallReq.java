package org.cwv.client.sdk.model.contract;

import org.cwv.client.sdk.model.ContractCallRequest;
import lombok.Data;

@Data
public abstract class AbsContractCallReq<T extends AbsContractCallResp> implements ContractCallRequest {

    private String contractName;//合约名称
    private String method;//合约方法名称

    public AbsContractCallReq(String name,String method){
        this.contractName = name;
        this.method = method;
    }

    public AbsContractCallReq(){}

    public String getContractC_M(){
        return contractName+"_"+method;
    }


    @Override
    public T buildResp() {
        T resp = buildAccResp();
        resp.setReq(this);
        return resp;
    }
    
    protected abstract T buildAccResp();

    @Override
    public String getExdata(){
        return null;
    }
}
