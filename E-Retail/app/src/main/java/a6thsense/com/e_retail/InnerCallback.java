package a6thsense.com.e_retail;

public interface InnerCallback<T>
{
  void handleResponse(T response);
}