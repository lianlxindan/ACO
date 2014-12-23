package org.whu.bobo.aco;

import java.util.List;

public class ShortestPath {
	public int startRoad;
	public int endRoad;
	Ant[] m_cAntAry = new Ant[ACO.N_ANT_COUNT];// ��������
	Ant m_cBestAnt;// //����һ�����ϱ����������������������е����Ž��

	/**
	 * ��ʼ������Ⱥ
	 */
	public void initAnt() {
		for (int i = 0; i < ACO.N_ANT_COUNT; i++) {
			Ant temp = new Ant();
			m_cAntAry[i] = temp;
		}
	}

	/**
	 * ��ʼ������
	 */
	public void initData(String startRoadStr, String endRoadStr) {
		initAnt();
		this.startRoad = ACOUtil.roadMaps.get(startRoadStr);
		this.endRoad = ACOUtil.roadMaps.get(endRoadStr);
		m_cBestAnt = new Ant();
		m_cBestAnt.m_dbPathLength = Double.MAX_VALUE;

		for (int i = 0; i < ACOUtil.getMobility().getAllEdges().size(); i++) {
			String curEdge = ACOUtil.getMobility().getAllEdges().get(i);
			List<String> edges = ACOUtil.getMobility().getNextEdges(curEdge);
			if (edges != null) {
				for (int j = 0; j < edges.size(); j++) {
					ACOUtil.getAOInstance().setDistanceTwo(curEdge,
							edges.get(j), 0.1);
				}
			}
			System.out.println((i + 1) + ":" + (i + 1) + "/"
					+ ACOUtil.roadMaps.size());
		}
	}

	/**
	 * ���»�����Ϣ��
	 */
	public void updateTrial() {
		double dbTempAry[][] = new double[ACO.N_CITY_COUNT][ACO.N_CITY_COUNT];
		int m = 0;
		int n = 0;
		for (int i = 0; i < ACO.N_ANT_COUNT; i++) {// ����ÿֻ�������µ���Ϣ��
			for (int j = 1; j < m_cAntAry[i].movedPath.size(); j++) {
				m = m_cAntAry[i].movedPath.get(j);
				n = m_cAntAry[i].movedPath.get(j - 1);
				dbTempAry[n][m] = dbTempAry[n][m] + ACO.DBQ
						/ m_cAntAry[i].m_dbPathLength;
			}
		}
		for (int i = 0; i < ACO.N_CITY_COUNT; i++) {
			for (int j = 0; j < ACO.N_CITY_COUNT; j++) {
				ACO.g_Trial[i][j] = ACO.g_Trial[i][j] * ACO.ROU
						+ dbTempAry[i][j]; // ���µĻ�����Ϣ��
			}
		}
	}

	/**
	 * ��ʼ�����������·��
	 */
	public void search() {
		for (int i = 0; i < ACO.N_IT_COUNT; i++) {
			// ÿֻ��������һ��
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {
				m_cAntAry[j].search(startRoad, endRoad);
				// m_cAntAry[j].displayPath();
			}
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {
				if (m_cAntAry[j].m_dbPathLength < m_cBestAnt.m_dbPathLength) {
					m_cBestAnt = m_cAntAry[j].clone();
				}
			}
			updateTrial();
			System.out.println((i + 1) + ": " + m_cBestAnt.m_dbPathLength);
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {// ��������ÿֻ����
				m_cAntAry[j].resetAnt(startRoad, endRoad);
			}
		}
	}

	/**
	 * dijkstra �㷨������·��
	 * 
	 * @param startRoadName
	 * @param endRoadName
	 */
	public void dijkstra(String startRoadName, String endRoadName) {
		int n = ACO.roadNetInfo.size();
		int startRoad = ACOUtil.roadMaps.get(startRoadName);
		int endRoad = ACOUtil.roadMaps.get(endRoadName);
		int[] pre = new int[n + 1];
		double[] dist = new double[n + 1];
		boolean[] p = new boolean[n + 1];
		for (int i = 1; i <= n; i++) {
			p[i] = false;
			if (i != startRoad) {
				dist[i] = ACOUtil.getAOInstance().getDistance(startRoad, i);
				pre[i] = startRoad;
			}
		}
		dist[startRoad] = 0;// ԭ�㵽�Լ��ľ���Ϊ0��
		p[startRoad] = true;
		// ѭ��n-1�Σ���s�㵽����������·��
		for (int i = 0; i < n - 1; i++) {
			double min = Double.MAX_VALUE;// s��ĳ��������·��
			int k = -1;
			// �ڼ���b��Ѱ�Ҵ�s����·����̵ĵĵ�k
			for (int j = 1; j <= n; j++) {
				if (!p[j] && dist[j] < min) {
					min = dist[j];
					k = j;
				}
			}
			if (k == -1)
				break;// �Ѿ������ˣ�û�е������չ
			p[k] = true;// ����k���뼯��a
			// ����s������b�еĵ��·������
			for (int j = 1; j <= n; j++) {
				double tkj = ACOUtil.getAOInstance().getDistance(k, j);
				if (!p[j] && tkj != Double.MAX_VALUE && dist[j] > dist[k] + tkj)
					dist[j] = dist[k] + tkj;
				pre[j] = k;
			}

		}
		if (dist[endRoad] == Double.MAX_VALUE)
			System.out.println("no road to the end!");
		else
			System.out.println("min price:" + dist[endRoad]);
	}

	public static void main(String[] args) {
		ShortestPath s = new ShortestPath();
		String startRoad = "-10425130";
		String endRoad = "-31241816#3";
		s.initData(startRoad, endRoad);// ��ʼ��
		long startTimeOne = System.currentTimeMillis();
		s.search(); // ��ʼ����
		long endTimeOne = System.currentTimeMillis();
		s.m_cBestAnt.displayPathName();// ����������Ϻ���
		System.out.println("costTime: " + (endTimeOne - startTimeOne) + "ms");
		long startTimeTwo = System.currentTimeMillis();
		s.dijkstra(startRoad, endRoad);
		long endTimeTwo = System.currentTimeMillis();
		System.out.println("costTime: " + (endTimeTwo - startTimeTwo) + "ms");
	}
}
