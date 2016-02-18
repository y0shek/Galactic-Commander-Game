import java.io.IOException;

/**
 * This class is the program entry point and implements game initialization,
 * input handling, and scoring. It keeps track of each player's ship, base,
 * flag, and score. This is where you must add code to create game objects and
 * add them to the game, handle input, and keep track of the score.
 */
public class GalacticCommander extends Game {
    // Declare additional class variables here if necessary. Remember that all
	// class variables you add must be declared private!

	/**
	 * Creates a new instance of the game. This method is completed for you.
	 * <p>
	 * DO NOT add, change, or remove code here!
	 * </p>
	 * 
	 * @throws IOException
	 */
	public GalacticCommander() throws IOException {
		super();
	}

	private Ship ship1;
	private Ship ship2;
	
	private Flag flag1;
	private Flag flag2;
	
	private Base base1;
	private Base base2;
	
	private int p1Score;
	private int p2Score;
	private boolean endGame;
	
	
	/**
	 * This is the game's main method.  It creates a new game object,
	 * initializes the game, and starts the game loop. It is completed for you.
	 * <p>
	 * DO NOT add, change, or remove code here!
	 * </p>
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		// THIS METHOD HAS BEEN COMPLETED FOR YOU. DO NOT ADD CODE HERE.
		
		// Create a new game
		Game game = new GalacticCommander();
		// Initialize the game
		game.initGame();
		// Start the game
		game.runGameLoop();
	}

	/**
	 * Initializes the game. Creates each of the ships, flags, bases, and
	 * asteroids and adds them to the game. Also initializes the player's
	 * scores.
	 */
	@Override
	void initGame() {
		// 0. Create and add asteroids (complete)
		for (int i = 0; i < Config.ASTEROID_POSITIONS.length; i++) {
			Asteroid a = new Asteroid(this, Config.ASTEROID_POSITIONS[i], i);
			addGameObject(a);
		}

		// 1. Create one Base object for each player. The first argument to the
		//    constructor is the parent Game, which is this. The second argument
		//    is the PlayerID, which should be one of Config.PLAYER1_ID or
		//    Config.PLAYER2_ID. The last two arguments give the position of the
		//    base, which should be set to Config.BASE_PLAYER1_X,
		//    Config.BASE_PLAYER1_Y, and so on.
		
		base1 = new Base(this, Config.PLAYER1_ID, Config.BASE_PLAYER1_X,
					Config.BASE_PLAYER1_Y);
		
		base2 = new Base(this, Config.PLAYER2_ID, Config.BASE_PLAYER2_X,
				Config.BASE_PLAYER2_Y);
		
		addGameObject(base1);
		addGameObject(base2);
		
		// 2. Add one ship for each player to the game. The first four arguments
		//    to the Ship constructor are analogous to the argument for bases.
        //    The fifth argument is the initial rotation of the ship, which
        //    should be set to Config.SHIP_PLAYER1_ROTATION or
        //    Config.SHIP_PLAYER2_ROTATION, as appropriate.
		
		ship1 = new Ship(this, Config.PLAYER1_ID, Config.SHIP_PLAYER1_X, 
					Config.SHIP_PLAYER1_Y, Config.SHIP_PLAYER1_ROTATION);
		
		ship2 = new Ship(this, Config.PLAYER2_ID, Config.SHIP_PLAYER2_X, 
					Config.SHIP_PLAYER2_Y, Config.SHIP_PLAYER2_ROTATION);
		
		
		addGameObject(ship1);
		addGameObject(ship2);
		
		// 3. Add one flag for each player to the game. The initial x and y
		//	  coordinates are the same as the corresponding base. That is,
		//	  player 1's flag starts at the exact same position as player 1's
		//	  base.
		
		flag1 = new Flag(this, Config.PLAYER1_ID, Config.BASE_PLAYER1_X, 
				Config.BASE_PLAYER1_Y);
		flag2 = new Flag(this, Config.PLAYER2_ID, Config.BASE_PLAYER2_X,
				Config.BASE_PLAYER2_Y);
		
		addGameObject(flag1);
		addGameObject(flag2);

		// 4. Initialize the score displays. Each player's score should be set
		//    to zero. Player1's score should be displayed in
		//    Graphics.LEFT_PANEL using getGraphics().setPanelText(). Player2's
		//    score should be displayed in Graphics.RIGHT_PANEL. You may
		//    optionally set the font, font size, and font color if you like.
		
		getGraphics().setPanelText(Graphics.LEFT_PANEL, "Player 1: "+p1Score );
				
	
		getGraphics().setPanelText(Graphics.RIGHT_PANEL, "Player 2: "+p2Score);
	
		
		
	} // End of initGame() 

