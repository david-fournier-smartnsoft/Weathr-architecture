package com.smartnsoft.weathr.ws;

/**
 * @author Jocelyn Girard, Ludovic Roland
 * @since 2015.12.02
 */
public final class AsyncTaskResult<T>
{

  private T result;

  private Exception error;

  public AsyncTaskResult(T result)
  {
    super();
    this.result = result;
  }

  public AsyncTaskResult(Exception error)
  {
    super();
    this.error = error;
  }

  public T getResult()
  {
    return result;
  }

  public Exception getError()
  {
    return error;
  }

}
