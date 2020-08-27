package etf.dotsandboxes.dn160597d;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private int n;
    private int m;
    private int nFromFile;
    private int mFromFile;
    private ArrayList<Linija> potezi = null;
    private Racunar redSolver, blueSolver;
    private String redName, blueName;
    private boolean isKorakPoKorak;
    private int redDepth, blueDepth;

    private JFrame frame = new JFrame();
    private JLabel greska;

    Integer[] brojevi = {3,4,5,6,7,8,9,10};
    String[] nivo = {"Human", "Computer", "AI"};
    
    JComboBox<String> redNivoList = new JComboBox<String>(nivo);
    JComboBox<String> blueNivoList = new JComboBox<String>(nivo);
    JComboBox<Integer> redDubinaList = new JComboBox<Integer>(brojevi);
    JComboBox<Integer> blueDubinaList = new JComboBox<Integer>(brojevi);
    JComboBox<Integer> mSize = new JComboBox<Integer>(brojevi);
    JComboBox<Integer> nSize = new JComboBox<Integer>(brojevi);
    JCheckBox korakPoKorak = new JCheckBox();
        
    //GUI
    JPanel grid = new JPanel(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    
    JButton openButton;
    JButton cancelButton;
    JTextArea log;
    JFileChooser fc;
    JScrollPane logScrollPanel;

    public Main() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5,5,0,0);
        
        log = new JTextArea(3,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        fc = new JFileChooser();
        
    }

    private boolean startGame;

    private Racunar dohvatiRacunar(int level) {
        switch (level) {
		case 1:
			return new RacunarPocetnik();
		case 2:
			return new RacunarAlfaBeta();
		case 3:
			return new RacunarTakmicarski();
		default:
			return null;
		}
    }

    private ActionListener submitListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int rNIndex = redNivoList.getSelectedIndex();
            int bNIndex = blueNivoList.getSelectedIndex();
            
            redDepth = brojevi[redDubinaList.getSelectedIndex()];
            blueDepth = brojevi[blueDubinaList.getSelectedIndex()];
            
            isKorakPoKorak = korakPoKorak.isSelected() && rNIndex>0 && bNIndex>0;

            redName = nivo[rNIndex];
            blueName = nivo[bNIndex];

            redSolver = dohvatiRacunar(rNIndex);
            blueSolver = dohvatiRacunar(bNIndex);
            
            if(rNIndex>1) {
            	redSolver.setDepth(redDepth);
            }
            
            if(bNIndex>1) {
            	blueSolver.setDepth(blueDepth);
            }
            if (mFromFile == 0 && nFromFile == 0) {
                m = brojevi[mSize.getSelectedIndex()];
                n = brojevi[nSize.getSelectedIndex()];
            }else {
            	m = mFromFile;
            	n = nFromFile;
            }
            
            startGame = true;

        }
    };
    
    
    private ActionListener cancelFileListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

        	potezi = null;
			mFromFile = 0;
			nFromFile = 0;

            log.append("Canceled." + "\n");

        }
    };
    
    private ActionListener openFileListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int returnVal = fc.showOpenDialog(grid);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                Scanner sc = null;
                try {
                	potezi = new ArrayList<>();
					sc = new Scanner(file);
					mFromFile = sc.nextInt();
					nFromFile = sc.nextInt();
					
					while (sc.hasNext()) {
						String line = sc.next();
						Character prvi = line.charAt(0);
						Character drugi = line.charAt(1);
						if (Character.isUpperCase(prvi) && Character.isDigit(drugi)) {
							potezi.add(new Linija(prvi-'A',drugi-'0', false));
						}
						if (Character.isDigit(prvi) && Character.isUpperCase(drugi)) {
							potezi.add(new Linija(prvi-'0',drugi-'A', true));
						}
					}
					System.out.println(potezi);
					
				} catch (FileNotFoundException e1) {
					log.append("File not found."+ "\n");
				} finally {
					sc.close();
				}
                log.append("Moves from file: " + file.getName() + " are imported." + "\n");
            } else {
                log.append("Canceled." + "\n");
            }
            log.setCaretPosition(log.getDocument().getLength());

        }
    };

    public void inicijalizujGUI() {
    	
    	++constraints.gridy;
    	constraints.gridwidth = 3;
        grid.add(new JLabel("<html><h1>Dots and boxes", SwingConstants.CENTER), constraints);
        ++constraints.gridy;        
        
        greska = new JLabel("", SwingConstants.CENTER);
        greska.setForeground(Color.RED);
        greska.setPreferredSize(new Dimension(350, 20));
        grid.add(greska, constraints);
        ++constraints.gridy;    
        constraints.gridwidth = 1;

        
        constraints.gridx=1;
        grid.add(new JLabel("Player: ", SwingConstants.CENTER), constraints);
        ++constraints.gridx;
        grid.add(new JLabel("Tree depth (AI): ", SwingConstants.CENTER), constraints);
        constraints.gridx=0;
        ++constraints.gridy;
    
        JLabel crveniIgrac = new JLabel("Red player:    ", SwingConstants.RIGHT);
        crveniIgrac.setPreferredSize(new Dimension(120, 30));
        grid.add(crveniIgrac, constraints);
        ++constraints.gridx;
        grid.add(redNivoList, constraints);
        ++constraints.gridx;
        grid.add(redDubinaList, constraints);
        ++constraints.gridy;
        constraints.gridx=0;
        
        JLabel plaviIgrac = new JLabel("Blue player:    ", SwingConstants.RIGHT);
        plaviIgrac.setPreferredSize(new Dimension(120, 30));
        grid.add(plaviIgrac, constraints);
        ++constraints.gridx;
        grid.add(blueNivoList, constraints);
        ++constraints.gridx;
        grid.add(blueDubinaList, constraints);
        ++constraints.gridy;
        constraints.gridx=0;
        
        grid.add(new JLabel(" ", SwingConstants.CENTER), constraints);
        ++constraints.gridy;
        
        constraints.gridx++;
        grid.add(new JLabel("Step by step: ", SwingConstants.CENTER), constraints);
        constraints.gridy++;
        grid.add(korakPoKorak, constraints);
        ++constraints.gridy;
        constraints.gridx=0;
        
        grid.add(new JLabel(" ", SwingConstants.CENTER), constraints);
        ++constraints.gridy;
        
        constraints.gridwidth = 3;
        grid.add(new JLabel("Board size: ", SwingConstants.RIGHT), constraints);
        constraints.gridwidth = 1;
        ++constraints.gridy;
        
        JPanel velicina = new JPanel(new GridLayout(1,3));
        velicina.add(mSize);
        velicina.add(new JLabel("x", SwingConstants.CENTER));
        velicina.add(nSize);
        constraints.gridx =1;
        grid.add(velicina, constraints);
        ++constraints.gridy;
        constraints.gridx=0;  
        
        openButton = new JButton("Import game from file...");
        openButton.addActionListener(openFileListener);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(cancelFileListener);
 
        grid.add(new JLabel(" ", SwingConstants.CENTER), constraints);
        ++constraints.gridy;
        constraints.gridwidth=3;
        grid.add(openButton, constraints);
        ++constraints.gridy;
        grid.add(cancelButton, constraints);
        ++constraints.gridy;
        grid.add(log, constraints);
        ++constraints.gridy;
        constraints.gridwidth=1;

        
        grid.add(new JLabel(" ", SwingConstants.CENTER), constraints);
        JButton submitButton = new JButton("Start Game");
        submitButton.addActionListener(submitListener);
        ++constraints.gridy;
        constraints.gridwidth = 3;
        grid.add(submitButton, constraints);
        ++constraints.gridy;
        grid.add(new JLabel(" ", SwingConstants.CENTER), constraints);
         

        frame.setContentPane(grid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startGame = false;
        while(!startGame) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new Igra(this, frame, m, n, redSolver, blueSolver, redName, blueName, potezi, isKorakPoKorak);
    }

    public static void main(String[] args) {
        new Main().inicijalizujGUI();
    }

}
