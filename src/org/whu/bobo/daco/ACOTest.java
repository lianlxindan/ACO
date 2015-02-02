package org.whu.bobo.daco;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
/**
 * 求解最优路径类
 * @author bobo
 *
 */
public class ACOTest {
	private Ant[] ordAnts;// 普通蚂蚁数组
	private Ant bestAnt; // 定义一个蚂蚁变量，用来保存搜索过程中的最优结果

	private Ant badestAnt; // 保存最差蚂蚁

	public Ant getBadestAnt() {
		return badestAnt;
	}

	public void setBadestAnt(Ant badestAnt) {
		this.badestAnt = badestAnt;
	}

	public Ant[] getOrdAnts() {
		return ordAnts;
	}

	public void setOrdAnts(Ant[] ordAnts) {
		this.ordAnts = ordAnts;
	}

	public Ant getBestAnt() {
		return bestAnt;
	}

	public void setBestAnt(Ant bestAnt) {
		this.bestAnt = bestAnt;
	}

	// 构造函数
	public ACOTest() {
		ordAnts = new Ant[ACO.N_ANT_COUNT];
		bestAnt = new Ant();
		badestAnt = new Ant();
	}

	// 初始化蚂蚁
	public void initData(String startRoad, String endRoad) {
		ACO aco = new ACO();
		aco.setRoadMap(0.1); // 构建路网拓扑
		// aco.setTempMap(0.1);
		AntNode sAntNode = aco.getAntNode(startRoad);
		AntNode eAntNode = aco.getAntNode(endRoad);
		eAntNode.setPheromone(2.0); // 最后一条路信息素设为最大
		AntNode curAntNode = sAntNode;
		for (int i = 0; i < ACO.N_ANT_COUNT; i++) {
			Ant temp = new Ant(sAntNode, eAntNode, curAntNode);
			ordAnts[i] = temp;
		}
		bestAnt.setMovedPathLength(Double.MAX_VALUE); // 初始化最好的蚂蚁的数据
		badestAnt.setMovedPathLength(Double.MIN_VALUE);// 初始化最差的蚂蚁的数据
	}

	// 更新每次迭代最好位置上的蚂蚁信息素
	public void updateBestAntBadestTrial() {
		double bestPheromone = (ACO.DBQ) / bestAnt.getMovedPathLength();
		double badestPheromone = (ACO.DBQ) / badestAnt.getMovedPathLength();
		if (bestPheromone - badestPheromone > 0.08) {
			for (int i = 0; i < bestAnt.getMovedPath().size() - 1; i++) {
				AntNode bestTemp = bestAnt.getMovedPath().get(i);
				if (!badestAnt.getMovedPath().contains(bestTemp)) {
					double tempPheromone = bestTemp.getPheromone();
					bestTemp.setPheromone(tempPheromone + bestPheromone);
					if (bestTemp.getPheromone() > 2.0) {
						bestTemp.setPheromone(2.0);
					}
				}
			}
			for (int i = 0; i < badestAnt.getMovedPath().size() - 1; i++) {
				AntNode badestTemp = badestAnt.getMovedPath().get(i);
				if (!bestAnt.getMovedPath().contains(badestTemp)) {
					badestTemp.setPheromone(badestTemp.getPheromone()
							- badestPheromone);
					if (badestTemp.getPheromone() < 0.01) {
						badestTemp.setPheromone(0.01);
					}
				}
			}
		}
		// else if (bestPheromone - badestPheromone < 0.01) {
		// // 这个有问题
		// for (int i = 0; i < ordAnts.length; i++) {
		// jumpOutLocalBest(ordAnts[i]);
		// }
		// }

		Set<AntNode> set = ACO.roadMap.keySet();
		for (Iterator<AntNode> iter = set.iterator(); iter.hasNext();) {
			AntNode key = (AntNode) iter.next();
			if (key.getPheromone() != 0.0) {
				double curTrial = key.getPheromone() * ACO.ROU;
				key.setPheromone(curTrial);
			}
			if (key.getPheromone() != 0.0) { // 最小最大信息素系统
				if (key.getPheromone() < 0.01) {
					key.setPheromone(0.01);
				} else if (key.getPheromone() > 2.0) {
					key.setPheromone(2.0);
				}
			}
		}
	}

	// 更新环境信息素
	public void updateTrial() {
		HashMap<AntNode, Double> tempTrial = new HashMap<AntNode, Double>();
		for (int i = 0; i < ACO.N_ANT_COUNT; i++) {
			for (int j = 0; j < ordAnts[i].getMovedPath().size() - 1; j++) {
				AntNode temp = ordAnts[i].getMovedPath().get(j);
				double pheromone = (ACO.DBQ) / ordAnts[i].getMovedPathLength();
				if (tempTrial.containsKey(temp)) {
					double tempPheromone = tempTrial.get(temp);
					tempTrial.put(temp, (tempPheromone + pheromone));
				} else {
					tempTrial.put(temp, pheromone);
				}
			}
		}
		Set<AntNode> set = ACO.roadMap.keySet();
		for (Iterator<AntNode> iter = set.iterator(); iter.hasNext();) {
			AntNode key = (AntNode) iter.next();
			if (tempTrial.containsKey(key)) {
				double preTrial = key.getPheromone() * ACO.ROU;
				double curTrial = tempTrial.get(key);
				key.setPheromone(preTrial + curTrial);
			} else {
				double curTrial = key.getPheromone() * ACO.ROU;
				key.setPheromone(curTrial);
			}
			if (key.getPheromone() != 0.0) { // 最小最大信息素系统
				if (key.getPheromone() < 0.01) {
					key.setPheromone(0.01);
				} else if (key.getPheromone() > 2.0) {
					key.setPheromone(2.0);
				}
			}
		}
	}

