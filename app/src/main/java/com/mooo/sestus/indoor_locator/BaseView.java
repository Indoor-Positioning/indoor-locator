package com.mooo.sestus.indoor_locator;

import android.support.annotation.StringRes;

public interface BaseView<T> {
    void setPresenter(T presenter);
    void makeToast(@StringRes int stringId);
    void makeToast(String message);
}
