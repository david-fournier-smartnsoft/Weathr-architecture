package com.smartnsoft.weathr.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.smartnsoft.weathr.bo.BusinessObject;
import com.smartnsoft.weathr.bo.LinkedBusinessObject;
import com.smartnsoft.weathr.dao.BusinessObjectDao;

/**
 * The class description here.
 *
 * @author David Fournier
 * @since 2017.06.15
 */

@Database(entities = {BusinessObject.class, LinkedBusinessObject.class}, version = 2)
public abstract class MyDatabase extends RoomDatabase
{
  public abstract BusinessObjectDao businessObjectDao();
}
