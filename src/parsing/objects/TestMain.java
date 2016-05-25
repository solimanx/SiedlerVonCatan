package parsing.objects;

import java.io.IOException;
import enums.ResourceType;
import model.Field;

public class TestMain {

	public static void main(String[] args) throws IOException {

		Field f = new Field();
		f.setDiceIndex(10);
		f.setFieldID("A");
		f.setResourceType(ResourceType.CORN);
		
		FieldJSON fj = new FieldJSON(f);
		fj.write();
		
		Field g = FieldJSON.read();

	}
}
