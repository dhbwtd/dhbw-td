package de.dhbw.td.core.tower;

import static playn.core.PlayN.log;
import playn.core.Image;
import playn.core.Surface;
import pythagoras.d.Vector;
import de.dhbw.td.core.enemies.Enemy;
import de.dhbw.td.core.game.IUpdateable;
import de.dhbw.td.core.ui.IDrawable;
import de.dhbw.td.core.util.EFlavor;
import de.dhbw.td.core.util.Point;

public class Projectile implements IDrawable, IUpdateable {
	
	private final int damage;
	private final double speed;
	private final Enemy target;
	private final Image image;
	private final EFlavor flavor;
	
	private final float hwidth;
	private final float hheight;
	
	private boolean hit;
	
	private Point currentPosition;
	
	public Projectile(Point position, int damage, EFlavor flavor, double speed, Enemy target, Image image) {
		this.currentPosition = new Point(position);
		this.damage = damage;
		this.flavor = flavor;
		this.speed = speed;
		this.target = target;
		this.image = image;
		this.hwidth = image.width() / 2;
		this.hheight = image.height() / 2;
	}

	@Override
	public void update(double delta) {
		//Check if target is still alive
		if (!target.alive()) {
			hit = true;
			return;
		}
		
		//Calculates the move vector
		Vector vector = vector();
		vector = vector.scale(speed / vector.length());
		vector.set(vector.x * delta / 1000, vector.y * delta / 1000);
		
		//Check if projectile will hit the target, otherwies move the projectile
		if (Math.abs(target.position().x() - currentPosition.x()) <= Math.abs(vector.x) &&
				Math.abs(target.position().y() - currentPosition.y()) <= Math.abs(vector.y)) {
			hit = true;
			target.takeDamage(calcDamage(damage));
		} else {
			currentPosition.translate((int) vector.x, (int) vector.y);
		}
	}
	
	private int calcDamage(int damage) {
		log().debug("CRIT");
		return flavor == target.enemyType() ? 2 * damage : damage;
	}

	/**
	 * Calculates vector between target and current position
	 * @return The calculated vector
	 */
	private Vector vector() {
		return new Vector(target.position().x() - currentPosition.x(), 
				target.position().y() - currentPosition.y());
	}

	@Override
	public void draw(Surface surf) {
		if (!hit) {
			surf.save();
			surf.translate(currentPosition.x() + hwidth, currentPosition.y() + hwidth);
			surf.rotate((float) (vector().angle() + Math.PI / 2));
			surf.drawImage(image, -hwidth, -hheight);
			surf.restore();
		}
	}
	
	/**
	 * Returns if projectile has hit a tower
	 * @return True if target was hit, otherwise false
	 */
	public boolean hasHit() {
		return hit;
	}

}