package com.wzq.jz_app.base;

import android.app.Service;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseService extends Service {

    private CompositeDisposable mDisposable;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    protected void addDisposable(Disposable disposable){
        if (mDisposable == null){
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null){
            mDisposable.dispose();
        }
    }
}
