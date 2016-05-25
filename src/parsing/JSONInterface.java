package parsing;

import com.google.gson.Gson;

public interface JSONInterface<T> {
	public String toString();
	public void createJSON(T t);
	public T getFromJSON(Gson g);
	public void handleJSON();

}
