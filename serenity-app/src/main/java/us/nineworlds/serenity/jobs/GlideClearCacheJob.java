package us.nineworlds.serenity.jobs;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import us.nineworlds.serenity.GlideApp;

/**
 * Created by dcarver on 7/2/17.
 */

public class GlideClearCacheJob extends Job {

  Context context;

  public GlideClearCacheJob(Context context) {
    super(new Params(500));
    this.context = context;
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    GlideApp.get(context).clearDiskCache();
    GlideApp.get(context).clearMemory();
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount,
      int maxRunCount) {
    return null;
  }
}
