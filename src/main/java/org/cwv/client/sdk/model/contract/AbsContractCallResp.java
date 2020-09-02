package org.cwv.client.sdk.model.contract;

import org.cwv.client.sdk.model.ContractCallResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class AbsContractCallResp implements ContractCallResponse {
    
    
    private int code;//状态码。1成功；其他失败
    private String msg;//错误信息
    private String txHash;//合约调用的交易hash
    private String result;//主链原始返值,json
    private long acceptTimestamp;//交易接受时间
    private long blockTimestamp;//交易落块时间
    
    private transient AbsContractCallReq req;

    @Override
    public ContractCallResponse decode(String code) {
        this.setCode(-1);
        this.setMsg("交易结果解析失败");
        this.setTxHash(null);
        this.setResult(code);
        try {
            decode(null,code);
        }catch (Exception e){
            log.error("交易结果解析失败",e);
        }
        return this;
    }
    
    public abstract ContractCallResponse decode(String txhash,String result) throws Exception;
}
