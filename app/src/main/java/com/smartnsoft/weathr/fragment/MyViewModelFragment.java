package com.smartnsoft.weathr.fragment;

import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartnsoft.weathr.R;

/**
 * The class description here.
 *
 * @author David Fournier
 * @since 2017.06.20
 */

public class MyViewModelFragment
    extends LifecycleFragment
    implements LifecycleObserver
{

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
    getLifecycle().addObserver(this);
    return rootView;
  }

  @OnLifecycleEvent(Event.ON_RESUME)
  public void updateUI()
  {
    // DO UPDATE
  }
}
