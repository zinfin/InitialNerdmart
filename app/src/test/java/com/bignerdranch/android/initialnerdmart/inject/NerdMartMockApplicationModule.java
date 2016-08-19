package com.bignerdranch.android.initialnerdmart.inject;

import android.content.Context;
import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.initialnerdmart.model.service.NerdMartServiceManager;
import com.bignerdranch.android.nerdmartservice.model.NerdDataSource;
import com.bignerdranch.android.nerdmartservice.model.NerdMartDataSourceInterface;
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by sand8529 on 8/16/16.
 */
@Module(includes = {NerdMartMockServiceModule.class, NerdMartCommonModule.class})
public class NerdMartMockApplicationModule {
  private Context mApplicationContext;

  public NerdMartMockApplicationModule(Context applicationContext){
    mApplicationContext = applicationContext;
  }
  @Provides
  @Singleton
  public NerdMartDataSourceInterface providesNerdMartDataSourceInterface(){
    return new NerdDataSource();
  }
  @Provides
  public Context providesContext(){
    return mApplicationContext;
  }
}
