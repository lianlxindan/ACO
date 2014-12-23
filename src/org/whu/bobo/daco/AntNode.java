package org.whu.bobo.daco;

public class AntNode {
	private String roadName; // 道路名称
	private double roadWeight;// 道路权值
	private double pheromone; // 道路信息素

	public AntNode(String roadName, double roadWeight, double pheromone) {
		this.roadName = roadName;
		this.roadWeight = roadWeight;
		this.pheromone = pheromone;
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

	public double getPheromone() {
		return pheromone;
	}

	public void setPheromone(double pheromone) {
		this.pheromone = pheromone;
	}

}
