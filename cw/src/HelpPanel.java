// you can still make this prettier :)

import java.awt.*;
import javax.swing.*;

public class HelpPanel extends JPanel{
	public JPanel getPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JTextArea instructions = new JTextArea(
			"1. Move your character around using the arrow keys on your keyboard.\n\n" +
			"2. You cannot cross the river.\n\n" +
			"3. Shookt the other team's players by using the [ / ] button.\n\n" +
			"4. The team that has the highest kills when time runs out wins the game!",
			10, 28);
		instructions.setLineWrap(true);
		instructions.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		instructions.setBackground(new Color(52, 56, 63));
		instructions.setForeground(Color.WHITE);
		panel.add(instructions, BorderLayout.CENTER);
		return panel;
	}
}