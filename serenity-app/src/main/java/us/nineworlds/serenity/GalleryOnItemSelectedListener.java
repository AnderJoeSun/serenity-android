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
 * <p/>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p/>
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
import android.content.SharedPreferences;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import javax.inject.Inject;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.injection.BaseInjector;

public class GalleryOnItemSelectedListener extends BaseInjector
    implements DpadAwareRecyclerView.OnItemSelectedListener {

  @Inject SharedPreferences preferences;

  @BindView(R.id.mainGalleryBackground) ImageView mainGalleryBackgroundView;

  protected boolean shouldFadeIn() {
    boolean shouldFadein = preferences.getBoolean("animation_background_mainmenu_fadein", true);
    return shouldFadein;
  }

  int getBackgroundImageId(MenuItem menuItem) {
    if ("movie".equals(menuItem.getType()) || "movies".equals(menuItem.getType())) {
      return R.drawable.movies;
    }

    if ("show".equals(menuItem.getType()) || "tvshows".equals(menuItem.getType())) {
      return R.drawable.tvshows;
    }

    if ("artist".equals(menuItem.getType()) || "music".equals(menuItem.getType())) {
      return R.drawable.music;
    }

    if ("settings".equals(menuItem.getType())) {
      return R.drawable.settings;
    }

    if ("options".equals(menuItem.getType())) {
      return R.drawable.settings;
    }

    if ("search".equals(menuItem.getType())) {
      return R.drawable.search;
    }

    return R.drawable.serenity_bonsai_logo;
  }

  @Override
  public void onItemSelected(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i,
      long l) {
    MainMenuTextViewAdapter adapter = (MainMenuTextViewAdapter) dpadAwareRecyclerView.getAdapter();
    MenuItem menuItem = adapter.getItemAtPosition(i);
    Activity context = (Activity) view.getContext();
    if (context.isDestroyed()) {
      return;
    }

    ButterKnife.bind(context);

    mainGalleryBackgroundView = (ImageView) context.findViewById(R.id.mainGalleryBackground);
    mainGalleryBackgroundView.clearAnimation();

    Glide.with(context).load(getBackgroundImageId(menuItem)).into(mainGalleryBackgroundView);

    if (shouldFadeIn()) {
      Animation fadeIn = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in);
      fadeIn.setDuration(500);
      mainGalleryBackgroundView.startAnimation(fadeIn);
    }
  }

  @Override
  public void onItemFocused(DpadAwareRecyclerView dpadAwareRecyclerView, View view, int i, long l) {

  }
}
