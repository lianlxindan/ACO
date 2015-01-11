package org.whu.bobo.daco;

public class AntNode {
	private String roadName; // 道路名称

	private int count; // 记录街道被蚂蚁走过的次数
	private double xPos; // 道路横坐标
	private double yPos; // 道路纵坐标
	private double roadWeight;// 道路权值
	private double pheromone; // 道路信息素
	//默认构造函数
	public AntNode() {

	}

	public AntNode(String roadName, double roadWeight, double xPos,
			double yPos, double pheromone) {
		this.roadName = roadName;
		this.roadWeight = roadWeight;
		this.xPos = xPos;
		this.yPos = yPos;
		this.pheromone = pheromone;

		this.count = 0;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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
