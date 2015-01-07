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
		// aco.setTempMap(0.1);
		AntNode sAntNode = aco.getAntNode(startRoad);
		AntNode eAntNode = aco.getAntNode(endRoad);
		AntNode curAntNode = sAntNode;
		for (int i = 0; i < ACO.N_ANT_COUNT; i++) {
			Ant temp = new Ant(sAntNode, eAntNode, curAntNode);
			ordAnts[i] = temp;
		}
		bestAnt.setMovedPathLength(Double.MAX_VALUE); // ��ʼ����õ����ϵ�����
		badestAnt.setMovedPathLength(Double.MIN_VALUE);// ��ʼ���������ϵ�����
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

	// �����ֲ����ź���
	public void reduceBestPathTrial() {
		for (int i = 0; i < ACO.N_ANT_COUNT; i++) {
			for (int j = 0; j < ordAnts[i].getMovedPath().size(); j++) {// ��ǰ�ֲ�������Ϣ�ؼ�С
				if (ordAnts[i].getMovedPathLength() == bestAnt
						.getMovedPathLength()) {
					AntNode temp = ordAnts[i].getMovedPath().get(j);
					temp.setPheromone(temp.getPheromone() * 0.5);
				} else if (ordAnts[i].getMovedPathLength() > bestAnt
						.getMovedPathLength()) {
					AntNode temp = ordAnts[i].getMovedPath().get(j);
					temp.setPheromone(temp.getPheromone() * 0.1);
				}
			}
		}
		Set<AntNode> set = ACO.roadMap.keySet(); // û���߹���·����Ϣ������
		for (Iterator<AntNode> iter = set.iterator(); iter.hasNext();) {
			AntNode key = (AntNode) iter.next();
			if (key.getCount() == 0 && key.getPheromone() != 0.0) {
				key.setPheromone(key.getPheromone() * 5);
			}
		}
		setMaxMinTrial();
	}

	// ����ÿ�ε������λ���ϵ�������Ϣ��
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
				if (key.getPheromone() < 0.1) {
					key.setPheromone(0.1);
				} else if (key.getPheromone() > 1.0) {
					key.setPheromone(1.0);
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
				if (key.getPheromone() < 0.1) {
					key.setPheromone(0.1);
				} else if (key.getPheromone() > 1.0) {
					key.setPheromone(1.0);
				}
			}
		}
	}

	// ������ʼ����
	public void search(String startRoad, String endRoad) {
		int continuousTime = 0; // ��������� �ж��������ٴ�����·��û�и���
		double p = 0.1; // �������������ܵ����İٷ�֮��ʮ ���ж�Ϊ����ֲ�����
		for (int i = 0; i < ACO.N_IT_COUNT; i++) {
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {
				ordAnts[j].move(); // ���ĺ��� ���Ͽ�ʼ�ƶ�����
				if (ordAnts[0].getMovedPath().size() == 0) {
					System.out.println("no road to the end!");
					return;
				}
			}
			Ant localBestAnt = null;
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {
				localBestAnt = ordAnts[0]; // һ�ε����ֲ���������
				if (ordAnts[j].getMovedPathLength() < localBestAnt
						.getMovedPathLength()) {
					localBestAnt = ordAnts[j];
				}
				if (badestAnt.getMovedPathLength() < ordAnts[j] // �ҳ�һ�ε����е��������
						.getMovedPathLength()) {
					badestAnt = ordAnts[j].clone(); // ��¡һ�ε������������
				}
			}
			if (localBestAnt.getMovedPathLength() < bestAnt
					.getMovedPathLength()) {
				bestAnt = localBestAnt.clone();
				continuousTime = 0;
			} else {
				continuousTime++;
			}
			if (continuousTime >= p * ACO.N_IT_COUNT) {// ���������ô��ε���û�и�������
				reduceBestPathTrial(); // ���ٵ�ǰ����·������Ϣ��
			}
			int count = (int) (ACO.N_IT_COUNT * ACO.P * 5);
			if (i < count) {
				updateBestTrial();// ��������·����Ϣ��
				updateBadestTrial();// �������·����Ϣ��
			} else if (i >= count) {
				updateTrial(); // ������Ϣ��
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
