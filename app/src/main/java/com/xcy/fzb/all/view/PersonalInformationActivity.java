package com.xcy.fzb.all.view;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xcy.fzb.R;
import com.xcy.fzb.all.api.FinalContents;
import com.xcy.fzb.all.modle.AddPhotoBean;
import com.xcy.fzb.all.modle.ChangeNameBean;
import com.xcy.fzb.all.modle.ChangeSexBean;
import com.xcy.fzb.all.modle.GWDataBean;
import com.xcy.fzb.all.modle.PersonalPhotoBean;
import com.xcy.fzb.all.modle.TZBean;
import com.xcy.fzb.all.modle.UserMessageBean;
import com.xcy.fzb.all.modle.XSDataBean;
import com.xcy.fzb.all.modle.ZYDataBean;
import com.xcy.fzb.all.modle.ZhangBingDataBean;
import com.xcy.fzb.all.persente.OkHttpPost;
import com.xcy.fzb.all.persente.StatusBar;
import com.xcy.fzb.all.service.MyService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//TODO 个人信息
public class PersonalInformationActivity extends AllActivity implements View.OnClickListener {

    RelativeLayout personalReturnImg;
    RelativeLayout personalRl1;
    RelativeLayout personalRl2;
    RelativeLayout personalRl3;
    RelativeLayout personalRl4;
    RelativeLayout personalRl5;
    RelativeLayout personalRl6;
    RelativeLayout information_rl;

    LinearLayout information_ll;

    ImageView personal_photo;
    TextView personal_name;
    TextView personal_sex;
    TextView personal_phone;
    TextView personal_city;
    TextView personal_store;
    TextView personal_identity;
    EditText personal_et_name;


    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 0;
    private Bitmap bm;

    private Intent intent;

    String imgStr = "";
    String isPhoto = "";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private String s;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        ButterKnife.bind(this);

