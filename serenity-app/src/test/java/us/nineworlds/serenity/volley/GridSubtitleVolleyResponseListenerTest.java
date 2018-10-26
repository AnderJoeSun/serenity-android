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

package us.nineworlds.serenity.volley;

import android.view.LayoutInflater;
import android.view.View;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo;
import us.nineworlds.serenity.ui.activity.SerenityDrawerLayoutActivity;
import us.nineworlds.serenity.ui.browser.movie.MovieBrowserActivity;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author dcarver
 * 
 */
@RunWith(RobolectricTestRunner.class)
public class GridSubtitleVolleyResponseListenerTest {

	Serializer serializer;
	SerenityDrawerLayoutActivity movieBrowserActivity;

	@Before
	public void setUp() throws Exception {
		serializer = new Persister();
		Robolectric.getBackgroundThreadScheduler().pause();
		Robolectric.getForegroundThreadScheduler().pause();
		movieBrowserActivity = Robolectric
				.buildActivity(MovieBrowserActivity.class).create().get();
	}

	@After
	public void tearDown() throws Exception {
		if (movieBrowserActivity != null) {
			movieBrowserActivity.finish();
		}
	}

	@Test
	public void testVideoHasSubtitles() throws Exception {
		MoviePosterInfo video = new MoviePosterInfo();
		String data = IOUtils.toString(getClass().getResourceAsStream(
				"/resources/library/metadata/526/patriotSubtitle.xml"));
		MediaContainer mediaContainer = serializer.read(MediaContainer.class,
				data, false);

		LayoutInflater layoutInflator = movieBrowserActivity
				.getLayoutInflater();
		View view = layoutInflator
				.inflate(R.layout.poster_indicator_view, null);

		GridSubtitleVolleyResponseListener gridSubtitleResponseListener = new GridSubtitleVolleyResponseListener(
				video, movieBrowserActivity, view);
		gridSubtitleResponseListener.onResponse(mediaContainer);

		assertThat(video.getAvailableSubtitles()).isNotEmpty();
	}

	@Test
	public void testVideoHasNoSubtitles() throws Exception {
		MoviePosterInfo video = new MoviePosterInfo();
		String data = IOUtils.toString(getClass().getResourceAsStream(
				"/resources/library/metadata/523/nosubtitles.xml"));
		MediaContainer mediaContainer = serializer.read(MediaContainer.class,
				data, false);

		LayoutInflater layoutInflator = movieBrowserActivity
				.getLayoutInflater();
		View view = layoutInflator
				.inflate(R.layout.poster_indicator_view, null);

		GridSubtitleVolleyResponseListener gridSubtitleResponseListener = new GridSubtitleVolleyResponseListener(
				video, movieBrowserActivity, view);
		gridSubtitleResponseListener.onResponse(mediaContainer);

		assertThat(video.getAvailableSubtitles()).isNullOrEmpty();
	}

}
