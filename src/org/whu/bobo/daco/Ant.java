package org.whu.bobo.daco;

import java.util.ArrayList;
import java.util.List;

/**
 * ������
 * 
 * @author bobo
 *
 */
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
		startRoad = null;
		endRoad = null;
		curRoad = null;
		movedPathLength = 0.0;
		movedPath = new ArrayList<AntNode>();
	}

	// ���캯��
	public Ant(AntNode startRoad, AntNode endRoad, AntNode curRoad) {
		this.startRoad = startRoad;
		this.endRoad = endRoad;
		this.curRoad = startRoad;
		movedPathLength = startRoad.getRoadWeight();
		movedPath = new ArrayList<AntNode>();
		movedPath.add(startRoad);
		startRoad.setCount(startRoad.getCount() + 1);
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
				curRoad.setCount(curRoad.getCount() + 1); // �����߹���·�� ��ʶ+1;
				movedPath.add(curRoad);
				if (curRoad.getRoadName().equals(endRoad.getRoadName())) {
					checkmovedPath(); // ����·��
					break;
				}
			} else {
				return;
			}
		}
		calPathLength(); // ���������߹��ĵ�·�Ļ���
	}

	// ѡ����һ���ֵ�
	public AntNode chooseNextRoad() {
		AntNode selectedRoad = null;
		List<AntNode> allowedRoad = setAllowedRoad();
		if (allowedRoad.contains(endRoad)) {
			return endRoad;
		}
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
			//2015-2-2 18:56:13 bobo add
			double distOne = countH(nextRoad, endRoad);
			double distTwo = countH(curRoad, endRoad);
			if (distOne > distTwo) {
				double preTrial = nextRoad.getPheromone();
				double curTrial = preTrial * (distTwo / distOne);
				nextRoad.setPheromone(curTrial);
			}
			if (!movedPath.contains(nextRoad)) {
				double a = nextRoad.getPheromone();
				double b = nextRoad.getRoadWeight();
				double c = countH(nextRoad, this.endRoad);// �����յ�ľ��������ĳ�����Ϊ��������
				if (c == 0.0) { // ��ǰ���Ѿ����յ� ֱ�ӷ���
					selectedRoad = nextRoad;
					return selectedRoad;
				}
				double x = Math.pow(a, ACO.ALPHA);
				double y = Math.pow(10.0 / b, ACO.BETA);
				double z = Math.pow(100.0 / c, ACO.RFORE);
				prob[i] = x * y * z;
				dbTotal += prob[i];
				if (prob[i] > maxProb) {
					maxProbRoad = allowedRoad.get(i);
					maxProb = prob[i];
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
		// 2015-2-2 17:04:48 bobo add
		double distOne = countH(selectedRoad, endRoad);
		double distTwo = countH(curRoad, endRoad);
		if (distOne > distTwo) {
			double preTrial = selectedRoad.getPheromone();
			double curTrial = preTrial * (distTwo / distOne);
			selectedRoad.setPheromone(curTrial);
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
	}

	// ��ӡ�����߹���·��
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

	// ����ڵ㵽�յ��ŷʽ����
	public double countH(AntNode node, AntNode eNode) {
		double res = 0.0;
		res = Math.sqrt(Math.pow(node.getxPos() - eNode.getxPos(), 2)
				+ Math.pow(node.getyPos() - eNode.getyPos(), 2));
		return res;
	}

	// ����Ƿ����·���Ƿ������·
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
						temp.setCount(temp.getCount() - 1);
					}
					j = i + 1;
				}
			}
		}
	}

	public static void main(String[] args) {
		System.out.println((double) 1000.0 / 5579.110000000001);
		System.out.println((double) 1000.0 / 4145.97);
	}
}
