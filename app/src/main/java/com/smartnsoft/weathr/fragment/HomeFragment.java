package com.smartnsoft.weathr.fragment;

import java.net.UnknownHostException;

import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smartnsoft.weathr.AboutActivity;
import com.smartnsoft.weathr.Constants;
import com.smartnsoft.weathr.DetailActivity;
import com.smartnsoft.weathr.HomeActivity;
import com.smartnsoft.weathr.R;
import com.smartnsoft.weathr.adapter.WeatherAdapter;
import com.smartnsoft.weathr.adapter.WeatherAdapter.WeatherItemDecoration;
import com.smartnsoft.weathr.bo.Weather;
import com.smartnsoft.weathr.bo.Weather.Forecast;
import com.smartnsoft.weathr.interfaces.WeatherActionInterface;
import com.smartnsoft.weathr.viewmodel.HomeViewModel;

/**
 * @author Ludovic Roland
 * @since 2015.10.19
 */
public final class HomeFragment
    extends LifecycleFragment
    implements OnRefreshListener, OnSharedPreferenceChangeListener, WeatherActionInterface, OnClickListener, LifecycleObserver
{

  public static final String BUSINESS_OBJECT_EXTRA = "businessObjectExtra";

  public static final String BACKGROUND_COLOR_EXTRA = "backgroundColorExtra";

  public static final String CITY_PREFERENCES_KEY = "cityPreferencesKey";

  public static final String FORECASTS_DAYS_PREFRENCES_KEY = "forecastsPreferencesKey";

  public static final String TAG = HomeFragment.class.getSimpleName();

  private SharedPreferences preferences;

  private ProgressBar progressBar;

  private SwipeRefreshLayout swipeRefreshLayout;

  private RecyclerView recyclerView;

  private View errorView;

  private TextView errorMessage;

  private Button errorButton;

  private Adapter adapter;

  private LayoutManager layoutManager;

  private HomeViewModel homeViewModel;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
    setHasOptionsMenu(true);

    preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    preferences.registerOnSharedPreferenceChangeListener(this);

    errorView = rootView.findViewById(R.id.errorView);
    errorMessage = (TextView) rootView.findViewById(R.id.errorMessage);
    errorButton = (Button) rootView.findViewById(R.id.errorButton);
    errorButton.setOnClickListener(this);

    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

    swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
    swipeRefreshLayout.setOnRefreshListener(this);
    swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.colors));

    recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.addItemDecoration(new WeatherItemDecoration((int) getResources().getDimension(R.dimen.weather_item_space)));

    layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.spanCount));
    recyclerView.setLayoutManager(layoutManager);

    homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

    getLifecycle().addObserver(this);

    return rootView;
  }

  @OnLifecycleEvent(Event.ON_RESUME)
  public void updateUI() {
    homeViewModel.getWeather(getContext(), preferences.getString(HomeFragment.CITY_PREFERENCES_KEY, Constants.DEFAULT_CITY),
        preferences.getInt(HomeFragment.FORECASTS_DAYS_PREFRENCES_KEY, Constants.DEFAULT_FORECASTS_DAYS)).observe(HomeFragment.this, new Observer<Weather>()
    {
      @Override
      public void onChanged(@Nullable Weather weather)
      {
        updateUI(weather);
      }
    });
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
  {
    inflater.inflate(R.menu.settings, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == R.id.menu_settings)
    {
      final ParameterDialogFragment parameterDialogFragment = new ParameterDialogFragment();
      parameterDialogFragment.show(getActivity().getFragmentManager(), "parameter");
      return true;
    }

    if (item.getItemId() == R.id.menu_about)
    {
      startActivity(new Intent(getActivity(), AboutActivity.class));
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onClick(View view)
  {
    if (view == errorButton)
    {
      onRefresh();
    }
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();

    preferences.unregisterOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onRefresh()
  {
    homeViewModel.getWeather(getContext(), preferences.getString(HomeFragment.CITY_PREFERENCES_KEY, Constants.DEFAULT_CITY),
        preferences.getInt(HomeFragment.FORECASTS_DAYS_PREFRENCES_KEY, Constants.DEFAULT_FORECASTS_DAYS), true);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
  {
    homeViewModel.getWeather(getContext(), preferences.getString(HomeFragment.CITY_PREFERENCES_KEY, Constants.DEFAULT_CITY),
        preferences.getInt(HomeFragment.FORECASTS_DAYS_PREFRENCES_KEY, Constants.DEFAULT_FORECASTS_DAYS));
  }

  @Override
  public void openWeatherDetails(Forecast forecast, int backgroundColor, Pair<View, String>... sharedElements)
  {
    final Intent intent = new Intent(getActivity(), DetailActivity.class);
    intent.putExtra(HomeFragment.BUSINESS_OBJECT_EXTRA, forecast);
    intent.putExtra(HomeFragment.BACKGROUND_COLOR_EXTRA, backgroundColor);
    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), sharedElements);
    ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
  }

  private void updateUI(Weather weather)
  {
    if (weather != null)
    {
      if (weather.code == 200)
      {
        adapter = new WeatherAdapter(weather.getForecasts(), getResources(), HomeFragment.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);
      }
      else
      {
        //parameter errorMessage
        errorMessage.setText(weather.message);
        errorView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
      }
    }
    else
    {
      errorMessage.setText(homeViewModel.getException() instanceof UnknownHostException ? R.string.connectivity_problem : R.string.unavailable_item);
      errorView.setVisibility(View.VISIBLE);
      recyclerView.setVisibility(View.INVISIBLE);
    }

    progressBar.setVisibility(View.INVISIBLE);
    swipeRefreshLayout.setRefreshing(false);
    swipeRefreshLayout.setEnabled(true);

    final ActionBar supportActionBar = ((HomeActivity) getActivity()).getSupportActionBar();
    supportActionBar.setTitle(preferences.getString(HomeFragment.CITY_PREFERENCES_KEY, Constants.DEFAULT_CITY));
    supportActionBar.setDisplayShowTitleEnabled(true);
  }

}
