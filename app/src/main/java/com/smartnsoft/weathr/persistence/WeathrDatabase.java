package com.smartnsoft.weathr.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.smartnsoft.weathr.bo.Weather;
import com.smartnsoft.weathr.bo.Weather.Forecast;
import com.smartnsoft.weathr.dao.WeatherDao;

/**
 * The class description here.
 *
 * @author David Fournier
 * @since 2017.06.05
 */

@Database(entities = {Weather.class, Forecast.class}, version = 2)
public abstract class WeathrDatabase
    extends RoomDatabase
{
  public abstract WeatherDao weatherDao();
}
