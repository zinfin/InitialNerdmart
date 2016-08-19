package com.bignerdranch.android.initialnerdmart.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.bignerdranch.android.initialnerdmart.BR;
import com.bignerdranch.android.initialnerdmart.R;
import com.bignerdranch.android.nerdmartservice.service.payload.Cart;
import com.bignerdranch.android.nerdmartservice.service.payload.User;

/**
 * Created by sand8529 on 8/16/16.
 */
public class NerdMartViewModel extends BaseObservable {
  private Context mContext;
  private Cart mCart;
  private User mUser;

  public NerdMartViewModel(Context context, Cart cart, User user){
    mCart = cart;
    mContext = context;
    mUser = user;
  }

  public String formatCartItemDisplay(){
    int numItems = 0;
    if (mCart != null && mCart.getProducts() != null){
      numItems = mCart.getProducts().size();
    }
    return mContext.getResources().getQuantityString(R.plurals.cart, numItems, numItems);
  }
  public String getUserGreeting(){
    return mContext.getString(R.string.user_greeting, mUser.getName());
  }
  @Bindable
  public String getCartDisplay(){
    return formatCartItemDisplay();
  }
  public void updateCartStatus(Cart cart){
    mCart = cart;
    notifyPropertyChanged(BR.cartDisplay);
  }
}
