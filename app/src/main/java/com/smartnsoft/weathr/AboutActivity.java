package com.smartnsoft.weathr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * @author Ludovic Roland
 * @since 2015.12.08
 */
public final class AboutActivity
    extends AppCompatActivity
{

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_about);
    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setIcon(R.drawable.logo_weathr);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (android.R.id.home == item.getItemId())
    {
      onBackPressed();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

}
