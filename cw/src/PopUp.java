// Contains panels for popups
// you can still make these prettier :)

import java.awt.*;
import javax.swing.*;

public class PopUp extends JPanel{
	JPanel panel; // this panel that will be returned
	int winner; // winning team

	// used for Help Panel
	JLabel instLabel;
	JTextArea instructions;

	// used for End Panel
	JPanel northPanel, centerPanel;
	JLabel endLabel, winnerTeam;
	JLabel nameL, teamL, scoreL;
	String[] players;

	public JPanel getHelpPanel() {
		this.panel = new JPanel(new BorderLayout());
		this.panel.setPreferredSize(new Dimension(500, 200));

		instLabel = new JLabel("INSTRUCTIONS", SwingConstants.CENTER);
		instLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		instLabel.setForeground(Color.WHITE);
		instLabel.setBackground(new Color(52, 56, 63));

		instructions = new JTextArea(
			"1. Move your character around using the arrow keys on your keyboard.\n\n" +
			"2. You cannot cross the river.\n\n" +
			"3. Shookt the other team's players by using the SLASH button.\n\n" +
			"4. The team that has the highest kills when time runs out wins the game!\n\n" +
			"5. Press SPACEBAR to chat with other players. Press ENTER to send your message.",
			10, 28);
		instructions.setLineWrap(true);
		instructions.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		instructions.setBackground(new Color(52, 56, 63));
		instructions.setForeground(Color.WHITE);
		this.panel.add(instLabel, BorderLayout.NORTH);
		this.panel.add(instructions, BorderLayout.CENTER);
		return this.panel;
	}

	// this can serve as a scoreboard
	public JPanel getEndPanel() {
		this.panel = new JPanel(new BorderLayout());
		this.panel.setBackground(new Color(52, 56, 63));
		this.panel.setPreferredSize(new Dimension(500, 200));

		// set up north panel: contains title and game winner
		setupEndNorth();

		// set up center panel: contains stats of the players
		setupEndCenter();

		this.panel.add(northPanel, BorderLayout.NORTH);
		this.panel.add(centerPanel, BorderLayout.CENTER);
		return this.panel;
	}

	public void setupEndNorth(){
		northPanel = new JPanel(new GridLayout(2, 1));
		northPanel.setOpaque(false);

		endLabel = new JLabel("END GAME", SwingConstants.CENTER);
		endLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
		endLabel.setForeground(Color.WHITE);

		if (getWinner() == 0) { // blue
			winnerTeam = new JLabel("BLUE TEAM WINS!", SwingConstants.CENTER);
			winnerTeam.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
			winnerTeam.setForeground(Color.CYAN);
		} else { // red
			winnerTeam = new JLabel("RED TEAM WINS!", SwingConstants.CENTER);
			winnerTeam.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
			winnerTeam.setForeground(Color.RED);
		}

		northPanel.add(endLabel, BorderLayout.NORTH);
		northPanel.add(winnerTeam, BorderLayout.CENTER);
	}

	public void setupEndCenter() {
		// get player info and put them into a gridLayout

		centerPanel = new JPanel(new GridLayout(players.length+1, 3));
		centerPanel.setOpaque(false);

		// name, team, and score
		nameL = new JLabel("NAME", SwingConstants.CENTER);
		nameL.setForeground(Color.WHITE);
		nameL.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		teamL = new JLabel("TEAM", SwingConstants.CENTER);
		teamL.setForeground(Color.WHITE);
		teamL.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		scoreL = new JLabel("SCORE", SwingConstants.CENTER);
		scoreL.setForeground(Color.WHITE);
		scoreL.setFont(new Font(Font.DIALOG, Font.BOLD, 15));

		centerPanel.add(nameL);
		centerPanel.add(teamL);
		centerPanel.add(scoreL);

		JLabel[] playerNames = new JLabel[players.length];
		JLabel[] playerTeams = new JLabel[players.length];
		JLabel[] playerScores = new JLabel[players.length];
		for (int i=0; i<players.length; i++) {
			String[] info = players[i].split(" ");
			playerNames[i] = new JLabel(info[1], SwingConstants.CENTER);
			playerNames[i].setFont(new Font(Font.DIALOG, Font.BOLD, 15));
			
			playerScores[i] = new JLabel("0", SwingConstants.CENTER); // wala pa kasing scoring huhu
			playerScores[i].setFont(new Font(Font.DIALOG, Font.BOLD, 15));

			if (Integer.parseInt(info[5]) == 0) { // everything is blue...
				playerTeams[i] = new JLabel("BLUE", SwingConstants.CENTER);
				playerTeams[i].setFont(new Font(Font.DIALOG, Font.BOLD, 15));

				playerNames[i].setForeground(Color.CYAN);
				playerTeams[i].setForeground(Color.CYAN);
				playerScores[i].setForeground(Color.CYAN);
			} else if (Integer.parseInt(info[5]) == 1){ // everything is red...
				playerTeams[i] = new JLabel("RED", SwingConstants.CENTER);
			playerTeams[i].setFont(new Font(Font.DIALOG, Font.BOLD, 15));

				playerNames[i].setForeground(Color.RED);
				playerTeams[i].setForeground(Color.RED);
				playerScores[i].setForeground(Color.RED);
			}

			centerPanel.add(playerNames[i]);
			centerPanel.add(playerTeams[i]);
			centerPanel.add(playerScores[i]);
		}

	}

	// for winning team
	public void setWinner(int team) {
		this.winner = team;
	}

	public int getWinner() {
		return this.winner;
	}

	// for player stats
	public void setPlayers(String[] p) {
		this.players = p;
	}

	public String[] getPlayers() {
		return this.players;
	}
}