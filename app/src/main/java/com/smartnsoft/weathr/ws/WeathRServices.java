package com.smartnsoft.weathr.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.Uri;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.smartnsoft.weathr.bo.Weather;

/**
 * @author Ludovic Roland
 * @since 2015.10.30
 */
public final class WeathRServices
{

  private static volatile WeathRServices instance;

  public static WeathRServices getInstance()
  {
    if (instance == null)
    {
      synchronized (WeathRServices.class)
      {
        if (instance == null)
        {
          instance = new WeathRServices();
        }
      }
    }
    return instance;
  }

  private ObjectMapper objectMapper;

  private WeathRServices()
  {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
  }

  public Weather getWeather(String city, int forecastCount)
      throws IOException
  {
    Log.d(WeathRServices.class.getSimpleName(), "Retrieving the weather for the city '" + city + "' for the '" + forecastCount + "' days");

    final Uri.Builder uriBuilder = new Uri.Builder();
    uriBuilder.scheme("https");
    uriBuilder.authority("smartnsoft.com");
    uriBuilder.appendPath("shared");
    uriBuilder.appendPath("weather");
    uriBuilder.appendPath("index.php");
    uriBuilder.appendQueryParameter("city", city);
    uriBuilder.appendQueryParameter("forecasts", String.valueOf(forecastCount));

    final HttpURLConnection urlConnection = (HttpURLConnection) new URL(uriBuilder.build().toString()).openConnection();

    try
    {
      urlConnection.connect();
      final InputStream inputStream = urlConnection.getInputStream();
      return deserializeJson(inputStream, Weather.class);
    }
    finally
    {
      if (urlConnection != null)
      {
        urlConnection.disconnect();
      }
    }
  }

  private <ContentType> ContentType deserializeJson(InputStream inputStream, Class<ContentType> valueType)
      throws IOException
  {
    final String json = getString(inputStream);
    return objectMapper.readValue(json, valueType);
  }

  private String getString(InputStream inputStream)
      throws IOException
  {
    final StringWriter stringWriter = new StringWriter();
    final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
    final BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8192);

    String line;

    while (null != (line = bufferedReader.readLine()))
    {
      stringWriter.write(line);
    }

    return stringWriter.toString();
  }

}
