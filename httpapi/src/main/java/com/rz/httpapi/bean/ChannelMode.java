package com.rz.httpapi.bean;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
public class ChannelMode {

//    {
//        :   "ret":2021,
//        :   "msg":"success",
//        :   "act":0,
//        :   "data":
//        :   [
//        :   :   {
//        :   :   :   "uid":"ebee8f25e25f434d8f4299fbebcdd0c7",
//        :   :   :   "channelPid":"0",
//        :   :   :   "channelName":"名人明星",
//        :   :   :   "channelOrder":"111",
//        :   :   :   "channelLogo":null,
//

//    是否有上级
//    分类名称
//    分类排序
//    分类图标
//    所属类型


    public String uid;
    public String channelPid;
    public String channelName;
    public String channelOrder;
    public String channelLogo;
    public int channelType;
    public String isTransfer;//0否1是

    @Override
    public String toString() {
        return "ChannelMode{" +
                "uid='" + uid + '\'' +
                ", channelPid=" + channelPid +
                ", channelName='" + channelName + '\'' +
                ", channelOrder=" + channelOrder +
                ", channelLogo='" + channelLogo + '\'' +
                ", channelType=" + channelType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelMode that = (ChannelMode) o;

        if (!uid.equals(that.uid)) return false;
        return channelName.equals(that.channelName);

    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + channelName.hashCode();
        return result;
    }
}
