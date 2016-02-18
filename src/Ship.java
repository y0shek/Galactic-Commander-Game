/**
 * This class represents a ship. A ship can be in one of three states:
 * <ul>
 * <li><b>CAPTURING</b>: The ship is able to fly about the playing area. It
 * can pick up a flag by flying over the opponent's base (if the opponent's flag
 * is at home) or by flying over the flag itself (if the flag has been dropped
 * on the playing field). The ship is visible and collidable and can fire
 * bullets.</li>
 * <li><b>SCORING</b>: The ship is carrying a flag. The player can score by
 * flying over the player's base (if the player's flag is at home). The ship is
 * visible and collidable. It cannot fire a bullet until it has dropped the flag
 * (thereby entering the CAPTURING state).</li>
 * <li><b>DESTROYED</b>: The ship has been destroyed. Its position is constant
 * and its rotation is zero. It is visible but not collidable.</li>
 * </ul>
 * A ship has two timers: the firing timer and the respawn timer. The firing
 * timer is set each time the ship fires; the ship cannot fire again until the
 * timer has expired. For the purposes of resetting the firing timer, dropping
 * the flag by attempting to fire counts as firing, even though no bullet is
 * actually fired. The respawn timer is set when the ship is destroyed. When it
 * expires, the ship is respawned with its starting position and rotation and in
 * the Capturing state.
 */
public class Ship extends GameObject {
	/**
	 * Indicates a ship that has not picked up a flag.
	 */
    public static final int CAPTURING  = 1;
    
    /**
     * Indicates a ship that has picked up a flag.
     */
    public static final int SCORING    = 2;
    
    /**
     * Indicates a ship that has been destroyed.
     */
    public static final int DESTROYED  = 3;
    
    // Declare additional class variables here if necessary. Remember that all
 	// class variables you add must be declared private!
    private int playerID;
    private int playerState;
    private GameTimer respawnTimer;
    private GameTimer firingTimer;
	private Position reset;
	private Position bulletPosition;
	private double bulletRotation;
	private Flag shipFlag;

    /**
     * Creates a new ship. The ship's position and rotation are set to the
     * provided coordinates and rotation. The ship's speed is set to zero. The
	 * ship's state is {@code Ship.CAPTURING}, the image is
	 * {@code Config.SHIP_CAPTURING_IMG[playerID]}, and it is made visible and
	 * collidable. The firing timer and respawn timer are created (but not
	 * started).
     * 
     * @param game the parent game
     * @param playerID the ID of the ship's owning player
     * @param x the ship's initial x position.
     * @param y the ship's initial y position.
     * @param rotation the ship's initial rotation.
     */
    public Ship(Game game, int playerID, double x, double y, double rotation) {
        super(game, new Position(x, y), Config.SHIP_RADIUS);

		// Set the image using setImage() and make the ship visible using
		// setVisible().
        setImage(Config.SHIP_CAPTURING_IMG[playerID]);
        setVisible(true);
        setCollidable(true);
        this.playerID = playerID;
        playerState = CAPTURING;
       
        reset = getPosition();

		// Set the rotation of the ship using setRotation().
        setRotation(rotation);
        
        //Create Timers
        respawnTimer = new GameTimer();
        firingTimer = new GameTimer();
    }

 	/**
	 * Rotates the ship by the specified angle. That is, this method adds its
	 * argument to the ship's current rotation. This method does not rotate the
	 * ship if the ship is destroyed.
	 *
	 * @param angle the angle of rotation in radians
	 */
	public void rotate(double angle) {
		
		// To rotate the ship, you must add the angle parameter to the
		// ship's current rotation.  You can get the ship's current rotation
		// by calling getRotation().
		if(getState() != DESTROYED){
		 setRotation(angle + getRotation());
		}
		
	}

	/**
	 * Adds the specified change in velocity to the ship's speed. This method
	 * guarantees that the ship's speed is within the range 
	 * {@code -Config.SHIP_MAX_SPEED} to {@code Config.SHIP_MAX_SPEED}.
	 *
	 * @param speedDelta the change in speed in pixels/sec
	 */
	public void accelerate(double speedDelta) {

		// Add speedDelta to the ship's current speed.
		double currentSpeed = speedDelta + getSpeed();
	
		// If the new speed is outside the allowed range, set the ship's speed
		// to the nearest allowed value.
		
		if (currentSpeed >= Config.SHIP_MAX_SPEED){
			currentSpeed = Config.SHIP_MAX_SPEED;
		}
		
		if (currentSpeed <= -Config.SHIP_MAX_SPEED){
			currentSpeed = -Config.SHIP_MAX_SPEED;
		}
		
		// Set the ship's speed by calling setSpeed().
		setSpeed(currentSpeed);
	}

