package com.wzq.jz_app.model.bean.local;

import com.wzq.jz_app.model.bean.BaseBean;

import java.util.List;

public class NoteBean extends BaseBean {

    private List<BSort> outSortlis;
    private List<BSort> inSortlis;


    public List<BSort> getOutSortlis() {
        return outSortlis;
    }

    public void setOutSortlis(List<BSort> outSortlis) {
        this.outSortlis = outSortlis;
    }

    public List<BSort> getInSortlis() {
        return inSortlis;
    }

    public void setInSortlis(List<BSort> inSortlis) {
        this.inSortlis = inSortlis;
    }


}
