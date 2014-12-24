package org.whu.bobo.daco;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ACOTest {
	private Ant[] ordAnts;// ��ͨ��������
	private Ant bestAnt; // ����һ�����ϱ����������������������е����Ž��

	private Ant badestAnt; // �����������

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

	// ���캯��
	public ACOTest() {
		ordAnts = new Ant[ACO.N_ANT_COUNT];
		bestAnt = new Ant();
		badestAnt = new Ant();
	}

	// ��ʼ������
	public void initData(String startRoad, String endRoad) {
		ACO aco = new ACO();
		aco.setRoadMap(0.1); // ����·������
		AntNode sAntNode = ACO.getAntNode(startRoad);
		AntNode eAntNode = ACO.getAntNode(endRoad);
		AntNode curAntNode = sAntNode;
		for (int i = 0; i < ACO.N_ANT_COUNT; i++) {
			Ant temp = new Ant(sAntNode, eAntNode, curAntNode);
			ordAnts[i] = temp;
		}
		bestAnt.setMovedPathLength(Double.MAX_VALUE); // ��ʼ����õ����ϵ�����
		badestAnt.setMovedPathLength(Double.MIN_NORMAL);
	}

	// ����ÿ�ε������λ���ϵ�������Ϣ��
	public void updateBestTrial() {
		for (int i = 0; i < bestAnt.getMovedPath().size(); i++) {
			AntNode bestTemp = bestAnt.getMovedPath().get(i);
			double pheromone = ACO.DBQ / bestAnt.getMovedPathLength();
			bestTemp.setPheromone(bestTemp.getPheromone() + pheromone);
		}
		setMaxMinTrial();
	}

	public void updateBadestTrial() {
		for (int i = 0; i < badestAnt.getMovedPath().size(); i++) {
			AntNode badestTemp = badestAnt.getMovedPath().get(i);
			badestTemp.setPheromone(badestTemp.getPheromone() * ACO.ROU);
		}
		setMaxMinTrial();
	}

	// ���»�����Ϣ��
	public void updateTrial() {
		HashMap<AntNode, Double> tempTrial = new HashMap<AntNode, Double>();
		for (int i = 0; i < ACO.N_ANT_COUNT; i++) {
			for (int j = 0; j < ordAnts[i].getMovedPath().size(); j++) {
				AntNode temp = ordAnts[i].getMovedPath().get(j);
				double pheromone = ACO.DBQ / ordAnts[i].getMovedPathLength();
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
			if (key.getPheromone() != 0.0) { // ��С�����Ϣ��ϵͳ
				if (key.getPheromone() < 0.01) {
					key.setPheromone(0.01);
				} else if (key.getPheromone() > 10.0) {
					key.setPheromone(10.0);
				}
			}
		}
	}

	// ������С���ϵͳ
	private void setMaxMinTrial() {
		Set<AntNode> set = ACO.roadMap.keySet();
		for (Iterator<AntNode> iter = set.iterator(); iter.hasNext();) {
			AntNode key = (AntNode) iter.next();
			if (key.getPheromone() != 0.0) {
				if (key.getPheromone() < 0.01) {
					key.setPheromone(0.01);
				} else if (key.getPheromone() > 10.0) {
					key.setPheromone(10.0);
				}
			}
		}
	}

	// ������ʼ����
	public void search(String startRoad, String endRoad) {
		for (int i = 0; i < ACO.N_IT_COUNT; i++) {
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {
				ordAnts[j].move();
			}
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {
				if (bestAnt.getMovedPathLength() > ordAnts[j]
						.getMovedPathLength()) {
					bestAnt = ordAnts[j].clone(); // ��¡һ�ε�������������
				}
				if (badestAnt.getMovedPathLength() < ordAnts[j]
						.getMovedPathLength()) {
					badestAnt = ordAnts[j].clone(); // ��¡һ�ε������������
				}
			}
			updateTrial(); // ������Ϣ��
			int count = (int) (ACO.N_IT_COUNT * ACO.P * 2);
			if (i >= count && i <= count) {
				updateBestTrial();// ������Ϣ��
				updateBadestTrial();
			}
			System.out.println((i + 1) + ": " + bestAnt.getMovedPathLength());
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {// ��������ÿֻ����
				ordAnts[j].setMovedPathLength(ordAnts[j].getStartRoad()
						.getRoadWeight());
				ordAnts[j].setCurRoad(ordAnts[j].getStartRoad());
				List<AntNode> movedPath = ordAnts[j].getMovedPath();
				movedPath.clear();
				movedPath.add(ordAnts[j].getStartRoad());
			}
		}
	}

	public static void main(String[] args) {
		ACOTest acoTest = new ACOTest();
		String startRoad = "-10425130";
		String endRoad = "-31241816#3";
		acoTest.initData(startRoad, endRoad);
		long startTimeOne = System.currentTimeMillis();
		acoTest.search(startRoad, endRoad);
		long endTimeOne = System.currentTimeMillis();
		System.out.println("costTime: " + (endTimeOne - startTimeOne) + " ms");
		acoTest.bestAnt.displayPath();

	}
}