	// 开迭代始搜索
	public void search(String startRoad, String endRoad) {
		int continuousTime = 0; // 定义计数器 判断连续多少次最优路径没有更新
		double p = 0.3; // 连续迭代超过总迭代的百分之三十 则判断为陷入局部最优
		for (int i = 0; i < ACO.N_IT_COUNT; i++) {
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {
				ordAnts[j].move(); // 核心函数 蚂蚁开始移动搜索
				if (ordAnts[0].getMovedPath().size() == 0) { // 找不到路
					System.out.println("no road to the end!");
					return;
				}
			}
			Ant localBestAnt = ordAnts[0];
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {
				if (ordAnts[j].getMovedPathLength() < localBestAnt
						.getMovedPathLength()) {
					localBestAnt = ordAnts[j];
				}
				if (badestAnt.getMovedPathLength() < ordAnts[j] // 找出一次迭代中的最差蚂蚁
						.getMovedPathLength()) {
					badestAnt = ordAnts[j]; // 克隆一次迭代中最差蚂蚁
				}
			}
			if (localBestAnt.getMovedPathLength() < bestAnt
					.getMovedPathLength()) {
				bestAnt = localBestAnt.clone();
				continuousTime = 0;
			} else {
				continuousTime++;
			}
			if (continuousTime >= p * ACO.N_IT_COUNT) {// 如果经历这么多次迭代没有更新最优
				jumpOutLocalBest(localBestAnt); // 减少当前这条路径的信息素
			}
			if (continuousTime >= ACO.N_IT_COUNT / 2) {
				System.out.println((i + 1) + ": "
						+ bestAnt.getMovedPathLength());
				return;
			}
			int count = (int) (ACO.N_IT_COUNT * p);
			if (i < count) {
				updateBestAntBadestTrial();// 更新最优最差路径信息素
			} else if (i >= count && i <= ACO.N_IT_COUNT - count) {
				//updateBestAntBadestTrial();// 更新最优最差路径信息素
				updateTrial(); // 更新信息素
			} else {
				updateBestAntBadestTrial();// 更新最优最差路径信息素
			}
			System.out.println((i + 1) + ": " + bestAnt.getMovedPathLength());
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {// 重新设置每只蚂蚁
				resetAnt(ordAnts[j]);
			}
		}
	}

	// 跳出局部最优
	public void jumpOutLocalBest(Ant ant) {
		if (ant.getMovedPathLength() == bestAnt.getMovedPathLength()) {
			for (int i = 0; i < ant.getMovedPath().size(); i++) {
				AntNode temp = ant.getMovedPath().get(i);
				temp.setPheromone(temp.getPheromone() * 0.1);
				if (temp.getPheromone() < 0.01) {
					temp.setPheromone(0.01);
				}
			}
		} else if (ant.getMovedPathLength() > bestAnt.getMovedPathLength()) {
			for (int i = 0; i < ant.getMovedPath().size(); i++) {
				AntNode temp = ant.getMovedPath().get(i);
				temp.setPheromone(temp.getPheromone() * 0.01);
				if (temp.getPheromone() < 0.01) {
					temp.setPheromone(0.01);
				}
			}
		}
		Set<AntNode> set = ACO.roadMap.keySet(); // 没有走过的路的信息素增加
		for (Iterator<AntNode> iter = set.iterator(); iter.hasNext();) {
			AntNode key = (AntNode) iter.next();
			if (key.getCount() == 0 && key.getPheromone() != 0.0) {
				key.setPheromone(key.getPheromone() * 10);
			}
			if (key.getPheromone() > 2.0) {
				key.setPheromone(2.0);
			}
		}
	}

	// 重置蚂蚁
	public void resetAnt(Ant ant) {
		ant.setMovedPathLength(ant.getStartRoad().getRoadWeight());
		ant.setCurRoad(ant.getStartRoad());
		List<AntNode> movedPath = ant.getMovedPath();
		movedPath.clear();
		movedPath.add(ant.getStartRoad());
	}

	public static void main(String[] args) {
		ACOTest acoTest = new ACOTest();
		String startRoad = "-10425131";
		String endRoad = "4006702#2";
		acoTest.initData(startRoad, endRoad);
		long startTimeOne = System.currentTimeMillis();
		acoTest.search(startRoad, endRoad);
		long endTimeOne = System.currentTimeMillis();
		System.out.println("costTime: " + (endTimeOne - startTimeOne) + " ms");
		acoTest.bestAnt.displayPath();
	}
}
