package com.smartnsoft.weathr.interfaces;

import android.support.v4.util.Pair;
import android.view.View;

import com.smartnsoft.weathr.bo.Weather.Forecast;

/**
 * @author Jocelyn Girard
 * @since 2015.12.02
 */
public interface WeatherActionInterface
{

  void openWeatherDetails(Forecast forecast, int backgroundColor, Pair<View, String>... sharedElements);

}
