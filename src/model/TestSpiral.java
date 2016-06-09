package model;

import java.util.ArrayList;
import model.*;
import model.objects.Field;

public class TestSpiral {


	public static void main(String[] args){
		Board board = new Board();
		String result = "";
		result = HexService.getSpiral("B");
		for(int i = 0; i< result.length(); i++){
			System.out.println(result.charAt(i));
		}
	}
}
