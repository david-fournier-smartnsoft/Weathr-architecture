package com.smartnsoft.weathr.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartnsoft.weathr.R;
import com.smartnsoft.weathr.adapter.WeatherAdapter.WeatherSimpleViewHolder;
import com.smartnsoft.weathr.bo.Weather.Forecast;
import com.smartnsoft.weathr.fragment.HomeFragment;
import com.smartnsoft.weathr.interfaces.WeatherActionInterface;

/**
 * The class description here.
 *
 * @author David Fournier
 * @since 2017.06.06
 */

public final class WeatherAdapter
    extends Adapter<WeatherSimpleViewHolder>
{

  public static final class WeatherItemDecoration
      extends RecyclerView.ItemDecoration
  {

    private final int spaceElementsDimension;

    private int gridSpan = 0;

    public WeatherItemDecoration(int spaceElementsDimension)
    {
      this.spaceElementsDimension = spaceElementsDimension;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state)
    {
      if (gridSpan == 0)
      {
        final LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
        {
          gridSpan = ((GridLayoutManager) layoutManager).getSpanCount();
        }
        else
        {
          gridSpan = 1;
        }
      }
      final int position = parent.getChildAdapterPosition(view);
      if (position < gridSpan)
      {
        outRect.top = spaceElementsDimension;
      }
      if (position % gridSpan == 0)
      {
        outRect.left = spaceElementsDimension;
      }
      outRect.right = spaceElementsDimension;
      outRect.bottom = spaceElementsDimension;
    }
  }

  public static final class WeatherSimpleViewHolder
      extends ViewHolder
  {

    private final TypedArray forecastImagesResources;

    private final SimpleDateFormat dateFormat;

    private final CardView cardView;

    private final ImageView image;

    private final TextView date;

    private final TextView temperature;

    public WeatherSimpleViewHolder(View itemView)
    {
      super(itemView);

      dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
      forecastImagesResources = itemView.getResources().obtainTypedArray(R.array.forecastImages);
      date = (TextView) itemView.findViewById(R.id.date);
      image = (ImageView) itemView.findViewById(R.id.image);
      temperature = (TextView) itemView.findViewById(R.id.temperature);
      cardView = (CardView) itemView.findViewById(R.id.cardView);
    }

    public void update(final Forecast forecast, final int backgroundColor,
        final WeatherActionInterface weatherDetailsInterface)
    {
      try
      {
        final long dateInMilliseconds = dateFormat.parse(forecast.date).getTime();
        date.setText(DateUtils.formatDateTime(date.getContext(), dateInMilliseconds, DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR));
      }
      catch (ParseException exception)
      {
        Log.w(HomeFragment.TAG, "Unable to parse the forecast date '" + forecast.date + "'!");
        date.setText(forecast.date);
      }

      cardView.setCardBackgroundColor(backgroundColor);
      image.setImageDrawable(forecastImagesResources.getDrawable(forecast.type.ordinal()));
      temperature.setText(temperature.getResources().getString(R.string.weather_temperature, forecast.temperatureMax));

      if (weatherDetailsInterface != null)
      {
        itemView.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View view)
          {
            final Pair<View, String> transition1 = Pair.create(itemView, "weather_card");
            weatherDetailsInterface.openWeatherDetails(forecast, backgroundColor, transition1);
          }
        });
      }
    }

  }

  private final List<Forecast> forecasts;

  private final int[] backgroundColors;

  private final WeatherActionInterface weatherDetailsInterface;

  public WeatherAdapter(List<Forecast> forecasts, Resources resources, WeatherActionInterface weatherDetailsInterface)
  {
    if (forecasts != null)
    {
      this.forecasts = forecasts;
    }
    else
    {
      this.forecasts = new ArrayList<>();
    }
    this.backgroundColors = resources.getIntArray(R.array.colors);
    this.weatherDetailsInterface = weatherDetailsInterface;
  }

  @Override
  public WeatherSimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
    return new WeatherSimpleViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(WeatherSimpleViewHolder holder, int position)
  {
    holder.update(forecasts.get(position), backgroundColors[position % backgroundColors.length], weatherDetailsInterface);
  }

  @Override
  public int getItemCount()
  {
    return forecasts.size();
  }

}