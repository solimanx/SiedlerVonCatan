package parsing;

import java.io.IOException;

public interface JSONInterface<T> {
	public String toString();
	public void createJSON(T t) throws IOException;
	public T getFromJSON(String json);
	public void handleJSON();

}
