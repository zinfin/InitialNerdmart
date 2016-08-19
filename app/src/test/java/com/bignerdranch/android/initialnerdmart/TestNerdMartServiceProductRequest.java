package com.bignerdranch.android.initialnerdmart;

import com.bignerdranch.android.initialnerdmart.inject.NerdMartMockApplicationModule;
import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.initialnerdmart.model.service.NerdMartServiceManager;
import com.bignerdranch.android.nerdmartservice.model.NerdMartDataSourceInterface;
import com.bignerdranch.android.nerdmartservice.service.payload.Cart;
import com.bignerdranch.android.nerdmartservice.service.payload.Product;
import dagger.Component;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import rx.observers.TestSubscriber;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by sand8529 on 8/16/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants=BuildConfig.class, sdk=21)
public class TestNerdMartServiceProductRequest {
  @Inject
  NerdMartServiceManager mNerdMartServiceManager;

  @Inject
  DataStore mDataStore;

  @Inject
  NerdMartDataSourceInterface mNerdMartDataSoureInterface;

  @Singleton
  @Component(modules= NerdMartMockApplicationModule.class)
  public interface TestNerdMartServiceProductsComponent{
    TestNerdMartServiceProductRequest inject(
        TestNerdMartServiceProductRequest testNerdMartServiceProductRequest);
  }
  @Before
  public void setup(){
    NerdMartMockApplicationModule nerdMartMockApplicationModule
        = new NerdMartMockApplicationModule(RuntimeEnvironment.application);
    DaggerTestNerdMartServiceProductRequest_TestNerdMartServiceProductsComponent
        .builder()
        .nerdMartMockApplicationModule(nerdMartMockApplicationModule)
        .build()
        .inject(this);
    mDataStore.setCachedUser(mNerdMartDataSoureInterface.getUser());
  }
  @Test
  public void testGetProductsReturnsExpectedProductListing(){
    TestSubscriber<List<Product>> subscriber = new TestSubscriber<>();
    mNerdMartServiceManager.getProducts().toList().subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    assertThat(subscriber.getOnNextEvents().get(0).containsAll(mNerdMartDataSoureInterface.getProducts()));
  }

  @Test
  public void testGetCartReturnsCartAndCachesCartInDataStore(){
    TestSubscriber<Cart> subscriber = new TestSubscriber<>();
    mNerdMartServiceManager.getCart().subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    Cart actual = subscriber.getOnNextEvents().get(0);
    assertThat(actual).isNotEqualTo(null);
    assertThat(mDataStore.getCachedCart()).isEqualTo(actual);
    assertThat(mDataStore.getCachedCart().getProducts()).hasSize(0);
  }
  @Test
  public void testPostProductToCartAddsProductsToUserCart(){
    TestSubscriber<Boolean> subscriber = new TestSubscriber<>();
    ArrayList<Product> products = Lists.newArrayList();
    products.addAll(mNerdMartDataSoureInterface.getProducts());
    mNerdMartServiceManager.postProductToCart(products.get(0)).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    assertThat(subscriber.getOnNextEvents().get(0)).isEqualTo(true);
    TestSubscriber<Cart> cartTestSubscriber = new TestSubscriber<>();
    mNerdMartServiceManager.getCart().subscribe(cartTestSubscriber);
    cartTestSubscriber.awaitTerminalEvent();
    Cart cart = cartTestSubscriber.getOnNextEvents().get(0);
    assertThat(cart.getProducts()).hasSize(1);


  }
}
