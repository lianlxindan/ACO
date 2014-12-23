package org.whu.bobo.test;

import org.whu.bobo.astar.AStar;

public class AlgorithmTest {
	public static void main(String[] args) {
		AStar astar = new AStar();
		astar.setRoadTempMap();
		astar.search("1", "10");
	}
}
