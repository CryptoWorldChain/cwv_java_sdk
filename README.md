# jsdk - Java CWV Ðapp Api
This is the Java API which allows you to work with the CWV blockchain, without the additional overhead of having to write your own integration code for the platform.
## Getting started
Typically your application should depend on release versions of jsdk, Add the relevant dependency to your project:

#### Maven
```  java
<dependency>
  <groupId>io.github.cryptoworldchain</groupId>
  <artifactId>cwvj</artifactId>
  <version>1.0.1</version>
</dependency>
```
#### Gradle
```  java
compile ('io.github.cryptoworldchain:cwvj:1.0.1')
```
## Config
```  java
//the host of cwv blockchain node
Config.host = "http://localhost:8000";

/**Not required*/
/**Transaction hash query interval(milliseconds)*/
Config.HashTrackerIntervalTime = 300;
/**Transaction hash query retry times*/
Config.TxTrackRetryTimes = 10;
```

### Function category
#### WalletUtils
* [getMnemonic](#getMnemonic)
* [getKeyPair](#getKeyPair)
* [genKeyStoreFromMnemonic](#genKeyStoreFromMnemonic)
* [restoreFromKeyStore](#restoreFromKeyStore)

#### HiChain
* [getUserInfo](#getUserInfo)
* [transferTo](#transferTo)
* [getTxContent](#getTxContent)
* [tokenCreate](#tokenCreate)
* [contractCreate](#contractCreate)
* [contractCall](#contractCall)

#### ContractUtils
* [getFunctionBinCode](#getFunctionBinCode)
* [getFunctionParamsCode](#getFunctionParamsCode)
* [getTokenEncodeAddr](#getTokenEncodeAddr)

#### * ContractUtils for CWVSwap
* [getContractBinCodeMSwap](#getContractBinCodeMSwap)
* [getFunctionWithMSwap](#getFunctionWithMSwap)
* [getFunctionBinCodeWithMSwap](#getFunctionBinCodeWithMSwap)

##### getMnemonic
To generate Mnemonic Words which is to generate keyPair with function [getKeyPair](#getKeyPair) :
```  java
String words= WalletUtil.getMnemonic();
```
##### getKeyPair
To generate KeyPair which consists of address,privateKey,publicKey :
```  java
KeyPairs kp = WalletUtil.getKeyPair(words);
```
##### genKeyStoreFromMnemonic
To generate KeyStore File Content ,with Mnemonic Words, which should be encrypted and written into the file :
```  java
String ksJson = WalletUtil.genKeyStoreFromMnemonic(words,"password");
```
##### restoreFromKeyStore
To restore Mnemonic Words from KeyStore content with correct password:
```  java
KeyPairs kp2 = WalletUtil.restoreFromKeyStore(ksJson,"password");
        
```
##### getUserInfo
To get account info from an address:
```  java
String userInfo = HiChain.getUserInfo(address);
               
```
##### transferTo
To send cwv or token to an address,from this :
```  java
String resultTransfer = HiChain.transferTo(address,prikey,"测试转账",
                new ArrayList<TransferInfo>(){{
                    TransferInfo transferTo = new TransferInfo();
                    transferTo.setToAddr("fda8e7b96c92a9fd75ae77b87bf7fdb4cbaea0d5");
                    transferTo.setAmount("100"+"000000000000000000");
                    transferTo.setToken("AAA");
                    transferTo.setTokenAmount("10"+"000000000000000000");
                    this.add(transferTo);
                }},null
        );         
```

##### getTxContent
To get transaction content from transaction result:
```  java
TxContent txContentTokenCreate = HiChain.getTxContent(resultTransfer);
               
```

##### tokenCreate
To create token  :
```  java
String resultMETH2 = HiChain.tokenCreate(address,prikey,"METH2",100000000+"000000000000000000","create token test",null);
```
##### contractCreate
To create a contract:
```  java
String binCode = "6080604052600060015534801561001557600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610191806100656000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680633fa4f2451461005c57806364a4635c14610087578063a74c2bb6146100c8575b600080fd5b34801561006857600080fd5b5061007161011f565b6040518082815260200191505060405180910390f35b34801561009357600080fd5b506100b260048036038101908080359060200190929190505050610125565b6040518082815260200191505060405180910390f35b3480156100d457600080fd5b506100dd61013c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60015481565b600081600154016001819055506001549050919050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050905600a165627a7a7230582079ff7660c8107f8062425b54b0499303df4e97aa6b0c7729adee57d1eff986470029";
String resultCreate = HiChain.contractCreate(address,prikey,binCode,"test create contract");      
               
```
##### contractCall
To execute a function to a contract address:
```  java
int fromIndex = resultCreate.indexOf("contractHash\": \"")+"contractHash\": \"".length();
String contractAddr = resultCreate.substring(fromIndex,resultCreate.indexOf("\"",fromIndex));
String abi = "[{\"constant\": true,\"inputs\": [],\"name\": \"value\",\"outputs\": [{\"name\": \"\",\"type\": \"uint256\"}],\"payable\": false,\"stateMutability\": \"view\",\"type\": \"function\"},{\"constant\": false,\"inputs\": [{\"name\": \"inc\",\"type\": \"uint256\"}],\"name\": \"valueInc\",\"outputs\": [{\"name\": \"\",\"type\": \"uint256\"}],\"payable\": false,\"stateMutability\": \"nonpayable\",\"type\": \"function\"},{\"constant\": true,\"inputs\": [],\"name\": \"getAddr\",\"outputs\": [{\"name\": \"\",\"type\": \"address\"}],\"payable\": false,\"stateMutability\": \"view\",\"type\": \"function\"},{\"inputs\": [],\"payable\": false,\"stateMutability\": \"nonpayable\",\"type\": \"constructor\"}]";
String functionBinCode = ContractUtil.getFunctionBinCode(abi,"valueInc",BigInteger.valueOf(1));
String resultValueInc = HiChain.contractCall(address, prikey, contractAddr, binCode, "test function valueInc", null);
               
```    

##### getFunctionBinCode
Before calling the interface [contractCall](#contractCall) with a specific function, you should encode this function with or without its params by current method:
```  java
String functionBinCode = ContractUtil.getFunctionBinCode(abi,"valueInc",BigInteger.valueOf(1));
```
##### getFunctionParamsCode
To get encoded params of function, e.g. the params for constructor needs to be encoded without function name:
```  java
String addrMETH2 = ContractUtil.getTokenEncodeAddr("METH2");
String addrMOF2 = ContractUtil.getTokenEncodeAddr("MOF2");
Function f = ContractUtil.getFunctionWithMSwap(MSwapUtil.FunctionEnum.construtor);
String contructorParams = ContractUtil.getFunctionParamsCode(f,new BigInteger(addrMETH2,16),new BigInteger(addrMOF2, 16),new BigInteger[]{new BigInteger("fda8e7b96c92a9fd75ae77b87bf7fdb4cbaea0d5",16)});
```
##### getTokenEncodeAddr
Generally speaking, transfer tokens need to pass in the token name, but if you want to transfer token in the contract, you should encode the token name, the result is same to an token address :
```  java
String addrMOF2 = ContractUtil.getTokenEncodeAddr("MOF2");
```
##### getContractBinCodeMSwap
To get the Swap Contract bin code :
```  java
String binCode = ContractUtil.getContractBinCodeMSwap();
```
##### getFunctionWithMSwap
To get the function of Swap Contract :
```  java
Function f = ContractUtil.getFunctionWithMSwap(MSwapUtil.FunctionEnum.construtor);
```
##### getFunctionBinCodeWithMSwap
It calls the method [getFunctionBinCode](#getFunctionBinCode) for Swap Contract
```  java
String binCode = ContractUtil.getFunctionBinCodeWithMSwap(MSwapUtil.FunctionEnum.initPool, new BigInteger(10000+"000000000000000000"),new BigInteger(20000+"000000000000000000"));            
```

