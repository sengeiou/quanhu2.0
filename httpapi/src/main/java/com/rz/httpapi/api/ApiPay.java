package com.rz.httpapi.api;

/**
 * Created by rzw2 on 2017/9/15.
 */

public class ApiPay {
    /**
     * 执行支付订单
     */
    static final String PAY_ORDER = "v3/pay/executeOrder";

    /**
     * 查询订单详情
     */
    static final String PAY_ORDER_DETAILS = "v3/pay/getOrderInfo";


    /**
     * 设置支付密码-修改支付密码
     */
    static final String SETORMODIFY_PAYPW = "v3/pay/setPayPassword";


    /**
     * 忘记支付密码，重置支付密码
     */
    static final String RESET_PAY_PASSWORD = "v3/pay/reSetPayPassword";

    /**
     *  支付接口(支付宝支付、微信支付(需签名认证))
     */
    static final String PAY = "v3/pay/getNewPayFlowId";//----200


    /**
     *  收益兑换到账户
     */
    static final String POINT_TO_ACCOUNT = "v3/pay/pointsToAccount";


    /**
     * 查询用户账户余额
     */
    static final String GET_ACCOUNT = "v3/pay/getUserAccount";//--200

    /**
     *  提现
     */
    static final String GET_CASH = "v3/pay/getCash";//和之前比,少个参数//绑定银行关联ID	cust2BankId	varchar(n)


    /**
     *  计算手续费
     */
    static final String GET_CHARGE = "v3/pay/getServiceCharge";
    /**
     *  消息免打扰
     */
    static final String Message_Free = "v3/userConfig/update";
    /**
     *  查询消息免打扰
     */
    static final String QUERY_Message_Free = "v3/userConfig/get";

    /**
     * 绑定银行卡
     */
    static final String BAND_CARD = "v3/pay/bindBankCard";//---200


    /**
     * 获取绑定的银行卡列表
     */
    static final String GET_BAND_LIST = "v3/pay/bankCardList";//--200

    /**
     * //     *  设置默认的银行卡
     * //
     */
    static final String SET_DEFAULT_CARD = "v3/pay/setDefaultBankCard";//--200


    /**
     * 解除绑定银行卡
     */
    static final String UNBAND_CARD = "v3/pay/unbindBankCard";//少了参数//支付密码	password	varchar(n)		密码需用md5加密----200


    /**
     * 开启或者关闭免密支付
     */
    static final String OPEN_OR_CLOSE_EASY_PAY = "v3/pay/setFreePay";//之前接口名不正确,需要再次核实//---200

    /**
     *  查询最小充值额度
     */
    static final String GET_MIN_CHARGE_AMOUNT = "v3/pay/getMinChargeAmount";

    /**
     * 获取用户安全信息
     */
    static final String SEARCH_USER_NEWS = "v3/pay/getUserPhy";//--200

    /**
     * 查询订单明细
     */
    static final String GET_BILL_ORDER = "v3/pay/getOrderList";//--200

    /**
     * 验证安全信息
     */
    static final String CHECK_SECURITY_PROBLEM = "v3/pay/checkSecurityProblem";//--200

    /**
     * 设置密保问题
     */
    static final String SET_SECURITY_PROBLEM = "v3/pay/setSecurityProblem";//-200

    /**
     * 验证支付密码
     */
    static final String CHECK_PAY_PWD = "v3/pay/checkPayPwd";//该接口2.0已有,但是未使用,

    /**
     * 获取登录方式
     */
    static final String GET_LOGIN_METHOD = "v3/user/loginMethod";



}
