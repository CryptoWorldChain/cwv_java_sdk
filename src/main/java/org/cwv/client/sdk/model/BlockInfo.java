package org.cwv.client.sdk.model;

import java.util.List;

public class BlockInfo {


    /**
     * retCode : 1
     * block : [{"header":{"hash":"19c756f6e6f54c8567497d348bebe95db0e738ea622a5e3a731d8644bdf526a1","parentHash":"0000000000000000000000000000000000000000","height":1,"stateRoot":"003a35a9c4f0f12b7c4caf30df877ef789a7e0f7a167a2e2069a23e27c3efddf","receiptRoot":"c5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470","timestamp":1567153882120,"txHashCount":0},"miner":{"nid":"Dd97IzkSPgCfWZlZO4jutzPX7wO43","address":"a77d02543e9c932e1b9edb8cb3ee3f4efe2688d2","reward":10},"version":0}]
     */

    private int retCode;
    private List<BlockBean> block;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public List<BlockBean> getBlock() {
        return block;
    }

    public void setBlock(List<BlockBean> block) {
        this.block = block;
    }

    public static class BlockBean {
        /**
         * header : {"hash":"19c756f6e6f54c8567497d348bebe95db0e738ea622a5e3a731d8644bdf526a1","parentHash":"0000000000000000000000000000000000000000","height":1,"stateRoot":"003a35a9c4f0f12b7c4caf30df877ef789a7e0f7a167a2e2069a23e27c3efddf","receiptRoot":"c5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470","timestamp":1567153882120,"txHashCount":0}
         * miner : {"nid":"Dd97IzkSPgCfWZlZO4jutzPX7wO43","address":"a77d02543e9c932e1b9edb8cb3ee3f4efe2688d2","reward":10}
         * version : 0
         */

        private HeaderBean header;
        private MinerBean miner;
        private int version;

        public HeaderBean getHeader() {
            return header;
        }

        public void setHeader(HeaderBean header) {
            this.header = header;
        }

        public MinerBean getMiner() {
            return miner;
        }

        public void setMiner(MinerBean miner) {
            this.miner = miner;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public static class HeaderBean {
            /**
             * hash : 19c756f6e6f54c8567497d348bebe95db0e738ea622a5e3a731d8644bdf526a1
             * parentHash : 0000000000000000000000000000000000000000
             * height : 1
             * stateRoot : 003a35a9c4f0f12b7c4caf30df877ef789a7e0f7a167a2e2069a23e27c3efddf
             * receiptRoot : c5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470
             * timestamp : 1567153882120
             * txHashCount : 0
             */

            private String hash;
            private String parentHash;
            private int height;
            private String stateRoot;
            private String receiptRoot;
            private long timestamp;
            private int txHashCount;

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public String getParentHash() {
                return parentHash;
            }

            public void setParentHash(String parentHash) {
                this.parentHash = parentHash;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public String getStateRoot() {
                return stateRoot;
            }

            public void setStateRoot(String stateRoot) {
                this.stateRoot = stateRoot;
            }

            public String getReceiptRoot() {
                return receiptRoot;
            }

            public void setReceiptRoot(String receiptRoot) {
                this.receiptRoot = receiptRoot;
            }

            public long getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(long timestamp) {
                this.timestamp = timestamp;
            }

            public int getTxHashCount() {
                return txHashCount;
            }

            public void setTxHashCount(int txHashCount) {
                this.txHashCount = txHashCount;
            }
        }

        public static class MinerBean {
            /**
             * nid : Dd97IzkSPgCfWZlZO4jutzPX7wO43
             * address : a77d02543e9c932e1b9edb8cb3ee3f4efe2688d2
             * reward : 10
             */

            private String nid;
            private String address;
            private int reward;

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

            public int getReward() {
                return reward;
            }

            public void setReward(int reward) {
                this.reward = reward;
            }
        }
    }
}
