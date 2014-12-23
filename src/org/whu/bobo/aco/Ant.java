package org.whu.bobo.aco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Ant {
	int startRoadNumber;// ���ֵ�
	int endRoadNumber;// �յ�ֵ�
	int m_nCurRoadNumber;// ��ǰ���ڽֵ�
	double m_dbPathLength;// �����߹���·������
	List<Integer> movedPath = new ArrayList<Integer>();// �����Ѿ��߹���·��

	/**
	 * ���Ͽ�¡����
	 */
	public Ant clone() {
		Ant ant = new Ant();
		ant.startRoadNumber = this.startRoadNumber;
		ant.endRoadNumber = this.endRoadNumber;
		ant.m_nCurRoadNumber = this.m_nCurRoadNumber;
		ant.m_dbPathLength = this.m_dbPathLength;
		for (Integer i : this.movedPath) {
			ant.movedPath.add(i);
		}
		return ant;
	}

	/**
	 * ���ϳ�ʼ��
	 * 
	 * @param name
	 */
	public void init(int startRoadNumber, int endRoadNumber) {
		this.startRoadNumber = startRoadNumber;
		this.endRoadNumber = endRoadNumber;
		m_nCurRoadNumber = startRoadNumber;
		m_dbPathLength = 0.0;
		movedPath.add(startRoadNumber);
	}

	/**
	 * �����������ߵĽֵ�
	 * 
	 * @return
	 */
	public List<Integer> setAllowRoad() {
		HashMap<Integer, Double> temp = ACO.roadNetInfo.get(m_nCurRoadNumber);
		List<Integer> allowedRoad = new ArrayList<Integer>();
		if (temp != null) {
			Set<Integer> set = temp.keySet();
			for (Iterator<Integer> iter = set.iterator(); iter.hasNext();) {
				Integer key = (Integer) iter.next();
				if (ACO.g_Trial[m_nCurRoadNumber][key] != 0.0
						&& !movedPath.contains(key)) {
					allowedRoad.add(key);
				}
			}
		}
		return allowedRoad;
	}

	/**
	 * ѡ����һ��·��
	 * 
	 * @return
	 */
	public int chooseNextRoad() {
		int selectedRoad = -1;
		double dbTotal = 0.0;
		List<Integer> allowedRoad = setAllowRoad();
		int preRoadNumber = -1;
		while (allowedRoad.size() == 0) {// �ߵ�����ͬ
			if (movedPath.size() > 2) {
				preRoadNumber = movedPath.get(movedPath.size() - 2);
			} else {
				preRoadNumber = startRoadNumber;
			}
			ACO.g_Trial[preRoadNumber][m_nCurRoadNumber] = 0.0;
			m_nCurRoadNumber = preRoadNumber;
			movedPath.remove(movedPath.size() - 1);
			allowedRoad = setAllowRoad();
		}
		double[] prob = new double[allowedRoad.size()];
		double maxProb = Double.MIN_VALUE;
		int maxProbRoad = -1;
		for (int i = 0; i < allowedRoad.size(); i++) {
			int nextRoadNum = allowedRoad.get(i);
			if (!movedPath.contains(nextRoadNum)) {// �¸�·�ڲ����Ѿ��߹���·����
				double a = ACO.g_Trial[m_nCurRoadNumber][nextRoadNum];
				double b = ACOUtil.getAOInstance().getDistance(
						m_nCurRoadNumber, nextRoadNum);
				double x = Math.pow(a, ACO.ALPHA);
				double y = Math.pow(1.0 / b, ACO.BETA);
				prob[i] = x * y;
				if (prob[i] > maxProb) {
					maxProbRoad = allowedRoad.get(i);
				}
				dbTotal = dbTotal + prob[i];
			} else {
				prob[i] = 0.0;
			}
		}

		double p = ACOUtil.rnd(0.0, 1.0); // α�����¼�ѡ���·
		if (p > ACO.P) {
			// �����������ѡ��
			double dbTemp = 0.0;
			if (dbTotal > 0.0) {
				dbTemp = ACOUtil.rnd(0.0, dbTotal);// ȡһ�������
				for (int i = 0; i < allowedRoad.size(); i++) {
					int nextRoadNum = allowedRoad.get(i);
					if (!movedPath.contains(nextRoadNum)) {
						dbTemp = dbTemp - prob[i];
						if (dbTemp < 0.0) {// ����ֹͣת��������·�ڱ�ţ�ֱ������ѭ��
							selectedRoad = nextRoadNum;
							break;
						}
					}
				}
			}
		} else { // ѡ�������ʵ�·��
			selectedRoad = maxProbRoad;
		}
		return selectedRoad;
	}

	/**
	 * ���Ͻ�������һ��
	 */
	public void search(int startRoad, int endRoad) {
		init(startRoad, endRoad);
		if (startRoad == endRoad) {
			return;
		}
		int nRoadNo = -1;
		while (nRoadNo != endRoad) {
			nRoadNo = chooseNextRoad();
			if (nRoadNo > 0) {
				movedPath.add(nRoadNo);
				m_nCurRoadNumber = nRoadNo;
			} else {
				System.out.println("No way to the road!");
				return;
			}
		}
		calPathLength();
	}

	/**
	 * ��ӡ����·��
	 */
	public void displayPath() {
		for (int i = 0; i < movedPath.size(); i++) {
			if (i != movedPath.size() - 1) {
				System.out.print(movedPath.get(i) + "->");
			} else {
				System.out.print(movedPath.get(i));
			}
		}
		System.out.println();
	}

	/**
	 * ��ӡ����·������
	 */
	public void displayPathName() {
		System.out.print("bestAnt Path: ");
		for (int i = 0; i < movedPath.size(); i++) {
			if (i != movedPath.size() - 1) {
				System.out.print(getmovedPathName(movedPath.get(i)) + "->");
			} else {
				System.out.print(getmovedPathName(movedPath.get(i)) + ".");
			}
		}
		System.out.println();
	}

	public String getmovedPathName(int MovedPathID) {
		Iterator<String> it = ACOUtil.roadMaps.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (ACOUtil.roadMaps.get(key) == MovedPathID) {
				return key;
			}
		}
		return null;
	}

	/**
	 * ���������߹���·������
	 */
	public void calPathLength() {
		m_dbPathLength = 0.0;
		for (int i = 1; i < movedPath.size(); i++) {
			int m = movedPath.get(i);
			int n = movedPath.get(i - 1);
			m_dbPathLength += ACOUtil.getAOInstance().getDistance(n, m);
		}
	}

	/**
	 * ������������
	 */
	public void resetAnt(int startRoadNumber, int endRoadNumber) {
		movedPath.clear();
		this.startRoadNumber = startRoadNumber;
		this.endRoadNumber = endRoadNumber;
		m_nCurRoadNumber = startRoadNumber;
		m_dbPathLength = 0.0;
	}

	public static void main(String[] args) {
		Ant a = new Ant();
		System.out.println(a.getmovedPathName(5));
	}
}
