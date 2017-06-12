package com.xm.frame.update;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xm.frame.R;
import com.xm.frame.utils.StringUtils;

import com.xm.frame.widget.DownloadButton;
import com.xm.frame.widget.WaveDrawable;
import com.xm.xdownload.net.buffer.DownInfo;
import com.zhy.autolayout.utils.AutoUtils;


/**
 * 更新版面
 * by：小民
 */

public class UpdateDialog extends Dialog{
    private final View mInflate;
    private WaveDrawable mWaveDrawable;
    private DownloadButton mDownloadButton;

    public UpdateDialog(Context context, @DrawableRes int imgRes, String version, String content, View.OnClickListener onClickListener){
        super(context, R.style.loading_dialog_style);
        mInflate = View.inflate(context, R.layout.dialog_update, null);
        AutoUtils.autoSize(mInflate);
        setContentView(mInflate);
        //点击空白处消失
        setCanceledOnTouchOutside(false);
        //按返回键不允许取消 - 如果你是强制更新。那么还是建议你为 false
        setCancelable(true);
        //初始化View
        initView(mInflate,imgRes,version,content,onClickListener);
        //下载按钮监听
        mInflate.findViewById(R.id.btn_download).setOnClickListener(onClickListener);
    }

    /** 初始化View */
    private void initView(View inflate, @DrawableRes int imgRes , String version, String content, View.OnClickListener onClickListener){
        ImageView ivLogo = (ImageView) mInflate.findViewById(R.id.iv_logo);
        int percentWidthSize = AutoUtils.getPercentWidthSize(250);
        mWaveDrawable = new WaveDrawable(getContext(), imgRes,percentWidthSize,percentWidthSize);
        mWaveDrawable.setLevel(1000);
        ivLogo.setImageDrawable(mWaveDrawable);
        //初始化更新内容
        ((TextView)mInflate.findViewById(R.id.tv_version)).setText("v" + version);
        if (StringUtils.isNotEmpty(content)){
            ((TextView)mInflate.findViewById(R.id.tv_content)).setText(content);
        }
        //更新按钮
        mDownloadButton = (DownloadButton) mInflate.findViewById(R.id.btn_download);
    }

    /** 更新按钮状态 */
    public void updateButtonState(DownInfo info){
        switch (info.getState()){
            case DOWN:
                mDownloadButton.start(DownloadButton.Status.START);  //显示进度条
                break;
            default:
                mDownloadButton.start(DownloadButton.Status.PAUSE);
                break;
        }
        //刷新文本
        mDownloadButton.setText(info.getStateText());
    }
    /** 更新进度条 */
    public void updateProgress(int progress){
        //为了美观，10以下显示固定显示10 -> 太小没有水波纹
        if(progress < 10 ){
            progress = 10;
        }
        mWaveDrawable.setLevel(progress * 100);
    }

}
