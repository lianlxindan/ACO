package org.whu.bobo.test;

import org.whu.bobo.daco.ACOTest;

public class AlgorithmTest {
	public static void main(String[] args) {
		String startRoad = "-10425130";
		String endRoad = "4006702#2";
		ACOTest acoTest = new ACOTest();
		acoTest.initData(startRoad, endRoad);
		long startTimeOne = System.currentTimeMillis();
		acoTest.search(startRoad, endRoad);
		long endTimeOne = System.currentTimeMillis();
		System.out.println("costTime: " + (endTimeOne - startTimeOne) + " ms");
		acoTest.getBestAnt().displayPath();
	}
}
