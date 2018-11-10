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

package us.nineworlds.serenity.core.model.impl;

import dagger.Module;
import dagger.Provides;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.plex.rest.model.impl.Video;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.modules.AndroidModule;
import us.nineworlds.serenity.injection.modules.SerenityModule;
import us.nineworlds.serenity.test.InjectingTest;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class MovieMediaContainerTest extends InjectingTest {

  @Mock MediaContainer mockMediaContainer;

  @Mock SerenityClient mockFactory;

  MovieMediaContainer movieMediaContainer;

  List<VideoContentInfo> videos;

  @Override @Before public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    super.setUp();

    videos = new ArrayList<VideoContentInfo>();

    doReturn("http://1.1.1.1:32400/").when(mockFactory).baseURL();

    movieMediaContainer = new MovieMediaContainer(createTestVideos());
  }

  @Test public void createMoviesReturnsAnEmptyListWhenNoItems() {
    movieMediaContainer = new MovieMediaContainer(mockMediaContainer);
    doReturn(new ArrayList<Video>()).when(mockMediaContainer).getVideos();
    List<VideoContentInfo> result = movieMediaContainer.createVideos();
    assertThat(result).isEmpty();
  }

  @Test public void createMovieReturnsAnEmptyListWhenNoVideos() {
    movieMediaContainer = new MovieMediaContainer(mockMediaContainer);
    doReturn(null).when(mockMediaContainer).getVideos();
    List<VideoContentInfo> result = movieMediaContainer.createVideos();
    assertThat(result).isEmpty();
  }

  @Test public void createMoviesReturnsANonEmptyListWhenThereAreVideos() {
    List<VideoContentInfo> result = movieMediaContainer.createVideos();
    assertThat(result).isNotEmpty();
  }

  @Test public void directPlayUrlIsNotNullForAVideo() {
    List<VideoContentInfo> result = movieMediaContainer.createVideos();
    VideoContentInfo videoContentInfo = result.get(0);
    assertThat(videoContentInfo.getDirectPlayUrl()).isNotNull().isNotEmpty();
  }

  @Override public List<Object> getModules() {
    List<Object> modules = new ArrayList<Object>();
    modules.add(new AndroidModule(application));
    modules.add(new TestModule());
    return modules;
  }

  protected MediaContainer createTestVideos() throws Exception {
    InputStream inputstream = this.getClass().getResourceAsStream("/resources/samples/allmovies.xml");
    Serializer serializer = new Persister();

    MediaContainer mediaContainer = serializer.read(MediaContainer.class, inputstream, false);
    IOUtils.closeQuietly(inputstream);
    return mediaContainer;
  }

  @Module(includes = SerenityModule.class, addsTo = AndroidModule.class, overrides = true, injects = {
      MovieMediaContainerTest.class, MovieMediaContainer.class
  })
  public class TestModule {

    @Provides @Singleton SerenityClient providesPlexFactory() {
      return mockFactory;
    }
  }
}
