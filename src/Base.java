/**
 * This class represents a base.  Each player has one Base; the player to whom
 * the base belongs is referred to as the owning player. Each player's base,
 * ship, and flag start on the same side of the screen.
 */
public class Base extends GameObject {
    // Declare additional class variables here if necessary. Remember that all
	// class variables you add must be declared private!
	private int playerID;
	

	/**
	 * Creates a new Base. The image is set to {@code Config.BASE_IMG[playerID]}
	 * and it is visible and collidable.
	 * 
	 * @param game the {@link Game} to which this Base belongs.
	 * @param playerID the owning player (one of Config.PLAYER1 or Config.PLAYER2).
	 * @param x the initial x position
	 * @param y the initial y position
	 */
	public Base(Game game, int playerID, double x, double y) {
		super(game, new Position(x, y), Config.BASE_RADIUS);		
		setImage(Config.BASE_IMG[playerID]);
		setVisible(true);
		setCollidable(true);
		this.playerID = playerID;
	}
	
	/**
	 * Returns the ID of the owning player of this base. One of
	 * {@code Config.PLAYER1_ID} or {@code Config.PLAYER2_ID}.
	 * 
	 * @return the owning player.
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Returns true if the specified player can score a flag at this base.
	 * (i.e., the player is the owning player).
	 *  
	 * @param playerID one of {@link Config#PLAYER1_ID} or {@link
	 * Config#PLAYER2_ID}.
	 * @return {@code true} if the player can score a flag.
	 */
	public boolean canScoreFlag(int playerID) {
		if(getPlayerID() == playerID){
			return true;
		}else{
		return false;}
	}
	
	/**
	 * Called automatically at each game tick to handle game logic. However,
	 * bases have no associated logic that must be run at every tick.
	 */
	@Override
	public void update(double delta) {
		// This method does nothing.  DO NOT ADD CODE HERE!
	}
}
