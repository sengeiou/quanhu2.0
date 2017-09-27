package com.rz.httpapi.api;

/**
 * Created by JS01 on 2016/6/7.
 */
public interface API {

    /**
     * 8.18日接口调试,内网
     */
    String URL_DEBUG = "http://192.168.30.15:8090/";

    //阿里测试环境
    //用户，账户，好友
    String URL_APP_USER_17 = "http://192.168.30.17:8093/";
    //红包，打赏,首页
    String URL_APP_REDPACKET_17 = "http://192.168.30.17:8091/";
    //圈子，作品
    String URL_APP_CIRCLE_17 = "http://192.168.30.17:8090/";
    //晒一晒
    String URL_APP_SHINE_17 = "http://192.168.30.17:8092/";

    //阿里测试环境
    //用户，账户，好友
    String URL_APP_USER_MO = "https://moau.yryz.com/";
    //红包，打赏,首页
    String URL_APP_REDPACKET_MO = "https://moar.yryz.com/";
    //圈子，作品
    String URL_APP_CIRCLE_MO = "https://moac.yryz.com/";
    //晒一晒
    String URL_APP_SHINE_MO = "https://moas.yryz.com/";

    //本地测试环境域名
    //用户，账户，好友
    String URL_APP_USER_TEST = "http://aute.muder.pw/";
    //红包，打赏,首页
    String URL_APP_REDPACKET_TEST = "http://arte.muder.pw/";
    //圈子，作品
    String URL_APP_CIRCLE_TEST = "http://acte.muder.pw/";
    //晒一晒
    String URL_APP_SHINE_TEST = "http://aste.muder.pw/";

    //阿里正式域名
    //用户，账户，好友
    String URL_APP_USER_RELEASE = "https://au.yryz.com/";
    //红包，打赏,首页
    String URL_APP_REDPACKET_RELEASE = "https://ar.yryz.com/";
    //圈子，作品
    String URL_APP_CIRCLE_RELEASE = "https://ac.yryz.com/";
    //晒一晒
    String URL_APP_SHINE_RELEASE = "https://as.yryz.com/";

    //公告
    String URL_ANNOUNCEMENT = "v3/notice/getNewNoticeList";

    //申请通过
    String URL_APPLY_FOR_PASS = "xycfq/message/001/{业务类型}";
    //申请不通过
    String URL_APPLY_FOR_REFUSE = "/xycfq/message/002/{业务类型}";
    //资料被打回
    String URL_INFORMATION_IS_INCORRECT = "/xycfq/message/003/";
    //后台已发货
    String URL_SHIPPED = "/xycfq/message/004/";
    //提前三天
    String URL_REPAYMENT_LAST_THREE_DAY = "/xycfq/message/005/{orderNo}";
    //冻结超过三天
    String URL_FREEZE_EXCEED_THREE_DAY = "/xycfq/message/006/{业务类型}";
    //逾期后一天提醒
    String URL_OVERDUE_ONE_DAY = "/xycfq/message/007/{orderNo}";
    //手动、自动解冻后通知
    String URL_UNFREEZE = "/xycfq/message/008/";
    //审核通过
    String URL_REVIEW_PASS = "/xycfq/message/009/";
    //资料被打回
    String URL_REVIEW_REJECT = "/xycfq/message/010/";
    //审核不通过
    String URL_REVIEW_REFUSE = "/xycfq/message/011/";

    /**
     * 举报
     */
    String REPORT = "v2/reported/save";


    /**
     * 圈子成员列表
     */
    String CIRCLE_MEMBER = "v3/circle/getCircleMember";

    /**
     * 圈子收益统计
     */
    String CLUB_STATS = "v2/collection/getClubStatistics";

    /**
     * 圈子转发详情（圈子列表点开）
     */
    String TRANSFER_CLUB_DETAIL = "v2/collection/transferClubDetail";

    /**
     * 作品标签列表
     */
    String OPUS_TAG = "v2/opus/getTags";/**
     * 作品标签列表
     */
    String GET_PERMISSION = "v3/permission/getAllPermission";

    /**
     * 作品标签搜索
     */
    String OPUS_TAGINFO_LIST = "v2/opus/getTagsInfoByList";

    /**
     * 作品列表
     */
    String OPUS_LIST = "v2/opus/list";


    /**
     * 圈子图片
     */
    String CIRCLE_IMGS = "v2/collection/getCustImg";


}
