package com.smartnsoft.weathr.bo;

import java.io.Serializable;
import java.util.List;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartnsoft.weathr.bo.Weather.Forecast.Converters;

/**
 * @author Ludovic Roland
 * @since 2015.10.19
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Weather
    implements Serializable
{

  @Entity(primaryKeys = {"city", "date"})
  @TypeConverters({Converters.class})
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static final class Forecast
      implements Serializable
  {

    public static class Converters
    {

      @TypeConverter
      public static Type typeFromJSONValue(String value) {
        return Type.valueOf(value);
      }

      @TypeConverter
      public static String toJSONValue(Type type) {
        return type.name();
      }

      @TypeConverter
      public static WindDirection windDirectionFromJSONValue(String value) {
        return WindDirection.valueOf(value);
      }

      @TypeConverter
      public static String toJSONValue(WindDirection windDirection) {
        return windDirection.name();
      }

    }

    public enum Type
    {
      SUNNY, CLOUDY, RAINY, SNOWY, STORMY
    }

    public enum WindDirection
    {
      N, NW, W, SW, S, SE, E, NE
    }

    @ForeignKey(entity = Weather.class, parentColumns = "city", childColumns = "city")
    public final String city;

    public final String date;

    public final int temperatureMin;

    public final int temperatureMax;

    public final Type type;

    public final int uvIndex;

    public final WindDirection windDirection;

    public final int windSpeed;

    @Ignore
    @JsonCreator
    public Forecast(@JsonProperty("date") String date, @JsonProperty("temperatureMin") int temperatureMin,
        @JsonProperty("temperatureMax") int temperatureMax, @JsonProperty("type") Type type,
        @JsonProperty("uvIndex") int uvIndex, @JsonProperty("windDirection") WindDirection windDirection,
        @JsonProperty("windSpeed") int windSpeed)
    {
      this(date, temperatureMin, temperatureMax, type, uvIndex, windDirection, windSpeed, null);
    }

    public Forecast(String date, int temperatureMin, int temperatureMax, Type type,
        int uvIndex, WindDirection windDirection, int windSpeed, String city)
    {
      this.date = date;
      this.temperatureMin = temperatureMin;
      this.temperatureMax = temperatureMax;
      this.type = type;
      this.uvIndex = uvIndex;
      this.windDirection = windDirection;
      this.windSpeed = windSpeed;
      this.city = city;
    }

    public Forecast(Forecast forecast, String city) {
      this.city = city;
      this.date = forecast.date;
      this.temperatureMin = forecast.temperatureMin;
      this.temperatureMax = forecast.temperatureMax;
      this.type = forecast.type;
      this.uvIndex = forecast.uvIndex;
      this.windDirection = forecast.windDirection;
      this.windSpeed = forecast.windSpeed;
    }

  }

  public final int code;

  public final String message;

  @PrimaryKey
  public final String city;

  @Ignore
  private List<Forecast> forecasts;

  @JsonCreator
  public Weather(@JsonProperty("code") int code, @JsonProperty("message") String message,
      @JsonProperty("city") String city, @JsonProperty("forecasts") List<Forecast> forecasts)
  {
    this(code, message, city);
    this.forecasts = forecasts;
  }

  public Weather(int code, String message, String city) {
    this.code = code;
    this.message = message;
    this.city = city;
  }

  public List<Forecast> getForecasts()
  {
    return forecasts;
  }

  public void setForecasts(List<Forecast> forecasts)
  {
    this.forecasts = forecasts;
  }
}
