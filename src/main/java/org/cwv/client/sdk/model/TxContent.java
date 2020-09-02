package org.cwv.client.sdk.model;

public class TxContent {

    /**
     * retCode : 1
     * transaction : {"hash":"be613cd58c1e86efbd3f348ab75b375827cfec00e9455eb3b16218af4f1caebb","body":{"nonce":1,"address":"dfd55cae94f7953d4b8890ecff081242fe368937","data":{"type":"CALLCONTRACT","callContractData":{"data":"70a08231000000000000000000000000dfd55cae94f7953d4b8890ecff081242fe368937","contract":"f9d0937e2a398a5ea5bb1ad84c32d42e2caee6d9","amount":"0"}},"timestamp":1566473190343},"signature":"9425f4e3d4f90cda36fc0d271d8213399f7801628956a0173d92d9555ec103f981e1df796616c875389988bb44860772b3b0a56920b6a4a7d016e4a8a252c39ed22204ca9f4ffdfbe92d655b2a8702e458bc639190bda86884efffd08e74c2b1925b660de89c22e3e2de270ee479edd5cc2b01b8eaf999a65ad99b7e274d36663fbf8208bc4aabf8a75aa80a8d3bacc68a05ebad","status":{"status":"D","result":"0000000000000000000000000000000000000000000000000000000000000000","height":404,"timestamp":1566473193905},"node":{"nid":"DNt2rAqpDRiGSQ3WDyVPt9ugGX0L","address":"a77d02543e9c932e1b9edb8cb3ee3f4efe2688d2"},"accepttimestamp":1566473191978}
     */
    private int retCode;
    private TransactionBean transaction;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public TransactionBean getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionBean transaction) {
        this.transaction = transaction;
    }

    public static class TransactionBean {
        /**
         * hash : be613cd58c1e86efbd3f348ab75b375827cfec00e9455eb3b16218af4f1caebb
         * body : {"nonce":1,"address":"dfd55cae94f7953d4b8890ecff081242fe368937","data":{"type":"CALLCONTRACT","callContractData":{"data":"70a08231000000000000000000000000dfd55cae94f7953d4b8890ecff081242fe368937","contract":"f9d0937e2a398a5ea5bb1ad84c32d42e2caee6d9","amount":"0"}},"timestamp":1566473190343}
         * signature : 9425f4e3d4f90cda36fc0d271d8213399f7801628956a0173d92d9555ec103f981e1df796616c875389988bb44860772b3b0a56920b6a4a7d016e4a8a252c39ed22204ca9f4ffdfbe92d655b2a8702e458bc639190bda86884efffd08e74c2b1925b660de89c22e3e2de270ee479edd5cc2b01b8eaf999a65ad99b7e274d36663fbf8208bc4aabf8a75aa80a8d3bacc68a05ebad
         * status : {"status":"D","result":"0000000000000000000000000000000000000000000000000000000000000000","height":404,"timestamp":1566473193905}
         * node : {"nid":"DNt2rAqpDRiGSQ3WDyVPt9ugGX0L","address":"a77d02543e9c932e1b9edb8cb3ee3f4efe2688d2"}
         * accepttimestamp : 1566473191978
         */

        private String hash;
        private BodyBean body;
        private String signature;
        private StatusBean status;
        private NodeBean node;
        private long accepttimestamp;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public BodyBean getBody() {
            return body;
        }

        public void setBody(BodyBean body) {
            this.body = body;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public StatusBean getStatus() {
            return status;
        }

        public void setStatus(StatusBean status) {
            this.status = status;
        }

        public NodeBean getNode() {
            return node;
        }

        public void setNode(NodeBean node) {
            this.node = node;
        }

        public long getAccepttimestamp() {
            return accepttimestamp;
        }

        public void setAccepttimestamp(long accepttimestamp) {
            this.accepttimestamp = accepttimestamp;
        }

        public static class BodyBean {
            /**
             * nonce : 1
             * address : dfd55cae94f7953d4b8890ecff081242fe368937
             * data : {"type":"CALLCONTRACT","callContractData":{"data":"70a08231000000000000000000000000dfd55cae94f7953d4b8890ecff081242fe368937","contract":"f9d0937e2a398a5ea5bb1ad84c32d42e2caee6d9","amount":"0"}}
             * timestamp : 1566473190343
             */

            private int nonce;
            private String address;
            private DataBean data;
            private long timestamp;

            public int getNonce() {
                return nonce;
            }

            public void setNonce(int nonce) {
                this.nonce = nonce;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public long getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(long timestamp) {
                this.timestamp = timestamp;
            }

            public static class DataBean {
                /**
                 * type : CALLCONTRACT
                 * callContractData : {"data":"70a08231000000000000000000000000dfd55cae94f7953d4b8890ecff081242fe368937","contract":"f9d0937e2a398a5ea5bb1ad84c32d42e2caee6d9","amount":"0"}
                 */

                private String type;
                private CallContractDataBean callContractData;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public CallContractDataBean getCallContractData() {
                    return callContractData;
                }

                public void setCallContractData(CallContractDataBean callContractData) {
                    this.callContractData = callContractData;
                }

                public static class CallContractDataBean {
                    /**
                     * data : 70a08231000000000000000000000000dfd55cae94f7953d4b8890ecff081242fe368937
                     * contract : f9d0937e2a398a5ea5bb1ad84c32d42e2caee6d9
                     * amount : 0
                     */

                    private String data;
                    private String contract;
                    private String amount;

                    public String getData() {
                        return data;
                    }

                    public void setData(String data) {
                        this.data = data;
                    }

                    public String getContract() {
                        return contract;
                    }

                    public void setContract(String contract) {
                        this.contract = contract;
                    }

                    public String getAmount() {
                        return amount;
                    }

                    public void setAmount(String amount) {
                        this.amount = amount;
                    }
                }
            }
        }

        public static class StatusBean {
            /**
             * status : D
             * result : 0000000000000000000000000000000000000000000000000000000000000000
             * height : 404
             * timestamp : 1566473193905
             */

            private String status;
            private String result;
            private String hash;
            private int height;
            private long timestamp;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getResult() {
                return result;
            }

            public void setResult(String result) {
                this.result = result;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public long getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(long timestamp) {
                this.timestamp = timestamp;
            }

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }
        }

        public static class NodeBean {
            /**
             * nid : DNt2rAqpDRiGSQ3WDyVPt9ugGX0L
             * address : a77d02543e9c932e1b9edb8cb3ee3f4efe2688d2
             */

            private String nid;
            private String address;

            public String getNid() {
                return nid;
            }

            public void setNid(String nid) {
                this.nid = nid;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
    }
}
