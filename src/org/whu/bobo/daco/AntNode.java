package org.whu.bobo.daco;

public class AntNode {
	private String roadName; // ��·����
	private double roadWeight;// ��·Ȩֵ
	private double pheromone; // ��·��Ϣ��

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
