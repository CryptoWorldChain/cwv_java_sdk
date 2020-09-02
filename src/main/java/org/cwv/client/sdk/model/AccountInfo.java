package org.cwv.client.sdk.model;

import java.util.List;

public class AccountInfo {

    /**
     * retCode : 1
     * account : {"address":"30aa55ebf6c7335d3c1d01fbadafc653f58714b5","value":{"nonce":43,"balance":"0","tokens":[{"token":"AAA","balance":"1000000"}]}}
     */

    private int retCode;
    private AccountBean account;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

    public static class AccountBean {
        /**
         * address : 30aa55ebf6c7335d3c1d01fbadafc653f58714b5
         * value : {"nonce":43,"balance":"0","tokens":[{"token":"AAA","balance":"1000000"}]}
         */

        private String address;
        private ValueBean value;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public ValueBean getValue() {
            return value;
        }

        public void setValue(ValueBean value) {
            this.value = value;
        }

        public static class ValueBean {
            /**
             * nonce : 43
             * balance : 0
             * tokens : [{"token":"AAA","balance":"1000000"}]
             */

            private int nonce;
            private String balance;
            private List<TokensBean> tokens;

            public int getNonce() {
                return nonce;
            }

            public void setNonce(int nonce) {
                this.nonce = nonce;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public List<TokensBean> getTokens() {
                return tokens;
            }

            public void setTokens(List<TokensBean> tokens) {
                this.tokens = tokens;
            }

            public static class TokensBean {
                /**
                 * token : AAA
                 * balance : 1000000
                 */

                private String token;
                private String balance;

                public String getToken() {
                    return token;
                }

                public void setToken(String token) {
                    this.token = token;
                }

                public String getBalance() {
                    return balance;
                }

                public void setBalance(String balance) {
                    this.balance = balance;
                }
            }
        }
    }
}
