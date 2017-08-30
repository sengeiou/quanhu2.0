package com.rz.circled.modle;
import com.rz.common.utils.StringUtils;

/**
 * Created by Administrator on 2016/8/27 0027.
 * 用户账户信息
 */
public class AccountModel {

    //消费余额
    public String accountSum;
    //收益余额
    public String integralSum;
    //累计余额
    public String costSum;
    //1表示账户正常，0表示账户被冻结
    private int accountState;

    public String getAccountSum() {
        if (StringUtils.isEmpty(accountSum)) {
            return "0";
        }
        return accountSum;
    }

    public void setAccountSum(String accountSum) {
        this.accountSum = accountSum;
    }

    public String getIntegralSum() {
        return integralSum;
    }

    public void setIntegralSum(String integralSum) {
        this.integralSum = integralSum;
    }

    public String getCostSum() {
        return costSum;
    }

    public void setCostSum(String costSum) {
        this.costSum = costSum;
    }

    public int getAccountState() {
        return accountState;
    }

    public void setAccountState(int accountState) {
        this.accountState = accountState;
    }
}
