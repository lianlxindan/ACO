package org.whu.bobo.daco;

import java.util.ArrayList;
import java.util.List;

public class Ant {
	private AntNode startRoad;// 起点街道
	private AntNode endRoad;// 终点街道
	private AntNode curRoad; // 当前所在街道
	private double movedPathLength;// 蚂蚁走过的路径长度
	private List<AntNode> movedPath;// 蚂蚁已经走过的路径

	public AntNode getStartRoad() {
		return startRoad;
	}

	public void setStartRoad(AntNode startRoad) {
		this.startRoad = startRoad;
	}

	public AntNode getEndRoad() {
		return endRoad;
	}

	public void setEndRoad(AntNode endRoad) {
		this.endRoad = endRoad;
	}

	public AntNode getCurRoad() {
		return curRoad;
	}

	public void setCurRoad(AntNode curRoad) {
		this.curRoad = curRoad;
	}

	public double getMovedPathLength() {
		return movedPathLength;
	}

	public void setMovedPathLength(double movedPathLength) {
		this.movedPathLength = movedPathLength;
	}

	public List<AntNode> getMovedPath() {
		return movedPath;
	}

	public void setMovedPath(List<AntNode> movedPath) {
		this.movedPath = movedPath;
	}

	// 默认构造函数
	public Ant() {
		startRoad = null;
		endRoad = null;
		curRoad = null;
		movedPathLength = 0.0;
		movedPath = new ArrayList<AntNode>();
	}

	// 构造函数
	public Ant(AntNode startRoad, AntNode endRoad, AntNode curRoad) {
		this.startRoad = startRoad;
		this.endRoad = endRoad;
		this.curRoad = startRoad;
		movedPathLength = startRoad.getRoadWeight();
		movedPath = new ArrayList<AntNode>();
		movedPath.add(startRoad);
		startRoad.setCount(startRoad.getCount() + 1);
	}

	// 蚂蚁克隆函数
	public Ant clone() {
		AntNode startRoad = this.startRoad;
		AntNode endRoad = this.endRoad;
		AntNode curRoad = this.curRoad;
		double movedPathLength = this.movedPathLength;
		Ant ant = new Ant(startRoad, endRoad, curRoad);
		ant.movedPath.clear();
		for (AntNode node : this.movedPath) {
			ant.movedPath.add(node);
		}
		ant.setMovedPathLength(movedPathLength);
		return ant;
	}

	// 蚂蚁进行搜索一次
	public void move() {
		if (curRoad.getRoadName().equals(endRoad.getRoadName())) {
			return;
		}
		while (true) {
			curRoad = chooseNextRoad();
			if (curRoad != null) {
				curRoad.setCount(curRoad.getCount() + 1); // 蚂蚁走过的路径 标识+1;
				movedPath.add(curRoad);
				if (curRoad.getRoadName().equals(endRoad.getRoadName())) {
					checkmovedPath(); // 后处理路径
					break;
				}
			} else {
				return;
			}
		}
		calPathLength(); // 计算蚂蚁走过的道路的花费
	}

