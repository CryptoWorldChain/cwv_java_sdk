package org.cwv.client.sdk;

import org.cwv.client.sdk.https.PureOkHttpExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/**区块链浏览器*/
public final class Browser {

    //查询父级邀请码
    private static final String uri_pbspc = "/web/app/pbspc.do";
    //查询我的费率
    private static final String uri_pbsof = "/web/app/pbsof.do";
    //所有被邀请人信息查询
    private static final String uri_pbsii = "/web/app/pbsii.do";
    //所有历史订单查询
    private static final String uri_pbsah = "/web/app/pbsah.do";
    //根据订单号查询订单信息
    private static final String uri_pbsoi = "/web/app/pbsoi.do";
    //所有申诉信息
    private static final String uri_pbsal = "/web/app/pbsal.do";
    //获取分润信息
    private static final String uri_pbsrm = "/web/app/pbsrm.do";
    //获取提现手续费
    private static final String uri_pbsfm = "/web/app/pbsfm.do";
    //获取充值，提现限额
    private static final String uri_pbspm = "/web/app/pbspm.do";
    //获取经销商审核状态
    private static final String uri_pbpas = "/web/app/pbpas.do";
    //经销商商户信息查询(停业状态查询 是否被停业)
    private static final String uri_pbsps = "/web/app/pbsps.do";
    //收款方式查询
    private static final String uri_pbspi = "/web/app/pbspi.do";
    //未完成订单查询
    private static final String uri_pbsno = "/web/app/pbsno.do";

    //返佣金额及总的交易量
    private static final String uri_pbsrt = "/web/app/pbsrt.do";
    //下级返佣金统计
    private static final String uri_pbsir = "/web/app/pbsir.do";

    //查询不同动态支付最高的金额
    private static final String uri_pbsmi = "/web/app/pbsmi.do";
    //查询对应地址订单数
    private static final String uri_pbsnt = "/web/app/pbsnt.do";
    //查询支付等级范围
    private static final String uri_pbsag = "/web/app/pbsag.do";

    private static String doGet(String url,String body){
        try {
            log.debug("body----"+body);
            return PureOkHttpExecutor.post(url,body);
        } catch (Exception e) {
            log.error("请求异常",e);
            return "{\"err_code\": \"-1\",\"msg\": \"请求异常\"}";
        }
    }
    
}
