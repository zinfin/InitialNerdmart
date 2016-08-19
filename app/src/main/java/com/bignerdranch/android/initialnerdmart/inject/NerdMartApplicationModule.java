package com.bignerdranch.android.initialnerdmart.inject;

import android.content.Context;
import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.initialnerdmart.model.service.NerdMartServiceManager;
import com.bignerdranch.android.initialnerdmart.viewmodel.NerdMartViewModel;
import com.bignerdranch.android.nerdmartservice.service.NerdMartService;
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by sand8529 on 8/16/16.
 */
@Module(includes = {NerdMartCommonModule.class, NerdMartServiceModule.class})
public class NerdMartApplicationModule {
  private Context mApplicationContext;

  public NerdMartApplicationModule (Context aplicationContext){
    mApplicationContext = aplicationContext;
  }

  @Provides
  public Context proviesContext(){
    return mApplicationContext;
  }

}
