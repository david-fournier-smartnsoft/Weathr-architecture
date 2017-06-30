package com.smartnsoft.weathr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartnsoft.weathr.R;
import com.smartnsoft.weathr.adapter.WeatherAdapter.WeatherSimpleViewHolder;
import com.smartnsoft.weathr.bo.Weather.Forecast;

/**
 * @author Ludovic Roland
 * @since 2015.11.18
 */
public final class DetailFragment
    extends Fragment
{

  private Bundle weatherArguments;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

    final Forecast forecast = (Forecast) weatherArguments.getSerializable(HomeFragment.BUSINESS_OBJECT_EXTRA);
    final int backgroundColor = weatherArguments.getInt(HomeFragment.BACKGROUND_COLOR_EXTRA, ContextCompat.getColor(getContext(), R.color.pink));

    final WeatherSimpleViewHolder weatherViewHolder = new WeatherSimpleViewHolder(rootView);
    weatherViewHolder.update(forecast, backgroundColor, null);

    final TextView tempMin = (TextView) rootView.findViewById(R.id.tempMin);
    tempMin.setText(getString(R.string.weather_temperature, forecast.temperatureMin));

    final TextView tempMax = (TextView) rootView.findViewById(R.id.tempMax);
    tempMax.setText(getString(R.string.weather_temperature, forecast.temperatureMax));

    final TextView uv = (TextView) rootView.findViewById(R.id.uv);
    uv.setText(String.valueOf(forecast.uvIndex));

    final TextView wind = (TextView) rootView.findViewById(R.id.wind);
    wind.setText(getString(R.string.weather_wind, forecast.windDirection.toString(), forecast.windSpeed));

    return rootView;
  }

  public void setWeatherArguments(Bundle weatherArguments)
  {
    this.weatherArguments = weatherArguments;
  }

}
