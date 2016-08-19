package com.bignerdranch.android.initialnerdmart.model.service;

import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface;
import com.bignerdranch.android.nerdmartservice.service.payload.Cart;
import com.bignerdranch.android.nerdmartservice.service.payload.Product;
import com.bignerdranch.android.nerdmartservice.service.payload.User;
import rx.Observable;
import rx.Observable.Transformer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.UUID;

/**
 * Created by sand8529 on 8/16/16.
 */
public class NerdMartServiceManager {
  private Scheduler mScheduler;
  private NerdMartServiceInterface mServiceInterface;
  private DataStore mDataStore;

  public NerdMartServiceManager (NerdMartServiceInterface serviceInterface,
      DataStore dataStore, Scheduler scheduler){
    mServiceInterface = serviceInterface;
    mDataStore = dataStore;
    mScheduler = scheduler;
  }

  private final Observable.Transformer<Observable, Observable>
    mSchedulersTransformer = observable ->
      observable.subscribeOn(Schedulers.newThread())
      .observeOn(mScheduler);

  @SuppressWarnings("unchecked")
  private <T> Observable.Transformer<T,T> applySchedulers(){
    return (Observable.Transformer<T,T>) mSchedulersTransformer;
  }

  public Observable<User> authenticate (String username, String password){
    return mServiceInterface.authenticate(username, password)
        .doOnNext(mDataStore::setCachedUser)
        .compose(applySchedulers());
  }
  private Observable<UUID> getToken(){
    return Observable.just(mDataStore.getCachedAuthToken());
  }

  public Observable<Product> getProducts(){
    return getToken().flatMap(mServiceInterface::requestProducts)
        .doOnNext(mDataStore::setCachedProduces)
        .flatMap(Observable::from)
        .compose(applySchedulers());
  }
  public Observable<Cart> getCart(){
    return getToken().flatMap(mServiceInterface::fetchUserCart)
        .doOnNext(mDataStore::setCachedCart)
        .compose(applySchedulers());
  }
  public Observable<Boolean> postProductToCart(final Product product){
    return getToken().flatMap(uuid -> mServiceInterface.addProductToCart(uuid, product))
        .compose(applySchedulers());
  }
  public Observable<Boolean> signOut(){
    mDataStore.clearCache();
    return mServiceInterface.signout();
  }
}
