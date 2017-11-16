import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.BorderLayout;

public class Landing extends JPanel {
	JButton enterGame = new JButton("Enter Game");
	JTextField playerName = new JTextField("Enter player name.", 20);

	public Landing() {
		this.setLayout(new BorderLayout());

		this.add(playerName, BorderLayout.CENTER);
		this.add(enterGame, BorderLayout.CENTER);
	}
}