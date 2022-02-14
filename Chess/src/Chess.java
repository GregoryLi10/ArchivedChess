import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Chess {
	
	private int width = 400, height = 400, textHeight = 50;
	
	private int score = 0;
	
	private Image whitePawnImg, blackPawnImg, whiteKnightImg, blackKnightImg, whiteBishopImg, blackBishopImg, whiteRookImg, whiteQueenImg, whiteKingImg;
	
	private final int len=8;
	
	private boolean turn=true;
	
	private int[][] whitePawn=new int[8][2], blackPawn=new int[8][2],
			whiteKnight=new int[2][2], blackKnight=new int[2][2],
			whiteBishop=new int[2][2], blackBishop=new int[2][2],
			whiteRook=new int[2][2], blackRook=new int[2][2],
			whiteQueen=new int[1][2], blackQueen=new int[1][2],
			whiteKing=new int[1][2], blackKing=new int[1][2];
	
	private int[][] whitePawnMoves={
				{-width/len, -height/len},
				{0, -height/len},
				{width/len, -height/len},
				{0, -2*height/len}},
			
			blackPawnMoves={
				{-width/len, height/len},
				{0, height/len},
				{width/len, height/len},
				{0, 2*height/len}},
			
			whiteKnightMoves={
				{-2*width/len, -height/len},
				{-width/len, -2*height/len},
				{width/len, -2*height/len},
				{2*width/len, -height/len},
				{2*width/len, height/len},
				{width/len, 2*height/len},
				{-width/len, 2*height/len},
				{-2*width/len, height/len}},
			
			blackKnightMoves={
				{-2*width/len, -height/len},
				{-width/len, -2*height/len},
				{width/len, -2*height/len},
				{2*width/len, -height/len},
				{2*width/len, height/len},
				{width/len, 2*height/len},
				{-width/len, 2*height/len},
				{-2*width/len, height/len}},
				
			whiteBishopMoves=new int[4][14],
			blackBishopMoves=new int[4][14]
			;
	
	private boolean[] whitePawnClicked=new boolean[8], blackPawnClicked=new boolean[8],
			whiteKnightClicked=new boolean[2], blackKnightClicked=new boolean[2],
			whiteBishopClicked=new boolean[2], blackBishopClicked=new boolean[2];
	
	private boolean[] whitePawnSpots=new boolean[4], blackPawnSpots=new boolean[4], 
			whiteKnightSpots=new boolean[8], blackKnightSpots=new boolean[8];
	
	private boolean[][]whiteBishopSpots=new boolean[4][7], blackBishopSpots=new boolean[4][7];
	
	public void set() {
		whitePawnImg = Toolkit.getDefaultToolkit().getImage("whitepawn.png");
		blackPawnImg = Toolkit.getDefaultToolkit().getImage("blackpawn.png");
		whiteKnightImg = Toolkit.getDefaultToolkit().getImage("whiteknight.png");
		blackKnightImg = Toolkit.getDefaultToolkit().getImage("blackknight.png");
		whiteBishopImg = Toolkit.getDefaultToolkit().getImage("whitebishop.png");
		blackBishopImg = Toolkit.getDefaultToolkit().getImage("blackbishop.png");
		whiteRookImg = Toolkit.getDefaultToolkit().getImage("whiterook.png");
		for (int i=0; i<len; i++) {
			whitePawn[i][0]=i*width/len;
			whitePawn[i][1]=6*height/len;
			blackPawn[i][0]=i*width/len;
			blackPawn[i][1]=height/len;
		}
		for (int i=0; i<2; i++) {
			whiteKnight[i][0]=(5*i+1)*width/len;
			whiteKnight[i][1]=7*height/len;
			blackKnight[i][0]=(5*i+1)*width/len;
			blackKnight[i][1]=0;
			whiteBishop[i][0]=(3*i+2)*width/len;
			whiteBishop[i][1]=7*height/len;
			blackBishop[i][0]=(3*i+2)*width/len;
			blackBishop[i][1]=0;
			whiteRook[i][0]=7*i*width/len;
			whiteRook[i][1]=7*height/len;
		}
		for (int i=0; i<whiteBishopMoves.length; i+=2) {
			int a=i==0?1:-1;
			for (int j=0; j<whiteBishopMoves[i].length/2; j++) {
				whiteBishopMoves[i][2*j]=(j+1)*a*width/len;
				whiteBishopMoves[i][2*j+1]=(j+1)*height/len;
				whiteBishopMoves[i+1][2*j]=-whiteBishopMoves[i][2*j];
				whiteBishopMoves[i+1][2*j+1]=-whiteBishopMoves[i][2*j+1];
				blackBishopMoves[i][2*j]=(j+1)*a*width/len;
				blackBishopMoves[i][2*j+1]=(j+1)*height/len;
				blackBishopMoves[i+1][2*j]=-blackBishopMoves[i][2*j];
				blackBishopMoves[i+1][2*j+1]=-blackBishopMoves[i][2*j+1];
			}
		}
	}
	
	public void resized() {
		
	}
	
	public void draw(Graphics g) {
		for (int i=0; i<len; i++) {
			g.drawLine(0, (i+1)*height/len, width, (i+1)*height/len);
			g.drawLine((i+1)*width/len, 0, (i+1)*width/len, height);
		}
		
		g.setColor(new Color(144, 82, 227));
		for (int i=0; i<whitePawn.length; i++) {
			g.drawImage(whitePawnImg, whitePawn[i][0], whitePawn[i][1], width/len, height/len, null);
			if (!turn) continue;
			if (whitePawnClicked[i]){
				for (int j=0; j<whitePawnMoves.length; j++) {
					if (!whitePawnSpots[j]) continue;
					g.fillOval(whitePawn[i][0]+whitePawnMoves[j][0]+width/len*3/8, whitePawn[i][1]+whitePawnMoves[j][1]+height/len*3/8, width/len/4, height/len/4);
				}
			}
		}
		
		for (int i=0; i<blackPawn.length; i++) {
			g.drawImage(blackPawnImg, blackPawn[i][0], blackPawn[i][1], width/len, height/len, null);
			if (turn) continue;
			if (blackPawnClicked[i]){
				for (int j=0; j<blackPawnMoves.length; j++) {
					if (!blackPawnSpots[j]) continue;
					g.fillOval(blackPawn[i][0]+blackPawnMoves[j][0]+width/len*3/8, blackPawn[i][1]+blackPawnMoves[j][1]+height/len*3/8, width/len/4, height/len/4);
				}
			}
		}
		
		for (int i=0; i<whiteKnight.length; i++) {
			g.drawImage(whiteKnightImg, whiteKnight[i][0], whiteKnight[i][1], width/len, height/len, null);
			if (!turn) continue;
			if (whiteKnightClicked[i]) {
				for (int j=0; j<whiteKnightMoves.length; j++) {
					if (whiteKnightSpots[j])
						g.fillOval(whiteKnight[i][0]+whiteKnightMoves[j][0]+width/len*3/8, whiteKnight[i][1]+whiteKnightMoves[j][1]+height/len*3/8, width/len/4, height/len/4);
				}
			}
		}
		
		for (int i=0; i<blackKnight.length; i++) {
			g.drawImage(blackKnightImg, blackKnight[i][0], blackKnight[i][1], width/len, height/len, null);
			if (turn) continue;
			if (blackKnightClicked[i]) {
				for (int j=0; j<blackKnightMoves.length; j++) {
					if (blackKnightSpots[j])
						g.fillOval(blackKnight[i][0]+blackKnightMoves[j][0]+width/len*3/8, blackKnight[i][1]+blackKnightMoves[j][1]+height/len*3/8, width/len/4, height/len/4);
				}
			}
		}
		
		for (int i=0; i<whiteBishop.length; i++) {
			g.drawImage(whiteBishopImg, whiteBishop[i][0], whiteBishop[i][1], width/len,height/len, null);
			if (!turn) continue;
			if (whiteBishopClicked[i]) {
				for (int j=0; j<whiteBishopSpots.length; j++) {
					for (int k=0; k<whiteBishopSpots[j].length; k++) {
						if (whiteBishopSpots[j][k])
							g.fillOval(whiteBishop[i][0]+whiteBishopMoves[j][2*k]+width/len*3/8, whiteBishop[i][1]+whiteBishopMoves[j][2*k+1]+height/len*3/8, width/len/4, height/len/4);
					}
				}
			}
		}

		for (int i=0; i<blackBishop.length; i++) {
			g.drawImage(blackBishopImg, blackBishop[i][0], blackBishop[i][1], width/len,height/len, null);
			if (turn) continue;
			if (blackBishopClicked[i]) {
				for (int j=0; j<blackBishopSpots.length; j++) {
					for (int k=0; k<blackBishopSpots[j].length; k++) {
						if (blackBishopSpots[j][k])
							g.fillOval(blackBishop[i][0]+blackBishopMoves[j][2*k]+width/len*3/8, blackBishop[i][1]+blackBishopMoves[j][2*k+1]+height/len*3/8, width/len/4, height/len/4);
					}
				}
			}
		}
		
		for (int i=0; i<whiteRook.length; i++) {
			g.drawImage(whiteRookImg, whiteRook[i][0], whiteRook[i][1], width/len,height/len, null);
			if (!turn) continue;
//			if (whiteRookClicked[i]) {
//				for (int j=0; j<whiteRookSpots.length; j++) {
//					for (int k=0; k<whiteRookSpots[j].length; k++) {
//						if (whiteRookSpots[j][k])
//							g.fillOval(whiteRook[i][0]+whiteRookMoves[j][2*k]+width/len*3/8, whiteRook[i][1]+whiteRookMoves[j][2*k+1]+height/len*3/8, width/len/4, height/len/4);
//					}
//				}
//			}
		}
	}
	
	public boolean in(int mx, int my, int ox, int oy) {
		return mx>ox&&mx<ox+width/len&&my>oy&&my<oy+height/len;
	}
	
	public void resetSelected() {
		whitePawnClicked=new boolean[whitePawnClicked.length];
		whiteKnightClicked=new boolean[whiteKnightClicked.length];
		whiteBishopClicked=new boolean[whiteBishopClicked.length];
		blackPawnClicked=new boolean[blackPawnClicked.length];
		blackKnightClicked=new boolean[blackKnightClicked.length];
		blackBishopClicked=new boolean[blackBishopClicked.length];
		
	}
	
	public boolean checkWhite(int x, int y) {
		if (x>=width||y>=height||x<0||y<0) return false;
		for (int i=0; i<whitePawn.length; i++) {
			if (whitePawn[i][0]==x&&whitePawn[i][1]==y) return false;
		}
		for (int i=0; i<whiteKnight.length; i++) {
			if (whiteKnight[i][0]==x&&whiteKnight[i][1]==y) return false;
		}
		for (int i=0; i<whiteBishop.length; i++) {
			if (whiteBishop[i][0]==x&&whiteBishop[i][1]==y) return false;
		}
		return true;
	}
	
	public boolean checkBlack(int x, int y) {
		for (int i=0; i<blackPawn.length; i++) {
			if (blackPawn[i][0]==x&&blackPawn[i][1]==y) return false;
		}
		for (int i=0; i<blackKnight.length; i++) {
			if (blackKnight[i][0]==x&&blackKnight[i][1]==y) return false;
		}
		for (int i=0; i<blackBishop.length; i++) {
			if (blackBishop[i][0]==x&&blackBishop[i][1]==y) return false;
		}
		return true;
	}
	
	public void click(int mouseX, int mouseY) {
		if (turn) { //white to move
			for (int i=0; i<whitePawn.length; i++) {
				if (in(mouseX, mouseY, whitePawn[i][0], whitePawn[i][1])) {
					resetSelected();
					whitePawnSpots=new boolean[whitePawnSpots.length];
					for (int j=0; j<whitePawnSpots.length; j+=2) {
						whitePawnSpots[j]=!checkBlack(whitePawn[i][0]+whitePawnMoves[j][0], whitePawn[i][1]+whitePawnMoves[j][1]);
						whitePawnSpots[j+1]=checkWhite(whitePawn[i][0]+whitePawnMoves[j+1][0], whitePawn[i][1]+whitePawnMoves[j+1][1])&&checkBlack(whitePawn[i][0]+whitePawnMoves[j+1][0], whitePawn[i][1]+whitePawnMoves[j+1][1]);
					}
					if (!whitePawnSpots[1]||whitePawn[i][1]!=6*height/len) whitePawnSpots[3]=false;
					whitePawnClicked[i]=true;
					break;
				}
				else if (whitePawnClicked[i]) {
					for (int j=0; j<whitePawnMoves.length; j++) {
						if (whitePawnSpots[j]&&mouseX>whitePawn[i][0]+whitePawnMoves[j][0]&&mouseX<whitePawn[i][0]+whitePawnMoves[j][0]+width/len&&mouseY>whitePawn[i][1]+whitePawnMoves[j][1]&&mouseY<whitePawn[i][1]+whitePawnMoves[j][1]+height/len) {
							whitePawn[i][0]+=whitePawnMoves[j][0];
							whitePawn[i][1]+=whitePawnMoves[j][1];
							resetSelected();
							turn=!turn;
							break;
						}
					}
				}
			}
			
			for (int i=0; i<whiteKnight.length; i++) {
				if (mouseX>whiteKnight[i][0]&&mouseX<whiteKnight[i][0]+width/len&&mouseY>whiteKnight[i][1]&&mouseY<whiteKnight[i][1]+height/len) {
					resetSelected();
					whiteKnightClicked[i]=true;
					whiteKnightSpots=new boolean[whiteKnightSpots.length];
					for (int j=0; j<whiteKnightMoves.length; j++) {
						whiteKnightSpots[j]=checkWhite(whiteKnight[i][0]+whiteKnightMoves[j][0], whiteKnight[i][1]+whiteKnightMoves[j][1]);
					}
					break;
				}
				
				else if (whiteKnightClicked[i]) {
					for (int j=0; j<whiteKnightMoves.length; j++) {
						if (!whiteKnightSpots[j]) continue;
						if (in(mouseX, mouseY, whiteKnight[i][0]+whiteKnightMoves[j][0], whiteKnight[i][1]+whiteKnightMoves[j][1])){
							whiteKnight[i][0]+=whiteKnightMoves[j][0];
							whiteKnight[i][1]+=whiteKnightMoves[j][1];
							resetSelected();
							turn=!turn;
							break;
						}
					}
				}
			}
			
			for (int i=0; i<whiteBishop.length; i++) {
				if (in(mouseX, mouseY, whiteBishop[i][0], whiteBishop[i][1])) {
					resetSelected();
					whiteBishopClicked[i]=true;
					whiteBishopSpots=new boolean[whiteBishopSpots.length][whiteBishopSpots[0].length];
					for (int j=0; j<whiteBishopSpots.length; j++) {
						for (int k=0; k<whiteBishopSpots[j].length; k++) {
							whiteBishopSpots[j][k]=checkWhite(whiteBishop[i][0]+whiteBishopMoves[j][2*k], whiteBishop[i][1]+whiteBishopMoves[j][2*k+1]);
							if (!whiteBishopSpots[j][k]) break;
							whiteBishopSpots[j][k]=checkBlack(whiteBishop[i][0]+whiteBishopMoves[j][2*k], whiteBishop[i][1]+whiteBishopMoves[j][2*k+1]);
							if (!whiteBishopSpots[j][k]) {
								whiteBishopSpots[j][k]=true;
								break;
							}
						}
					}
				}
				
				else if (whiteBishopClicked[i]) {
					for (int j=0; j<whiteBishopSpots.length; j++) {
						for (int k=0; k<whiteBishopSpots[j].length; k++) {
							if (!whiteBishopSpots[j][k]) break;
							if (in(mouseX, mouseY, whiteBishop[i][0]+whiteBishopMoves[j][2*k], whiteBishop[i][1]+whiteBishopMoves[j][2*k+1])) {
								whiteBishop[i][0]+=whiteBishopMoves[j][2*k];
								whiteBishop[i][1]+=whiteBishopMoves[j][2*k+1];
								resetSelected();
								turn=!turn;
							}
						}
					}
				}
			}
			
			
		}
		
		else { //black to move
			for (int i=0; i<blackPawn.length; i++) {
				if (mouseX>blackPawn[i][0]&&mouseX<blackPawn[i][0]+width/len&&mouseY>blackPawn[i][1]&&mouseY<blackPawn[i][1]+height/len) {
					resetSelected();
					blackPawnSpots=new boolean[blackPawnSpots.length];
					for (int j=0; j<blackPawnSpots.length; j+=2) {
						blackPawnSpots[j]=!checkWhite(blackPawn[i][0]+blackPawnMoves[j][0], blackPawn[i][1]+blackPawnMoves[j][1]);
						blackPawnSpots[j+1]=checkWhite(blackPawn[i][0]+blackPawnMoves[j+1][0], blackPawn[i][1]+blackPawnMoves[j+1][1])&&checkBlack(blackPawn[i][0]+blackPawnMoves[j+1][0], blackPawn[i][1]+blackPawnMoves[j+1][1]);
					}
					if (!blackPawnSpots[1]||blackPawn[i][1]!=height/len) blackPawnSpots[3]=false;
					blackPawnClicked[i]=true;
					break;
				}
				else if (blackPawnClicked[i]) {
					for (int j=0; j<blackPawnMoves.length; j++) {
						if (in(mouseX, mouseY, blackPawn[i][0]+blackPawnMoves[j][0], blackPawn[i][1]+blackPawnMoves[j][1])) {
							blackPawn[i][0]+=blackPawnMoves[j][0];
							blackPawn[i][1]+=blackPawnMoves[j][1];
							resetSelected();
							turn=!turn;
							break;
						}
					}
				}
			}
			
			for (int i=0; i<blackKnight.length; i++) {
				if (in(mouseX, mouseY, blackKnight[i][0], blackKnight[i][1])) {
					resetSelected();
					blackKnightClicked[i]=true;
					blackKnightSpots=new boolean[blackKnightSpots.length];
					for (int j=0; j<blackKnightMoves.length; j++) 
						blackKnightSpots[j]=checkBlack(blackKnight[i][0]+blackKnightMoves[j][0], blackKnight[i][1]+blackKnightMoves[j][1]);
				}
				
				else if (blackKnightClicked[i]) {
					for (int j=0; j<blackKnightMoves.length; j++) {
						if (!blackKnightSpots[j]) continue;
						if (in(mouseX, mouseY, blackKnight[i][0]+blackKnightMoves[j][0], blackKnight[i][1]+blackKnightMoves[j][1])) {
							blackKnight[i][0]+=blackKnightMoves[j][0];
							blackKnight[i][1]+=blackKnightMoves[j][1];
							resetSelected();
							turn=!turn;
							break;
						}
					}
				}
			}
			
			for (int i=0; i<blackBishop.length; i++) {
				if (in(mouseX, mouseY, blackBishop[i][0], blackBishop[i][1])) {
					resetSelected();
					blackBishopClicked[i]=true;
					blackBishopSpots=new boolean[blackBishopSpots.length][blackBishopSpots[0].length];
					for (int j=0; j<blackBishopSpots.length; j++) {
						for (int k=0; k<blackBishopSpots[j].length; k++) {
							blackBishopSpots[j][k]=checkBlack(blackBishop[i][0]+blackBishopMoves[j][2*k], blackBishop[i][1]+blackBishopMoves[j][2*k+1]);
							if (!blackBishopSpots[j][k]) break;
							blackBishopSpots[j][k]=checkWhite(blackBishop[i][0]+blackBishopMoves[j][2*k], blackBishop[i][1]+blackBishopMoves[j][2*k+1]);
							if (!blackBishopSpots[j][k]) {
								blackBishopSpots[j][k]=true;
								break;
							}
						}
					}
				}
				
				else if (blackBishopClicked[i]) {
					for (int j=0; j<blackBishopSpots.length; j++) {
						for (int k=0; k<blackBishopSpots[j].length; k++) {
							if (!blackBishopSpots[j][k]) break;
							if (in(mouseX, mouseY, blackBishop[i][0]+blackBishopMoves[j][2*k], blackBishop[i][1]+blackBishopMoves[j][2*k+1])) {
								blackBishop[i][0]+=blackBishopMoves[j][2*k];
								blackBishop[i][1]+=blackBishopMoves[j][2*k+1];
								resetSelected();
								turn=!turn;
							}
						}
					}
				}
			}
			
		}
	}
	
	
	// DO NOT TOUCH BELOW CODE //
	
	public Chess() {
		
		set();
		JFrame window = new JFrame();
		window.setTitle("Chess");
		window.setSize(width, height + textHeight);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
			
		JPanel canvas = new JPanel() {
			public void paint(Graphics g) {
				width = window.getWidth();
				height = window.getHeight() - textHeight;
				draw(g);
				window.getContentPane().repaint();
			}
		};
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.addMouseListener(new MouseListener() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				click(e.getX(), e.getY());
				window.getContentPane().repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});
		
		container.add(canvas);
		window.add(container);
		window.setVisible(true);
		
//		window.addComponentListener(new ComponentAdapter() {
//            public void componentResized(ComponentEvent componentEvent) {
//               resized();
//            }
//        });
	}
	
	public static void main(String[] args) {
		new Chess();
	}

}
