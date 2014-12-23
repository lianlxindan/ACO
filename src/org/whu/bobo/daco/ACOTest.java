package org.whu.bobo.daco;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ACOTest {
	private Ant[] ordAnts;// ��ͨ��������
	private Ant bestAnt; // ����һ�����ϱ����������������������е����Ž��

	// ���캯��
	public ACOTest() {
		ordAnts = new Ant[ACO.N_ANT_COUNT];
		bestAnt = new Ant();
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
				key.setPheromone(key.getPheromone() * ACO.ROU);
			}
		}
	}

	// ������ʼ����
	public void search(String startRoad, String endRoad) {
		initData(startRoad, endRoad);
		for (int i = 0; i < ACO.N_IT_COUNT; i++) {
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {
				ordAnts[j].move();
			}
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {
				if (bestAnt.getMovedPathLength() > ordAnts[j]
						.getMovedPathLength()) {
					bestAnt = ordAnts[j].clone(); // ��¡һ�ε�������������
				}
			}
			updateTrial(); // ������Ϣ��
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
		acoTest.search(startRoad, endRoad);
		acoTest.bestAnt.displayPath();
	}
}
