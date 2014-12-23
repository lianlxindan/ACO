package org.whu.bobo.dstar;

public class DNode {
	private String roadName;
	private double xPos;
	private double yPos;
	private double roadWeight;
	private DNode parentNode;
	private double rhs;
	private double h;
	private double g;
	private Key key;

	public DNode(String roadName, double xPos, double yPos, double roadWeight,
			DNode parentNode) {
		this.roadName = roadName;
		this.xPos = xPos;
		this.yPos = yPos;
		this.roadWeight = roadWeight;
		this.parentNode = parentNode;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
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

	public DNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(DNode parentNode) {
		this.parentNode = parentNode;
	}

	public double getRhs() {
		return rhs;
	}

	public void setRhs(double rhs) {
		this.rhs = rhs;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

}
