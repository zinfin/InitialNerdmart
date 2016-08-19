package com.bignerdranch.android.initialnerdmart.inject;

import com.bignerdranch.android.initialnerdmart.NerdMartAbstractActivity;
import com.bignerdranch.android.initialnerdmart.NerdMartAbstractFragment;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by sand8529 on 8/16/16.
 */
@Singleton
@Component (modules = {NerdMartApplicationModule.class})
public interface NerdMartComponent {
  void inject(NerdMartAbstractFragment fragment);
  void inject(NerdMartAbstractActivity activity);
}
