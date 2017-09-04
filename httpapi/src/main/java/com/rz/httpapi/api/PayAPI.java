package com.rz.httpapi.api;

/**
 * Created by Administrator on 2017/3/28/028.
 */

public class PayAPI {
    /**
     * 设置支付密码-修改支付密码
     */
    //    public static final String SETORMODIFY_PAYPW = "v2/pay/setPayPassword";
    public static final String SETORMODIFY_PAYPW = "v3/pay/setPayPassword";//--200



    /**
     * 忘记支付密码，重置支付密码
     */
    //    public static final String RESET_PAY_PASSWORD = "v2/pay/reSetPayPassword";
    public static final String RESET_PAY_PASSWORD = "v3/pay/reSetPayPassword";

    /**
     *  支付接口(支付宝支付、微信支付(需签名认证))
     */
//    public static final String PAY = "v2/pay/getNewPayFlowId";
    public static final String PAY = "v3/pay/getNewPayFlowId";//----200




    /**
     *  收益兑换到账户
     */
//    public static final String POINT_TO_ACCOUNT = "v2/pay/pointsToAccount";
    public static final String POINT_TO_ACCOUNT = "v3/pay/pointsToAccount";


    /**
     * 查询用户账户余额
     */
//    public static final String GET_ACCOUNT = "v2/pay/getUserAccount";
    public static final String GET_ACCOUNT = "v3/pay/getUserAccount";//--200

    /**
     *  提现
     */
//    public static final String GET_CASH = "v2/pay/getCash";
    public static final String GET_CASH = "v3/pay/getCash";//和之前比,少个参数//绑定银行关联ID	cust2BankId	varchar(n)


    /**
     *  计算手续费
     */
//    public static final String GET_CHARGE = "v2/pay/getServiceCharge";
    public static final String GET_CHARGE = "v3/pay/getServiceCharge";

    /**
     * 绑定银行卡
     */
//    public static final String BAND_CARD = "v2/pay/bindBankCard";
    public static final String BAND_CARD = "v3/pay/bindBankCard";//---200


    /**
     * 获取绑定的银行卡列表
     */
//    public static final String GET_BAND_LIST = "v2/pay/bankCardList";
    public static final String GET_BAND_LIST = "v3/pay/bankCardList";//--200

    /**
     //     *  设置默认的银行卡
     //     */
//    public static final String SET_DEFAULT_CARD = "v2/pay/setDefaultBankCard";
    public static final String SET_DEFAULT_CARD = "v3/pay/setDefaultBankCard";//--200


    /**
     * 解除绑定银行卡
     */
//    public static final String UNBAND_CARD = "v2/pay/unbindBankCard";
    public static final String UNBAND_CARD = "v3/pay/unbindBankCard";//少了参数//支付密码	password	varchar(n)		密码需用md5加密----200


    /**
     * 开启或者关闭免密支付
     */
//    public static final String OPEN_OR_CLOSE_EASY_PAY = "v2/pay/setFreePay";
    public static final String OPEN_OR_CLOSE_EASY_PAY = "v3/pay/setFreePay";//之前接口名不正确,需要再次核实//---200

    /**
     *  查询最小充值额度
     */
//    public static final String GET_MIN_CHARGE_AMOUNT = "v2/pay/getMinChargeAmount";
    public static final String GET_MIN_CHARGE_AMOUNT = "v3/pay/getMinChargeAmount";

    /**
     * 获取用户安全信息
     */
//    public static final String SEARCH_USER_NEWS = "v2/pay/getUserPhy";
    public static final String SEARCH_USER_NEWS = "v3/pay/getUserPhy";//--200

    /**
     * 查询订单明细
     */
//    public static final String GET_BILL_ORDER = "v2/pay/getOrderList";
    public static final String GET_BILL_ORDER = "v3/pay/getOrderList";//--200

    /**
     * 验证安全信息
     */
//    public static final String CHECK_SECURITY_PROBLEM = "v2/pay/checkSecurityProblem";
    public static final String CHECK_SECURITY_PROBLEM = "v3/pay/checkSecurityProblem";//--200

    /**
     * 设置密保问题
     */
//    public static final String SET_SECURITY_PROBLEM = "v2/pay/setSecurityProblem";
    public static final String SET_SECURITY_PROBLEM = "v3/pay/setSecurityProblem";//-200

    /**
     * 验证支付密码
     */
    public static final String CHECK_PAY_PWD = "v3/pay/checkPayPwd";//该接口2.0已有,但是未使用,


}