        initView();

    }

    private void initView() {

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBar.makeStatusBarTransparent(PersonalInformationActivity.this);

        information_ll = findViewById(R.id.information_ll);
        personalReturnImg = findViewById(R.id.personal_return_img);
        personalRl1 = findViewById(R.id.personal_rl_1);
        personalRl2 = findViewById(R.id.personal_rl_2);
        personalRl3 = findViewById(R.id.personal_rl_3);
        personalRl4 = findViewById(R.id.personal_rl_4);
        personalRl5 = findViewById(R.id.personal_rl_5);
        personalRl6 = findViewById(R.id.personal_rl_6);
        information_rl = findViewById(R.id.information_rl);

        personal_photo = findViewById(R.id.personal_photo);
        personal_name = findViewById(R.id.personal_name);
        personal_sex = findViewById(R.id.personal_sex);
        personal_phone = findViewById(R.id.personal_phone);
        personal_city = findViewById(R.id.personal_city);
        personal_store = findViewById(R.id.personal_store);
        personal_identity = findViewById(R.id.personal_identity);
        personal_et_name = findViewById(R.id.personal_et_name);

        personalReturnImg.setOnClickListener(this);
        personalRl1.setOnClickListener(this);
        personalRl2.setOnClickListener(this);
        personalRl3.setOnClickListener(this);
        personalRl4.setOnClickListener(this);
        personalRl5.setOnClickListener(this);
        personalRl6.setOnClickListener(this);


        if (FinalContents.getIdentity().equals("1") || FinalContents.getIdentity().equals("2") || FinalContents.getIdentity().equals("3") || FinalContents.getIdentity().equals("4") || FinalContents.getIdentity().equals("5") || FinalContents.getIdentity().equals("7")) {
            initData();
        } else if (FinalContents.getIdentity().equals("60")) {
            initDataTDZ();
        } else if (FinalContents.getIdentity().equals("61")) {
            initDataTDZ();
        } else if (FinalContents.getIdentity().equals("62")) {
            initDataTDZ();
        } else if (FinalContents.getIdentity().equals("63")) {
            initDataTDZ();
        } else {
            initData();
        }

    }

    private void initData() {

        information_rl.setVisibility(View.VISIBLE);
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(FinalContents.getBaseUrl());
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit build = builder.build();
        MyService fzbInterface = build.create(MyService.class);
        Log.i("公共个人信息详情", "FinalContents.getUserID()：" + FinalContents.getUserID());
        if (FinalContents.getIdentity().equals("1") || FinalContents.getIdentity().equals("2") || FinalContents.getIdentity().equals("3")) {
            Observable<UserMessageBean> userMessage = fzbInterface.getUserMessage(FinalContents.getUserID());
            userMessage.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UserMessageBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(UserMessageBean userMessageBean) {
                            UserMessageBean.DataBean data = userMessageBean.getData();
                            Glide.with(PersonalInformationActivity.this).load("http://39.98.173.250:8080" + data.getPhoto()).into(personal_photo);
                            personal_name.setText(data.getName());
                            personal_identity.setText("经纪人");
                            personal_city.setText(data.getCity());
                            personal_sex.setText(data.getSex());
                            personal_phone.setText(data.getPhone());
                            s = data.getSex();
                            UserMessageBean.DataBean.StoreManageBean storeManage = data.getStoreManage();
                            personal_store.setText(storeManage.getStoreName());

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("MyCL", "根据用户id获取用户信息错误:" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else if (FinalContents.getIdentity().equals("4")) {
            Observable<XSDataBean> userMessage = fzbInterface.getXSDataBean(FinalContents.getUserID());
            userMessage.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<XSDataBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(XSDataBean userMessageBean) {
                            information_rl.setVisibility(View.GONE);
                            information_ll.setVisibility(View.GONE);
                            XSDataBean.DataBean data = userMessageBean.getData();
                            Glide.with(PersonalInformationActivity.this).load("http://39.98.173.250:8080" + data.getPhoto()).into(personal_photo);
                            personal_name.setText(data.getName());
                            personal_identity.setText("专案");
                            personal_city.setText(data.getCity());
                            personal_sex.setText(data.getSex());
                            personal_phone.setText(data.getPhone());
                            s = data.getSex();
                            String storeManage = data.getStoreManage();
                            personal_store.setText(storeManage);

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("MyCL", "根据用户id获取用户信息错误:" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else if (FinalContents.getIdentity().equals("5") || FinalContents.getIdentity().equals("7")) {
            Observable<ZYDataBean> userMessage = fzbInterface.getZYDataBean(FinalContents.getUserID());
            userMessage.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ZYDataBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ZYDataBean userMessageBean) {
                            information_rl.setVisibility(View.GONE);
                            information_ll.setVisibility(View.GONE);
                            ZYDataBean.DataBean data = userMessageBean.getData();
                            Glide.with(PersonalInformationActivity.this).load("http://39.98.173.250:8080" + data.getPhoto()).into(personal_photo);
                            personal_name.setText(data.getName());
                            if (data.getIdentity().equals("5")) {
                                personal_identity.setText("专员");
                            } else if (data.getIdentity().equals("7")) {
                                personal_identity.setText("导购");
                            }
                            personal_city.setText(data.getCity());
                            personal_sex.setText(data.getSex());
                            personal_phone.setText(data.getPhone());
                            s = data.getSex();
                            String storeManage = data.getStoreManage();
                            personal_store.setText(storeManage);

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("MyCL", "根据用户id获取用户信息错误:" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private void initDataTDZ() {

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(FinalContents.getBaseUrl());
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit build = builder.build();
        MyService fzbInterface = build.create(MyService.class);
        Log.i("团助", "FinalContents.getUserID()：" + FinalContents.getUserID());
        if (FinalContents.getIdentity().equals("60")) {
            Observable<ZhangBingDataBean> userMessage = fzbInterface.getZhangBingBean(FinalContents.getUserID(), FinalContents.getUserID());
            userMessage.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ZhangBingDataBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ZhangBingDataBean userMessageBean) {

                            if (userMessageBean.getData().getSysUser().getPhoto().equals("")) {
                                Glide.with(PersonalInformationActivity.this).load("http://39.98.173.250:8080" + userMessageBean.getData().getSysUser().getManager().getPhoto()).into(personal_photo);
                            } else {
                                Glide.with(PersonalInformationActivity.this).load("http://39.98.173.250:8080" + userMessageBean.getData().getSysUser().getPhoto()).into(personal_photo);
                            }

                            personal_name.setText(userMessageBean.getData().getSysUser().getName());
                            if (userMessageBean.getData().getSysUser().getIdentity().equals("60")) {
                                personal_identity.setText("团队长");
                            } else if (userMessageBean.getData().getSysUser().getIdentity().equals("61")) {
                                personal_identity.setText("销售");
                            } else if (userMessageBean.getData().getSysUser().getIdentity().equals("62")) {
                                personal_identity.setText("顾问");
                            } else if (userMessageBean.getData().getSysUser().getIdentity().equals("63")) {
                                personal_identity.setText("团助");
                            }

                            personal_city.setText(userMessageBean.getData().getSysUser().getCity());
                            personal_sex.setText(userMessageBean.getData().getSysUser().getSex());
                            personal_phone.setText(userMessageBean.getData().getSysUser().getPhone());
                            s = userMessageBean.getData().getSysUser().getSex();
                            personal_store.setText(userMessageBean.getData().getSysUser().getStoreManage());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("MyCL", "根据用户id获取用户信息错误:" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else if (FinalContents.getIdentity().equals("61") || FinalContents.getIdentity().equals("62")) {
            Observable<GWDataBean> userMessage = fzbInterface.getGWDataBean(FinalContents.getUserID(), FinalContents.getUserID());
            userMessage.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GWDataBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(GWDataBean userMessageBean) {

                            if (userMessageBean.getData().getSysUser().getPhoto().equals("")) {
                                Glide.with(PersonalInformationActivity.this).load("http://39.98.173.250:8080" + userMessageBean.getData().getSysUser().getManager().getPhoto()).into(personal_photo);
                            } else {
                                Glide.with(PersonalInformationActivity.this).load("http://39.98.173.250:8080" + userMessageBean.getData().getSysUser().getPhoto()).into(personal_photo);
                            }


                            personal_name.setText(userMessageBean.getData().getSysUser().getName());
                            if (userMessageBean.getData().getSysUser().getIdentity().equals("60")) {
                                personal_identity.setText("团队长");
                            } else if (userMessageBean.getData().getSysUser().getIdentity().equals("61")) {
                                personal_identity.setText("销售");
                            } else if (userMessageBean.getData().getSysUser().getIdentity().equals("62")) {
                                personal_identity.setText("顾问");
                            } else if (userMessageBean.getData().getSysUser().getIdentity().equals("63")) {
                                personal_identity.setText("团助");
                            }

                            personal_city.setText(userMessageBean.getData().getSysUser().getCity());
                            personal_sex.setText(userMessageBean.getData().getSysUser().getSex());
                            personal_phone.setText(userMessageBean.getData().getSysUser().getPhone());
                            s = userMessageBean.getData().getSysUser().getSex();
                            personal_store.setText(userMessageBean.getData().getSysUser().getStoreManage());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("MyCL", "根据用户id获取用户信息错误:" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else if (FinalContents.getIdentity().equals("63")) {
            Observable<TZBean> userMessage = fzbInterface.getTZBean(FinalContents.getUserID(), FinalContents.getUserID());
            userMessage.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TZBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(TZBean userMessageBean) {

                            if (userMessageBean.getData().getSysUser().getPhoto().equals("")) {
                                Glide.with(PersonalInformationActivity.this).load("http://39.98.173.250:8080" + userMessageBean.getData().getSysUser().getManager().getPhoto()).into(personal_photo);
                            } else {
                                Glide.with(PersonalInformationActivity.this).load("http://39.98.173.250:8080" + userMessageBean.getData().getSysUser().getPhoto()).into(personal_photo);
                            }


                            personal_name.setText(userMessageBean.getData().getSysUser().getName());
                            if (userMessageBean.getData().getSysUser().getIdentity().equals("60")) {
                                personal_identity.setText("团队长");
                            } else if (userMessageBean.getData().getSysUser().getIdentity().equals("61")) {
                                personal_identity.setText("销售");
                            } else if (userMessageBean.getData().getSysUser().getIdentity().equals("62")) {
                                personal_identity.setText("顾问");
                            } else if (userMessageBean.getData().getSysUser().getIdentity().equals("63")) {
                                personal_identity.setText("团助");
                            }

                            personal_city.setText(userMessageBean.getData().getSysUser().getCity());
                            personal_sex.setText(userMessageBean.getData().getSysUser().getSex());
                            personal_phone.setText(userMessageBean.getData().getSysUser().getPhone());
                            s = userMessageBean.getData().getSysUser().getSex();
                            personal_store.setText(userMessageBean.getData().getSysUser().getStoreManage());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("MyCL", "根据用户id获取用户信息错误:" + e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }


    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
//返回
            case R.id.personal_return_img:
                finish();
                break;
//头像
            case R.id.personal_rl_1:

                //   弹出框
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationActivity.this);
                builder.setTitle("请选择图片来源");
                builder.setItems(new String[]{"相机", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            try {
                                //检测是否有写的权限
                                int permission = ActivityCompat.checkSelfPermission(PersonalInformationActivity.this,
                                        "android.permission.WRITE_EXTERNAL_STORAGE");
                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    // 没有写的权限，去申请写的权限，会弹出对话框
                                    ActivityCompat.requestPermissions(PersonalInformationActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String SDState = Environment.getExternalStorageState();
                            if (SDState.equals(Environment.MEDIA_MOUNTED)) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
                                /***
                                 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
                                 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
                                 */
                                ContentValues values = new ContentValues();
                                Uri photoUri = PersonalInformationActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                Log.i("MyCL", "图片路径：" + photoUri);
                                file = uri2File(photoUri);
//                                TODO 提交头像

                                if (isPhoto.equals("")) {
                                    isPhoto = "拍照";
                                }


                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(intent, 1);
                            } else {

                            }
                            Toast.makeText(PersonalInformationActivity.this, "相机", Toast.LENGTH_SHORT).show();
                        } else if (i == 1) {
                            Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                            getAlbum.setType(IMAGE_TYPE);
                            startActivityForResult(getAlbum, IMAGE_CODE);
                            Toast.makeText(PersonalInformationActivity.this, "相册", Toast.LENGTH_SHORT).show();
                            Toast.makeText(PersonalInformationActivity.this, "相册", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

                break;
//昵称
            case R.id.personal_rl_2:
//                intent = new Intent(PersonalInformationActivity.this, NickNameActivity.class);
//                intent.putExtra("tx", "");
//                intent.putExtra("xb", data.getSex());
//                startActivity(intent);
//                finish();

                String s1 = personal_name.getText().toString();
                personal_name.setVisibility(View.GONE);
                personal_et_name.setVisibility(View.VISIBLE);
                personal_et_name.setText(s1);

                personal_et_name.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (KeyEvent.KEYCODE_ENTER == i && KeyEvent.ACTION_DOWN == keyEvent.getAction()) {
                            //TODO 处理事件
                            String s = personal_et_name.getText().toString();

                            String url = "http://39.98.173.250:8080/fangfang/app/v1/commonUpdate/updateName?" + "userId=" + FinalContents.getUserID() + "&name=" + s;

                            OkHttpPost okHttpPost = new OkHttpPost(url);
                            String data = okHttpPost.post();
                            Gson gson = new Gson();
                            ChangeNameBean changeNameBean = gson.fromJson(data, ChangeNameBean.class);
                            ChangeNameBean.DataBean data1 = changeNameBean.getData();
                            if (data1.getMessage().equals("修改昵称成功")) {
                                personal_et_name.setVisibility(View.GONE);
                                personal_name.setVisibility(View.VISIBLE);
                                personal_name.setText(s);
                                Toast.makeText(PersonalInformationActivity.this, data1.getMessage(), Toast.LENGTH_SHORT).show();
                                initData();
                            } else {
                                Toast.makeText(PersonalInformationActivity.this, "修改昵称失败", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                        return false;
                    }

                });

                break;
//性别
            case R.id.personal_rl_3:


                String s2 = personal_sex.getText().toString();

                final AlertDialog.Builder builder1 = new AlertDialog.Builder(PersonalInformationActivity.this);
                builder1.setTitle("性别");
                final String[] items = {"男", "女"};
                Log.i("性别", "男女：" + items[0]);
                if (s2.equals("男")) {
                    builder1.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            s = items[i].toString();
//                        Toast.makeText(PersonalInformationActivity.this, items[i], Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (s2.equals("女")) {
                    builder1.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            s = items[i].toString();
//                        Toast.makeText(PersonalInformationActivity.this, items[i], Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (s2.equals("")) {
                    builder1.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            s = items[i].toString();
//                        Toast.makeText(PersonalInformationActivity.this, items[i], Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String url = "http://39.98.173.250:8080/fangfang/app/v1/commonUpdate/updateSex?" + "userId=" + FinalContents.getUserID() + "&sex=" + s;
//                        修改性别成功
                        OkHttpPost okHttpPost = new OkHttpPost(url);
                        String data = okHttpPost.post();
                        Log.i("MyCL", "参数：" + data);
                        Gson gson = new Gson();
                        ChangeSexBean changeSexBean = gson.fromJson(data, ChangeSexBean.class);
                        ChangeSexBean.DataBean data1 = changeSexBean.getData();
                        if (data1.getMessage().equals("修改性别成功")) {
                            Toast.makeText(PersonalInformationActivity.this, data1.getMessage(), Toast.LENGTH_SHORT).show();
                            personal_sex.setText(s);
                        } else {
                            Toast.makeText(PersonalInformationActivity.this, "性别修改失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder1.show();
                break;
//手机号
            case R.id.personal_rl_4:
                String s = personal_phone.getText().toString();
                if (s.equals("")) {
                    Intent intent1 = new Intent(PersonalInformationActivity.this, BindingPhoneActivity.class);
                    startActivity(intent1);
                } else {
                    Intent intent2 = new Intent(PersonalInformationActivity.this, ChangePhoneActivity.class);
                    startActivity(intent2);
                }
                break;
//修改密码
            case R.id.personal_rl_5:
                intent = new Intent(this, SetPasswordActivity.class);
                startActivity(intent);
                finish();
                break;
//第三方账号设置
            case R.id.personal_rl_6:
//                intent = new Intent(this, ToLoginActivity.class);
//                startActivity(intent);
                Toast.makeText(PersonalInformationActivity.this, "暂无功能", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//TODO  获取相册图片地址
        if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
            Log.e("MyCL", "ActivityResult resultCode error");
            return;
        }
        bm = null;
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口

        ContentResolver resolver = getContentResolver();


        //此处的用于判断接收的Activity是不是你想要的那个

        if (requestCode == IMAGE_CODE) {

            try {

                Uri originalUri = data.getData();        //获得图片的uri

                Log.i("MyCL", "图片uri" + originalUri);
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);        //显得到bitmap图片

                final File san = saveFile(bm, "tx.png");
//                                TODO 提交头像
                new Thread() {
                    @Override
                    public void run() {

                        Retrofit.Builder builder = new Retrofit.Builder();
                        builder.baseUrl(FinalContents.getBaseUrl());
                        builder.addConverterFactory(GsonConverterFactory.create());
                        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                        Retrofit build = builder.build();
                        MyService fzbInterface = build.create(MyService.class);

                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), san);

                        MultipartBody.Part part = MultipartBody.Part.createFormData("file", san.getName(), requestBody);

                        Observable<AddPhotoBean> addPhoto = fzbInterface.getAddPhoto(FinalContents.getUserID(), "p", part);
                        Log.i("MyCL", "addPhoto：" + addPhoto.toString());
                        addPhoto.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<AddPhotoBean>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(AddPhotoBean addPhotoBean) {
                                        imgStr = addPhotoBean.getData().getUrl();
                                        Log.i("MyCL", "解析完成后图片路径：" + imgStr);
                                        Glide.with(PersonalInformationActivity.this).load("http://39.98.173.250:8080" + imgStr).into(personal_photo);
                                        Retrofit.Builder builder = new Retrofit.Builder();
                                        builder.baseUrl(FinalContents.getBaseUrl());
                                        builder.addConverterFactory(GsonConverterFactory.create());
                                        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                                        Retrofit build = builder.build();
                                        MyService fzbInterface = build.create(MyService.class);
                                        Observable<PersonalPhotoBean> personalPhoto = fzbInterface.getPersonalPhoto(FinalContents.getUserID(), imgStr);
                                        personalPhoto.subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Observer<PersonalPhotoBean>() {
                                                    @Override
                                                    public void onSubscribe(Disposable d) {

                                                    }

                                                    @Override
                                                    public void onNext(PersonalPhotoBean personalPhotoBean) {
                                                        if (personalPhotoBean.getData().getStatus().equals("1")) {
                                                            Toast.makeText(PersonalInformationActivity.this, personalPhotoBean.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(PersonalInformationActivity.this, personalPhotoBean.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }

                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Log.i("MyCL", "头像提交错误信息：" + e.getMessage());
                                                    }

                                                    @Override
                                                    public void onComplete() {

                                                    }
                                                });

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.i("MyCL", "经济圈发布图片上传错误信息：" + e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                }.start();


                String[] proj = {MediaStore.Images.Media.DATA};


                //好像是android多媒体数据库的封装接口，具体的看Android文档

                Cursor cursor = managedQuery(originalUri, proj, null, null, null);

                //按我个人理解 这个是获得用户选择的图片的索引值

                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                //将光标移至开头 ，这个很重要，不小心很容易引起越界

                cursor.moveToFirst();

                //最后根据索引值获取图片路径

                String path = cursor.getString(column_index);
                Log.i("MyCL", "图片路径：" + path);
            } catch (IOException e) {

                Log.e("MyCL", e.toString());

            }

        }
    }

    //TODO 将Uri转换成File
    private File uri2File(Uri uri) {
        String img_path;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = managedQuery(uri, proj, null,
                null, null);
        if (actualimagecursor == null) {
            img_path = uri.getPath();
        } else {
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            img_path = actualimagecursor
                    .getString(actual_image_column_index);
        }
        File file = new File(img_path);
        return file;
    }

    //TODO 将bitmap转换成File
    public File saveFile(Bitmap bm, String fileName) throws IOException {//将Bitmap类型的图片转化成file类型，便于上传到服务器
        String path = Environment.getExternalStorageDirectory() + "/Ask";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (isPhoto.equals("拍照")) {
            new Thread() {
                @Override
                public void run() {

                    Retrofit.Builder builder = new Retrofit.Builder();
                    builder.baseUrl(FinalContents.getBaseUrl());
                    builder.addConverterFactory(GsonConverterFactory.create());
                    builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                    Retrofit build = builder.build();
                    MyService fzbInterface = build.create(MyService.class);

                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);

                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

                    Observable<AddPhotoBean> addPhoto = fzbInterface.getAddPhoto(FinalContents.getUserID(), "p", part);
                    Log.i("MyCL", "addPhoto：" + addPhoto.toString());
                    addPhoto.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<AddPhotoBean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(AddPhotoBean addPhotoBean) {
                                    imgStr = addPhotoBean.getData().getUrl();
                                    Log.i("MyCL", "解析完成后图片路径：" + imgStr);
                                    Glide.with(PersonalInformationActivity.this).load("http://39.98.173.250:8080" + imgStr).into(personal_photo);
                                    Retrofit.Builder builder = new Retrofit.Builder();
                                    builder.baseUrl(FinalContents.getBaseUrl());
                                    builder.addConverterFactory(GsonConverterFactory.create());
                                    builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                                    Retrofit build = builder.build();
                                    MyService fzbInterface = build.create(MyService.class);
                                    Observable<PersonalPhotoBean> personalPhoto = fzbInterface.getPersonalPhoto(FinalContents.getUserID(), imgStr);
                                    personalPhoto.subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Observer<PersonalPhotoBean>() {
                                                @Override
                                                public void onSubscribe(Disposable d) {

                                                }

                                                @Override
                                                public void onNext(PersonalPhotoBean personalPhotoBean) {
                                                    if (personalPhotoBean.getData().getStatus().equals("1")) {
                                                        Toast.makeText(PersonalInformationActivity.this, personalPhotoBean.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                                        isPhoto = "";
                                                    } else {
                                                        Toast.makeText(PersonalInformationActivity.this, personalPhotoBean.getData().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Log.i("MyCL", "头像提交错误信息：" + e.getMessage());
                                                }

                                                @Override
                                                public void onComplete() {

                                                }
                                            });
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.i("MyCL", "经济圈发布图片上传错误信息：" + e.getMessage());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
            }.start();
        }

    }
}
