# cwv 主链Java-sdk

## 接口

### 配置与初始化

```  java
/**必须配置*/
/**主链地址*/
Config.host = "必须配置";

/**非必须配置*/
/**交易hash查询间隔，毫秒数*/
Config.HashTrackerIntervalTime = 300;
/**交易hash查询重试次数*/
Config.TxTrackRetryTimes = 10;
/**是否使用多队列交易执行器，默认不启用*/
Config.isMultiQueueExecUsed = false;
/**交易并行队列数目，isMultiQueueExecUsed=true时才有用*/
Config.MultiExecQueueNum = 20;
/**是否开启串行化交易发送，即单个账户并发发起交易，默认不启用*/
Config.isSerialSendTx = false;

/**初始化，地址可以不填*/
HiChain.init("address");

```

### 转账（包含主币，ERC20，ERC721）

```  java
TransferInfo ti = new TransferInfo();
ti.setToAddr("reciver Addr");
ti.setAmount("1000");
ti.setToken("AAA");
ti.setTokenAmount("2000");
ti.setSymbol("TTT");
List<String> tokens = new ArrayList<>();
tokens.add("a1");
tokens.add("a2");
ti.setCryptoToken(tokens);

List<TransferInfo> outs = new ArrayList();
outs.add(ti);
outs.add(ti);
String result = HiChain.transferTo("from","fromPk","exdata",outs);     
```

其中：

```  java
public class TransferInfo {
    private String toAddr;
    private String amount;//转账主币数量
    private String token;
    private String tokenAmount;//转账erc20token数量
    private String symbol;
    private List<String> cryptoToken = new ArrayList<>();//转账erc721

    public TransferInfo() {
    }
    
    public void addCryptoToken(String token){
        cryptoToken.add(token);
    }
}
```



### 生成交易签名

```  java
byte[] sign = HiChain.signTx("pk",txData);
```

api：

```  java
/**
* 签名交易
* @param privateKey 私钥
* @param txData  交易数据
*/
public static byte[] signTx(String privateKey,byte[] txData){
    return CryptoUtil.sign(privateKey,txData);
}
```



### 获取助记词

```  java 
//生成助记词
String words = WalletUtil.getMnemonic();
```



### 助记词生成钱包

```  java
//从助记词生成钱包
KeyPairs kp  =WalletUtil.getKeyPair(words);
System.out.println("钱包地址："+kp.getAddress());
System.out.println("钱包公钥："+kp.getPubkey());
System.out.println("钱包私钥："+kp.getPrikey());
```

### 生成keyStore文件，需要密码

```  java
//从助记词生成keyStore文件，需要密码
String keyStoreJson = WalletUtil.genKeyStoreFromMnemonic(words,"a123456");
System.out.println("keyStore文件json："+keyStoreJson);

//公私钥对生成keyStore文件
KeyStoreHelper ksh = new KeyStoreHelper(CryptoUtil.crypto);
KeyStoreFile keyStoreFile = ksh.generate(kp,password);
String keyStoreJson = ksh.parseToJsonStr(keyStoreFile);
```

### 从keyStore文件恢复钱包，需要密码

```   java
KeyPairs kp =  WalletUtil.restoreFromKeyStore(keyStoreJson,"a123456");
System.out.println("钱包地址："+kp.getAddress());
System.out.println("钱包公钥："+kp.getPubkey());
System.out.println("钱包私钥："+kp.getPrikey());
```

