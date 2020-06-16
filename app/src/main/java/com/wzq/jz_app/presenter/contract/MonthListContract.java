package com.wzq.jz_app.presenter.contract;

import com.wzq.jz_app.base.BaseContract;
import com.wzq.jz_app.model.bean.local.BBill;
import com.wzq.jz_app.model.bean.local.MonthListBean;

public interface MonthListContract extends BaseContract {

    interface View extends BaseContract.BaseView {

        void loadDataSuccess(MonthListBean list);
        void loadDataSuccess1(MonthListBean list);


    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void getMonthList(String id, String year, String month);
        void getMonthList1(String id);

        void deleteBill(Long id);

        void updateBill(BBill bBill);
    }
}