    /**
     * Called automatically at each game tick to handle the ship's game logic.
     * If the ship is not in the Destroyed state, updates the ship's position
     * and destroys the ship if it has collided with the boundary of the playing
     * field. Otherwise, if the ship is destroyed and the respawn timer has
     * expired, respawns the ship in the Capturing state at its original
     * position and with its original speed and rotation.
     * <p>
     * Do NOT call this method from your code! It is called automatically each
     * tick.
     * </p>
     *
     * @param deltaTime the time since the previous tick in seconds.
     */
    @Override
    public void update(double deltaTime) {
    	
        // If the ship is destroyed, check if the respawn timer has expired, and
        // if so respawn the ship.  
    	if(getState() == DESTROYED && respawnTimer.hasTimerExpired()){
			respawnShip();
    	}
    	
        // Otherwise, if the ship is not destroyed, update its position.
    	if(getState() != DESTROYED){
    		updatePosition(deltaTime);
		}
        // A ship must be destroyed if it collides with the boundary of the
        // playing field. The ship collides with the playing field if its
		// position goes beyond any edge of the playing field (i.e. if the X
		// coordinate is less than 0 or more than Config.GAME_WIDTH and
		// similarly for the Y coordinate and the height of the playing field).
    	
    	//Current Position of the Ship
    	Position ShipPos = getPosition();
    	double yPos;
    	//If ship goes over left and right boundaries
    	if( (ShipPos.getX() - Config.SHIP_RADIUS < 0) || 
    			(ShipPos.getX() + Config.SHIP_RADIUS > Config.GAME_WIDTH )){
    		if( getState() != DESTROYED){
    		destroyShip();
    		
    		}
    	}
    	//If ship goes over top and bottom boundaries
		if (getPosition().getY() 
		- Config.SHIP_RADIUS < 0 || getPosition().getY() + 
		Config.SHIP_RADIUS > Config.GAME_HEIGHT){
			yPos = (wrap(Config.GAME_HEIGHT, getPosition().getY()));
		} else {yPos = getPosition().getY();}
		
		setPosition(getPosition().getX(), yPos);
    	
    }

    /**
     * Returns the ID of the owning player of this ship: one of {@code
	 * Config.PLAYER1} or {@code Config.PLAYER2}.
     * 
     * @return the ID owning player
     */
    public int getPlayerID() {
    	return playerID;
    }
    
    /**
     * Returns the ship's current state: one of {@code Ship.CAPTURING},
     * {@code Ship.SCORING}, or {@code Ship.DESTROYED}.
     * 
     * @return the ship's state.
     */
    public int getState() {
    	return playerState;
    }

    /**
     * Handles a collision with a ship. This method is called automatically by
	 * the game at each tick that this ship and another ship collide. This
	 * method destroys the ship.
	 * <p>
     * Do NOT call this method from your code! It is called automatically when
     * this ship collides with another ship.
     * </p>
     */
    public void collideShip() {
    	destroyShip();
	}

    /**
     * Handles a collision with an asteroid. This method is called automatically
     * by the game at each tick that this ship collides with an asteroid. This
	 * method destroys the ship.
	 * <p>
     * Do NOT call this method from your code! It is called automatically when
     * this ship collides with an asteroid.
     * </p>
     */
	public void collideAsteroid() {
		destroyShip();
	}

    /**
     * If this ship is in the Scoring state, returns the flag this ship is
     * carrying. If the ship is not in the Scoring state, returns {@code null}.
     *
     * @return the {@link Flag} this ship is carrying, or null.
     */
    public Flag getFlag() {
    	if(getState() == SCORING){
    		return shipFlag;
    	}

        return null;
    }

    /**
     * Handles a collision with a flag. This method is called automatically by
     * the game at each tick that this ship collides with a flag. This method
     * checks whether the flag can be picked up by this ship. If so, it picks it
     * up, saving a reference to the flag and changing the ship's state to
     * SCORING.
     * <p>
     * Do NOT call this method from your code! It is called automatically when
     * this ship collides with a flag.
     * </p>
	 * 
     * @param flag the {@link Flag} this ship collided with.
     */
	public void collideFlag(Flag flag) {
		
		// If this ship collided with a flag that can be picked up, as
		// determined by the flag's canBePicked() method, save a reference in
		// this ship object, notify the flag that it has been picked up, and
		// put this ship in the Scoring state.
		if(flag.canBePicked(getPlayerID())){
			//Saved Reference
			shipFlag = flag;
			flag.pickUp();
			playerState = SCORING;
			setImage(Config.SHIP_SCORING_IMG[getPlayerID()]);
		}
		//Owning player can return the flag
		if(flag.getPlayerID() == playerID){
			flag.reset();
		}
	}
   