	/**
	 * Handles keyboard input at each game tick. This method checks if each of
	 * the control keys is pressed, and if so calls the corresponding method of
	 * the appropriate player's ship.
	 * 
	 * @param deltaTime the time since the previous tick in seconds.
	 */
	@Override
	void handleInput(double deltaTime) {

		// Handle rotation for Player 1. If the A key is pressed, rotate player
		// 1's ship counter-clockwise (CCW). If the D key is pressed, rotate
        // player 1's ship clockwise (CW). If both A and D are pressed, the ship
        // should not rotate. Remember that the direction of rotation (CW or
        // CCW) is given by the sign of the angle: positive angles are CW,
        // negative  angles are CCW. To calculate how much to rotate the ship,
        // you must multiply the rotation speed (given by
        // Config.SHIP_ROTATE_SPEED) by deltaTime.

		if(getKeyboard().isAPressed() && getKeyboard().isDPressed()){
				ship1.rotate(0);
		} else if (getKeyboard().isAPressed()) {
				ship1.rotate(-Config.SHIP_ROTATE_SPEED * deltaTime);
		} else if (getKeyboard().isDPressed()) {
				ship1.rotate(Config.SHIP_ROTATE_SPEED * deltaTime);
		}
		
		// Handle acceleration for Player 1. If the W key is pressed, increase
        // the speed of player 1's ship. If the S key is pressed, decrease the
        // speed. To calculate how much to change the speed, you must multiply
        // the ship's acceleration (given by Config.SHIP_ACCELERATION) by
        // deltaTime.
		if(getKeyboard().isWPressed() && getKeyboard().isSPressed()){
			ship1.accelerate(0);
		} else if (getKeyboard().isWPressed()) {
			ship1.accelerate(Config.SHIP_MAX_SPEED * deltaTime);
		} else if (getKeyboard().isSPressed()) {
			ship1.accelerate(-Config.SHIP_MAX_SPEED * deltaTime);
		}
		
		// Handle rotation for Player 2 
		if(getKeyboard().isLeftPressed() && getKeyboard().isRightPressed()){
			ship2.rotate(0);
		} else if (getKeyboard().isLeftPressed()) {
			ship2.rotate(-Config.SHIP_ROTATE_SPEED * deltaTime);
		} else if (getKeyboard().isRightPressed()) {
			ship2.rotate(Config.SHIP_ROTATE_SPEED * deltaTime);
		}

		// Handle acceleration for Player 2 	
		if(getKeyboard().isUpPressed() && getKeyboard().isDownPressed()){
			ship2.accelerate(0);
		} else if (getKeyboard().isUpPressed()) {
			ship2.accelerate(Config.SHIP_MAX_SPEED * deltaTime);
		} else if (getKeyboard().isDownPressed()) {
			ship2.accelerate(-Config.SHIP_MAX_SPEED * deltaTime);
		}

		// Handle firing for Player 1. If the left shift key is pressed, call
        // player 1's ship's fire() method.
		if(getKeyboard().isLshiftPressed()){
			ship1.fire();
		}

		// Handle firing for Player 2 
		if(getKeyboard().isEnterPressed()){
			ship2.fire();
		}

	} // End of handleInput()

	/**
	 * Increments the score for the indicated player. If the player has scored
	 * {@code Config.POINTS_TO_WIN} points, this method ends the game by
	 * calling {@code endGame()} and sets the text in {@code
	 * Graphics.CENTER_PANEL} to a message indicating which player won.
	 * 
	 * @param playerID the player who scored.
	 */
	@Override
	public void score(int playerID) {

		if(playerID == Config.PLAYER1_ID){
			p1Score++;
			getGraphics().setPanelText(Graphics.LEFT_PANEL, 
					"Player 1: " + p1Score);
		}
		
		if(playerID == Config.PLAYER2_ID){
			p2Score++;
			getGraphics().setPanelText(Graphics.RIGHT_PANEL, 
					"Player 2: " + p2Score);
		}
		
		if(p1Score == Config.POINTS_TO_WIN || p2Score == Config.POINTS_TO_WIN){
			endGame = true;
			endGame();
		}
		
		// Check if the game is over; if so, set an appropriate victory message
		// in Graphics.CENTER_PANEL and end the game. 
		if(endGame == true){
			
			if (p1Score == Config.POINTS_TO_WIN)
			{
			getGraphics().setPanelText(Graphics.CENTER_PANEL, "Player 1 " +
					"has won! ALL YOUR BASE ARE BELONG TO US");
			}
			if (p2Score == Config.POINTS_TO_WIN)
			{
				getGraphics().setPanelText(Graphics.CENTER_PANEL, "Player 2 " +
						"has won! ALL YOUR BASE ARE BELONG TO US");
			}
		}
	} // End of score()
	
}
