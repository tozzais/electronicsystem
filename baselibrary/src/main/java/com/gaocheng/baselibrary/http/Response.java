package com.gaocheng.baselibrary.http;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.gaocheng.baselibrary.BaseApplication;
import com.gaocheng.baselibrary.util.LoadingUtils;
import com.gaocheng.baselibrary.util.ToastCommom;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class Response<T> extends Subscriber<T> {

    private Context mContext;
    private boolean mNeedDialog = false;
    private onCancelRequestListener cancelRequestListener;

    public Response(Context context) {
        this.mContext = context;
    }

    public Response(Context context, boolean needDialog) {
        this.mContext = context;
        this.mNeedDialog = needDialog;
    }

    public Response(Context context, boolean needDialog, onCancelRequestListener cancelRequestListener) {
        this.mContext = context;
        this.mNeedDialog = needDialog;
        this.cancelRequestListener = cancelRequestListener;
    }


    /**
     * 此方法现在onNext或者onError之后都会调用
     * 所以一般要处理请求结束时的信息是，需要重写此方法
     * 例如，loading结束时，刷新下拉刷新结果时等…………
     */
    @Override
    public void onCompleted() {
        if (mNeedDialog) {
            LoadingUtils.dismiss();
        }
        mContext = null;
    }

    @Override
    public void onNext(T str) {


    }

    @Override
    public void onStart() {
        if (mNeedDialog) {
            LoadingUtils.show(mContext);
            LoadingUtils.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (i == KeyEvent.KEYCODE_BACK) {
                        unsubscribe();
                        LoadingUtils.dismiss();
                        if (cancelRequestListener != null) {
                            cancelRequestListener.onCancelRequest();
                        }
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 除非非要获取网络错误信息，否则一般不需要重写此方法；
     * 例如：网络400，404，断网，超时，等等…………
     */
    @Override
    public void onError(Throwable e) {
        onCompleted();
        if (e == null)
            return;
        try {
            if (e instanceof ConnectException || e instanceof UnknownHostException) {
                ToastCommom.createToastConfig().ToastShow(BaseApplication.mContext, "连接服务器失败");
            } else if (e instanceof SocketTimeoutException) {
                ToastCommom.createToastConfig().ToastShow(BaseApplication.mContext, "连接超时");
            } else if (e instanceof HttpException) {
                ToastCommom.createToastConfig().ToastShow(BaseApplication.mContext, "服务器发生错误");
            } else if (e instanceof JsonSyntaxException) {
                ToastCommom.createToastConfig().ToastShow(BaseApplication.mContext, "解析失败");
            } else {
                ToastCommom.createToastConfig().ToastShow(BaseApplication.mContext, "未知错误");
            }
        } catch (Exception ignored) {

        }
        if (e.getMessage() != null) ;
    }

    public interface onCancelRequestListener {
        public void onCancelRequest();
    }

}