	/**
	 * Handles a collision with a base. This method is called automatically by
     * the game at each tick that this ship collides with a base. If the ship
     * is in the Capturing state and the opponent's flag can be stolen from this
     * base, this method steals the flag and puts the ship in the Scoring state.
     * If the ship is in the Scoring state and the flag can be scored at this
     * base, this method scores the flag and the ship returns to the Capturing
     * state.
     * <p>
     * Do NOT call this method from your code! It is called automatically when
     * this ship collides with a base.
     * </p>
	 * 
	 * @param base the {@link Base} this ship collided with.
	 */
	public void collideBase(Base base) {
		// If the ship is in the Scoring state and the ship can score a flag at
        // this base, call the game's score() method, the flag's reset() method,
        // and put the ship in the Capturing state.
		if(getState() == SCORING && base.canScoreFlag(getPlayerID())== true){
			//GalacticCommander.getGame().score(getPlayerID());
			playerState = CAPTURING;
			setImage(Config.SHIP_CAPTURING_IMG[getPlayerID()]);
			shipFlag.reset();
			getGame().score(getPlayerID());
		}
		

    }

	/**
	 * Fires a bullet if this ship is able to fire. If the ship is destroyed or
	 * the firing timer has not expired yet, this method does nothing. If the
	 * ship is in the Scoring state, drops the flag, resets the firing timer,
	 * and enters the capturing state. Otherwise, creates a new bullet object
	 * and adds it to the game.
	 */
    public void fire() {
    	
    	// If the ship is destroyed or the timer hasn't expired, it can't fire
    	// and this method does nothing.
    	if(getState() == DESTROYED || !firingTimer.hasTimerExpired()){
    		return;
    	// Otherwise, set the firing timer.
    	}else{
    		firingTimer.startTimer(Config.SHIP_FIRE_DELAY);
    	}
        
    	
        // If the ship is in the Capturing state, first calculate the position
		// of the new bullet. Get a copy of the ship's position, then move that
		// position in the direction of the ship's rotation using the
		// moveInDirection() method provided by Position objects. The distance
		// moved should be equal to the ship's radius plus the bullet's radius.
        // Finally, create the new bullet and add it to the game.
    	if(getState() == CAPTURING){
    		bulletPosition = getPosition();
    		bulletRotation = getRotation();
    		bulletPosition.moveInDirection(bulletRotation,(Config.SHIP_RADIUS + 
    				Config.BULLET_RADIUS));
    		
    		Bullet bullet = new Bullet(getGame(), 
    				bulletPosition, bulletRotation);
    		getGame().addGameObject(bullet);
    	}
		// If the ship is in the Scoring state, drop the flag and enter the
		// Capturing state without firing a bullet. 
    	if(getState() == SCORING){
    		shipFlag.drop(getPosition());
    		playerState = CAPTURING;
            setImage(Config.SHIP_CAPTURING_IMG[getPlayerID()]);
    		return;
    	}
    }

    /**
     * Handles a collision with a bullet. This method is called automatically by
	 * the game at each tick that this ship collides with a bullet. This method
     * destroys the ship.
     * <p>
     * Do NOT call this method from your code! It is called automatically when
     * this ship collides with a bullet.
     * </p>
     */
	public void collideBullet() {
		destroyShip();
	}
	
	/**Drops the flag where the ship is destroyed and sends that 
	*information to the drop method
	*/
	private void destroyShip() {
		if(getState() == SCORING){
			shipFlag.drop(getPosition());
		}
		playerState = DESTROYED;
		setImage(Config.SHIP_DESTROYED_IMG);
		respawnTimer.startTimer(Config.SHIP_RESPAWN_TIME);
		rotate(0);
		setCollidable(false);
		setPosition(getPosition());
		setVisible(true);
		setSpeed(0);
		
		
	}
	
	/**
	 * Respawns this Ship to initial orientation and location
	 */
	
	private void respawnShip(){
		setPosition(reset);
		playerState = CAPTURING;
		setImage(Config.SHIP_CAPTURING_IMG[getPlayerID()]);
		setVisible(true);
		setCollidable(true);
		setSpeed(0);
		if(getPlayerID() == Config.PLAYER2_ID){
			setRotation(Config.SHIP_PLAYER2_ROTATION);
		}else{setRotation(Config.SHIP_PLAYER1_ROTATION);}
	}
	
	public static double wrap(int size, double coord) {

	double finalCoord;
	if (coord >= size)
	{
	finalCoord = coord % size;
	}
	else if (coord % size == 0 && coord <= -1){
	finalCoord = coord % size;	
	}
	else if (coord <= -1){
	finalCoord = coord % size + size;
	}
	else finalCoord = coord;
		return finalCoord;
	}
	
}//End of Ship Class
