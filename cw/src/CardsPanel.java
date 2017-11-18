import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.event.*;

public class CardsPanel extends JPanel {
	static Boolean inGame = false, clickedEnter = false;
	static JPanel cards, landing, subLanding;
	static GamePanel game;
	static JButton enterGame;
	final static String LAND = "LANDING";
	final static String GAME = "GAME";

	// Landing page component
	static JTextField playerName = new JTextField("Enter your name", 20);

	public CardsPanel() {
		super(new BorderLayout());
		this.cards = new JPanel(new CardLayout());
		this.setComponents();
	}

	public void setComponents() {
		// Ask for player name
		this.landing = new JPanel(new BorderLayout());
		this.subLanding = new JPanel();	// holds components of landing

		this.enterGame = new JButton("Waiting for more players...");
		this.subLanding.add(playerName);
		this.subLanding.add(enterGame);
		this.landing.add(subLanding, BorderLayout.CENTER);

		// Game screen: GamePanel
		this.game = new GamePanel();

		// ADD CARDS
		this.cards.add(landing, LAND);
		this.cards.add(game, GAME);

		this.add(cards, BorderLayout.CENTER);

	}

	public static class GameListener implements ActionListener {
		JPanel cards;
		String panel;

		public GameListener(JPanel cards, String panel) {
			this.cards = cards;
			this.panel = panel;
		}

		// put the checkers for the connection in here???
		// we can include "waiting for players..."
		public void actionPerformed(ActionEvent e) {
			CardLayout cl = (CardLayout)(cards.getLayout());
			cl.show(cards, panel);
			clickedEnter = true;
		}
	}

	public static Boolean getInGame(String serverData) {
		if (serverData.startsWith("PLAYER")) {
			inGame = true;
			enterGame.setText("Ready!");
			// Add actionListener to enterGame (for switching cards)
			enterGame.addActionListener(new GameListener(cards, GAME));
		} else {
			inGame = false;
		}
		return inGame;
	}

	public static Boolean getClicked() {
		System.out.println("CLICKED: " + clickedEnter);
		return clickedEnter;
	}
}