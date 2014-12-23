package org.whu.bobo.astar;

public class ANode {
	private String roadName;
	private double xPos;
	private double yPos;
	private double roadWeight;
	private ANode parentNode;
	private double g;
	private double h;
	private double f;

	public ANode(String roadName, double xPos, double yPos, double roadWeight,
			ANode parentNode) {
		this.roadName = roadName;
		this.xPos = xPos;
		this.yPos = yPos;
		this.roadWeight = roadWeight;
		this.parentNode = parentNode;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	public double getRoadWeight() {
		return roadWeight;
	}

	public void setRoadWeight(double roadWeight) {
		this.roadWeight = roadWeight;
	}

	public ANode getParentNode() {
		return parentNode;
	}

	public void setParentNode(ANode parentNode) {
		this.parentNode = parentNode;
	}

	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getF() {
		return f;
	}

	public void setF(double f) {
		this.f = f;
	}

}
