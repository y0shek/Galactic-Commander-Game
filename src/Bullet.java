/**
 * This class represents a bullet. Each time a ship fires, a new bullet is
 * created and added to the game. Bullets travel at a constant speed and in a
 * constant direction. The direction is determined by the rotation of the ship
 * that fired the bullet at the moment the bullet is fired. The bullet continues
 * traveling forward at constant speed until it collides with a ship or an
 * asteroid, or it leaves the playing field completely. When one of these events
 * occurs, the bullet must call the game's {@link
 * Game#removeGameObject(GameObject) removeGameObject()} method, providing
 * itself as the argument.
 */
public class Bullet extends GameObject {
    // Declare additional class variables here if necessary. Remember that all
	// class variables you add must be declared private!
	
	
	/**
	 * Creates a new bullet at the given position traveling in the given
	 * direction. The bullet's speed is set to {@code Config.BULLET_SPEED}, its
	 * image is set to {@code Config.BULLET_IMG}, and it is made visible and
	 * collidable.
	 * 
	 * @param game the parent {@link Game}
	 * @param position the initial position
	 * @param rotation the direction of travel
	 */
    public Bullet(Game game, Position position, double rotation) {
        super(game, position, Config.BULLET_RADIUS);
        setImage(Config.BULLET_IMG);
		setVisible(true);
		setCollidable(true);
		setPosition(position);
		setRotation(rotation);
		setSpeed(Config.BULLET_SPEED);
    }
   
    /**
     * Called automatically at each game tick to handle game logic. At each game
     * tick, the bullet moves itself by calling the super class method
     * updatePosition(). If it has moved completely off the playing field, it is
     * removed from the game.
     * 
     * @param deltaTime the time in seconds since the last tick
     */
    @Override
    public void update(double deltaTime) {
    	// Move the bullet by calling updatePosition()
    	updatePosition(deltaTime);
    	
        // If the bullet has left the playing field altogether, it must be
        // removed from the game. This is different from ships! A ship is
        // destroyed if it collides with the boundary of the playing field. A
        // bullet is removed from the game if it leaves the playing field.
    	Position BulletPos = getPosition();  
    	if( (BulletPos.getX() - Config.BULLET_RADIUS < 0) || 
    			(BulletPos.getX() + Config.BULLET_RADIUS > Config.GAME_WIDTH)){
    		getGame().removeGameObject(this);
    	}
        
    	if( (BulletPos.getY() - Config.BULLET_RADIUS < 0) || 
    			(BulletPos.getY() + Config.BULLET_RADIUS > Config.GAME_HEIGHT)){
    		getGame().removeGameObject(this);
    	}
    	
        // The bullet leaves the playing field if its position goes beyond 
        // any edge of the playing field (i.e., if the X coordinate is less than
        // 0 or more than the width of the game and similarly for the Y
		// coordinate).

    }

    /**
     * Called automatically by the game when this bullet collides with a ship.
     * The bullet removes itself from the game.
     */
    public void collideShip() {
		getGame().removeGameObject(this);
    }

    /**
     * Called automatically by the game when this bullet collides with an
     * asteroid. The bullet removes itself from the game.
     */
	public void collideAsteroid() {
		getGame().removeGameObject(this);
	}

}
