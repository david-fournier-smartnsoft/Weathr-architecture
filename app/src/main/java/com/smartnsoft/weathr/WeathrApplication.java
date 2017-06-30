package com.smartnsoft.weathr;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.smartnsoft.weathr.persistence.WeathrDatabase;

/**
 * The class description here.
 *
 * @author David Fournier
 * @since 2017.06.05
 */

public class WeathrApplication
    extends Application
{

  static private WeathrDatabase database;

  @Override
  public void onCreate()
  {
    super.onCreate();

    database = Room.databaseBuilder(getApplicationContext(), WeathrDatabase.class, "weathr").build();
  }

  public static WeathrDatabase getDatabase()
  {
    return database;
  }
}
