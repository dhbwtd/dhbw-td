/*  Copyright (C) 2013 by Jan-Christoph Klie, Inc. All rights reserved.
 *  Released under the terms of the GNU General Public License version 3 or later.
 *  
 *  Contributors:
 *  Jan-Christoph Klie - All
 *  Sebastian Muszytowski - Add waypoint support
 */

package de.dhbw.td.core.level;

import java.util.LinkedList;
import java.util.Queue;

import playn.core.Surface;
import de.dhbw.td.core.game.IDrawable;
import de.dhbw.td.core.util.Point;
import de.dhbw.td.core.util.ETileType;

public class Level implements IDrawable {

	private static final int TILE_OFFSET = 64; // px
	public final int height;
	public final int width;
	public final int tilesize;
	public final int startx;
	public final int starty;
	public final ETileType[][] map;
	private Queue<Point> waypoints;

	private static Queue<Point> changeCopyWaypoints(Queue<Point> waypoints) {
		LinkedList<Point> cloned = new LinkedList<Point>();
		for (Point p : waypoints) {
			cloned.add(new Point(p));
		}
		cloned.getFirst().translate(-TILE_OFFSET, 0);
		cloned.getLast().translate(TILE_OFFSET, 0);
		return cloned;
	}

	public static Queue<Point> copyWaypoints(Queue<Point> waypoints) {
		LinkedList<Point> cloned = new LinkedList<Point>();
		for (Point p : waypoints) {
			cloned.add(new Point(p));
		}
		return cloned;
	}

	public Level(ETileType[][] map, Queue<Point> waypoints, int tilesize, int width, int height, int startx, int starty) {
		this.tilesize = tilesize;
		this.width = width;
		this.height = height;
		this.startx = startx;
		this.starty = starty;
		this.waypoints = waypoints;

		if (map.length != height) {
			throw new IllegalArgumentException("Specified height differs from height of map array!");
		}

		this.map = map;
	}

	public int width() {
		return width * tilesize;
	}

	public int height() {
		return height * tilesize;
	}

	/**
	 * Creates a deep copy of the waypoints in this level
	 * 
	 * @return Returns a -copy- of the waypoints specified for this level
	 */
	public Queue<Point> waypoints() {
		return changeCopyWaypoints(waypoints);
	}

	@Override
	public void draw(Surface surf) {
		surf.clear();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				surf.drawImage(map[row][col].image(), col * tilesize, row * tilesize);
			}
		}
	}

}
