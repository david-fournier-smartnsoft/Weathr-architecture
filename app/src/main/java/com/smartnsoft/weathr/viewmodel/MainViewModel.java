package com.smartnsoft.weathr.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.smartnsoft.weathr.bo.BusinessObject;

/**
 * The class description here.
 *
 * @author David Fournier
 * @since 2017.06.16
 */

public class MainViewModel
    extends ViewModel
{
  private MutableLiveData<BusinessObject> businessObject;

  public LiveData<BusinessObject> getBusinessObject() {
    if (businessObject == null)
    {
      businessObject = new MutableLiveData<>();
    }
    loadBusinessObject();
    return businessObject;
  }

  private void loadBusinessObject()
  {
    //Retrieves asynchronously the business object and set its value
    //Setting the value will trigger the Observers
    businessObject.setValue(new BusinessObject());
  }
}
