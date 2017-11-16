import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.event.*;

public class CardsPanel extends JPanel {
	static Boolean inGame = new Boolean(false);
	static JPanel cards, landing, subLanding;
	static GamePanel game;
	JButton enterGame;
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
		this.enterGame = new JButton("Enter Game");
		this.subLanding.add(playerName);
		this.subLanding.add(enterGame);
		this.landing.add(subLanding, BorderLayout.CENTER);

		// Game screen: GamePanel
		this.game = new GamePanel();

		// ADD CARDS
		this.cards.add(landing, LAND);
		this.cards.add(game, GAME);

		this.add(cards, BorderLayout.CENTER);

		// Add actionListener to enterGame (for switching cards)
		this.enterGame.addActionListener(new GameListener(cards, GAME));

	}

	public class GameListener implements ActionListener {
		JPanel cards;
		String panel;

		public GameListener(JPanel cards, String panel) {
			inGame = true;
			this.cards = cards;
			this.panel = panel;
		}

		// put the checkers for the connection in here???
		public void actionPerformed(ActionEvent e) {
			CardLayout cl = (CardLayout)(cards.getLayout());
			cl.show(cards, panel);
		}
	}

	public static Boolean getInGame() {
		return inGame;
	}

	public GamePanel getGP() {
		return this.game;
	}
}