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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import java.io.InputStream;
import java.util.Calendar;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.core.OkHttpStack;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SimpleXmlRequestTest {

	MockWebServer webserver;

	@Before
	public void setUp() throws Exception {
		webserver = new MockWebServer();

		MockResponse response = new MockResponse();
		InputStream xmlStream = this.getClass().getResourceAsStream(
				"/resources/index.xml");
		response.setBody(IOUtils.toString(xmlStream));
		webserver.enqueue(response);

		webserver.start();
	}

	@After
	public void tearDown() throws Exception {
		webserver.shutdown();
	}

	@Ignore @Test
	public void assertThatResponseDoesNotReturnResponseError() throws Exception {
		HttpUrl url = webserver.url("/");

		MockSimpleXmlRequest request = new MockSimpleXmlRequest(
				Request.Method.GET, url.toString(), MediaContainer.class,
				new MockSuccessListener(), new MockErrorListener());
		request.setShouldCache(false);
		RequestQueue requestQueu = Volley.newRequestQueue(
				application, new OkHttpStack());
		Request r = requestQueu.add(request);
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime + 3000;
		long currentTime = startTime;
		while (currentTime < endTime) {
			currentTime = Calendar.getInstance().getTimeInMillis();
		}
		assertThat(webserver.getRequestCount()).isGreaterThan(0);
	}

	@Test
	public void testGetHeadersIsNotEmpty() throws Exception {
		HttpUrl url = webserver.url("/");
		MockSimpleXmlRequest request = new MockSimpleXmlRequest(
				Request.Method.GET, url.toString(), MediaContainer.class,
				new MockSuccessListener(), new MockErrorListener());
		assertThat(request.getHeaders()).isNotNull();
	}

	protected class MockSuccessListener implements Listener {

		@Override
		public void onResponse(Object response) {
			System.out.println("Success Called");
			assertThat(response).isNotNull().isInstanceOf(MediaContainer.class);
		}
	}

	protected class MockErrorListener implements ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			Assert.fail(error.getMessage());

		}
	}

	public class MockSimpleXmlRequest extends SimpleXmlRequest {

		/**
		 * @param method
		 * @param url
		 * @param clazz
		 * @param listener
		 * @param errorListener
		 */
		public MockSimpleXmlRequest(int method, String url, Class clazz,
				Listener listener, ErrorListener errorListener) {
			super(method, url, clazz, listener, errorListener);
		}

		@Override
		protected Response parseNetworkResponse(NetworkResponse response) {
			Response resp = super.parseNetworkResponse(response);
			assertThat(resp.isSuccess()).isTrue();

			deliverResponse(resp);
			return resp;
		}

	}
}
