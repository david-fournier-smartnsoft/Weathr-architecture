package com.smartnsoft.weathr.dao;

import java.lang.annotation.Annotation;
import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.smartnsoft.weathr.bo.BusinessObject;
import com.smartnsoft.weathr.bo.LinkedBusinessObject;

/**
 * The class description here.
 *
 * @author David Fournier
 * @since 2017.06.15
 */

@Dao
public interface BusinessObjectDao
{
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void add(BusinessObject businessObject);

  @Delete
  void remove(BusinessObject businessObject);

  @Query("SELECT * FROM businessobject")
  List<BusinessObject> getAll();

  @Query("SELECT * FROM linkedObjects WHERE businessObject = :id")
  List<LinkedBusinessObject> getAllFromId(int id);
}