	// 选择下一条街道
	public AntNode chooseNextRoad() {
		AntNode selectedRoad = null;
		List<AntNode> allowedRoad = setAllowedRoad();
		AntNode preRoad = null; // 走过的前一个街道
		while (allowedRoad.size() == 0) {// 走到死胡同
			if (movedPath.size() > 2) {
				int index = movedPath.size() - 2;
				preRoad = movedPath.get(index);
			} else {
				preRoad = startRoad;
			}
			curRoad.setPheromone(0.0);// 将该条道路的信息素置为0.0
			curRoad = preRoad; // 进行回溯
			int removeIndex = movedPath.size() - 1;
			movedPath.remove(removeIndex);
			allowedRoad = setAllowedRoad();
			if (allowedRoad.size() == 0 && curRoad == startRoad) {
				movedPath.clear();
				return null;
			}
		}
		int size = allowedRoad.size();
		double[] prob = new double[size];
		double maxProb = Double.MIN_VALUE;
		AntNode maxProbRoad = null;
		double dbTotal = 0.0;
		for (int i = 0; i < allowedRoad.size(); i++) {
			AntNode nextRoad = allowedRoad.get(i);
			if (!movedPath.contains(nextRoad)) {
				double p = 0.1;
				double a = nextRoad.getPheromone();
				double b = countH(nextRoad, this.endRoad) * (1 - p) // 用与终点的距离和自身的长度作为期望因子
						+ p * nextRoad.getRoadWeight();
				if (b == 0.0) { // 当前点已经是终点 直接返回
					selectedRoad = nextRoad;
					return selectedRoad;
				}
				double x = Math.pow(a, ACO.ALPHA);
				double y = Math.pow(1.0 / b, ACO.BETA);
				prob[i] = x * y;
				dbTotal += prob[i];
				if (prob[i] > maxProb) {
					maxProbRoad = allowedRoad.get(i);
				}
			} else {
				nextRoad.setPheromone(0.0);
			}
		}
		double p = ACOUtil.rnd(0.0, 1.0);// 伪概率事件选择道路
		if (p > ACO.P) {// 下面进行轮盘选择
			double dbTemp = 0.0;
			if (dbTotal > 0.0) {
				dbTemp = ACOUtil.rnd(0.0, dbTotal);// 取一个随机数
				for (int i = 0; i < allowedRoad.size(); i++) {
					AntNode nextRoad = allowedRoad.get(i);
					dbTemp = dbTemp - prob[i];
					if (dbTemp < 0.0) {// 轮盘停止转动，记下路口，直接跳出循环
						selectedRoad = nextRoad;
						break;
					}
				}
			}
		} else {
			selectedRoad = maxProbRoad;
		}
		return selectedRoad;
	}

	// 计算蚂蚁走过的路径花费
	private void calPathLength() {
		movedPathLength = 0.0;
		for (int i = 0; i < movedPath.size(); i++) {
			AntNode temp = movedPath.get(i);
			movedPathLength += temp.getRoadWeight();
		}
	}

	// 打印蚂蚁走过的路径
	public void displayPath() {
		if (movedPath.size() == 0) {
			return;
		}
		System.out.print("bestAnt Path: ");
		for (int i = 0; i < movedPath.size(); i++) {
			if (i != movedPath.size() - 1) {
				System.out.print(movedPath.get(i).getRoadName() + "->");
			} else {
				System.out.print(movedPath.get(i).getRoadName() + ".");
			}
		}
		System.out.println();
	}

	// 蚂蚁可以选择的街道
	public List<AntNode> setAllowedRoad() {
		List<AntNode> allowedRoad = ACO.roadMap.get(curRoad);
		List<AntNode> result = new ArrayList<AntNode>();
		for (int i = 0; i < allowedRoad.size(); i++) {
			AntNode temp = allowedRoad.get(i);
			if (temp.getPheromone() != 0.0 && !movedPath.contains(temp)) {
				result.add(temp);
			}
		}
		return result;
	}

	// 计算节点到终点的欧式距离
	public double countH(AntNode node, AntNode eNode) {
		double res = 0.0;
		res = Math.sqrt(Math.pow(node.getxPos() - eNode.getxPos(), 2)
				+ Math.pow(node.getyPos() - eNode.getyPos(), 2));
		return res;
	}

	// 计算自适应阈值
	public double countThreshold(int iter) {
		double num = Math.pow(iter, 2);
		return 1 - Math.pow(Math.E, -num);
	}

	// 检查是否出现路径是否出现绕路
	private void checkmovedPath() {
		for (int i = 0; i < movedPath.size(); i++) {
			for (int j = i + 2; j < movedPath.size(); j++) {
				AntNode node_i = movedPath.get(i);
				AntNode node_j = movedPath.get(j);
				if (ACO.roadMap.get(node_i).contains(node_j)) {
					int index_i = i;
					int index_j = j;
					for (int k = index_i + 1; k < index_j; k++) {
						AntNode temp = movedPath.get(index_i + 1);
						movedPath.remove(temp);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
	}
}
