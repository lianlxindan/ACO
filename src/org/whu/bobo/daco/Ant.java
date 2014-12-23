package org.whu.bobo.daco;

import java.util.ArrayList;
import java.util.List;

public class Ant {
	private AntNode startRoad;// ���ֵ�
	private AntNode endRoad;// �յ�ֵ�
	private AntNode curRoad; // ��ǰ���ڽֵ�
	private double movedPathLength;// �����߹���·������
	private List<AntNode> movedPath;// �����Ѿ��߹���·��

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

	// Ĭ�Ϲ��캯��
	public Ant() {

	}

	// ���캯��
	public Ant(AntNode startRoad, AntNode endRoad, AntNode curRoad) {
		this.startRoad = startRoad;
		this.endRoad = endRoad;
		this.curRoad = startRoad;
		movedPathLength = startRoad.getRoadWeight();
		movedPath = new ArrayList<AntNode>();
		movedPath.add(startRoad);
	}

	// ���Ͽ�¡����
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

	// ���Ͻ�������һ��
	public void move() {
		if (curRoad.getRoadName().equals(endRoad.getRoadName())) {
			return;
		}
		while (true) {
			curRoad = chooseNextRoad();
			if (curRoad != null) {
				movedPath.add(curRoad);
				if (curRoad.getRoadName().equals(endRoad.getRoadName())) {
					break;
				}
			} else {
				System.out.println("No way to the road!");
				return;
			}
		}
		calPathLength(); // ���������߹��ĵ�·�Ļ���
	}

	// ѡ����һ���ֵ�
	public AntNode chooseNextRoad() {
		AntNode selectedRoad = null;
		List<AntNode> allowedRoad = setAllowedRoad();
		AntNode preRoad = null; // �߹���ǰһ���ֵ�
		while (allowedRoad.size() == 0) {// �ߵ�����ͬ
			if (movedPath.size() > 2) {
				int index = movedPath.size() - 2;
				preRoad = movedPath.get(index);
			} else {
				preRoad = startRoad;
			}
			curRoad.setPheromone(0.0);// ��������·����Ϣ����Ϊ0.0
			curRoad = preRoad; // ���л���
			int removeIndex = movedPath.size() - 1;
			movedPath.remove(removeIndex);
			allowedRoad = setAllowedRoad();
		}
		int size = allowedRoad.size();
		double[] prob = new double[size];
		double maxProb = Double.MIN_VALUE;
		AntNode maxProbRoad = null;
		double dbTotal = 0.0;
		for (int i = 0; i < allowedRoad.size(); i++) {
			AntNode nextRoad = allowedRoad.get(i);
			if (!movedPath.contains(nextRoad)) {
				double a = nextRoad.getPheromone();
				double b = nextRoad.getRoadWeight();
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
		double p = ACOUtil.rnd(0.0, 1.0);// α�����¼�ѡ���·
		if (p > ACO.P) {// �����������ѡ��
			double dbTemp = 0.0;
			if (dbTotal > 0.0) {
				dbTemp = ACOUtil.rnd(0.0, dbTotal);// ȡһ�������
				for (int i = 0; i < allowedRoad.size(); i++) {
					AntNode nextRoad = allowedRoad.get(i);
					dbTemp = dbTemp - prob[i];
					if (dbTemp < 0.0) {// ����ֹͣת��������·�ڣ�ֱ������ѭ��
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

	// ���������߹���·������
	private void calPathLength() {
		movedPathLength = 0.0;
		for (int i = 0; i < movedPath.size(); i++) {
			AntNode temp = movedPath.get(i);
			movedPathLength += temp.getRoadWeight();
		}
		movedPathLength -= movedPath.get(0).getRoadWeight();
	}

	// ��ӡ�����߹���·��
	public void displayPath() {
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

	// ���Ͽ���ѡ��Ľֵ�
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
}
