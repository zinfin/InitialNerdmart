package com.bignerdranch.android.initialnerdmart.inject;

import com.bignerdranch.android.initialnerdmart.BuildConfig;
import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.initialnerdmart.model.service.NerdMartServiceManager;
import com.bignerdranch.android.nerdmartservice.model.NerdMartDataSourceInterface;
import com.bignerdranch.android.nerdmartservice.service.payload.User;
import dagger.Component;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import javax.inject.Inject;
import javax.inject.Singleton;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by sand8529 on 8/16/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants= BuildConfig.class, sdk=21)
public class TestNerdMartServiceAuthentication {
  @Inject
  NerdMartServiceManager mNerdMartServiceManager;

  @Inject
  DataStore mDataStore;

  @Inject
  NerdMartDataSourceInterface mNerdMartDataSourceInterface;

  @Singleton
  @Component(modules = NerdMartMockApplicationModule.class)
  public interface TestNerdMartServiceAuthenticationComponent{
    TestNerdMartServiceAuthentication inject(
        TestNerdMartServiceAuthentication testNerdMartServiceManager);
    }

  @Before
  public void setup(){
    NerdMartMockApplicationModule nerdMartMockApplicationModule
        = new NerdMartMockApplicationModule(RuntimeEnvironment.application);
    DaggerTestNerdMartServiceAuthentication_TestNerdMartServiceAuthenticationComponent
        .builder()
        .nerdMartMockApplicationModule(nerdMartMockApplicationModule)
        .build()
        .inject(this);
  }
  @Test
  public void testAuthenticateMethodReturnFalseWithInvalidCredentials(){
    TestSubscriber<User> subscriber = new TestSubscriber<>();
    mNerdMartServiceManager.authenticate("johnnydoe","WRONGPASSWORD").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    assertThat(subscriber.getOnNextEvents().get(0)).isEqualTo(null);
    assertThat(mDataStore.getCachedUser()).isEqualTo(null);
  }

  @Test
  public void testAuthenticateMethodReturnsTrueWithValidCredentials(){
    TestSubscriber<User> subscriber = new TestSubscriber<>();
    mNerdMartServiceManager.authenticate("johnnydoe","pizza").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    assertThat(subscriber.getOnNextEvents().get(0)).isNotNull();
    assertThat(mDataStore.getCachedUser()).isEqualTo(mNerdMartDataSourceInterface.getUser());
  }

  @Test
  public void testSignoutRemovesUserObjects(){
    TestSubscriber<User> subscriber = new TestSubscriber<>();
    mNerdMartServiceManager.authenticate("johnnydoe", "pizza").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    TestSubscriber<Boolean> signoutSubscriber = new TestSubscriber<>();
    mNerdMartServiceManager.signOut().subscribe(signoutSubscriber);
    signoutSubscriber.awaitTerminalEvent();
    assertThat(mDataStore.getCachedUser()).isEqualTo(null);
    assertThat(mDataStore.getCachedCart()).isEqualTo(null);
  }
}
