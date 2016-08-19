package com.bignerdranch.android.initialnerdmart;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bignerdranch.android.initialnerdmart.model.service.NerdMartServiceManager;
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import javax.inject.Inject;

public abstract class NerdMartAbstractFragment extends Fragment {
    @Inject
    NerdMartServiceManager mNerdMartServiceManager;
    private CompositeSubscription mCompositeSubscription;
    private ProgressDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        NerdMartApplication.component(getContext()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCompositeSubscription = new CompositeSubscription();
        setUpLoadingDialog();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setUpLoadingDialog() {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setIndeterminate(true);
        mDialog.setMessage(getString(R.string.loading_text));
    }
    protected <T>Observable.Transformer<T,T> loadingTransformer(){
        return observable -> observable.doOnSubscribe(mDialog::show)
            .doOnCompleted(()-> {
                if(mDialog !=null && mDialog.isShowing()){
                    mDialog.dismiss();
                }
            });
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        mCompositeSubscription.clear();
    }
    protected void addSubscription(Subscription subscription){
        mCompositeSubscription.add(subscription);
    }
}
