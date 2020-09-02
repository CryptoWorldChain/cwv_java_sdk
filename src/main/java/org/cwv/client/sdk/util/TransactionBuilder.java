package org.cwv.client.sdk.util;

import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.cwv.client.sdk.model.ICrypto;
import org.cwv.client.sdk.model.Model.SendTransaction;
import org.cwv.client.sdk.model.Model.SendTransactionOutput;
import org.cwv.client.sdk.model.Transaction.TransactionBody;
import org.cwv.client.sdk.model.Transaction.TransactionData;
import org.cwv.client.sdk.model.Transaction.TransactionData.*;
import org.cwv.client.sdk.model.Transaction.TransactionData.OwnerTokenData.OwnerTokenOpCode;
import org.cwv.client.sdk.model.Transaction.TransactionData.UserTokenData.UserTokenOpCode;
import org.cwv.client.sdk.model.Transaction.TransactionInfo;
import org.cwv.client.sdk.model.Transaction.TransactionOutput;

import java.math.BigInteger;

@Slf4j
public class TransactionBuilder {


    public static String build(SendTransaction pb, ICrypto iCrypto){
        TransactionInfo.Builder ti = TransactionInfo.newBuilder();
        TransactionBody.Builder tb = TransactionBody.newBuilder();
        tb.setAddress(ByteString.copyFrom(CryptoUtil.hexStrToBytes(pb.getAddress())));

        //<editor-fold desc="非转账交易">
        if (pb.getData() != null) {
            TransactionData.Builder td = TransactionData.newBuilder();
            if (pb.getData().getCallContractData() != null
                    && isNoneBlank(pb.getData().getCallContractData().getContract())) {
                CallContractData.Builder oCallContractData = CallContractData.newBuilder();
                if(pb.getData().getCallContractData().getAmount()!=null && !pb.getData().getCallContractData().getAmount().equals(""))
                    oCallContractData.setAmount(ByteString.copyFrom(
                        bigIntegerToBytes(new BigInteger(pb.getData().getCallContractData().getAmount()))));
                oCallContractData.setContract(ByteString
                        .copyFrom(CryptoUtil.hexStrToBytes(pb.getData().getCallContractData().getContract())));
                oCallContractData.setData(
                        ByteString.copyFrom(CryptoUtil.hexStrToBytes(pb.getData().getCallContractData().getData())));
                td.setCallContractData(oCallContractData);
                td.setType(DataType.CALLCONTRACT);
                tb.setData(td);
            } else if (pb.getData().getCryptoTokenData() != null
                    && isNotBlank(pb.getData().getCryptoTokenData().getSymbol())) {
                PublicCryptoTokenData.Builder oPublicCryptoTokenData = PublicCryptoTokenData.newBuilder();
                for (String code : pb.getData().getCryptoTokenData().getCodeList()) {
                    oPublicCryptoTokenData.addCode(code);
                }
                for (String name : pb.getData().getCryptoTokenData().getNameList()) {
                    oPublicCryptoTokenData.addName(name);
                }
                for (String prop : pb.getData().getCryptoTokenData().getPropList()) {
                    oPublicCryptoTokenData.addProp(prop);
                }
                oPublicCryptoTokenData.setTotal(pb.getData().getCryptoTokenData().getTotal());
                oPublicCryptoTokenData
                        .setSymbol(ByteString.copyFrom(pb.getData().getCryptoTokenData().getSymbol().getBytes()));
                td.setCryptoTokenData(oPublicCryptoTokenData);
                td.setType(DataType.PUBLICCRYPTOTOKEN);
                tb.setData(td);
            } else if (pb.getData().getOwnerTokenData() != null
                    && isNotBlank(pb.getData().getOwnerTokenData().getToken())) {
                OwnerTokenData.Builder oOwnerTokenData = OwnerTokenData.newBuilder();
                oOwnerTokenData.setAmount(ByteString.copyFrom(
                        bigIntegerToBytes(new BigInteger(pb.getData().getOwnerTokenData().getAmount()))));
                oOwnerTokenData
                        .setOpCode(OwnerTokenOpCode.forNumber(pb.getData().getOwnerTokenData().getOpCodeValue()));
                oOwnerTokenData
                        .setToken(ByteString.copyFrom(pb.getData().getOwnerTokenData().getToken().getBytes()));
                td.setOwnerTokenData(oOwnerTokenData);
                td.setType(DataType.OWNERTOKEN);
                tb.setData(td);
            } else if (pb.getData().getPublicContractData() != null
                    && isNotBlank(pb.getData().getPublicContractData().getData())) {
                PublicContractData.Builder oPublicContractData = PublicContractData.newBuilder();
                oPublicContractData.setCode(
                        ByteString.copyFrom(CryptoUtil.hexStrToBytes(pb.getData().getPublicContractData().getCode())));
                oPublicContractData.setData(
                        ByteString.copyFrom(CryptoUtil.hexStrToBytes(pb.getData().getPublicContractData().getData())));
                td.setPublicContractData(oPublicContractData);
                td.setType(DataType.PUBLICCONTRACT);
                tb.setData(td);
            } else if (pb.getData().getUnionAccountData() != null
                    && pb.getData().getUnionAccountData().getAddressCount() > 0) {
                UnionAccountData.Builder oUnionAccountData = UnionAccountData.newBuilder();
                oUnionAccountData.setAcceptLimit(pb.getData().getUnionAccountData().getAcceptLimit());
                oUnionAccountData.setAcceptMax(ByteString.copyFrom(
                        bigIntegerToBytes(new BigInteger(pb.getData().getUnionAccountData().getAcceptMax()))));
                oUnionAccountData.setMax(ByteString.copyFrom(
                        bigIntegerToBytes(new BigInteger(pb.getData().getUnionAccountData().getMax()))));
                for (String addr : pb.getData().getUnionAccountData().getAddressList()) {
                    oUnionAccountData.addAddress(ByteString.copyFrom(CryptoUtil.hexStrToBytes(addr)));
                }
                td.setUnionAccountData(oUnionAccountData);
                td.setType(DataType.PUBLICUNIONACCOUNT);
                tb.setData(td);
            } else if (pb.getData().getUserTokenData() != null
                    && isNotBlank(pb.getData().getUserTokenData().getToken())) {
                UserTokenData.Builder oUserTokenData = UserTokenData.newBuilder();
                oUserTokenData.setAddress(
                        ByteString.copyFrom(CryptoUtil.hexStrToBytes(pb.getData().getUserTokenData().getAddress())));
                oUserTokenData.setAmount(ByteString.copyFrom(
                        bigIntegerToBytes(new BigInteger(pb.getData().getUserTokenData().getAmount()))));
                oUserTokenData
                        .setOpCode(UserTokenOpCode.forNumber(pb.getData().getUserTokenData().getOpCodeValue()));
                oUserTokenData.setToken(ByteString.copyFrom(pb.getData().getUserTokenData().getToken().getBytes()));
                td.setUserTokenData(oUserTokenData);
                td.setType(DataType.USERTOKEN);
                tb.setData(td);
            } else if (pb.getData().getUnionAccountTransferData() != null
                    && isNotBlank(pb.getData().getUnionAccountTransferData().getFromSubAccount())) {
                UnionAccountTransferData.Builder oUnionAccountTransferData = UnionAccountTransferData.newBuilder();
                oUnionAccountTransferData.setFromSubAccount(ByteString.copyFrom(
                        CryptoUtil.hexStrToBytes(pb.getData().getUnionAccountTransferData().getFromSubAccount())));

                td.setUnionAccountTransferData(oUnionAccountTransferData);
                td.setType(DataType.UNIONACCOUNTTRANSFER);
                tb.setData(td);
            } else if (pb.getData().getUnionAccountConfirmData() != null
                    && isNoneBlank(pb.getData().getUnionAccountConfirmData().getHash())) {
                UnionAccountConfirmData.Builder oUnionAccountConfirmData = UnionAccountConfirmData.newBuilder();
                oUnionAccountConfirmData.setFromSubAccount(ByteString.copyFrom(
                        CryptoUtil.hexStrToBytes(pb.getData().getUnionAccountConfirmData().getFromSubAccount())));
                oUnionAccountConfirmData.setHash(ByteString
                        .copyFrom(CryptoUtil.hexStrToBytes(pb.getData().getUnionAccountConfirmData().getHash())));

                td.setUnionAccountConfirmData(oUnionAccountConfirmData);
                td.setType(DataType.UNIONACCOUNTCONFIRM);
                tb.setData(td);
            }
        }
        //</editor-fold>


        if (isNotBlank(pb.getExdata())) {
            byte[] datas = new byte[]{}; 
            String exdata = pb.getExdata();
            try {
                datas = exdata.getBytes("utf-8");
            } catch (Exception e) {
                log.error("exdata 编码错误",e);
            }

            tb.setExdata(ByteString.copyFrom(datas));
//            tb.setExdata(ByteString.copyFrom(CryptoUtil.hexStrToBytes(pb.getExdata())));
        }
        // tb.setFee(ByteString.copyFrom(ByteUtil.bigIntegerToBytes(new
        // BigInteger(pb.getFee()))));

//        if (nonce == 0) {
//            tb.setNonce(accountHelper.getNonce(CryptoUtil.hexStrToBytes(pb.getAddress())));
//        } else {
            tb.setNonce(pb.getNonce());
//        }

        if (pb.getTimestamp() == 0) {
            tb.setTimestamp(System.currentTimeMillis());
        } else {
            tb.setTimestamp(pb.getTimestamp());
        }

        for (SendTransactionOutput output : pb.getOutputsList()) {
            TransactionOutput.Builder oTransactionOutput = TransactionOutput.newBuilder();
            oTransactionOutput.setAddress(ByteString.copyFrom(CryptoUtil.hexStrToBytes(output.getAddress())));
            if(output.getAmount()!=null && !output.getAmount().equals(""))
                oTransactionOutput.setAmount(ByteString.copyFrom(bigIntegerToBytes(new BigInteger(output.getAmount()))));
            if (output.getCryptoTokenCount() > 0) {
                for (String cryptoToken : output.getCryptoTokenList()) {
                    oTransactionOutput.addCryptoToken(ByteString.copyFrom(CryptoUtil.hexStrToBytes(cryptoToken)));
                }
            }
            if (isNotBlank(output.getSymbol())) {
                oTransactionOutput.setSymbol(ByteString.copyFrom(output.getSymbol().getBytes()));
            }
            if (isNotBlank(output.getToken())) {
                oTransactionOutput.setToken(ByteString.copyFrom(output.getToken().getBytes()));
                oTransactionOutput.setTokenAmount(ByteString
                        .copyFrom(bigIntegerToBytes(new BigInteger(output.getTokenAmount()))));
            }
            tb.addOutputs(oTransactionOutput);
        }

        ti.setBody(tb);
        log.debug("任务签名执行中...");
        ti.setSignature(ByteString
                .copyFrom(
                        iCrypto.sign(pb.getPrivateKey(),tb.build().toByteArray())
//                        CryptoUtil.sign(pb.getPrivateKey(), tb.build().toByteArray())
                ));
        log.debug("任务签名执行完毕");

        return CryptoUtil.bytesToHexStr(ti.build().toByteArray());

    }


    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static byte[] bigIntegerToBytes(BigInteger value) {
        if (value == null) {
            return null;
        } else {
            byte[] data = value.toByteArray();
            if (data.length != 1 && data[0] == 0) {
                byte[] tmp = new byte[data.length - 1];
                System.arraycopy(data, 1, tmp, 0, tmp.length);
                data = tmp;
            }

            return data;
        }
    }

    public static boolean isAnyBlank(final CharSequence... css) {
        if (css == null || css.length == 0) {
            return false;
        }
        for (final CharSequence cs : css){
            if (isBlank(cs)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isNoneBlank(final CharSequence... css) {
        return !isAnyBlank(css);
    }
    
}
