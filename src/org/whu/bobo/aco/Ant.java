package org.whu.bobo.aco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Ant {
	int startRoadNumber;// 起点街道
	int endRoadNumber;// 终点街道
	int m_nCurRoadNumber;// 当前所在街道
	double m_dbPathLength;// 蚂蚁走过的路径长度
	List<Integer> movedPath = new ArrayList<Integer>();// 蚂蚁已经走过的路径

	/**
	 * 蚂蚁克隆函数
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
	 * 蚂蚁初始化
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
	 * 允许蚂蚁行走的街道
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
	 * 选择下一个路口
	 * 
	 * @return
	 */
	public int chooseNextRoad() {
		int selectedRoad = -1;
		double dbTotal = 0.0;
		List<Integer> allowedRoad = setAllowRoad();
		int preRoadNumber = -1;
		while (allowedRoad.size() == 0) {// 走到死胡同
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
			if (!movedPath.contains(nextRoadNum)) {// 下个路口不在已经走过的路径中
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

		double p = ACOUtil.rnd(0.0, 1.0); // 伪概率事件选择道路
		if (p > ACO.P) {
			// 下面进行轮盘选择
			double dbTemp = 0.0;
			if (dbTotal > 0.0) {
				dbTemp = ACOUtil.rnd(0.0, dbTotal);// 取一个随机数
				for (int i = 0; i < allowedRoad.size(); i++) {
					int nextRoadNum = allowedRoad.get(i);
					if (!movedPath.contains(nextRoadNum)) {
						dbTemp = dbTemp - prob[i];
						if (dbTemp < 0.0) {// 轮盘停止转动，记下路口编号，直接跳出循环
							selectedRoad = nextRoadNum;
							break;
						}
					}
				}
			}
		} else { // 选择最大概率的路走
			selectedRoad = maxProbRoad;
		}
		return selectedRoad;
	}

	/**
	 * 蚂蚁进行搜索一次
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
	 * 打印最优路径
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
	 * 打印最优路径名称
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
	 * 计算蚂蚁走过的路径长度
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
	 * 重新设置蚂蚁
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
