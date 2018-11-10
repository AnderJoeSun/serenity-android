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

package us.nineworlds.serenity.core;

import org.junit.Before;
import org.junit.Test;

import us.nineworlds.serenity.core.model.VideoContentInfo;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrailerYouTubeSearchTest {

	TrailersYouTubeSearch ytsearch;

	@Before
	public void setUp() {
		ytsearch = new TrailersYouTubeSearch();
	}

	@Test
	public void validMovieTrailerSearchURL() {
		VideoContentInfo movie = mock(VideoContentInfo.class);
		when(movie.getTitle()).thenReturn("Die Hard");
		when(movie.getYear()).thenReturn("1988");

		String result = ytsearch.queryURL(movie);
		assertThat(result)
				.startsWith(
						"https://www.googleapis.com/youtube/v3/search?part=id%2Csnippet&maxResults=1&order=relevance&q=");
		assertThat(result).contains("%22Die+Hard%22");
		assertThat(result).contains("1988");
	}

	@Test
	public void validTVShowTrailerSearchURL() {
		VideoContentInfo episode = mock(VideoContentInfo.class);
		when(episode.getTitle()).thenReturn("Currahee");
		when(episode.getSeriesTitle()).thenReturn("Band of Brothers");
		when(episode.getSeason()).thenReturn("Season 1");
		when(episode.getEpisode()).thenReturn("1");
		when(episode.getYear()).thenReturn("2001");

		String result = ytsearch.queryURL(episode);
		assertThat(result)
				.startsWith(
						"https://www.googleapis.com/youtube/v3/search?part=id%2Csnippet&maxResults=1&order=relevance&q=");
		assertThat(result).contains("%22Band+of+Brothers%22");
		assertThat(result).contains("Season+1");
	}
}
