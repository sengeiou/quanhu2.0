package com.rz.circled.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rz.circled.R;
import com.rz.circled.widget.GlideCircleImage;
import com.rz.common.adapter.CommonAdapter;
import com.rz.common.adapter.ViewHolder;
import com.rz.common.utils.TimeUtil;
import com.rz.httpapi.bean.MineRewardBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class MineRewardAdapter extends CommonAdapter {

    private String RESOURCE_TYPE = "1000";         //资源类型为文章

    public MineRewardAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder helper, Object item, int position) {

        MineRewardBean model = (MineRewardBean) item;
        ImageView avatar = (ImageView) helper.getViewById(R.id.avatar);
        TextView tvName = (TextView) helper.getViewById(R.id.tv_name);
        TextView tvTime = (TextView) helper.getViewById(R.id.tv_time);
        TextView tvTitle = (TextView) helper.getViewById(R.id.tv_title);
        ImageView rewardImg = (ImageView) helper.getViewById(R.id.reward_img);
        TextView tvContent = (TextView) helper.getViewById(R.id.tv_content);

        if(model.getUser()!= null && model.getUser().getCustImg() != null){
            Glide.with(mContext).load(model.getUser().getCustImg()).placeholder(R.drawable.ic_default_head).error(R.drawable.ic_default_head).transform(new GlideCircleImage(mContext)).into(avatar);
        }

        //获取当前时间
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);

        long lt = new Long(model.getCreateTime());
        Date date = new Date(lt);
        String res = sdf.format(date);
        String time = TimeUtil.getTime(res,dateNowStr);
        String timeArray[] = time.replace("-","").split(",");
        String time1 = timeArray[0];
        String time2 = timeArray[1];
        String time3 = timeArray[2];

        if(Integer.parseInt(time1)>0){
            if(Integer.valueOf(time1)>=2){
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                tvTime.setText(sdf2.format(model.getCreateTime()));
            }else{
                tvTime.setText(time1+"天" + time2 + "时" + time3 + "分前" );
            }
        }else{
            if(Integer.parseInt(time2)>0){
                tvTime.setText(time2 + "时" + time3 + "分前");
            }else{
                tvTime.setText(time3 + "分前");
            }
        }

        tvName.setText(model.getUser().getCustNname());
//        tvTime.setText(model.getCreateTime()+"");
        tvTitle.setText( model.getUser().getCustNname() +"打赏了价值" + model.getRewardPrice()/100 +"悠然币"+ model.getGiftInfo().getName()+"给");

        if( !TextUtils.isEmpty(model.getResourceInfo().getPics())){
            rewardImg.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(model.getResourceInfo().getPics()).into(rewardImg);
        }else{
            rewardImg.setVisibility(View.GONE);
        }

        if(RESOURCE_TYPE.equals(model.getResourceInfo().getResourceType())){
            tvContent.setText(model.getResourceInfo().getTitle());
        }else{
            tvContent.setText(model.getResourceInfo().getContent());
        }
    }
}
