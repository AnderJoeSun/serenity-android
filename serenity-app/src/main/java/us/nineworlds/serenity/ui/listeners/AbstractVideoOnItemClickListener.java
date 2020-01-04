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
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.listeners;

import android.app.Activity;
import android.view.View;
import javax.inject.Inject;

import us.nineworlds.serenity.core.model.ContentInfo;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;

/**
 * Common class used by both the Poster Gallery view for itemClicks and the Grid
 * View. It launches video play back when a poster is selected.
 */
public abstract class AbstractVideoOnItemClickListener extends BaseInjector {

  protected VideoContentInfo videoInfo;

  @Inject protected VideoPlayerIntentUtils vpUtils;

  protected AbstractPosterImageGalleryAdapter adapter;

  public AbstractVideoOnItemClickListener() {

  }

  public AbstractVideoOnItemClickListener(AbstractPosterImageGalleryAdapter adapter) {
    super();
    this.adapter = adapter;
  }

  protected void onItemClick(View v) {
    Activity activity = (Activity) v.getContext();
    vpUtils.playVideo(activity, videoInfo, false);
  }

  @Deprecated
  protected abstract VideoContentInfo getVideoInfo(int position);

  public abstract void onItemClick(View v, int position);

  public abstract void onItemClick(View v, ContentInfo item);
}
