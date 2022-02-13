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
	
	private Image whitePawnImg, blackPawnImg, whiteKnightImg, blackKnightImg, whiteBishopImg, whiteRookImg, whiteQueenImg, whiteKingImg;
	
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
				
			whiteBishopMoves=new int[4][14]
			;
	
	private boolean[] whitePawnClicked=new boolean[8], blackPawnClicked=new boolean[8],
			whiteKnightClicked=new boolean[2], blackKnightClicked=new boolean[2];
	
	private boolean[] whitePawnSpots=new boolean[4], blackPawnSpots=new boolean[4], whiteKnightSpots=new boolean[8], blackKnightSpots=new boolean[8];
	
//	private ArrayList<int[]> whiteKnightMoves=new ArrayList<int[]>();
	
	public void set() {
		whitePawnImg = Toolkit.getDefaultToolkit().getImage("whitepawn.png");
		blackPawnImg = Toolkit.getDefaultToolkit().getImage("blackpawn.png");
		whiteKnightImg = Toolkit.getDefaultToolkit().getImage("whiteknight.png");
		blackKnightImg = Toolkit.getDefaultToolkit().getImage("blackknight.png");
		whiteBishopImg = Toolkit.getDefaultToolkit().getImage("whitebishop.png");
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
		}
		for (int i=0; i<whiteBishopMoves.length; i+=2) {
			int a;
			if (i%2==0) a=-1;
			else a=1;
			for (int j=0; j<whiteBishopMoves[i].length/2; j++) {
				whiteBishopMoves[i][2*j]=(j+1)*width/len;
				whiteBishopMoves[i][2*j+1]=(j+1)*a*height/len;
				whiteBishopMoves[i+1][2*j]=(j+1)*a*width/len;
				whiteBishopMoves[i+1][2*j+1]=(j+1)*height/len;
			}
			System.out.println(Arrays.toString(whiteBishopMoves[i])+"\n"+Arrays.toString(whiteBishopMoves[i+1]));
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
		}
	}
	
	public void click(int mouseX, int mouseY) {
		if (turn) { //white to move
			for (int i=0; i<whitePawn.length; i++) {
				if (mouseX>whitePawn[i][0]&&mouseX<whitePawn[i][0]+width/len&&mouseY>whitePawn[i][1]&&mouseY<whitePawn[i][1]+height/len) {
					whitePawnClicked=new boolean[whitePawnClicked.length];
					whiteKnightClicked=new boolean[whiteKnightClicked.length];
					whitePawnSpots=new boolean[whitePawnSpots.length];
					whitePawnSpots[1]=true;
					if (whitePawn[i][1]==6*height/len) whitePawnSpots[3]=true;
					for (int j=0; j<blackPawn.length; j++) {
						if (blackPawn[j][0]==whitePawn[i][0]+whitePawnMoves[0][0]&&blackPawn[j][1]==whitePawn[i][1]+whitePawnMoves[0][1])
							whitePawnSpots[0]=true;
						if (blackPawn[j][0]==whitePawn[i][0]+whitePawnMoves[2][0]&&blackPawn[j][1]==whitePawn[i][1]+whitePawnMoves[2][1])
							whitePawnSpots[2]=true;
						if (blackPawn[j][0]==whitePawn[i][0]+whitePawnMoves[1][0]&&blackPawn[j][1]==whitePawn[i][1]+whitePawnMoves[1][1]||whitePawn[i][1]+whitePawnMoves[1][1]<0)
							whitePawnSpots[1]=false;
						if (blackPawn[j][0]==whitePawn[i][0]+whitePawnMoves[3][0]&&blackPawn[j][1]==whitePawn[i][1]+whitePawnMoves[3][1])
							whitePawnSpots[3]=false;
					}
					for (int j=0; j<blackKnight.length; j++) {
						if (blackKnight[j][0]==whitePawn[i][0]+whitePawnMoves[0][0]&&blackKnight[j][1]==whitePawn[i][1]+whitePawnMoves[0][1])
							whitePawnSpots[0]=true;
						if (blackKnight[j][0]==whitePawn[i][0]+whitePawnMoves[2][0]&&blackKnight[j][1]==whitePawn[i][1]+whitePawnMoves[2][1])
							whitePawnSpots[2]=true;
						if (blackKnight[j][0]==whitePawn[i][0]+whitePawnMoves[1][0]&&blackKnight[j][1]==whitePawn[i][1]+whitePawnMoves[1][1]||whitePawn[i][1]+whitePawnMoves[1][1]<0)
							whitePawnSpots[1]=false;
						if (blackKnight[j][0]==whitePawn[i][0]+whitePawnMoves[3][0]&&blackKnight[j][1]==whitePawn[i][1]+whitePawnMoves[3][1])
						whitePawnSpots[3]=false;
					}
					
					whitePawnClicked[i]=true;
					break;
				}
				else if (whitePawnClicked[i]) {
					for (int j=0; j<whitePawnMoves.length; j++) {
						if (whitePawnSpots[j]&&mouseX>whitePawn[i][0]+whitePawnMoves[j][0]&&mouseX<whitePawn[i][0]+whitePawnMoves[j][0]+width/len&&mouseY>whitePawn[i][1]+whitePawnMoves[j][1]&&mouseY<whitePawn[i][1]+whitePawnMoves[j][1]+height/len) {
							whitePawn[i][0]+=whitePawnMoves[j][0];
							whitePawn[i][1]+=whitePawnMoves[j][1];
							whitePawnClicked=new boolean[whitePawnClicked.length];
							whiteKnightClicked=new boolean[whiteKnightClicked.length];
							turn=!turn;
							break;
						}
					}
				}
			}
			
			for (int i=0; i<whiteKnight.length; i++) {
				if (mouseX>whiteKnight[i][0]&&mouseX<whiteKnight[i][0]+width/len&&mouseY>whiteKnight[i][1]&&mouseY<whiteKnight[i][1]+height/len) {
					whiteKnightClicked=new boolean[whiteKnightClicked.length];
					whitePawnClicked=new boolean[whitePawnClicked.length];
					whiteKnightClicked[i]=true;
					whiteKnightSpots=new boolean[whiteKnightSpots.length];
					for (int j=0; j<whiteKnightMoves.length; j++) {
						for (int k=0; k<whitePawn.length; k++) {
							if (whitePawn[k][0]==whiteKnight[i][0]+whiteKnightMoves[j][0]&&whitePawn[k][1]==whiteKnight[i][1]+whiteKnightMoves[j][1]||whiteKnight[i][0]+whiteKnightMoves[j][0]>=width||whiteKnight[i][0]+whiteKnightMoves[j][0]<0||whiteKnight[i][1]+whiteKnightMoves[j][1]>=height||whiteKnight[i][1]+whiteKnightMoves[j][1]<0) {
								whiteKnightSpots[j]=false;
								break;
							}
							else whiteKnightSpots[j]=true;
						}
					}
					break;
				}
				
				else if (whiteKnightClicked[i]) {
					for (int j=0; j<whiteKnightMoves.length; j++) {
						if (!whiteKnightSpots[j]) continue;
						if (mouseX>whiteKnight[i][0]+whiteKnightMoves[j][0]&&mouseX<whiteKnight[i][0]+whiteKnightMoves[j][0]+width/len&&mouseY>whiteKnight[i][1]+whiteKnightMoves[j][1]&&mouseY<whiteKnight[i][1]+whiteKnightMoves[j][1]+height/len) {
							whiteKnight[i][0]+=whiteKnightMoves[j][0];
							whiteKnight[i][1]+=whiteKnightMoves[j][1];
							whiteKnightClicked=new boolean[whiteKnightClicked.length];
							whitePawnClicked=new boolean[whitePawnClicked.length];
							turn=!turn;
							break;
						}
					}
				}
			}
			
			for (int i=0; i<whiteBishop.length; i++) {
				
			}
			
		}
		
		else { //black to move
			for (int i=0; i<blackPawn.length; i++) {
				if (mouseX>blackPawn[i][0]&&mouseX<blackPawn[i][0]+width/len&&mouseY>blackPawn[i][1]&&mouseY<blackPawn[i][1]+height/len) {
					blackPawnClicked=new boolean[blackPawnClicked.length];
					whiteKnightClicked=new boolean[whiteKnightClicked.length];
					blackPawnSpots=new boolean[blackPawnSpots.length];
					blackPawnSpots[1]=true;
					if (blackPawn[i][1]==height/len) blackPawnSpots[3]=true;
					for (int j=0; j<whitePawn.length; j++) {
						if (whitePawn[j][0]==blackPawn[i][0]+blackPawnMoves[0][0]&&whitePawn[j][1]==blackPawn[i][1]+blackPawnMoves[0][1])
							blackPawnSpots[0]=true;
						if (whitePawn[j][0]==blackPawn[i][0]+blackPawnMoves[2][0]&&whitePawn[j][1]==blackPawn[i][1]+blackPawnMoves[2][1])
							blackPawnSpots[2]=true;
						if (whitePawn[j][0]==blackPawn[i][0]+blackPawnMoves[1][0]&&whitePawn[j][1]==blackPawn[i][1]+blackPawnMoves[1][1]||blackPawn[i][1]+blackPawnMoves[1][1]>=height)
							blackPawnSpots[1]=false;
						if (whitePawn[j][0]==blackPawn[i][0]+blackPawnMoves[3][0]&&whitePawn[j][1]==blackPawn[i][1]+blackPawnMoves[3][1])
							blackPawnSpots[3]=false;
					}
					for (int j=0; j<whiteKnight.length; j++) {
						if (whiteKnight[j][0]==blackPawn[i][0]+blackPawnMoves[0][0]&&whiteKnight[j][1]==blackPawn[i][1]+blackPawnMoves[0][1])
							blackPawnSpots[0]=true;
						if (whiteKnight[j][0]==blackPawn[i][0]+blackPawnMoves[2][0]&&whiteKnight[j][1]==blackPawn[i][1]+blackPawnMoves[2][1])
							blackPawnSpots[2]=true;
						if (whiteKnight[j][0]==blackPawn[i][0]+blackPawnMoves[1][0]&&whiteKnight[j][1]==blackPawn[i][1]+blackPawnMoves[1][1]||blackPawn[i][1]+blackPawnMoves[1][1]>=height)
							blackPawnSpots[1]=false;
						if (whiteKnight[j][0]==blackPawn[i][0]+blackPawnMoves[3][0]&&whiteKnight[j][1]==blackPawn[i][1]+blackPawnMoves[3][1])
							blackPawnSpots[3]=false;
					}
					blackPawnClicked[i]=true;
					break;
				}
				else if (blackPawnClicked[i]) {
					for (int j=0; j<blackPawnMoves.length; j++) {
						if (blackPawnSpots[j]&&mouseX>blackPawn[i][0]+blackPawnMoves[j][0]&&mouseX<blackPawn[i][0]+blackPawnMoves[j][0]+width/len&&mouseY>blackPawn[i][1]+blackPawnMoves[j][1]&&mouseY<blackPawn[i][1]+blackPawnMoves[j][1]+height/len) {
							blackPawn[i][0]+=blackPawnMoves[j][0];
							blackPawn[i][1]+=blackPawnMoves[j][1];
							blackPawnClicked=new boolean[blackPawnClicked.length];
							blackKnightClicked=new boolean[blackKnightClicked.length];
							turn=!turn;
							break;
						}
					}
				}
			}
			
			for (int i=0; i<blackKnight.length; i++) {
				if (mouseX>blackKnight[i][0]&&mouseX<blackKnight[i][0]+width/len&&mouseY>blackKnight[i][1]&&mouseY<blackKnight[i][1]+height/len) {
					blackKnightClicked=new boolean[blackKnightClicked.length];
					blackPawnClicked=new boolean[blackPawnClicked.length];
					blackKnightClicked[i]=true;
					blackKnightSpots=new boolean[blackKnightSpots.length];
					for (int j=0; j<blackKnightMoves.length; j++) {
						for (int k=0; k<blackPawn.length; k++) {
							if (blackPawn[k][0]==blackKnight[i][0]+blackKnightMoves[j][0]&&blackPawn[k][1]==blackKnight[i][1]+blackKnightMoves[j][1]||blackKnight[i][0]+blackKnightMoves[j][0]>=width||blackKnight[i][0]+blackKnightMoves[j][0]<0||blackKnight[i][1]+blackKnightMoves[j][1]>=height||blackKnight[i][1]+blackKnightMoves[j][1]<0) {
								blackKnightSpots[j]=false;
								break;
							}
							else blackKnightSpots[j]=true;
						}
					}
					break;
				}
				
				else if (blackKnightClicked[i]) {
					for (int j=0; j<blackKnightMoves.length; j++) {
						if (!blackKnightSpots[j]) continue;
						if (mouseX>blackKnight[i][0]+blackKnightMoves[j][0]&&mouseX<blackKnight[i][0]+blackKnightMoves[j][0]+width/len&&mouseY>blackKnight[i][1]+blackKnightMoves[j][1]&&mouseY<blackKnight[i][1]+blackKnightMoves[j][1]+height/len) {
							blackKnight[i][0]+=blackKnightMoves[j][0];
							blackKnight[i][1]+=blackKnightMoves[j][1];
							blackKnightClicked=new boolean[blackKnightClicked.length];
							blackPawnClicked=new boolean[blackPawnClicked.length];
							turn=!turn;
							break;
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
