import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Container;

public class GamePanel extends JPanel {
	// North panel elements
	JPanel northPanel;
	JPanel buttonPanel, scorePanel;
	JButton exitBtn, helpBtn;
	JLabel redScore, blueScore, timeRem;
	String redScoreTxt, blueScoreTxt, timeRemTxt;

	// South panel elements
	JPanel southPanel;
	// Temporary solution: Buttons or pure text
	JButton[] instBtn = new JButton[5];
	JLabel[] instText = new JLabel[5];

	// Middle panel elements: map
	static MapPanel mapPanel = new MapPanel();

	public GamePanel() {
		super(new BorderLayout());
		frameSetup();
	}

	public void frameSetup() {
		// North panel setup
		this.redScoreTxt = "0";
		this.blueScoreTxt = "0";
		this.timeRemTxt = "0:00";

		this.northPanel = new JPanel(new GridLayout(1, 3));

		this.buttonPanel = new JPanel();
		this.exitBtn = new JButton("EXIT");
		this.helpBtn = new JButton("HELP");
		this.buttonPanel.add(this.exitBtn);
		this.buttonPanel.add(this.helpBtn);

		this.scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.redScore = new JLabel("RED " + this.redScoreTxt);
		this.blueScore = new JLabel(this.blueScoreTxt + " BLUE");
		this.scorePanel.add(this.redScore);
		this.scorePanel.add(this.blueScore);
		
		this.timeRem = new JLabel("TIME: " + this.timeRemTxt);

		this.northPanel.add(this.buttonPanel);
		this.northPanel.add(this.scorePanel);
		this.northPanel.add(this.timeRem);

		// South panel setup
		this.southPanel = new JPanel(new FlowLayout());
		// Instructions: buttons and text
		this.instBtn[0] = new JButton("W");
		this.instBtn[1] = new JButton("A");
		this.instBtn[2] = new JButton("S");
		this.instBtn[3] = new JButton("D");
		this.instBtn[4] = new JButton("E");

		this.instText[0] = new JLabel("UP");
		this.instText[1] = new JLabel("LEFT");
		this.instText[2] = new JLabel("DOWN");
		this.instText[3] = new JLabel("RIGHT");
		this.instText[4] = new JLabel("SHOOT");

		for (int i=0; i<5; i++) {
			this.southPanel.add(this.instBtn[i]);
			this.southPanel.add(this.instText[i]);
		}

		this.add(this.northPanel, BorderLayout.NORTH);
		this.add(this.southPanel, BorderLayout.SOUTH);
		this.add(this.mapPanel, BorderLayout.CENTER);
	}

	public MapPanel getMP() {
		return this.mapPanel;
	}
}