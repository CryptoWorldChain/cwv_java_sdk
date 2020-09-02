package rft.fuc;

import org.cwv.client.sdk.contract.abi.Function;
import org.cwv.client.sdk.contract.abi.Tuple;
import org.cwv.client.sdk.contract.util.FastHex;
import org.cwv.client.sdk.model.contract.AbsContractCallResp;
import org.cwv.client.sdk.util.AccountUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

@Data
@Slf4j
public class GetFeeResp extends AbsContractCallResp {
    private Function fuc;
    {
        try {
            fuc = new Function("getFeeInfo(string)",
                        "(uint256,uint256,uint256,uint256,uint256,uint256,uint256,uint256,uint256,uint256,uint256,uint256)");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String _sellMin;
    private String _sellMax;
    private String _sellFee;
    private String _buyMin;
    private String _buyMax;
    private String _buyFee;

    private String _subSellMin;
    private String _subSellMax;
    private String _subSellFee;
    private String _subBuyMin;
    private String _subBuyMax;
    private String _subBuyFee;

    public GetFeeResp() {
        
    }


    @Override
    public GetFeeResp decode(String txhash, String result) {
        Tuple tp = fuc.decodeReturn(FastHex.hexStringToBytes(result));
        _sellMin = AccountUtil.div18(tp.get(0).toString());
        _sellMax = AccountUtil.div18(tp.get(1).toString());
        _sellFee = tp.get(2).toString();
        _buyMin = AccountUtil.div18(tp.get(3).toString());
        _buyMax = AccountUtil.div18(tp.get(4).toString());
        _buyFee = tp.get(5).toString();

        _subSellMin = AccountUtil.div18(tp.get(6).toString());
        _subSellMax = AccountUtil.div18(tp.get(7).toString());
        _subSellFee = tp.get(8).toString();
        _subBuyMin = AccountUtil.div18(tp.get(9).toString());
        _subBuyMax = AccountUtil.div18(tp.get(10).toString());
        _subBuyFee = tp.get(11).toString();
        
        this.setCode(1);
        this.setMsg("成功");
        this.setTxHash(txhash);
        
        return this;
    }
}
