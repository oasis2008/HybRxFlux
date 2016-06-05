package com.hardsoftstudio.rxflux.dispatcher;

import com.hardsoftstudio.rxflux.action.RxAction;

/**
 * This interface must be implemented by the store
 * 所有的store必须实现该接口
 */
public interface RxActionDispatch {

    /**
     * store在接收到RxAction时,调用该方法
     *
     * @param action
     */
    void onRxAction(RxAction action);
}
