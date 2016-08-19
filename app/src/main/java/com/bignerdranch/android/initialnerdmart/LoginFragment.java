package com.bignerdranch.android.initialnerdmart;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.bignerdranch.android.initialnerdmart.databinding.FragmentLoginBinding;
import com.bignerdranch.android.nerdmartservice.service.payload.User;
import rx.Subscriber;
import rx.functions.Action1;

public class LoginFragment extends NerdMartAbstractFragment {


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
      FragmentLoginBinding fragmentLoginBinding = DataBindingUtil.inflate(inflater,
          R.layout.fragment_login,
          container,
          false);
      fragmentLoginBinding.setLoginButtonClickListener(v -> {
        String username = fragmentLoginBinding.fragmentLoginUsername.getText().toString();
        String password = fragmentLoginBinding.fragmentLoginPassword.getText().toString();
              addSubscription(
                mNerdMartServiceManager
                    .authenticate(username,password)
                    .compose(loadingTransformer())
                    .subscribe(new Action1<User>() {
                      @Override public void call(User authenticated) {
                        if (authenticated!= null && authenticated.getAuthToken()!=null){
                          Toast.makeText(LoginFragment.this.getActivity(), R.string.auth_success_toast, Toast.LENGTH_SHORT)
                              .show();
                          Intent intent = ProductsActivity.newIntent(LoginFragment.this.getActivity());
                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          LoginFragment.this.startActivity(intent);
                          LoginFragment.this.getActivity().finish();
                        }else{
                          Toast.makeText(LoginFragment.this.getActivity(), R.string.auth_failure_toast, Toast.LENGTH_SHORT)
                              .show();
                        }
                      }
                    }));
      });
        return fragmentLoginBinding.getRoot();
    }
}
