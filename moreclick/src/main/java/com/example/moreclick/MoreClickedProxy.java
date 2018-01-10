package com.example.moreclick;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * author zouxiaobang
 * date 18-1-10.
 */

public class MoreClickedProxy {

    private int mDoubleClickTime = 500;
    private int mLongClickTime = 500;
    private boolean isDoubleClick = false;

    private int mClickCount;
    private long mLastDownTime;
    private long mFirstClick;


    private ClickedListener mClickedListener;
    private View mView;

    public void setClickedListener(ClickedListener clickedListener) {
        mClickedListener = clickedListener;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    mClickedListener.onLongClicked(mView);
                    mClickCount = 0;
                    break;
                case 1:
                    mClickCount = 0;
                    mClickedListener.onSingleClicked(mView);
                    break;
                case 2:
                    mClickCount = 0;
                    mClickedListener.onDoubleClicked(mView);
                    break;
            }
        }
    };

    public void onTouch(View view, MotionEvent event) {
        mView = view;

        if (mClickedListener == null){
            throw new RuntimeException("The ClickedListener must not be null !!!!!");
        }

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN){
            mClickCount ++;
            mLastDownTime = System.currentTimeMillis();

            if (!isDoubleClick){
                //发送延迟信息,启动长按
                mHandler.sendEmptyMessageDelayed(0, mLongClickTime);
            }
            if (mClickCount == 1){
                mFirstClick = System.currentTimeMillis();
            } else if (mClickCount == 2){
                //判断双击
                long secondClick = System.currentTimeMillis();
                if (secondClick - mFirstClick <= mDoubleClickTime){
                    isDoubleClick = true;
                    mClickCount = 0;
                    mFirstClick = 0;

                    mHandler.removeMessages(0);
                    mHandler.removeMessages(1);
                    mHandler.sendEmptyMessage(2);
                }
            }
        } else if (action == MotionEvent.ACTION_UP){
            long lastUpTime = System.currentTimeMillis();
            if (lastUpTime - mLastDownTime <= mDoubleClickTime){
                mHandler.removeMessages(0);
                if (!isDoubleClick){
                    mHandler.sendEmptyMessageDelayed(1, 500);
                }
            } else {
                mClickCount = 0;
            }

            if (isDoubleClick){
                isDoubleClick = false;
            }
        }
    }

    public interface ClickedListener {
        void onSingleClicked(View view);

        void onDoubleClicked(View view);

        void onLongClicked(View view);
    }
}
