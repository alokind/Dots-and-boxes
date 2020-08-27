package etf.dotsandboxes.dn160597d;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Igra {
	
	private Color backgroundCol =  new Color(211,239,243);
	private Color plavaBoja =  new Color(53,217,242);
	private Color crvenaBoja =  new Color(255,62,63);
	private Color lineCol =  new Color(73,136,143);
	
    private final static int size = 16;
    private final static int dist = 70;

    private int m;
    private int n;
    private boolean isKorakPoKorak;
    
    private ArrayList<Linija> potezi;
    private Tabla tabla;
    private int turn;
    private boolean mouseEnabled;

    Racunar redSolver, blueSolver, solver;
    String redName, blueName;
    Main parent;

    private JLabel[][] hEdge, vEdge, box;
    private boolean[][] isSetHEdge, isSetVEdge;

    private JFrame frame;
    private JLabel redScoreLabel, blueScoreLabel, statusLabel;

    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            if(!mouseEnabled) return;
            odigrajPotez(getSource(mouseEvent.getSource()), false);
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
            if(!mouseEnabled) return;
            Linija linija = getSource(mouseEvent.getSource());
            int x=linija.getX(), y=linija.getY();
            if(!linija.isHorizontalno()) {
                if(isSetVEdge[x][y]) return;
                vEdge[x][y].setBackground((turn == Tabla.CRVENA) ? crvenaBoja : plavaBoja);
            }
            else {
            	if(isSetHEdge[x][y]) return;
                hEdge[x][y].setBackground((turn == Tabla.CRVENA) ? crvenaBoja : plavaBoja);

            }
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            if(!mouseEnabled) return;
            Linija linija = getSource(mouseEvent.getSource());
            int x=linija.getX(), y=linija.getY();
            if(linija.isHorizontalno()) {
                if(isSetHEdge[x][y]) return;
                hEdge[x][y].setBackground(backgroundCol);
            }
            else {
                if(isSetVEdge[x][y]) return;
                vEdge[x][y].setBackground(backgroundCol);
            }
        }

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    };

    private void odigrajPotez(Linija linija, boolean izFajlaOpt) {
    	boolean izFajla = izFajlaOpt;
        int x=linija.getX(), y=linija.getY();
        ArrayList<Point> ret;
        if(!linija.isHorizontalno()) {
            if(isSetVEdge[x][y]) return;
            ret = tabla.setvLiniju(x,y,turn);
            if(!izFajla)
            	potezi.add(new Linija(x, y, false));
            vEdge[x][y].setBackground(lineCol);
            isSetVEdge[x][y] = true;
            
        }
        else {
            if(isSetHEdge[x][y]) return;
            ret = tabla.sethLiniju(x,y,turn);
            if(!izFajla)
            	potezi.add(new Linija(x, y, true));
            hEdge[x][y].setBackground(lineCol);
            isSetHEdge[x][y] = true;

        }
        
        for (Iterator<Point> iterator = ret.iterator(); iterator.hasNext();) {
			Point p = (Point) iterator.next();
			 box[p.x][p.y].setBackground((turn == Tabla.PLAVA) ? plavaBoja : crvenaBoja);
		}

//        for(Point p : ret)
//            box[p.x][p.y].setBackground((turn == Tabla.CRVENA) ? redCol : blueCol);

        redScoreLabel.setText(String.valueOf(tabla.getCrveniRezultat()));
        blueScoreLabel.setText(String.valueOf(tabla.getPlaviRezultat()));

        if(tabla.isKrajIgre()) {
            int winner = tabla.getWinner();
            if(winner == Tabla.CRVENA) {
                statusLabel.setText("Red won!");
                statusLabel.setForeground(crvenaBoja);
            }
            else if(winner == Tabla.PLAVA) {
                statusLabel.setText("Blue won!");
                statusLabel.setForeground(plavaBoja);
            }
            else {
                statusLabel.setText("It's a tie.");
                statusLabel.setForeground(lineCol);
            }
            exportMovesFunction();
            
        }

        if(ret.size()==0) {
            if(turn == Tabla.CRVENA) {
                turn = Tabla.PLAVA;
                solver = blueSolver;
                statusLabel.setForeground(plavaBoja);
            }
            else {
                turn = Tabla.CRVENA;
                solver = redSolver;
                statusLabel.setForeground(crvenaBoja);
            }
        }

    }

    private void manageGame() {
        while(!tabla.isKrajIgre()) {
            if(goBack) return;
            if(solver == null) {
                mouseEnabled = true;
            }
            else {
                mouseEnabled = false;
                RacunarRezultat result = solver.getResult(tabla, turn);
                if(isKorakPoKorak) {
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
                odigrajPotez(result.getBestMove(), false);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void displayHeuristic(ArrayList<Linija> moves, int[] value) {
		
    	for (int i = 0; i < moves.size(); i++) {
    		Linija linija = moves.get(i);
    		int x = linija.getX();
    		int y = linija.getY();
    		boolean isHorizontal = linija.isHorizontalno();
			int heuristic = value[i];
			
			if(isHorizontal)
				hEdge[x][y].setText(""+heuristic);
			else {
				String s = ""+heuristic;
				StringBuilder sb = new StringBuilder();
				sb.append("<html>");
				for (int j = 0; j < s.length(); j++) {
					sb.append(s.charAt(j));
					sb.append("<br>");
					
				}
				vEdge[x][y].setText(sb.toString());	
			}	
		}
		
	}
    
    protected void displayHeuristic(LinijaIHeuristika[] linijeIHeuristike) {
    	
    	for (LinijaIHeuristika linijaIHeuristika : linijeIHeuristike) {
			Linija linija = linijaIHeuristika.getLinija();
    		int x = linija.getX();
    		int y = linija.getY();
    		boolean isHorizontal = linija.isHorizontalno();
    		int heuristic = linijaIHeuristika.getHeuristika();
			if(isHorizontal)
				hEdge[x][y].setText(""+heuristic);
			else {
				String s = ""+heuristic;
				StringBuilder sb = new StringBuilder();
				sb.append("<html>");
				for (int j = 0; j < s.length(); j++) {
					sb.append(s.charAt(j));
					sb.append("<br>");
					
				}
				vEdge[x][y].setText(sb.toString());	
			}
		}
		
	}

	private Linija getSource(Object object) {
    	 	
        for(int i=0; i<m+1; i++)
            for(int j=0; j<n; j++)
                if(hEdge[i][j] == object) {
                	Linija e = new Linija(i,j,true);
                	return e;
                }
                    
        for(int i=0; i<m; i++)
            for(int j=0; j<n+1; j++)
                if(vEdge[i][j] == object){
                	Linija e = new Linija(i,j,false);
                	return e;
                }
        
        return new Linija();
    }

    private JLabel getHorizontalEdge() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(dist, size));
        label.setOpaque(true);
        label.addMouseListener(mouseListener);
        
        label.setBackground(backgroundCol);
        return label;
    }

    private JLabel getVerticalEdge() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(size, dist));
        label.setOpaque(true);
        label.addMouseListener(mouseListener);
        
        label.setBackground(backgroundCol);
        return label;
    }

    private JLabel getCircle() {
    	
    	class CircleLabel extends JLabel {

			private static final long serialVersionUID = 1L;

			@Override
    	    protected void paintComponent(Graphics g) {
    	        g.fillOval(0, 0, g.getClipBounds().width, g.getClipBounds().height);
    	    }
    	}
    	
        JLabel label = new CircleLabel();
        label.setPreferredSize(new Dimension(size, size));
        label.setOpaque(true);
        return label;
    }

    private JLabel getBox() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(dist-2, dist-2));
        label.setOpaque(true);
        label.setBackground(backgroundCol);
        return label;
    }

    private JLabel getEmptyLabel(Dimension d) {
        JLabel label = new JLabel();
        label.setPreferredSize(d);
        label.setBackground(backgroundCol);
        return label;
    }

    public Igra(Main parent, JFrame frame, int m, int n, Racunar redSolver, Racunar blueSolver, String redName, String blueName, ArrayList<Linija> potezi, boolean isKorakPoKorak) {
        this.parent = parent;
        this.frame = frame;
        this.m = m;
        this.n = n;
        this.isKorakPoKorak = isKorakPoKorak;
        this.redSolver = redSolver;
        this.blueSolver = blueSolver;
        this.redName = redName;
        this.blueName = blueName;
        if (potezi==null)
        	this.potezi = new ArrayList<>();
        else
        	this.potezi = potezi;
        
        if (redSolver!=null) {
        	redSolver.igra=this;
        }
        if (blueSolver!=null) {
        	blueSolver.igra=this;
        }
        initGame();
    }

    private boolean goBack;

    private ActionListener backListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            goBack = true;
        }
    };
    
    private void exportMovesFunction() {
    	File file = new File("moves.txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write(Integer.toString(m));
            fw.write(' ');
            fw.write(Integer.toString(n));
            fw.write('\n');
            for (Linija potez : potezi) {
				fw.write(potez.toString());
				fw.write('\n');
			}
            
            statusLabel.setText("<html>"+statusLabel.getText() + "<br/>Exported.</html>");
            statusLabel.setForeground(lineCol);
            
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private ActionListener exportMoves = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
        	exportMovesFunction();
        }
    };

    private void initGame() {
    	
		tabla = new Tabla(m, n);
		turn = Tabla.CRVENA;
		solver = redSolver;
    	
    	frame.setBackground(backgroundCol);
        int boardWidth = n * size + (n-1) * dist;
        int boardHeight = m * size + (m-1) * dist;

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(backgroundCol);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);
        constraints.gridy++;
        
        JPanel playerPanel = new JPanel(new GridLayout(7, 1));
        playerPanel.setPreferredSize(new Dimension(3* dist, boardHeight+dist*3));
        playerPanel.add(new JLabel("<html><h1><font color='rgb(255,62,63)'>" + redName, SwingConstants.CENTER));
        redScoreLabel = new JLabel("0", SwingConstants.CENTER);
        redScoreLabel.setForeground(crvenaBoja);
        redScoreLabel.setFont(new Font("Serif", Font.BOLD, 50));
        playerPanel.add(redScoreLabel);
        playerPanel.add(getEmptyLabel(new Dimension(dist, 5)));
        playerPanel.add(new JLabel("<html><h1><font color='rgb(53,217,242)'>" + blueName, SwingConstants.CENTER));
        blueScoreLabel = new JLabel("0", SwingConstants.CENTER);
        blueScoreLabel.setForeground(plavaBoja);
        blueScoreLabel.setFont(new Font("Serif", Font.BOLD, 50));
        playerPanel.add(blueScoreLabel);
        playerPanel.add(getEmptyLabel(new Dimension(dist, 5)));
        JButton exportMovesButton = new JButton("Export moves");
        exportMovesButton.addActionListener(exportMoves);
        playerPanel.add(exportMovesButton);
        
        hEdge = new JLabel[m+1][n];
        isSetHEdge = new boolean[m+1][n];

        vEdge = new JLabel[m][n+1];
        isSetVEdge = new boolean[m][n+1];

        box = new JLabel[m][n];
        
        JPanel board = new JPanel(new GridBagLayout());
        board.add(getEmptyLabel(new Dimension(dist + boardWidth, 10)), constraints);

        for(int i=0; i<(2*m+1); i++) {
            JPanel pane = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
            pane.setBackground(backgroundCol);
            
            if(i%2==0) {
                pane.add(getCircle());
                for(int j=0; j<n; j++) {
                    hEdge[i/2][j] = getHorizontalEdge();
                    hEdge[i/2][j].setText(new Linija(i/2, j, true).toString());
                    hEdge[i/2][j].setVerticalAlignment(SwingConstants.CENTER);
                    hEdge[i/2][j].setHorizontalAlignment(SwingConstants.CENTER);
                    pane.add(hEdge[i/2][j]);
                    pane.add(getCircle());
                }
            }
            else {
                for(int j=0; j<n; j++) {
                    vEdge[i/2][j] = getVerticalEdge();
                    vEdge[i/2][j].setText(new Linija(i/2, j, false).toString());
                    vEdge[i/2][j].setHorizontalAlignment(SwingConstants.CENTER);
                    vEdge[i/2][j].setVerticalAlignment(SwingConstants.CENTER);
                    pane.add(vEdge[i/2][j]);
                    box[i/2][j] = getBox();
                    pane.add(box[i/2][j]);
                }
                vEdge[i/2][n] = getVerticalEdge();
                vEdge[i/2][n].setText(new Linija(i/2, n, false).toString());
                vEdge[i/2][n].setHorizontalAlignment(SwingConstants.CENTER);
                vEdge[i/2][n].setVerticalAlignment(SwingConstants.CENTER);
                pane.add(vEdge[i/2][n]);
            }
            ++constraints.gridy;
            board.add(pane, constraints);
        }
        
        
        
        constraints.gridy = 0;
        constraints.gridx = 0;
        board.setBackground(backgroundCol);
        grid.add(board, constraints);
        ++constraints.gridx;
        playerPanel.setBackground(backgroundCol);
        grid.add(playerPanel, constraints);
        constraints.gridx = 0;

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(crvenaBoja);
        statusLabel.setFont(new Font("Serif", Font.BOLD, 25));
        statusLabel.setPreferredSize(new Dimension(2 * boardWidth, dist));
        ++constraints.gridy;
        grid.add(statusLabel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

        JButton goBackButton = new JButton("Nazad");
        goBackButton.addActionListener(backListener);
        ++constraints.gridy;
        grid.add(goBackButton, constraints);
        
        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);
        
        
        if(potezi != null) {
      	
        	for (Linija potez : new ArrayList<Linija>(potezi)) {
        		odigrajPotez(potez, true);
        	}
        
        }
        

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();

        frame.setContentPane(grid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        goBack = false;
        manageGame();

        while(!goBack) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        parent.inicijalizujGUI();
    }

}
