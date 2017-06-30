package com.smartnsoft.weathr.bo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * The class description here.
 *
 * @author David Fournier
 * @since 2017.06.15
 */

@Entity
public class BusinessObject
{
  @PrimaryKey(autoGenerate = true)
  public int id;

  @ColumnInfo(name = "name")
  private String name;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
}
