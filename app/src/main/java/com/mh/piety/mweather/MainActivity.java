package com.mh.piety.mweather;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mh.piety.mweather.Bean.LocalInfoBean;
import com.mh.piety.mweather.Bean.WeatherBean;
import com.mh.piety.mweather.Utils.WeatherUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String position="";
    private Context mContext = this;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Menu mMenu;
    private Toolbar toolbar;
    private TextView tv_position,tv_tmp,tv_fl,tv_txt,tv_dir,tv_sc,tv_hum,tv_vis,tv_day1Cond,tv_day2Cond,tv_day3Cond,tv_day1Tmp,tv_day2Tmp,tv_day3Tmp;
    private TextView tv_qlty,tv_aqi,tv_pm25,tv_dbrf,tv_dtxt,tv_sbrf,tv_stxt,tv_tbrf,tv_ttxt,tv_update;
    private ImageView iv_delete,iv_icon,iv_day1Icon,iv_day2Icon,iv_day3Icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},3);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_activity);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        tv_position= (TextView) findViewById(R.id.tv_position);
        iv_delete= (ImageView) findViewById(R.id.iv_delete);

        //天气信息相关控件
        tv_tmp= (TextView) findViewById(R.id.tv_tmp);
        tv_fl= (TextView) findViewById(R.id.tv_fl);
        tv_txt= (TextView) findViewById(R.id.tv_txt);
        tv_dir= (TextView) findViewById(R.id.tv_dir);
        tv_sc= (TextView) findViewById(R.id.tv_sc);
        tv_hum= (TextView) findViewById(R.id.tv_hum);
        tv_vis= (TextView) findViewById(R.id.tv_vis);
        iv_icon= (ImageView) findViewById(R.id.iv_icon);
        tv_day1Cond= (TextView) findViewById(R.id.tv_day1Cond);
        tv_day2Cond= (TextView) findViewById(R.id.tv_day2Cond);
        tv_day3Cond= (TextView) findViewById(R.id.tv_day3Cond);
        tv_day1Tmp= (TextView) findViewById(R.id.tv_day1Tmp);
        tv_day2Tmp= (TextView) findViewById(R.id.tv_day2Tmp);
        tv_day3Tmp= (TextView) findViewById(R.id.tv_day3Tmp);
        iv_day1Icon= (ImageView) findViewById(R.id.iv_day1Icon);
        iv_day2Icon= (ImageView) findViewById(R.id.iv_day2Icon);
        iv_day3Icon= (ImageView) findViewById(R.id.iv_day3Icon);
        tv_qlty= (TextView) findViewById(R.id.tv_qlty);
        tv_aqi= (TextView) findViewById(R.id.tv_aqi);
        tv_pm25= (TextView) findViewById(R.id.tv_pm25);
        tv_dbrf= (TextView) findViewById(R.id.tv_dbrf);
        tv_dtxt= (TextView) findViewById(R.id.tv_dtxt);
        tv_sbrf= (TextView) findViewById(R.id.tv_sbrf);
        tv_stxt= (TextView) findViewById(R.id.tv_stxt);
        tv_tbrf= (TextView) findViewById(R.id.tv_tbrf);
        tv_ttxt= (TextView) findViewById(R.id.tv_ttxt);
        tv_update= (TextView) findViewById(R.id.tv_update);

        //获取用户偏好地址

        final ArrayList<LocalInfoBean> list = new WeatherUtil().getPrePosition(mContext);
        if(!checkNet()) {
            Toast.makeText(mContext, "当前网络连接不可用", Toast.LENGTH_SHORT).show();
            if(list.size()>0){
                updateMenu();
                tv_position.setText(mMenu.getItem(0).getTitle());
                WeatherUtil wutil = new WeatherUtil();
                wutil.jsondata = list.get(0).weather_info;
                tv_position.setText(list.get(0).position);
                updateUI(setWeatherBean(wutil, 1));
            }
        } else {
            if(list.size()>0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WeatherUtil wutil = new WeatherUtil(list.get(0).position.split("-")[1]);
                    final WeatherBean wb = setWeatherBean(wutil, 0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_position.setText(list.get(0).position);
                            updateMenu();
                            updateUI(wb);
                        }
                    });
                }
            }).start();}
            else {
                Intent intent = new Intent();
                intent.setClass(mContext,PositionActivity.class);
                startActivity(intent);
            }
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton float_btn = (FloatingActionButton) findViewById(R.id.float_btn);
        float_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext,PositionActivity.class);
                startActivity(intent);
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            String str_position = item.getTitle().toString();
            tv_position.setText(str_position);
            update(str_position);
            mDrawerLayout.closeDrawer(mNavigationView);
            return true;
            }
        });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new WeatherUtil().getPrePosition(mContext).size()>0){
                new AlertDialog.Builder(mContext)
                        .setTitle("提示")
                        .setMessage("确认不想查看 "+tv_position.getText().toString().split("-")[1]+" 的天气信息吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new WeatherUtil().deleteInfo(mContext,tv_position.getText().toString());
                                updateMenu();
                                if(mMenu.size()>0){
                                    String str_position = mMenu.getItem(0).getTitle().toString();
                                    tv_position.setText(str_position);
                                    update(str_position);
                                }
                                else{
                                    Toast.makeText(mContext,"请添加地址！",Toast.LENGTH_SHORT).show();
                                    tv_position.setText("");
                                    tv_tmp.setText("...°");
                                    tv_fl.setText("体感温度：...°");
                                    tv_txt.setText("...");
                                    tv_dir.setText("...");
                                    tv_sc.setText("...");
                                    tv_hum.setText("...%");
                                    tv_vis.setText("...km");
                                    iv_icon.setImageResource(R.drawable.none);
                                    iv_day1Icon.setImageResource(R.drawable.none);
                                    iv_day2Icon.setImageResource(R.drawable.none);
                                    iv_day3Icon.setImageResource(R.drawable.none);
                                    tv_day1Cond.setText("...");
                                    tv_day2Cond.setText("...");
                                    tv_day3Cond.setText("...");
                                    tv_day1Tmp.setText("...°/...°");
                                    tv_day2Tmp.setText("...°/...°");
                                    tv_day3Tmp.setText("...°/...°");
                                    tv_qlty.setText("...");
                                    tv_aqi.setText("...");
                                    tv_pm25.setText("...");
                                    tv_dbrf.setText("...");
                                    tv_dtxt.setText("......");
                                    tv_sbrf.setText("...");
                                    tv_stxt.setText("......");
                                    tv_tbrf.setText("...");
                                    tv_ttxt.setText("......");
                                    tv_update.setText("最后更新时间：");
                                    Intent intent = new Intent();
                                    intent.setClass(mContext,PositionActivity.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (position!="") {
            final boolean isexist = new WeatherUtil().isExist(mContext,position);
            tv_position.setText(position);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String location=position.split("-")[1];
                        WeatherUtil wutil = new WeatherUtil(location);
                        if(isexist){new WeatherUtil().updateInfo(mContext,position,wutil.jsondata);}
                        else{new WeatherUtil().storeWeatherInfo(mContext,position,wutil.jsondata);}
                        final WeatherBean wb = setWeatherBean(wutil,0);
                        MainActivity.position = "";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateMenu();
                                updateUI(wb);
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext,"未能找到相关的天气信息!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }
    }
    private boolean checkNet(){
        try {
            ConnectivityManager conn = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo net = conn.getActiveNetworkInfo();
            if(net.getState()== NetworkInfo.State.CONNECTED){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    private void updateMenu(){
        mMenu=mNavigationView.getMenu();
        mMenu.clear();
        ArrayList<LocalInfoBean> list = new WeatherUtil().getPrePosition(mContext);
        int i=0;
        for(LocalInfoBean l:list){
            mMenu.add(Menu.NONE,Menu.FIRST+i,Menu.NONE,l.position);
            i++;
        }
    }
    private WeatherBean setWeatherBean(WeatherUtil wutil,int type){;
        WeatherBean wb = new WeatherBean();
        wb.now = wutil.getNowWeathre();
        wb.day1 = wutil.getDailyWeather(0);
        wb.day2 = wutil.getDailyWeather(1);
        wb.day3 = wutil.getDailyWeather(2);
        if(type==0){
        wb.now_icon = wutil.getIcon(wb.now.cond.code);
        wb.day1_icon = wutil.getIcon(wb.day1.cond.code_d);
        wb.day2_icon = wutil.getIcon(wb.day2.cond.code_d);
        wb.day3_icon = wutil.getIcon(wb.day3.cond.code_d);}
        else{
            wb.now_icon = wutil.getIconForLoc(wb.now.cond.code);
            wb.day1_icon = wutil.getIconForLoc(wb.day1.cond.code_d);
            wb.day2_icon = wutil.getIconForLoc(wb.day2.cond.code_d);
            wb.day3_icon = wutil.getIconForLoc(wb.day3.cond.code_d);
        }
        wb.aqi=wutil.getAQI();
        wb.drsg=wutil.getSuggestion("drsg");
        wb.sport=wutil.getSuggestion("sport");
        wb.trav=wutil.getSuggestion("trav");
        wb.time=wutil.getUpdateTime();
        return wb;
    }
    private void updateUI(WeatherBean wb){
        tv_tmp.setText(wb.now.tmp + "°");
        tv_fl.setText("体感温度：" + wb.now.fl + "°");
        tv_txt.setText(wb.now.cond.txt);
        tv_dir.setText(wb.now.wind.dir);
        tv_sc.setText(wb.now.wind.sc.indexOf("风") != -1 ? wb.now.wind.sc : wb.now.wind.sc + "级");
        tv_hum.setText(wb.now.hum + "%");
        tv_vis.setText(wb.now.vis + "km");
        iv_icon.setImageBitmap(wb.now_icon);
        iv_day1Icon.setImageBitmap(wb.day1_icon);
        iv_day2Icon.setImageBitmap(wb.day2_icon);
        iv_day3Icon.setImageBitmap(wb.day3_icon);
        tv_day1Cond.setText(wb.day1.cond.txt_d.equals(wb.day1.cond.txt_n) ? wb.day1.cond.txt_d : wb.day1.cond.txt_d + "转" + wb.day1.cond.txt_n);
        tv_day2Cond.setText(wb.day2.cond.txt_d.equals(wb.day2.cond.txt_n) ? wb.day2.cond.txt_d : wb.day2.cond.txt_d + "转" + wb.day2.cond.txt_n);
        tv_day3Cond.setText(wb.day3.cond.txt_d.equals(wb.day3.cond.txt_n) ? wb.day3.cond.txt_d : wb.day3.cond.txt_d + "转" + wb.day3.cond.txt_n);
        tv_day1Tmp.setText(wb.day1.tmp.min + "°/" + wb.day1.tmp.max + "°");
        tv_day2Tmp.setText(wb.day2.tmp.min + "°/" + wb.day2.tmp.max + "°");
        tv_day3Tmp.setText(wb.day3.tmp.min + "°/" + wb.day3.tmp.max + "°");
        if(wb.aqi==null){
            tv_qlty.setText("无");
            tv_aqi.setText("无");
            tv_pm25.setText("无");
        }
        else{
            tv_qlty.setText(wb.aqi.qlty);
            tv_aqi.setText(wb.aqi.aqi);
            tv_pm25.setText(wb.aqi.pm25);
        }
        tv_dbrf.setText(wb.drsg.brf);
        tv_dtxt.setText(wb.drsg.txt);
        tv_sbrf.setText(wb.sport.brf);
        tv_stxt.setText(wb.sport.txt);
        tv_tbrf.setText(wb.trav.brf);
        tv_ttxt.setText(wb.trav.txt);
        tv_update.setText("最后更新时间：" + wb.time);
    }
    public void update(String position){
        final String str_position=position;
        if(checkNet()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                    WeatherUtil wutil = new WeatherUtil(str_position.split("-")[1]);
                    final WeatherBean wb = setWeatherBean(wutil,0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI(wb);
                        }
                    });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else {
            WeatherUtil wutil = new WeatherUtil();
            ArrayList<LocalInfoBean> list = wutil.getPrePosition(mContext);
            for(LocalInfoBean l:list){
                if(l.position.equals(str_position)){
                    wutil.jsondata=l.weather_info;
                    WeatherBean wb = setWeatherBean(wutil,1);
                    updateUI(wb);
                    break;
                }
            }
        }
    }
}
