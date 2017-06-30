package com.smartnsoft.weathr.bo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.smartnsoft.weathr.bo.LinkedBusinessObject.LinkedBusinessObjectConverters;
import com.smartnsoft.weathr.bo.Weather.Forecast.Converters;

/**
 * The class description here.
 *
 * @author David Fournier
 * @since 2017.06.15
 */

@Entity(tableName = "linkedObjects", primaryKeys = {"id", "type"})
@TypeConverters({LinkedBusinessObjectConverters.class})
public class LinkedBusinessObject
{
  public enum EnumeratedType {
    ONE, TWO, THREE
  }

  public static class LinkedBusinessObjectConverters {

    @TypeConverter
    public static String fromEnumeratedType(EnumeratedType type) {
      return type.name();
    }

    @TypeConverter
    public static EnumeratedType toEnumeratedType(String typeName) {
      return EnumeratedType.valueOf(typeName);
    }

  }

  public final int id;

  @ForeignKey(entity = BusinessObject.class, parentColumns = "id", childColumns = "businessObject")
  public final int businessObject;

  public final EnumeratedType type;

  public LinkedBusinessObject(int id, int businessObject, EnumeratedType type)
  {
    this.id = id;
    this.businessObject = businessObject;
    this.type = type;
  }
}
