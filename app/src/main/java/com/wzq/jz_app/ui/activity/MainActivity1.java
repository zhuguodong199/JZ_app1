package com.wzq.jz_app.ui.activity;



import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.heima.tabview.library.TabView;
import com.heima.tabview.library.TabViewChild;
import com.wzq.jz_app.R;
import com.wzq.jz_app.base.BaseActivity;
import com.wzq.jz_app.ui.fragment.ChartFragment;
import com.wzq.jz_app.ui.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity1 extends BaseActivity {
    // tab用参数


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main1;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        List<TabViewChild> tabViewChildList = new ArrayList<>();
        TabViewChild tabViewChild01 = new TabViewChild(R.drawable.footer_home_selector, R.drawable.footer_home_selector, "账单", new HomeFragment());
        TabViewChild tabViewChild02 = new TabViewChild(R.drawable.footer_add_selector, R.drawable.footer_add_selector, " ", new HomeFragment());
        TabViewChild tabViewChild03 = new TabViewChild(R.drawable.footer_chart_selector, R.drawable.footer_chart_selector, "数据", new ChartFragment());
        tabViewChildList.add(tabViewChild01);
        tabViewChildList.add(tabViewChild02);
        tabViewChildList.add(tabViewChild03);
        TabView tabView= (TabView) findViewById(R.id.tabView);
        tabView.setTabViewChild(tabViewChildList,getSupportFragmentManager());
        tabView.setOnTabChildClickListener(new TabView.OnTabChildClickListener() {
            @Override
            public void onTabChildClick(int  position, ImageView currentImageIcon, TextView currentTextView) {
                if (position == 1) {
                    startActivity(new Intent(MainActivity1.this,AddActivity.class));
                }
            }
        });
    }
}
