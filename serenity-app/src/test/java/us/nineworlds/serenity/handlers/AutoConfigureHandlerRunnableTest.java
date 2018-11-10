/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.handlers;

import android.app.Activity;
import dagger.Module;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import us.nineworlds.plex.server.GDMServer;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.common.Server;
import us.nineworlds.serenity.injection.ForMediaServers;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Ignore
public class AutoConfigureHandlerRunnableTest extends InjectingTest {

  AutoConfigureHandlerRunnable handler;

  @Inject @ForMediaServers Map<String, Server> mediaServer;

  Activity activity;

  @Override @Before public void setUp() throws Exception {
    super.setUp();
    activity = Robolectric.buildActivity(Activity.class).create().get();
    handler = new AutoConfigureHandlerRunnable(activity);
  }

  @After public void tearDown() {
    mediaServer.clear();
    if (activity != null) {
      activity.finish();
    }
  }

  @Test public void noServersSetupToast() {
    handler.run();
    String toastText = ShadowToast.getTextOfLatestToast();
    assertThat(toastText).isNullOrEmpty();
  }

  @Test public void toastMessageIsSetWhenServerIsFoundAndNoneConfigured() {
    Server server = new GDMServer();
    server.setHostName("test");
    server.setIPAddress("10.0.0.1");

    mediaServer.put("testserver", server);

    handler.run();
    assertThat(ShadowToast.getTextOfLatestToast()).isNotNull();
  }

  @Override public List<Object> getModules() {
    List<Object> modules = new ArrayList<Object>();
    modules.add(new AndroidModule(RuntimeEnvironment.application));
    modules.add(new TestModule());
    return modules;
  }

  @Module(addsTo = AndroidModule.class, includes = SerenityModule.class, injects = {
      AutoConfigureHandlerRunnable.class, AutoConfigureHandlerRunnableTest.class
  })
  public class TestModule {

  }
}
