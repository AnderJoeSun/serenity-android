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

package us.nineworlds.serenity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.ui.browser.movie.MovieBrowserActivity;
import us.nineworlds.serenity.ui.browser.tv.TVShowBrowserActivity;
import us.nineworlds.serenity.ui.preferences.LeanbackSettingsActivity;

import static net.ganin.darv.DpadAwareRecyclerView.OnItemClickListener;

public class GalleryOnItemClickListener implements OnItemClickListener {

  private static final String MENU_TYPE_SEARCH = "search";
  private static final String MENU_TYPE_SHOW = "show";
  private static final String MENU_TYPE_MOVIE = "movie";
  private static final String MENU_TYPE_MUSIC = "artist";
  private static final String MENU_TYPE_OPTIONS = "options";
  private static final String MENU_TYPE_MOVIES = "movies";
  private static final String MENU_TYPE_TVSHOWS = "tvshows";

  @Override
  public void onItemClick(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i, long l) {
    MainMenuTextViewAdapter mainMenuTextViewAdapter =
        (MainMenuTextViewAdapter) dpadAwareRecyclerView.getAdapter();
    Activity context = (Activity) dpadAwareRecyclerView.getContext();
    MenuItem menuItem = mainMenuTextViewAdapter.getItemAtPosition(i);
    String librarySection = menuItem.getSection();
    String activityType = menuItem.getType();

    if (MENU_TYPE_SEARCH.equalsIgnoreCase(activityType)) {
      context.onSearchRequested();
      return;
    }

    if (MENU_TYPE_OPTIONS.equalsIgnoreCase(activityType)) {
      context.openOptionsMenu();
      return;
    }

    Intent intent;

    if (MENU_TYPE_MOVIE.equalsIgnoreCase(activityType) || MENU_TYPE_MOVIES.equalsIgnoreCase(activityType)) {
      intent = new Intent(context, MovieBrowserActivity.class);
      intent.putExtra("key", librarySection);
      context.startActivityForResult(intent, 0);
      return;
    }

    if (MENU_TYPE_SHOW.equalsIgnoreCase(activityType) || MENU_TYPE_TVSHOWS.equalsIgnoreCase(activityType)) {
      intent = new Intent(context, TVShowBrowserActivity.class);
      intent.putExtra("key", librarySection);
      context.startActivityForResult(intent, 0);
      return;
    }

    if (MENU_TYPE_MUSIC.equalsIgnoreCase(activityType)) {
      Toast.makeText(context, "Music support has been removed.", Toast.LENGTH_LONG);
      return;
    }

    intent = new Intent(context, LeanbackSettingsActivity.class);
    intent.putExtra("key", librarySection);
    context.startActivityForResult(intent, 0);
  }
}
