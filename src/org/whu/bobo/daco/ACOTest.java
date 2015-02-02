package org.whu.bobo.daco;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
/**
 * �������·����
 * @author bobo
 *
 */
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
		eAntNode.setPheromone(2.0); // ���һ��·��Ϣ����Ϊ���
		AntNode curAntNode = sAntNode;
		for (int i = 0; i < ACO.N_ANT_COUNT; i++) {
			Ant temp = new Ant(sAntNode, eAntNode, curAntNode);
			ordAnts[i] = temp;
		}
		bestAnt.setMovedPathLength(Double.MAX_VALUE); // ��ʼ����õ����ϵ�����
		badestAnt.setMovedPathLength(Double.MIN_VALUE);// ��ʼ���������ϵ�����
	}

	// ����ÿ�ε������λ���ϵ�������Ϣ��
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
		// // ���������
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
			if (key.getPheromone() != 0.0) { // ��С�����Ϣ��ϵͳ
				if (key.getPheromone() < 0.01) {
					key.setPheromone(0.01);
				} else if (key.getPheromone() > 2.0) {
					key.setPheromone(2.0);
				}
			}
		}
	}

	// ���»�����Ϣ��
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
			if (key.getPheromone() != 0.0) { // ��С�����Ϣ��ϵͳ
				if (key.getPheromone() < 0.01) {
					key.setPheromone(0.01);
				} else if (key.getPheromone() > 2.0) {
					key.setPheromone(2.0);
				}
			}
		}
	}

	// ������ʼ����
	public void search(String startRoad, String endRoad) {
		int continuousTime = 0; // ��������� �ж��������ٴ�����·��û�и���
		double p = 0.3; // �������������ܵ����İٷ�֮��ʮ ���ж�Ϊ����ֲ�����
		for (int i = 0; i < ACO.N_IT_COUNT; i++) {
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {
				ordAnts[j].move(); // ���ĺ��� ���Ͽ�ʼ�ƶ�����
				if (ordAnts[0].getMovedPath().size() == 0) { // �Ҳ���·
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
				if (badestAnt.getMovedPathLength() < ordAnts[j] // �ҳ�һ�ε����е��������
						.getMovedPathLength()) {
					badestAnt = ordAnts[j]; // ��¡һ�ε������������
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
				jumpOutLocalBest(localBestAnt); // ���ٵ�ǰ����·������Ϣ��
			}
			if (continuousTime >= ACO.N_IT_COUNT / 2) {
				System.out.println((i + 1) + ": "
						+ bestAnt.getMovedPathLength());
				return;
			}
			int count = (int) (ACO.N_IT_COUNT * p);
			if (i < count) {
				updateBestAntBadestTrial();// �����������·����Ϣ��
			} else if (i >= count && i <= ACO.N_IT_COUNT - count) {
				//updateBestAntBadestTrial();// �����������·����Ϣ��
				updateTrial(); // ������Ϣ��
			} else {
				updateBestAntBadestTrial();// �����������·����Ϣ��
			}
			System.out.println((i + 1) + ": " + bestAnt.getMovedPathLength());
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {// ��������ÿֻ����
				resetAnt(ordAnts[j]);
			}
		}
	}

	// �����ֲ�����
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
		Set<AntNode> set = ACO.roadMap.keySet(); // û���߹���·����Ϣ������
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

	// ��������
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
