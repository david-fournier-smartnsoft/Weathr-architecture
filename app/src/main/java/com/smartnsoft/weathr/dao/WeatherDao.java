package com.smartnsoft.weathr.dao;

import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smartnsoft.weathr.bo.Weather;
import com.smartnsoft.weathr.bo.Weather.Forecast;

/**
 * The class description here.
 *
 * @author David Fournier
 * @since 2017.06.05
 */

@Dao
public interface WeatherDao
{
  @Query("SELECT * FROM weather WHERE city = :cityName")
  Weather get(String cityName);

  @Query("SELECT * FROM forecast WHERE city = :cityName")
  List<Forecast> getForecasts(String cityName);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void add(Weather weather);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void add(List<Forecast> forecasts);
}
