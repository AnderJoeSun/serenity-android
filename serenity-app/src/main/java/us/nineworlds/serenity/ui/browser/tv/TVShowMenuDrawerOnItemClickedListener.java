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

package us.nineworlds.serenity.ui.browser.tv;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import app.com.tvrecyclerview.TvRecyclerView;
import javax.inject.Inject;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.ui.activity.SerenityMultiViewVideoActivity;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import us.nineworlds.serenity.widgets.DrawerLayout;

public class TVShowMenuDrawerOnItemClickedListener extends BaseInjector
    implements OnItemClickListener {
  private static final int GRID_VIEW = 0;
  private static final int DETAIL_VIEW = 1;
  private static final int PLAY_ALL_QUEUE = 2;
  private final DrawerLayout menuDrawer;

  @Inject VideoPlayerIntentUtils vpUtils;

  public TVShowMenuDrawerOnItemClickedListener(DrawerLayout drawer) {
    super();
    menuDrawer = drawer;
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    SerenityMultiViewVideoActivity activity = getActivity(view.getContext());

    switch (position) {
      case GRID_VIEW:
        activity.setContentView(R.layout.activity_tvbrowser_show_gridview_posters);
        toggleView(activity, true);
        break;
      case DETAIL_VIEW:
        if (activity.isPosterLayoutActive()) {
          activity.setContentView(R.layout.activity_tvbrowser_show_posters);
        } else {
          activity.setContentView(R.layout.activity_tvbrowser_show_banners);
        }
        toggleView(activity, false);
        break;
      case PLAY_ALL_QUEUE:
        menuDrawer.closeDrawers();
        if (!activity.getPackageManager().hasSystemFeature("android.hardware.touchscreen")) {
          parent.setVisibility(View.INVISIBLE);
        }
        vpUtils.playAllFromQueue(activity);
        return;
    }
    menuDrawer.closeDrawers();
    activity.recreate();
  }

  protected void toggleView(SerenityMultiViewVideoActivity activity, boolean enableGridView) {
    TvRecyclerView recyclerView = activity.findViewById(R.id.tvShowRecyclerView);
    recyclerView.setVisibility(View.GONE);
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    Editor e = prefs.edit();
    activity.setGridViewEnabled(enableGridView);
    e.putBoolean("series_layout_grid", enableGridView);
    e.apply();
  }

  protected SerenityMultiViewVideoActivity getActivity(Context contextWrapper) {
    Context context = contextWrapper;
    while (context instanceof ContextWrapper) {
      if (context instanceof SerenityMultiViewVideoActivity) {
        return (SerenityMultiViewVideoActivity) context;
      }
      context = ((ContextWrapper) context).getBaseContext();
    }
    return null;
  }
}
