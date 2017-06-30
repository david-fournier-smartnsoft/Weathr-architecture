package com.smartnsoft.weathr.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.smartnsoft.weathr.Constants;
import com.smartnsoft.weathr.R;

/**
 * @author Ludovic Roland
 * @since 2015.11.13
 */
public final class ParameterDialogFragment
    extends DialogFragment
    implements OnClickListener
{

  public static final String TAG = ParameterDialogFragment.class.getSimpleName();

  private SharedPreferences preferences;

  private EditText city;

  private EditText forecasts;

  @Override
  public void onClick(DialogInterface dialog, int which)
  {
    final Editor editor = preferences.edit();
    editor.putString(HomeFragment.CITY_PREFERENCES_KEY, city.getText().toString());

    try
    {
      final int forecastsNumber = Integer.valueOf(forecasts.getText().toString());
      editor.putInt(HomeFragment.FORECASTS_DAYS_PREFRENCES_KEY, forecastsNumber);
    }
    catch (NumberFormatException exception)
    {
      Log.w(ParameterDialogFragment.TAG, exception);
    }
    finally
    {
      editor.apply();
      dialog.dismiss();
    }
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
    dialogBuilder.setTitle(R.string.menu_settings);

    final View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_parameters, null, false);

    city = (EditText) rootView.findViewById(R.id.city);
    city.setText(preferences.getString(HomeFragment.CITY_PREFERENCES_KEY, Constants.DEFAULT_CITY));

    forecasts = (EditText) rootView.findViewById(R.id.forecasts);
    forecasts.setText(String.valueOf(preferences.getInt(HomeFragment.FORECASTS_DAYS_PREFRENCES_KEY, Constants.DEFAULT_FORECASTS_DAYS)));

    dialogBuilder.setView(rootView);

    dialogBuilder.setPositiveButton(android.R.string.ok, this);

    return dialogBuilder.create();
  }

}
