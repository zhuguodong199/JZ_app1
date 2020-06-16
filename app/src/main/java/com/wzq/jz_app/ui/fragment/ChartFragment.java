package com.wzq.jz_app.ui.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.wzq.jz_app.MyApplication;
import com.wzq.jz_app.R;
import com.wzq.jz_app.base.BaseMVPFragment;
import com.wzq.jz_app.model.bean.local.BBill;
import com.wzq.jz_app.model.bean.local.MonthChartBean;
import com.wzq.jz_app.model.bean.remote.MyUser;
import com.wzq.jz_app.model.repository.LocalRepository;
import com.wzq.jz_app.presenter.MonthChartPresenter;
import com.wzq.jz_app.presenter.contract.MonthChartContract;
import com.wzq.jz_app.ui.adapter.binder.MonthChartBillViewBinder;
import com.wzq.jz_app.ui.fragment.chart.LineChartEntity;
import com.wzq.jz_app.ui.fragment.chart.LineChartInViewPager;
import com.wzq.jz_app.ui.fragment.chart.NewMarkerView;
import com.wzq.jz_app.ui.fragment.chart.RealListEntity;
import com.wzq.jz_app.ui.fragment.chart.YoyListEntity;
import com.wzq.jz_app.utils.DateUtils;
import com.wzq.jz_app.utils.PieChartUtils;
import com.wzq.jz_app.utils.SnackbarUtils;
import com.wzq.jz_app.utils.StringUtils;
import com.wzq.jz_app.widget.CircleImageView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import me.drakeet.multitype.MultiTypeAdapter;

