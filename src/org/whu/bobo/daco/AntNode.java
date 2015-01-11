package org.whu.bobo.daco;

public class AntNode {
	private String roadName; // ��·����

	private int count; // ��¼�ֵ��������߹��Ĵ���
	private double xPos; // ��·������
	private double yPos; // ��·������
	private double roadWeight;// ��·Ȩֵ
	private double pheromone; // ��·��Ϣ��
	//Ĭ�Ϲ��캯��
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
