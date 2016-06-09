package model;

import java.util.ArrayList;
import model.*;
import model.objects.Field;

public class TestSpiral {


	public static void main(String[] args){
		Board board = new Board();
//		ArrayList<Field> result = new ArrayList<Field>();
//		result = HexService.getSpiral(board.getField(-2,2));
//		for(int i = 0; i< result.size(); i++){
//			System.out.println(result.get(i).getFieldID());
		System.out.println(Board.getNeighbouringFields(0, 0));
		System.out.println(HexService.getCornerFromEdge("AB")[1]);

	}
}