public class ChartFragment extends BaseMVPFragment<MonthChartContract.Presenter>
        implements MonthChartContract.View, OnChartValueSelectedListener, View.OnClickListener {

    private PieChart mChart;
    private TextView centerTitle;
    private TextView centerMoney;
    private MyUser currentUser;
    private LinearLayout layoutCenter;
    private ImageView centerImg;
    private CircleImageView circleBg;
    private ImageView circleImg;
    private RelativeLayout layoutCircle;
    private TextView title;
    private TextView money;
    private TextView rankTitle;
    private RelativeLayout layoutOther;
    private TextView otherMoney;
    private SwipeRefreshLayout swipe;
    private RelativeLayout itemType;
    private RelativeLayout itemOther;
//    private RecyclerView rvList;
    private LinearLayout layoutTypedata;
    private
    ArrayList<Integer> colors = new ArrayList<>();

    private boolean TYPE = true;//默认总收入true
    private List<MonthChartBean.SortTypeList> tMoneyBeanList;
    private String sort_name;
    private String back_color;
    private String advice;

    private MonthChartBean monthChartBean;
    private MonthChartBean yearDateBean;

    private MultiTypeAdapter adapter;

    private String setYear = DateUtils.getCurYear(DateUtils.FORMAT_Y);
    private String setMonth = DateUtils.getCurMonth(DateUtils.FORMAT_M);
    private Toolbar toolbar;
    private TextView chart_title;
    private ImageView search_date;
    private TextView chart_date;
    private LineChartInViewPager lineChart;
    private List<RealListEntity> realList;
    private List<YoyListEntity> yoyList;
    private PieChart mainPieChart;

    private List<Entry> values1, values2, dottedLineData, solidLineData;
    private List<String> dataList;
    private RealListEntity realListEntity;
    private YoyListEntity yoyListEntity;
    private DecimalFormat mFormat;
    private float maxData = 0;
    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
    private PieChart mPieChart;
    private TextView chart_date_year;
    float maxIn = 0;
    float maxOut = 0;
    int timeIn;
    int timeOut;
    float endIn;
    float endOut;

    //选择时间
    protected int mYear;
    protected int mMonth;
    protected int mDay;
    protected String days;
    private Button startTime;
    private Button endTime;
    private Button look;
    private int status = 0;

    /*****************************************************************************/


    /*****************************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.main_fragment_chart;
    }


    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        chart_title = getViewById(R.id.chart_title);
//        search_date = getViewById(R.id.toolbar_date1);
//        chart_date = getViewById(R.id.date_chart);
        chart_date_year = getViewById(R.id.date_chart_year);


        //设置日期选择器初始日期
        mYear = Integer.parseInt(DateUtils.getCurYear(DateUtils.FORMAT_Y));
        mMonth = Integer.parseInt(DateUtils.getCurMonth(DateUtils.FORMAT_M));
        mDay = Integer.parseInt(DateUtils.getCurDay(DateUtils.FORMAT_D));
//
        adapter = new MultiTypeAdapter();
        adapter.register(BBill.class, new MonthChartBillViewBinder(mContext));
//        rvList.setAdapter(adapter);
        test(setYear);

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initClick() {
        super.initClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.getMonthChart(MyApplication.getCurrentUserId(), setYear, setMonth, "1");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        int entryIndex = (int) h.getX();
        setNoteData(entryIndex, e.getY());
    }


    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    //光滑曲线图

    /**
     * 下面json转实体类
     */
    public void test(String year) {
        try {
            List<BBill> bBills = LocalRepository.getInstance().getBBillByUserIdWithYear(MyApplication.getCurrentUserId(), year);
            float totalMoneyIn1 = 0, totalMoneyOut1 = 0;
            float totalMoneyIn2 = 0, totalMoneyOut2 = 0;
            float totalMoneyIn3 = 0, totalMoneyOut3 = 0;
            float totalMoneyIn4 = 0, totalMoneyOut4 = 0;
            float totalMoneyIn5 = 0, totalMoneyOut5 = 0;
            float totalMoneyIn6 = 0, totalMoneyOut6 = 0;
            float totalMoneyIn7 = 0, totalMoneyOut7 = 0;
            float totalMoneyIn8 = 0, totalMoneyOut8 = 0;
            float totalMoneyIn9 = 0, totalMoneyOut9 = 0;
            float totalMoneyIn10 = 0, totalMoneyOut10 = 0;
            float totalMoneyIn11 = 0, totalMoneyOut11 = 0;
            float totalMoneyIn12 = 0, totalMoneyOut12 = 0;
            float[] totalMoneyIn = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            float[] totalMoneyOut = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            for (int j = 0; j < bBills.size(); j++) {
                String date = String.valueOf(DateUtils.getDay(bBills.get(j).getCrdate()));
                String date1 = date.substring(5, 7);
                switch (date1) {
                    case "01":
                        if (bBills.get(j).getIncome()) {
                            totalMoneyIn1 += bBills.get(j).getCost();
                        } else {
                            totalMoneyOut1 += bBills.get(j).getCost();
                        }
                        break;
                    case "02":
                        if (bBills.get(j).getIncome()) {
                            totalMoneyIn2 += bBills.get(j).getCost();
                        } else {
                            totalMoneyOut2 += bBills.get(j).getCost();
                        }
                        break;
                    case "03":
                        if (bBills.get(j).getIncome()) {
                            totalMoneyIn3 += bBills.get(j).getCost();
                        } else {
                            totalMoneyOut3 += bBills.get(j).getCost();
                        }
                        break;
                    case "04":
                        if (bBills.get(j).getIncome()) {
                            float in = bBills.get(j).getCost();
                            totalMoneyIn4 += in;
                        } else {
                            float out = bBills.get(j).getCost();
                            totalMoneyOut4 += out;
                        }
                        break;
                    case "05":
                        if (bBills.get(j).getIncome()) {
                            totalMoneyIn5 += bBills.get(j).getCost();
                        } else {
                            totalMoneyOut5 += bBills.get(j).getCost();
                        }
                        break;
                    case "06":
                        if (bBills.get(j).getIncome()) {
                            totalMoneyIn6 += bBills.get(j).getCost();
                        } else {
                            totalMoneyOut6 += bBills.get(j).getCost();
                        }
                        break;
                    case "07":
                        if (bBills.get(j).getIncome()) {
                            totalMoneyIn7 += bBills.get(j).getCost();
                        } else {
                            totalMoneyOut7 += bBills.get(j).getCost();
                        }
                        break;
                    case "08":
                        if (bBills.get(j).getIncome()) {
                            totalMoneyIn8 += bBills.get(j).getCost();
                        } else {
                            totalMoneyOut8 += bBills.get(j).getCost();
                        }
                        break;
                    case "09":
                        if (bBills.get(j).getIncome()) {
                            totalMoneyIn9 += bBills.get(j).getCost();
                        } else {
                            totalMoneyOut9 += bBills.get(j).getCost();
                        }
                        break;
                    case "10":
                        if (bBills.get(j).getIncome()) {
                            totalMoneyIn10 += bBills.get(j).getCost();
                        } else {
                            totalMoneyOut10 = +bBills.get(j).getCost();
                        }
                        break;
                    case "11":
                        if (bBills.get(j).getIncome()) {
                            totalMoneyIn11 = +bBills.get(j).getCost();
                        } else {
                            totalMoneyOut11 += bBills.get(j).getCost();
                        }
                        break;
                    case "12":
                        if (bBills.get(j).getIncome()) {
                            totalMoneyIn12 += bBills.get(j).getCost();
                        } else {
                            totalMoneyOut12 += bBills.get(j).getCost();
                        }
                        break;
                }
            }
            totalMoneyIn = new float[]{totalMoneyIn1, totalMoneyIn2, totalMoneyIn3, totalMoneyIn4, totalMoneyIn5, totalMoneyIn6, totalMoneyIn7, totalMoneyIn8, totalMoneyIn9, totalMoneyIn10, totalMoneyIn11, totalMoneyIn12};
            totalMoneyOut = new float[]{totalMoneyOut1, totalMoneyOut2, totalMoneyOut3, totalMoneyOut4, totalMoneyOut5,
                    totalMoneyOut6, totalMoneyOut7, totalMoneyOut8, totalMoneyOut9, totalMoneyOut10, totalMoneyOut11, totalMoneyOut12};
            realList = new ArrayList<>();
            yoyList = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                RealListEntity realListEntity = new RealListEntity();
                YoyListEntity yoyListEntity = new YoyListEntity();
                realListEntity.setAmount(totalMoneyIn[i] + "");//收入
                realListEntity.setMonth((i + 1) + "");
                realListEntity.setYear("收入");
                realList.add(realListEntity);

                yoyListEntity.setAmount(totalMoneyOut[i] + "");//支出
                yoyListEntity.setMonth((i + 1) + "");
                yoyListEntity.setYear("支出");
                yoyList.add(yoyListEntity);


                //分析年度消费最高、收入最高
                endIn += totalMoneyIn[i];
                endOut += totalMoneyOut[i];
                if (totalMoneyIn[i] > maxIn) {   // 判断收入最大值
                    maxIn = totalMoneyIn[i];
                    timeIn = i + 1;
                }
                if (totalMoneyOut[i] > maxOut) {
                    maxOut = totalMoneyOut[i];
                    timeOut = i + 1;
                }
            }
            if (endIn == 0 && endOut == 0) {
                advice = "您当前没有账单记录！请开始使用APP记账吧！";
                //年度分析
                TextView textView = getViewById(R.id.sys);
                textView.setText("分析：" + advice);
            } else {
                if (endOut > endIn)
                    advice = "收支不平衡【入不敷出】,建议您省吃俭用，少花钱！";
                else if (endIn == endOut) {
                    advice = "收支平衡【没有存款】，建议您合理消费";
                } else {
                    advice = "收支不平衡【您有存款了】，建议您享受生活，适当理财";
                }
                //年度分析
                TextView textView = getViewById(R.id.sys);
                textView.setText("分析：当前年度中" + timeIn + "月收入最高，" + timeOut + "月消费最高!" + advice);
            }
            initViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initViews() {


        lineChart = getViewById(R.id.new_lineChart);
        mFormat = new DecimalFormat("#,###.##");
        values1 = new ArrayList<>();
        values2 = new ArrayList<>();
        for (int i = 0; i < yoyList.size(); i++) {
            yoyListEntity = yoyList.get(i);
            String amount = yoyListEntity.getAmount();
            if (amount != null) {
                float f = 0;
                try {
                    f = Float.parseFloat(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                    f = 0;
                }
                Entry entry = new Entry(i + 1, f);
                values1.add(entry);
            }
        }

        for (int i = 0; i < realList.size(); i++) {
            realListEntity = realList.get(i);
            String amount = realListEntity.getAmount();
            if (amount != null) {
                float f = 0;
                try {
                    f = Float.parseFloat(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                    f = 0;
                }
                Entry entry = new Entry(i + 1, f);
                values2.add(entry);
            }
        }
        Drawable[] drawables = {
                ContextCompat.getDrawable(getActivity(), R.drawable.chart_thisyear_blue),
                ContextCompat.getDrawable(getActivity(), R.drawable.chart_callserice_call_casecount)
        };
        int[] callDurationColors = {Color.parseColor("#45A2FF"), Color.parseColor("#5fd1cc")};
        String thisYear = "";
        if (realList.size() > 0) {
            thisYear = realList.get(0).getYear();
        }

        String lastYear = "";
        if (yoyList.size() > 0) {
            lastYear = yoyList.get(0).getYear();
        }
        String[] labels = new String[]{thisYear, lastYear};
        updateLinehart(yoyList, realList, lineChart, callDurationColors, drawables, "", values1, values2, labels);
    }

    /**
     * 双平滑曲线传入数据，添加markview，添加实体类单位
     *
     * @param yoyList
     * @param realList
     * @param lineChart
     * @param colors
     * @param drawables
     * @param unit
     * @param values2
     * @param values1
     * @param labels
     */
    private void updateLinehart(final List<YoyListEntity> yoyList, final List<RealListEntity> realList,
                                LineChart lineChart, int[] colors, Drawable[] drawables,
                                final String unit, List<Entry> values2, List<Entry> values1, final String[] labels) {
        List<Entry>[] entries = new ArrayList[2];
        entries[0] = values1;
        entries[1] = values2;
        LineChartEntity lineChartEntity = new LineChartEntity(lineChart, entries, labels, colors,
                Color.parseColor("#999999"), 12f);
        lineChartEntity.drawCircle(true);
        lineChart.setScaleMinima(1.0f, 1.0f);
        toggleFilled(lineChartEntity, drawables, colors);
        lineChartEntity.setLineMode(LineDataSet.Mode.CUBIC_BEZIER);//曲线
        // lineChartEntity.setLineMode(LineDataSet.Mode.LINEAR);//折线
        lineChartEntity.initLegend(Legend.LegendForm.CIRCLE, 12f,
                Color.parseColor("#999999"));
        lineChartEntity.updateLegendOrientation(Legend.LegendVerticalAlignment.TOP,
                Legend.LegendHorizontalAlignment.RIGHT, Legend.LegendOrientation.HORIZONTAL);
        lineChartEntity.setAxisFormatter(
                new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        if (value == 1.0f) {
                            return mFormat.format(value) + "月";
                        }
                        String monthStr = mFormat.format(value);
                        if (monthStr.contains(".")) {
                            return "";
                        } else {
                            return monthStr;
                        }
//                        return mMonthFormat.format(value);
                    }
                },
                new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return mFormat.format(value) + unit;
                    }
                });

        lineChartEntity.setDataValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return mFormat.format(value) + unit;
            }
        });

    }

    /**
     * 双平滑曲线添加线下的阴影
     *
     * @param lineChartEntity
     * @param drawables
     * @param colors
     */
    private void toggleFilled(LineChartEntity lineChartEntity, Drawable[] drawables, int[] colors) {
        if (android.os.Build.VERSION.SDK_INT >= 18) {

            lineChartEntity.toggleFilled(drawables, null, true);
        } else {
            lineChartEntity.toggleFilled(null, colors, true);
        }
    }


    /**
     * 报表数据
     */
    private void setReportData() {

        if (monthChartBean == null) {
            return;
        }

        float totalMoney;
        if (TYPE) {
            centerTitle.setText("总支出");
            centerImg.setImageResource(R.mipmap.tallybook_output);
            tMoneyBeanList = monthChartBean.getOutSortlist();
            totalMoney = monthChartBean.getTotalOut();
        } else {
            centerTitle.setText("总收入");
            centerImg.setImageResource(R.mipmap.tallybook_input);
            tMoneyBeanList = monthChartBean.getInSortlist();
            totalMoney = monthChartBean.getTotalIn();
        }
        centerMoney.setText("" + totalMoney);

        ArrayList<PieEntry> entries = new ArrayList<>();

        if (tMoneyBeanList != null && tMoneyBeanList.size() > 0) {
            layoutTypedata.setVisibility(View.VISIBLE);
            for (int i = 0; i < tMoneyBeanList.size(); i++) {
                float scale = tMoneyBeanList.get(i).getMoney() / totalMoney;
                float value = (scale < 0.06f) ? 0.06f : scale;
                entries.add(new PieEntry(value, PieChartUtils.getDrawable(tMoneyBeanList.get(i).getSortImg())));
                colors.add(getResources().getColor(R.color.home_1));
                colors.add(getResources().getColor(R.color.home_2));
                colors.add(getResources().getColor(R.color.home_3));
                colors.add(getResources().getColor(R.color.home_5));
                colors.add(getResources().getColor(R.color.home_7));
                colors.add(getResources().getColor(R.color.home_10));
                colors.add(getResources().getColor(R.color.home_11));
                colors.add(getResources().getColor(R.color.home_4));
                colors.add(getResources().getColor(R.color.home_9));
                colors.add(getResources().getColor(R.color.home_8));
                colors.add(getResources().getColor(R.color.home_6));
                colors.add(getResources().getColor(R.color.home_12));
                colors.add(getResources().getColor(R.color.home_13));

            }
//            setNoteData(0, entries.get(0).getY());
        } else {//无数据时的显示
            layoutTypedata.setVisibility(View.GONE);
            entries.add(new PieEntry(1f));
            colors.add(getResources().getColor(R.color.home_3));
        }

        PieChartUtils.setPieChartData(mChart, entries, colors);


    }

    /**
     * 点击饼状图上区域后相应的数据设置
     *
     * @param index
     */
    private void setNoteData(int index, float value) {
        if (null == tMoneyBeanList || tMoneyBeanList.size() == 0)
            return;
//        sort_image = tMoneyBeanList.get(index).getSortImg();
        sort_name = tMoneyBeanList.get(index).getSortName();
        if (TYPE) {
            money.setText("-" + tMoneyBeanList.get(index).getMoney());
        } else {
            money.setText("+" + tMoneyBeanList.get(index).getMoney());
        }
        DecimalFormat df = new DecimalFormat("0.00%");
        title.setText(sort_name+" : "+df.format(value));
        rankTitle.setText(sort_name + "排行榜");
        circleBg.setImageDrawable(new ColorDrawable(colors.get(index)));//背景色
        circleImg.setImageDrawable(PieChartUtils.getDrawable(tMoneyBeanList.get(index).getSortImg()));
        adapter.setItems(tMoneyBeanList.get(index).getList());
        adapter.notifyDataSetChanged();

    }

    /*****************************************************************************/
    @Override
    protected MonthChartContract.Presenter bindPresenter() {
        return new MonthChartPresenter();
    }

    @Override
    public void loadDataSuccess(MonthChartBean bean) {
        monthChartBean = bean;
        setReportData();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(Throwable e) {
        SnackbarUtils.show(mActivity, e.getMessage());
    }



}
