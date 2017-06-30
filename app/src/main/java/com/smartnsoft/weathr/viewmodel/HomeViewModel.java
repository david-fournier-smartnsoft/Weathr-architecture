package com.smartnsoft.weathr.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.smartnsoft.weathr.bo.Weather;
import com.smartnsoft.weathr.service.WeatherService;
import com.smartnsoft.weathr.service.WeatherService.WeatherServiceCallback;

/**
 * The class description here.
 *
 * @author David Fournier
 * @since 2017.05.31
 */

public class HomeViewModel
    extends ViewModel
{

  private MutableLiveData<Weather> weather;

  private Exception exception;

  private String city;

  private int nbDays;

  public LiveData<Weather> getWeather(final Context context, String city, int nbDays, boolean forceRefresh)
  {
    if (forceRefresh || !city.equals(this.city) || this.nbDays != nbDays)
    {
      if (weather == null)
      {
        weather = new MutableLiveData<>();
      }
      this.city = city;
      this.nbDays = nbDays;
      loadWeather(context, forceRefresh);
    }
    return weather;
  }

  public LiveData<Weather> getWeather(final Context context, String city, int nbDays)
  {
    return getWeather(context, city, nbDays, false);
  }

  private void loadWeather(final Context context, final boolean forceRefresh)
  {
    new WeatherService(context)
        .city(city)
        .nbDays(nbDays)
        .forceRefresh(forceRefresh)
        .executeAsync(new WeatherServiceCallback() {
      @Override
      public void onError(Exception exception)
      {
        HomeViewModel.this.exception = exception;
        HomeViewModel.this.weather.setValue(null);
      }

      @Override
      public void onSuccess(Weather weather)
      {
        HomeViewModel.this.weather.setValue(weather);
      }
    });
  }

  public Exception getException()
  {
    return exception;
  }
}
