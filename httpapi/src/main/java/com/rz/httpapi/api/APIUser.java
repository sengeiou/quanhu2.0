package com.rz.httpapi.api;

/**
 * Created by rzw2 on 2017/3/27.
 */

public class APIUser {
    /**
     * 发送验证码
     * public static final String SEND_VERI_CODE = "v2/user/sendVerifyCode";
     */
    public static final String SEND_VERI_CODE = "v3/user/sendVerifyCode";

    /**
     * 验证短信验证码
     * public static final String CHECK_CODE = "v2/user/checkVerifyCode";
     */
    public static final String CHECK_CODE = "v3/user/checkVerifyCode";

    /**
     * 注册
     * public static final String REGISTER = "v2/user/register";
     */
    public static final String REGISTER = "v3/user/register";

    /**
     * 登录接口
     * public static final String LOGIN = "v2/user/login";
     */
    // FIXME: 2017/3/27 接口拆分 没有isPwdExist这个参数
    public static final String LOGIN = "v3/user/login";

    /**
     * 第三方登录
     * public static final String OTHER_LOGIN = "v2/user/thirdLogin";
     */
    public static final String OTHER_LOGIN = "v3/user/thirdLogin";

    /**
     * 退出APP
     * public static final String EXIT_APP = "v2/user/loginOut";
     */
    public static final String EXIT_APP = "v3/user/loginOut";

    /**
     * 获取登录方式
     * public static final String LOGIN_METHOD = "v2/user/loginMethod";
     */
    public static final String LOGIN_METHOD = "v3/user/loginMethod";

    /**
     * 修改登录密码
     * public static final String MODIFY_PW = "v2/user/editPassword";
     */
    public static final String MODIFY_PW = "v3/user/editPassword";

    /**
     * 忘记密码-重置密码
     * public static final String CHANGE_PW = "v2/user/forgotPassword";
     */
    public static final String CHANGE_PW = "v3/user/forgotPassword";

    /**
     *  用户个人信息编辑修改
     * public static final String PERSON_INFO_UPDATE = "v2/user/modify";
     */
    public static final String PERSON_INFO_UPDATE = "v3/user/modify";

    /**
     * 解绑、绑定第三方账号
     * public static final String BIND_THIRD_ACCOUNT = "v2/user/bindThirdAccount";
     */
    public static final String BIND_THIRD_ACCOUNT = "v3/user/bindThirdAccount";

    /**
     * 绑定手机号
     * public static final String BIND_PHONE = "v2/user/bindPhoneAccount";
     */
    public static final String BIND_PHONE = "v3/user/bindPhoneAccount";

    /**
     * 用户搜索
     * public static final String FRIEND_SEARCH = "v2/user/search";
     */
    public static final String FRIEND_SEARCH = "v3/search/cust";

    /**
     * 检索手机用户
     * public static final String FIND_PHONE = "v2/user/findPhones";
     */
    public static final String FIND_PHONE = "v3/user/findPhones";

    /**
     * 好友详情
     * public static final String FRIEND_DETAIL = "v2/user/find";
     */
    public static final String FRIEND_DETAIL = "v3/user/find";


    /**
     * 统一搜索
     * public static final String QH_SEARCH = "v3/search/searchQH";
     */
//    public static final String QH_SEARCH = "v3/search/searchQH";


    public static final String QH_SEARCH = "v3/search/searchQH";

    /**
     * 达人证人申请
     */
    public static final String PROVE_INFO = "v3/star/starApply";
}
