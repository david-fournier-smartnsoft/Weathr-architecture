package com.smartnsoft.weathr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.smartnsoft.weathr.R;

/**
 * @author Ludovic Roland
 * @since 2015.12.08
 */
public final class AboutFragment
    extends Fragment
{

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View rootView = inflater.inflate(R.layout.fragment_about, container, false);
    final WebView webView = (WebView) rootView.findViewById(R.id.webview);
    webView.loadUrl("file:///android_asset/about.html");
    return rootView;
  }

}
