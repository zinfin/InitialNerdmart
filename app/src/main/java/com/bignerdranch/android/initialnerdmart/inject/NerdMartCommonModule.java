package com.bignerdranch.android.initialnerdmart.inject;

import android.content.Context;
import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.initialnerdmart.viewmodel.NerdMartViewModel;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by sand8529 on 8/16/16.
 */
@Module
public class NerdMartCommonModule {

  @Provides
  NerdMartViewModel  providesNerdMartViewModel(Context context,
      DataStore dataStore){
    return new NerdMartViewModel(context, dataStore.getCachedCart(), dataStore.getCachedUser());
  }
  @Provides
  @Singleton
  DataStore providesDataStore(){
    return new DataStore();
  }
}
