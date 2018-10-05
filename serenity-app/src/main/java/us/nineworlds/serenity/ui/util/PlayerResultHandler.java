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

package us.nineworlds.serenity.ui.util;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.UpdateProgressRequest;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;
import us.nineworlds.serenity.injection.BaseInjector;

public class PlayerResultHandler extends BaseInjector {

  protected RecyclerView.Adapter adapter;
  protected Intent data;

  public PlayerResultHandler(Intent data, RecyclerView.Adapter adapter) {
    super();
    this.adapter = adapter;
    this.data = data;
  }

  public void updateVideoPlaybackPosition(VideoContentInfo video, View selectedView) {
    if (selectedView == null || video == null) {
      return;
    }

    View watchedView = selectedView.findViewById(R.id.posterWatchedIndicator);

    updateProgress(data, video);
    if (video.isWatched()) {
      watchedView.setVisibility(View.VISIBLE);
      toggleWatched(video);
    } else if (video.isPartiallyWatched()) {
      if (watchedView.isShown()) {
        watchedView.setVisibility(View.INVISIBLE);
      }
      ImageUtils.toggleProgressIndicator(selectedView, video.getResumeOffset(),
          video.getDuration());
    }
    adapter.notifyDataSetChanged();
  }

  public void updateVideoPlaybackPosition(VideoContentInfo video) {
    updateProgress(data, video);
    if (video.isWatched()) {
      toggleWatched(video);
    }
  }

  protected void toggleWatched(VideoContentInfo video) {
    if (video.isWatched()) {
      new WatchedVideoAsyncTask().execute(video.id());
      video.setResumeOffset(0);
      video.setViewCount(video.getViewCount() + 1);
    }
  }

  /**
   * @param data
   * @param video
   */
  protected void updateProgress(Intent data, VideoContentInfo video) {
    long position = 0;
    position = data.getIntExtra("position", 0);

    UpdateProgressRequest request = new UpdateProgressRequest(position, video);
    video.setResumeOffset(Long.valueOf(position).intValue());

    request.execute();
  }
}
