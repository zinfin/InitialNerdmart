package com.bignerdranch.android.initialnerdmart.inject;

import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.initialnerdmart.model.service.NerdMartServiceManager;
import com.bignerdranch.android.nerdmartservice.model.NerdMartDataSourceInterface;
import com.bignerdranch.android.nerdmartservice.service.NerdMartService;
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface;
import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

import javax.inject.Singleton;

/**
 * Created by sand8529 on 8/16/16.
 */
@Module
public class NerdMartMockServiceModule {
  @Provides
  @Singleton
  NerdMartServiceManager providesNerdMartServiceManager(
      NerdMartServiceInterface serviceInterface, DataStore dataStore){
    return new NerdMartServiceManager(serviceInterface, dataStore, Schedulers.immediate());
  }
  @Provides
  @Singleton
  NerdMartServiceInterface providesNerdMartServiceInterface(
      NerdMartDataSourceInterface dataSourceInterface){
    return new NerdMartService(dataSourceInterface);
  }

}
