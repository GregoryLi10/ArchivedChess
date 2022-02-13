package Chess;

import java.util.Arrays;

public class Chess {
	
	private int[][] whitePawn=new int[8][2], blackPawn=new int[8][2],
			whiteKnight=new int[2][2], blackKnight=new int[2][2],
			whiteBishop=new int[2][2], blackBishop=new int[2][2],
			whiteRook=new int[2][2], blackRook=new int[2][2],
			whiteQueen=new int[1][2], blackQueen=new int[1][2],
			whiteKing=new int[1][2], blackKing=new int[1][2];
	private int WIDTH=1000, HEIGHT=800, len=8;
	
	
	public void set() {
		for (int i=0; i<whitePawn.length; i++) {
			whitePawn[i][0]=i*WIDTH/len;
			whitePawn[i][1]=7*HEIGHT/len;
			blackPawn[i][0]=i*WIDTH/len;
			blackPawn[i][1]=2*HEIGHT/len;
		}
		for (int[] a:whitePawn)
			System.out.println(Arrays.toString(a));
		for (int[] a:blackPawn)
			System.out.println(Arrays.toString(a));
	}
	
	public static void main(String[] args) {
		Chess run=new Chess();
		run.set();
	}
}
