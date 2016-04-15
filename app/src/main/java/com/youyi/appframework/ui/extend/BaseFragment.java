package com.youyi.appframework.ui.extend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youyi.appframework.R;
import com.youyi.appframework.utils.LogUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by Rain on 2016/2/17.
 */
public abstract class BaseFragment extends Fragment {
    protected View view;
//    protected NavTitleBarControler controler;

    public String getName() {
        return String.format(">>>> HashCode %d, Name %s", hashCode(), ((Object) this).getClass().getSimpleName());
    }

    @Override
    public void onAttach(Activity activity) {
        LogUtil.d(getName() + "-->onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.d(getName() + "-->onCreate()");
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d(getName() + "-->onCreateView()");
        if (view != null) {
            ViewGroup p = (ViewGroup) view.getParent();

            if (p != null) {
                p.removeView(view);
            }
        } else {
            view = inflater.inflate(getLayoutResourceId(), container, false);
//            controler = new NavTitleBarControler(view.findViewById(R.id.nav_bar));
            initial();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        LogUtil.d(getName() + "--> onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LogUtil.d(getName() + "-->onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    /* 加载layout xml */
    protected abstract int getLayoutResourceId();

    /* 初始化数据 */
    protected abstract void initial();

    @Override
    public void onStart() {
        LogUtil.d(getName() + "-->onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        LogUtil.d(getName() + "-->onResume()");
        super.onResume();
        String name = getClass().getSimpleName();
//        UmengAnalysisTools.onPageStart(name);

    }

    @Override
    public void onPause() {
        LogUtil.d(getName() + "-->onPause()");
        super.onPause();
        String name = getClass().getSimpleName();
//        UmengAnalysisTools.onPageEnd(name);

    }


    @Override
    public void onStop() {
        LogUtil.d(getName() + "-->onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        LogUtil.d(getName() + "-->onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtil.d(getName() + "-->onDestroy()");
        super.onDestroy();
//        if(controler!=null)
//            controler.onDestory();
    }

    @Override
    public void onDetach() {
        LogUtil.d(getName() + "-->onDetach()");
        super.onDetach();
//        EventBus.getDefault().unregister(this);
    }

    /**
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.d(getName() + " UserVisibleHint: %s", isVisibleToUser + "");
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void startActivity(Intent intent) {
        LogUtil.d(getName() + "-->startActivity()");
        super.startActivity(intent);
        onTransitionAnim();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        LogUtil.d(getName() + "-->startActivityForResult()");
        super.startActivityForResult(intent, requestCode);
        onTransitionAnim();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.d(getName() + "-->onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onTransitionAnim() {
        getActivity().overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
    }

    public boolean checkActivityExit() {
        return (getActivity() == null || getActivity().isFinishing()) ? true : false;
    }

/*    public void onEventMainThread(LoginSuccessEvent event) {

    }*/

    public View findView() {
        if (null != view) {
            return view;
        }
        return null;
    }
}
