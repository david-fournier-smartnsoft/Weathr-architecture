package com.smartnsoft.weathr.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;

import com.smartnsoft.weathr.WeathrApplication;
import com.smartnsoft.weathr.bo.Weather;
import com.smartnsoft.weathr.bo.Weather.Forecast;
import com.smartnsoft.weathr.dao.BusinessObjectDao;
import com.smartnsoft.weathr.persistence.WeathrDatabase;
import com.smartnsoft.weathr.dao.WeatherDao;
import com.smartnsoft.weathr.ws.WeathRServices;

/**
 * The class description here.
 *
 * @author David Fournier
 * @since 2017.06.08
 */

public class WeatherService
    extends AsyncTask<Void, Void, Weather>
{

  public interface WeatherServiceCallback
  {

    void onError(Exception exception);

    void onSuccess(Weather weather);
  }

  private Context context;

  private String city;

  private int nbDays;

  private boolean forceRefresh;

  private WeatherServiceCallback callback;

  private Exception exception;

  public WeatherService(Context context)
  {
    this.context = context;
  }

  public WeatherService city(String city)
  {
    this.city = city;
    return this;
  }

  public WeatherService nbDays(int nbDays)
  {
    this.nbDays = nbDays;
    return this;
  }

  public WeatherService forceRefresh(boolean forceRefresh)
  {
    this.forceRefresh = forceRefresh;
    return this;
  }

  public void executeAsync(WeatherServiceCallback callback)
  {
    this.callback = callback;
    if (city == null)
    {
      throw new IllegalStateException("You should give a city using the city(String) method");
    }
    if (nbDays == 0)
    {
      throw new IllegalStateException("You should give a city using the city(String) method");
    }
    execute();
  }

  @Override
  protected Weather doInBackground(Void... params)
  {
    final WeatherDao weatherDao = WeathrApplication.getDatabase().weatherDao();
    final Weather dataBaseWeather = weatherDao.get(city);
    try
    {
      if (dataBaseWeather != null && !forceRefresh)
      {
        final List<Forecast> forecasts = weatherDao.getForecasts(city);
        dataBaseWeather.setForecasts(forecasts);
        return dataBaseWeather;
      }
      else
      {
        return WeathRServices.getInstance().getWeather(city, nbDays);
      }
    }
    catch (IOException exception)
    {
      this.exception = exception;
      return dataBaseWeather;
    }
  }

  @Override
  protected void onPostExecute(final Weather result)
  {
    if (callback != null)
    {
      if (exception != null)
      {
        callback.onError(exception);
      }
      else
      {
        callback.onSuccess(result);
      }
    }
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        final WeatherDao weathrDao = Room.databaseBuilder(context, WeathrDatabase.class, "weathr").build().weatherDao();
        weathrDao.add(result);
        List<Forecast> forecastsWithCity = new ArrayList<>();
        final List<Forecast> forecasts = result.getForecasts();
        if (forecasts != null)
        {
          for (Forecast forecast : forecasts)
          {
            forecastsWithCity.add(new Forecast(forecast, city));
          }
          weathrDao.add(forecastsWithCity);
        }
      }
    }).start();
  }

}
