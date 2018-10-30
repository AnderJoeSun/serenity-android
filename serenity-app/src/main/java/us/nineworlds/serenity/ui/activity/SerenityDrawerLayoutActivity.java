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

package us.nineworlds.serenity.ui.activity;

import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.ui.listeners.SettingsMenuDrawerOnItemClickedListener;
import us.nineworlds.serenity.widgets.DrawerLayout;

public abstract class SerenityDrawerLayoutActivity extends SerenityActivity {

  @BindView(R.id.drawer_layout) protected DrawerLayout drawerLayout;
  @BindView(R.id.left_drawer_list) protected ListView drawerList;
  protected ActionBarDrawerToggle drawerToggle;
  protected ActionBar actionBar;
  @BindView(R.id.left_drawer) protected LinearLayout linearDrawerLayout;
  @BindView(R.id.drawer_settings) Button settingsButton;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    actionBar = getSupportActionBar();
    actionBar.setDisplayUseLogoEnabled(true);
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.card_background)));
  }

  @Override protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    if (drawerToggle != null) {
      drawerToggle.syncState();
    }
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    if (drawerToggle != null) {
      drawerToggle.onConfigurationChanged(newConfig);
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (drawerToggle != null) {
      if (drawerToggle.onOptionsItemSelected(item)) {
        return true;
      }
    }
    return super.onOptionsItemSelected(item);
  }

  protected void initMenuDrawerViews() {
    settingsButton.setOnClickListener(new SettingsMenuDrawerOnItemClickedListener(drawerLayout));
  }
}
