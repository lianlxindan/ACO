package org.whu.bobo.dijkstra;

public class Node {
	private String roadName;
	private double roadWeight;
	private double shortestWeight;
	private Node parentNode;

	public Node(String roadName, double roadWeight) {
		this.roadName = roadName;
		this.roadWeight = roadWeight;
		this.shortestWeight = Double.MAX_VALUE;
		this.parentNode = null;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public double getRoadWeight() {
		return roadWeight;
	}

	public void setRoadWeight(double roadWeight) {
		this.roadWeight = roadWeight;
	}

	public double getShortestWeight() {
		return shortestWeight;
	}

	public void setShortestWeight(double shortestWeight) {
		this.shortestWeight = shortestWeight;
	}

}
