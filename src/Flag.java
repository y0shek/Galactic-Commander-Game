/**
 * This class represents a flag.  A flag can be in one of four states.
 * <ul>
 * <li><b>PICKED</b>: The flag is being carried by the opposing player's ship.
 * The opposing player will score a point if this flag can be brought to the
 * opposing player's base. The flag is not visible and not collidable.</li>
 * <li><b>DROPPING</b>: The flag is being dropped by the opposing player's ship.
 * It cannot be picked up again until the dropping timer has expired. The flag
 * is visible and collidable.</li>
 * <li><b>DROPPED</b>: The flag has been dropped on the playing field. It can be
 * picked up by the opposing player's ship. The flag is visible and collidable.
 * </li>
 * </ul>
 * The flag has a timer which determines when the flag can be picked up off the
 * field (the dropping timer). The timer is started when the flag is initially
 * dropped by a ship. However, the dropping timer is restarted every time the
 * opponent's ship collides with the flag and the flag is still in the DROPPING
 * state. This allows a ship to drop the flag while moving slowly and not
 * immediately pick it up again. In order to pick up a flag, the ship must move
 * off the flag so that they are no longer colliding. This allows the dropping
 * timer to expire. The {@code update()} method, which is called automatically
 * at every game tick, sees that the dropping timer has expired and sets the
 * flag's state to DROPPED. If the ship then moves back and collides with the
 * flag again, it can be picked up because it is in the DROPPED state.
 */
public class Flag extends GameObject {
	/**
	 * Indicates a flag that has been picked up by the opposing player's ship. 
	 */
	public static final int PICKED     = 1;
	
	/**
	 * Indicates a flag that is being dropped (the dropping timer has not
	 * expired).
	 */
	public static final int DROPPING   = 2;
	
	/**
	 * Indicates a flag that has been dropped on the playing field (the
	 * dropping timer has expired).
	 */
	public static final int DROPPED    = 3;
	
    // Declare additional class variables here if necessary. Remember that all
	// class variables you add must be declared private!
	private int playerID;
    private int flagState;
    private GameTimer droppingTimer;
    private Position flagInitPosition;


	/**
	 * Creates a new flag. The flag starts in the Dropped state. The image is
	 * set to {@code Config.FLAG_IMG[playerID]}. It is visible and collidable.
	 * The dropping timer is created but not started.
	 * 
	 * @param game the parent {@link Game}.
	 * @param playerID the owning player, one of {@code Config.PLAYER1_ID} or
	 * {@code Config.PLAYER2_ID}.
	 */
	public Flag(Game game, int playerID, double x, double y) {
		super(game, new Position(x, y), Config.FLAG_RADIUS);
		
		setImage(Config.FLAG_IMG[playerID]);
		setVisible(true);
		setCollidable(true);
		this.playerID = playerID;
		flagState = DROPPED;
		droppingTimer = new GameTimer();
		flagInitPosition = getPosition();
	}
	
	/**
	 * Returns the ID of the owning player of this flag.
	 * 
	 * @return the owning player's ID.
	 */
	public int getPlayerID() {

        return playerID;
	}

	/**
	 * Returns the flag's current state: one of {@code AT_HOME}, {@code PICKED},
	 * {@code DROPPING}, or {@code DROPPED}.
	 * 
	 * @return the flag's state.
	 */
	public int getState() {

        return flagState;
	}

	/**
	 * Returns {@code true} if this flag can be picked up by the specified
	 * player. That is, it returns true if the specified player is not the
	 * flag's owning player and the flag's state is DROPPED.
	 * 
	 * @param playerID one of {@code Config.PLAYER1} or {@code Config.PLAYER2}
	 * @return {@code true} if the flag can be picked up by the player.
	 */
	public boolean canBePicked(int playerID) {

		if(getState() == DROPPED && playerID != getPlayerID()){
			return true;
		}
		
        return false;
	}

	/**
	 * Puts the flag in the PICKED state. If the flag is not in the DROPPED
	 * state, does nothing. Otherwise, makes the flag invisible and not
	 * collidable.
	 */
	public void pickUp() {

		
		if(getState() == DROPPING){
			return;
		}
		else if(getState() != DROPPED){
			return;
		}
		else{
			flagState = PICKED;
			setVisible(false);
			setCollidable(false);
		}
		

	}

	/**
	 * Called automatically at each game tick to handle game logic. If this flag
	 * is in the DROPPING state and the dropping timer has expired, it enters
	 * the DROPPED state.
	 * <p>
     * Do NOT call this method from your code! It is called automatically each
     * tick.
     * </p>
	 * 
	 * @param deltaTime the time in seconds between this and the previous tick
	 */
	@Override
	public void update(double deltaTime) {
		if(getState() == DROPPING && droppingTimer.hasTimerExpired()){
			flagState = DROPPED;
		}
	}

	/**
	 * Handles a collision with a ship. This method is called automatically by
	 * the game at each tick that this flag and a ship collide. If the flag is
	 * in the DROPPING state and the other ship's owning player is not the same
	 * as the flag's owning player, this method restarts the dropping timer.
	 * <p>
     * Do NOT call this method from your code! It is called automatically when
     * this ship collides with a ship.
     * </p>
	 * 
	 * @param ship the {@link Ship} the flag collided with
	 */
	public void collideShip(Ship ship) {
		
		if(getState() == DROPPING && playerID != ship.getPlayerID()){
			droppingTimer.startTimer(Config.FLAG_DROP_TIME);
					
		}
		ship.collideFlag(this);
	}

	/**
	 * Drops the flag at the location specified by {@code here}. The flag enters
	 * the DROPPING state and the dropping timer is started. The flag becomes
	 * collidable and visible.
	 * 
	 * @param here a {@link Position} giving the flag's dropped position 
	 */
	public void drop(Position here) {
		//set location of dropped flag
		flagState = DROPPING;
		setPosition(here);
		//set flag attibutes after dropped
		setImage(Config.FLAG_IMG[getPlayerID()]);
		setVisible(true);
		setCollidable(true);
		droppingTimer.startTimer(Config.FLAG_DROP_TIME);
		
		
		
		
		
		
	}

	/**
	 * Sets the flag to its initial configuration. The position is set to
	 * the flag's starting position (i.e., the position it had when it was
	 * first created), it is made visible and collidable, and the state is
	 * set to DROPPED.
	 */
	public void reset() {
		flagState = DROPPED;
		setPosition(flagInitPosition);
		setVisible(true);
		setCollidable(true);
		setImage(Config.FLAG_IMG[getPlayerID()]);
		
	}
	/**
	 * getter method to retrieve private droppingTimer data
	 * @return boolean true if the dropping timer has expired
	 */
	public boolean getTimer(){
		return droppingTimer.hasTimerExpired();
	}
}
