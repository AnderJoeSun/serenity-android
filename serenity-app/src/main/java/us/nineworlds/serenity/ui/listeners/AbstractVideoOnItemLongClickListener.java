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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.inject.Inject;
import timber.log.Timber;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.menus.DialogMenuItem;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.util.AndroidHelper;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.ui.adapters.AbstractPosterImageGalleryAdapter;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import us.nineworlds.serenity.ui.util.VideoQueueHelper;

/**
 * A listener that handles long press for video content. Includes displaying a
 * dialog for toggling watched and unwatched status as well for possibly playing
 * on the TV.
 *
 * @author dcarver
 */
public abstract class AbstractVideoOnItemLongClickListener extends BaseInjector implements View.OnLongClickListener {

  @Inject protected AndroidHelper androidHelper;
  @Inject @ForVideoQueue protected LinkedList<VideoContentInfo> videoQueue;
  @Inject protected SharedPreferences prefs;
  @Inject protected VideoQueueHelper videoQueueHelper;

  protected Dialog dialog;
  protected Activity context;
  protected VideoContentInfo info;
  protected View vciv;
  protected int position;
  protected AbstractPosterImageGalleryAdapter adapter;


  public AbstractVideoOnItemLongClickListener(AbstractPosterImageGalleryAdapter adapter) {
    this.adapter = adapter;
  }

  public void setPosition(int position) {
    this.position = position;
    Timber.d("Position after setting: " + position);
  }

  protected boolean onItemLongClick() {
    context = (Activity) vciv.getContext();

    Timber.d("Position in OnItemLongClick: " + position);
    info = (VideoContentInfo) adapter.getItem(position);

    dialog = new Dialog(context);
    AlertDialog.Builder builder = new AlertDialog.Builder(
        new ContextThemeWrapper(context, android.R.style.Theme_Holo_Dialog));
    builder.setTitle(context.getString(R.string.video_options));

    ListView modeList = new ListView(context);
    modeList.setSelector(R.drawable.menu_item_selector);
    ArrayList<DialogMenuItem> options = addMenuOptions();

    ArrayAdapter<DialogMenuItem> modeAdapter =
        new ArrayAdapter<DialogMenuItem>(context, R.layout.simple_list_item, R.id.list_item_text,
            options);

    modeList.setAdapter(modeAdapter);
    modeList.setOnItemClickListener(getDialogSelectedListener());

    builder.setView(modeList);
    dialog = builder.create();
    dialog.show();

    return true;
  }

  /**
   * @return
   */
  protected OnItemClickListener getDialogSelectedListener() {
    return new DialogOnItemSelected();
  }

  protected ArrayList<DialogMenuItem> addMenuOptions() {
    ArrayList<DialogMenuItem> options = new ArrayList<DialogMenuItem>();
    options.add(createMenuItemToggleWatchStatus());
    options.add(createMenuItemAddToQueue());

    return options;
  }

  private DialogMenuItem createMenuItemToggleWatchStatus() {
    return createMenuItem(context.getString(R.string.toggle_watched_status), 0);
  }

  private DialogMenuItem createMenuItemAddToQueue() {
    return createMenuItem(context.getString(R.string.add_video_to_queue), 2);
  }

  protected DialogMenuItem createMenuItem(String title, int action) {
    DialogMenuItem menuItem = new DialogMenuItem();
    menuItem.setTitle(title);
    menuItem.setMenuDialogAction(action);
    return menuItem;
  }

  protected void performWatchedToggle() {
    View posterLayout = vciv;
    posterLayout.findViewById(R.id.posterInprogressIndicator).setVisibility(View.INVISIBLE);

    toggleGraphicIndicators(posterLayout);
    info.toggleWatchStatus();
  }

  protected void toggleGraphicIndicators(View posterLayout) {
    if (info.isPartiallyWatched() || info.isUnwatched()) {
      final float percentWatched = info.viewedPercentage();
      if (percentWatched <= 0.90f) {
        ImageInfographicUtils.setWatchedCount(vciv, context, info);
        ImageView view = posterLayout.findViewById(R.id.posterWatchedIndicator);
        view.setImageResource(R.drawable.overlaywatched);
        view.setVisibility(View.VISIBLE);
        return;
      }
    }

    ImageInfographicUtils.setUnwatched(vciv, context, info);
    posterLayout.findViewById(R.id.posterWatchedIndicator).setVisibility(View.INVISIBLE);
  }

  protected class DialogOnItemSelected implements OnItemClickListener {

    @Override
    public void onItemClick(android.widget.AdapterView<?> arg0, View v, int position, long arg3) {

      DialogMenuItem menuItem = (DialogMenuItem) arg0.getItemAtPosition(position);

      switch (menuItem.getMenuDialogAction()) {
        case 0:
          performWatchedToggle();
          break;
        case 2:
          videoQueueHelper.performAddToQueue(info);
          break;
      }
      v.requestFocusFromTouch();
      dialog.dismiss();
    }
  }
}
