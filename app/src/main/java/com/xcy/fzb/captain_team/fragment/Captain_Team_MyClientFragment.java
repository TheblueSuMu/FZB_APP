package com.xcy.fzb.captain_team.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xcy.fzb.R;
import com.xcy.fzb.all.api.FinalContents;
import com.xcy.fzb.all.api.NewlyIncreased;
import com.xcy.fzb.all.fragment.AllFragment;
import com.xcy.fzb.all.fragment.MyClientFragment2;
import com.xcy.fzb.all.fragment.MyClientFragment3;
import com.xcy.fzb.all.fragment.MyClientFragment4;
import com.xcy.fzb.all.fragment.MyClientFragment5;
import com.xcy.fzb.all.fragment.MyClientFragment6;
import com.xcy.fzb.all.fragment.MyClientFragment7;
import com.xcy.fzb.all.modle.ReportNoReadListBean;
import com.xcy.fzb.all.persente.MyClientName;
import com.xcy.fzb.all.persente.SingleClick;
import com.xcy.fzb.all.persente.StatusBar;
import com.xcy.fzb.all.service.MyService;
import com.xcy.fzb.all.view.MyClientAddActivity;
import com.xcy.fzb.all.view.PhoneActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Captain_Team_MyClientFragment extends AllFragment implements View.OnClickListener {

    LinearLayout my_client_return;
    ImageView client_add;

    EditText my_client_ss;

    TextView my_client_tv;

    RelativeLayout my_client_ll_1;
    RelativeLayout my_client_11_2;
    RelativeLayout my_client_11_3;
    RelativeLayout my_client_11_4;
    RelativeLayout my_client_11_5;
    RelativeLayout my_client_11_6;
    RelativeLayout my_client_11_7;
    LinearLayout my_client_11_8;
    LinearLayout my_client_11_9;
    LinearLayout my_client_11_10;
    LinearLayout my_client_11_11;
    LinearLayout my_client_11_12;
    LinearLayout my_client_11_13;
    LinearLayout my_client_11_14;

    FragmentManager manager;
    FragmentTransaction transaction;

    Captain_Team_MyClientFragment1 captain_team_myClientFragment1 = new Captain_Team_MyClientFragment1();
    MyClientFragment2 myClientFragment2 = new MyClientFragment2();
    MyClientFragment3 myClientFragment3 = new MyClientFragment3();
    MyClientFragment4 myClientFragment4 = new MyClientFragment4();
    MyClientFragment5 myClientFragment5 = new MyClientFragment5();
    MyClientFragment6 myClientFragment6 = new MyClientFragment6();
    MyClientFragment7 myClientFragment7 = new MyClientFragment7();

    private Context context;
    private View view;

    private TextView my_client_unread_1;
    private TextView my_client_unread_2;
    private TextView my_client_unread_3;
    private TextView my_client_unread_4;
    private TextView my_client_unread_5;
    private TextView my_client_unread_6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StatusBar.makeStatusBarTransparent(getActivity());
        view = inflater.inflate(R.layout.activity_my_client, container, false);
        initView();
        context = container.getContext();
        return view;
    }

    private void initView() {

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        my_client_return = view.findViewById(R.id.my_client_return);
        client_add = view.findViewById(R.id.client_add);
        my_client_ss = view.findViewById(R.id.my_client_ss);
        my_client_tv = view.findViewById(R.id.my_client_tv);
        my_client_ll_1 = view.findViewById(R.id.my_client_ll_1);
        my_client_11_2 = view.findViewById(R.id.my_client_ll_2);
        my_client_11_3 = view.findViewById(R.id.my_client_ll_3);
        my_client_11_4 = view.findViewById(R.id.my_client_ll_4);
        my_client_11_5 = view.findViewById(R.id.my_client_ll_5);
        my_client_11_6 = view.findViewById(R.id.my_client_ll_6);
        my_client_11_7 = view.findViewById(R.id.my_client_ll_7);
        my_client_11_8 = view.findViewById(R.id.my_client_ll_8);
        my_client_11_9 = view.findViewById(R.id.my_client_ll_9);
        my_client_11_10 = view.findViewById(R.id.my_client_ll_10);
        my_client_11_11 = view.findViewById(R.id.my_client_ll_11);
        my_client_11_12 = view.findViewById(R.id.my_client_ll_12);
        my_client_11_13 = view.findViewById(R.id.my_client_ll_13);
        my_client_11_14 = view.findViewById(R.id.my_client_ll_14);

        //  TODO    客户未读
        my_client_unread_1 = view.findViewById(R.id.my_client_unread_1);
        my_client_unread_2 = view.findViewById(R.id.my_client_unread_2);
        my_client_unread_3 = view.findViewById(R.id.my_client_unread_3);
        my_client_unread_4 = view.findViewById(R.id.my_client_unread_4);
        my_client_unread_5 = view.findViewById(R.id.my_client_unread_5);
        my_client_unread_6 = view.findViewById(R.id.my_client_unread_6);

        my_client_unread_1.bringToFront();
        my_client_unread_2.bringToFront();
        my_client_unread_3.bringToFront();
        my_client_unread_4.bringToFront();
        my_client_unread_5.bringToFront();
        my_client_unread_6.bringToFront();

        captain_team_myClientFragment1 = new Captain_Team_MyClientFragment1();
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.my_client_fl, captain_team_myClientFragment1);
        transaction.commit();

        my_client_return.setOnClickListener(this);
        client_add.setOnClickListener(this);
        my_client_ll_1.setOnClickListener(this);
        my_client_11_2.setOnClickListener(this);
        my_client_11_3.setOnClickListener(this);
        my_client_11_4.setOnClickListener(this);
        my_client_11_5.setOnClickListener(this);
        my_client_11_6.setOnClickListener(this);
        my_client_11_7.setOnClickListener(this);

        my_client_return.setVisibility(View.GONE);

        my_client_ss.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_ENTER == i && KeyEvent.ACTION_DOWN == keyEvent.getAction()) {
                    //处理事件
                    String s = my_client_ss.getText().toString();
                    EventBus.getDefault().post(new MyClientName(s));
                    return true;
                }
                return false;
            }

        });

    }

    private void initData(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(FinalContents.getBaseUrl());
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit build = builder.build();
        MyService fzbInterface = build.create(MyService.class);
        Observable<ReportNoReadListBean> clientFragment = fzbInterface.getreportNoRead(FinalContents.getAgentId(),"",FinalContents.getUserID(),FinalContents.getMySelf(), NewlyIncreased.getTag(), NewlyIncreased.getStartDate(), NewlyIncreased.getEndDate());
        clientFragment.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReportNoReadListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ReportNoReadListBean reportNoReadListBean) {
                        if (reportNoReadListBean.getData().getReports().equals("0") || reportNoReadListBean.getData().getReports().equals("")) {//  TODO 报备
                            my_client_unread_1.setVisibility(View.INVISIBLE);
                        }else {
                            my_client_unread_1.setVisibility(View.VISIBLE);
                            my_client_unread_1.setText(reportNoReadListBean.getData().getReports());
                        }

                        if (reportNoReadListBean.getData().getAccessing().equals("0") || reportNoReadListBean.getData().getAccessing().equals("")) {//  TODO 到访
                            my_client_unread_2.setVisibility(View.INVISIBLE);
                        }else {
                            my_client_unread_2.setVisibility(View.VISIBLE);
                            my_client_unread_2.setText(reportNoReadListBean.getData().getAccessing());
                        }

                        if (reportNoReadListBean.getData().getTrade().equals("0") || reportNoReadListBean.getData().getTrade().equals("")) {//  TODO 成交
                            my_client_unread_5.setVisibility(View.INVISIBLE);
                        }else {
                            my_client_unread_5.setVisibility(View.VISIBLE);
                            my_client_unread_5.setText(reportNoReadListBean.getData().getTrade());
                        }

                        if (reportNoReadListBean.getData().getLose().equals("0") || reportNoReadListBean.getData().getLose().equals("")) {//  TODO 失效
                            my_client_unread_6.setVisibility(View.INVISIBLE);
                        }else {
                            my_client_unread_6.setVisibility(View.VISIBLE);
                            my_client_unread_6.setText(reportNoReadListBean.getData().getLose());
                        }

                        if (reportNoReadListBean.getData().getEarnest().equals("0") || reportNoReadListBean.getData().getEarnest().equals("")) {//  TODO 认筹
                            my_client_unread_4.setVisibility(View.INVISIBLE);
                        }else {
                            my_client_unread_4.setVisibility(View.VISIBLE);
                            my_client_unread_4.setText(reportNoReadListBean.getData().getEarnest());
                        }

                        if (reportNoReadListBean.getData().getIsIsland().equals("0") || reportNoReadListBean.getData().getIsIsland().equals("")) {//  TODO 登岛
                            my_client_unread_3.setVisibility(View.INVISIBLE);
                        }else {
                            my_client_unread_3.setVisibility(View.VISIBLE);
                            my_client_unread_3.setText(reportNoReadListBean.getData().getIsIsland());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("MyCL", "未浏览错误信息" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SingleClick(1000)
    @Override
    public void onClick(View view) {
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        switch (view.getId()) {
            case R.id.my_client_return:
                break;
            case R.id.client_add:
                final List<String> list = new ArrayList<>();
                list.add("手动添加");
                list.add("通讯录导入");

                //      监听选中
                OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //               返回的分别是三个级别的选中位置
                        //              展示选中数据
                        if (list.get(options1).equals("手动添加")) {
                            Intent intent = new Intent(getContext(), MyClientAddActivity.class);
                            startActivity(intent);
                        }else if (list.get(options1).equals("通讯录导入")){
                            Intent intent = new Intent(getContext(), PhoneActivity.class);
                            startActivity(intent);
                        }
                    }
                })
                        .setSelectOptions(0)//设置选择第一个
                        .setOutSideCancelable(false)//点击背的地方不消失
                        .build();//创建
                //      把数据绑定到控件上面
                pvOptions.setPicker(list);
                //      展示
                pvOptions.show();
                break;
            case R.id.my_client_ll_1:
                my_client_tv.setVisibility(View.GONE);
                my_client_ss.setVisibility(View.VISIBLE);

                my_client_11_8.setVisibility(View.VISIBLE);
                my_client_11_9.setVisibility(View.INVISIBLE);
                my_client_11_10.setVisibility(View.INVISIBLE);
                my_client_11_11.setVisibility(View.INVISIBLE);
                my_client_11_12.setVisibility(View.INVISIBLE);
                my_client_11_13.setVisibility(View.INVISIBLE);
                my_client_11_14.setVisibility(View.INVISIBLE);
                transaction.replace(R.id.my_client_fl, captain_team_myClientFragment1);
                break;
            case R.id.my_client_ll_2:
                my_client_tv.setVisibility(View.VISIBLE);
                my_client_ss.setVisibility(View.GONE);

                my_client_11_8.setVisibility(View.INVISIBLE);
                my_client_11_9.setVisibility(View.VISIBLE);
                my_client_11_10.setVisibility(View.INVISIBLE);
                my_client_11_11.setVisibility(View.INVISIBLE);
                my_client_11_12.setVisibility(View.INVISIBLE);
                my_client_11_13.setVisibility(View.INVISIBLE);
                my_client_11_14.setVisibility(View.INVISIBLE);
                transaction.replace(R.id.my_client_fl, myClientFragment2);
                break;
            case R.id.my_client_ll_3:
                my_client_tv.setVisibility(View.VISIBLE);
                my_client_ss.setVisibility(View.GONE);

                my_client_11_8.setVisibility(View.INVISIBLE);
                my_client_11_9.setVisibility(View.INVISIBLE);
                my_client_11_10.setVisibility(View.VISIBLE);
                my_client_11_11.setVisibility(View.INVISIBLE);
                my_client_11_12.setVisibility(View.INVISIBLE);
                my_client_11_13.setVisibility(View.INVISIBLE);
                my_client_11_14.setVisibility(View.INVISIBLE);
                transaction.replace(R.id.my_client_fl, myClientFragment3);
                break;
            case R.id.my_client_ll_4:
                my_client_tv.setVisibility(View.VISIBLE);
                my_client_ss.setVisibility(View.GONE);

                my_client_11_8.setVisibility(View.INVISIBLE);
                my_client_11_9.setVisibility(View.INVISIBLE);
                my_client_11_10.setVisibility(View.INVISIBLE);
                my_client_11_11.setVisibility(View.VISIBLE);
                my_client_11_12.setVisibility(View.INVISIBLE);
                my_client_11_13.setVisibility(View.INVISIBLE);
                my_client_11_14.setVisibility(View.INVISIBLE);
                transaction.replace(R.id.my_client_fl, myClientFragment4);
                break;
            case R.id.my_client_ll_5:
                my_client_tv.setVisibility(View.VISIBLE);
                my_client_ss.setVisibility(View.GONE);

                my_client_11_8.setVisibility(View.INVISIBLE);
                my_client_11_9.setVisibility(View.INVISIBLE);
                my_client_11_10.setVisibility(View.INVISIBLE);
                my_client_11_11.setVisibility(View.INVISIBLE);
                my_client_11_12.setVisibility(View.VISIBLE);
                my_client_11_13.setVisibility(View.INVISIBLE);
                my_client_11_14.setVisibility(View.INVISIBLE);
                transaction.replace(R.id.my_client_fl, myClientFragment5);
                break;
            case R.id.my_client_ll_6:
                my_client_tv.setVisibility(View.VISIBLE);
                my_client_ss.setVisibility(View.GONE);

                my_client_11_8.setVisibility(View.INVISIBLE);
                my_client_11_9.setVisibility(View.INVISIBLE);
                my_client_11_10.setVisibility(View.INVISIBLE);
                my_client_11_11.setVisibility(View.INVISIBLE);
                my_client_11_12.setVisibility(View.INVISIBLE);
                my_client_11_13.setVisibility(View.VISIBLE);
                my_client_11_14.setVisibility(View.INVISIBLE);
                transaction.replace(R.id.my_client_fl, myClientFragment6);
                break;
            case R.id.my_client_ll_7:
                my_client_tv.setVisibility(View.VISIBLE);
                my_client_ss.setVisibility(View.GONE);

                my_client_11_8.setVisibility(View.INVISIBLE);
                my_client_11_9.setVisibility(View.INVISIBLE);
                my_client_11_10.setVisibility(View.INVISIBLE);
                my_client_11_11.setVisibility(View.INVISIBLE);
                my_client_11_12.setVisibility(View.INVISIBLE);
                my_client_11_13.setVisibility(View.INVISIBLE);
                my_client_11_14.setVisibility(View.VISIBLE);
                transaction.replace(R.id.my_client_fl, myClientFragment7);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            //TODO now visible to user 不显示fragment
        } else {
            onResume();
            //TODO now invisible to user 显示fragment
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
