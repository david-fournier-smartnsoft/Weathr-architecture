package com.smartnsoft.weathr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.smartnsoft.weathr.fragment.DetailFragment;

/**
 * @author Ludovic Roland
 * @since 2015.11.18
 */
public final class DetailActivity
    extends AppCompatActivity
{

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_detail);
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

  @Override
  public void onAttachFragment(Fragment fragment)
  {
    super.onAttachFragment(fragment);

    if (fragment instanceof DetailFragment)
    {
      ((DetailFragment) fragment).setWeatherArguments(getIntent().getExtras());
    }
  }

}
