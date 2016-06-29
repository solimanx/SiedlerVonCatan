package model;

import java.util.ArrayList;

import model.*;
import model.objects.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestSpiral {
	private static Logger logger = LogManager.getLogger(TestSpiral.class.getSimpleName());

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Board board = new Board();
		String result = "";
		result = HexService.getSpiral("N");
		for (int i = 0; i < result.length(); i++) {
			System.out.println(result.charAt(i));
			logger.debug(result.charAt(i));
		}
	}
}
