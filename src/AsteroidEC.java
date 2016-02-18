import java.util.Random;

/**
 * This class represents an asteroid. This code is completed for you, but you
 * may find it useful to study this code to understand how subclasses of
 * GameObject should be implemented.
 */
public class AsteroidEC extends Asteroid {	

private Random rng = new Random();

	/**
	 * Creates a new Asteroid at the location specified.
	 * 
	 * @param game the parent Game
	 * @param position the location of the asteroid
	 * @param type the type of asteroid
	 */
	public AsteroidEC(Game game, Position position, int type) {
		
		// Call the super class constructor to initialize this object.
		// The first argument, game, is a reference to the parent game.
		// The second argument, position, gives the position of this asteroid in
		// the playing field.
		// The third argument, Config.ASTEROID_RADIUS[type], gives the radius of
		// this asteroid. The radius defines the asteroid's collision boundary.
		super(game, position, type);
		
		// Set the image for this asteroid. setImage() is a method defined in
		// GameObject, the parent class of Asteroid.
		setImage(Config.ASTEROID_IMG[type]);
		
		// Make the asteroid visible. This will cause the asteroid to be drawn
		// automatically. visible is a field declared in GameObject.
		setVisible(true);
		
		// Make the asteroid collidable. This will cause the game to detect when
		// other objects collide with this asteroid. collidable is a field
		// declared in GameObject.
		setCollidable(true);
		
		setRotation(rng.nextDouble()*(2*Math.PI));
		setSpeed(rng.nextDouble()* Config.ASTEROID_RADIUS[type]);
	
	}

	
	/**
	 * Called automatically at each game tick to handle game logic. However,
	 * asteroids have no associated logic that must be run at every tick.
	 */
	@Override
	public void update(double deltaTime) {
		
		double xPos;
		double yPos;
		
		if (getPosition().getX()
		- Config.ASTEROID_RADIUS[1] < 0 || getPosition().getX() +
		Config.SHIP_RADIUS > Config.GAME_WIDTH) {
			xPos = (wrap(Config.GAME_WIDTH, getPosition().getX()));
		} else {xPos = getPosition().getX();}
		
		
		if (getPosition().getY() 
		- Config.ASTEROID_RADIUS[1] < 0 || getPosition().getY() + 
		Config.SHIP_RADIUS > Config.GAME_HEIGHT){
			yPos = (wrap(Config.GAME_HEIGHT, getPosition().getY()));
		} else {yPos = getPosition().getY();}
		
		setPosition(xPos, yPos);
		updatePosition(deltaTime);
	
		
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
}

