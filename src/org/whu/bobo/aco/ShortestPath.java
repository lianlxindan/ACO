package org.whu.bobo.aco;

import java.util.List;

public class ShortestPath {
	public int startRoad;
	public int endRoad;
	Ant[] m_cAntAry = new Ant[ACO.N_ANT_COUNT];// 蚂蚁数组
	Ant m_cBestAnt;// //定义一个蚂蚁变量，用来保存搜索过程中的最优结果

	/**
	 * 初始化蚂蚁群
	 */
	public void initAnt() {
		for (int i = 0; i < ACO.N_ANT_COUNT; i++) {
			Ant temp = new Ant();
			m_cAntAry[i] = temp;
		}
	}

	/**
	 * 初始化数据
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
	 * 更新环境信息素
	 */
	public void updateTrial() {
		double dbTempAry[][] = new double[ACO.N_CITY_COUNT][ACO.N_CITY_COUNT];
		int m = 0;
		int n = 0;
		for (int i = 0; i < ACO.N_ANT_COUNT; i++) {// 计算每只蚂蚁留下的信息素
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
						+ dbTempAry[i][j]; // 最新的环境信息素
			}
		}
	}

	/**
	 * 开始迭代搜索最短路径
	 */
	public void search() {
		for (int i = 0; i < ACO.N_IT_COUNT; i++) {
			// 每只蚂蚁搜索一遍
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
			for (int j = 0; j < ACO.N_ANT_COUNT; j++) {// 重新设置每只蚂蚁
				m_cAntAry[j].resetAnt(startRoad, endRoad);
			}
		}
	}

	/**
	 * dijkstra 算法求最优路径
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
		dist[startRoad] = 0;// 原点到自己的距离为0；
		p[startRoad] = true;
		// 循环n-1次，求s点到其它点的最短路径
		for (int i = 0; i < n - 1; i++) {
			double min = Double.MAX_VALUE;// s到某个点的最短路径
			int k = -1;
			// 在集合b中寻找从s到其路径最短的的点k
			for (int j = 1; j <= n; j++) {
				if (!p[j] && dist[j] < min) {
					min = dist[j];
					k = j;
				}
			}
			if (k == -1)
				break;// 已经找完了，没有点可以扩展
			p[k] = true;// 将点k加入集合a
			// 更新s到集合b中的点的路劲长度
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
		s.initData(startRoad, endRoad);// 初始化
		long startTimeOne = System.currentTimeMillis();
		s.search(); // 开始搜索
		long endTimeOne = System.currentTimeMillis();
		s.m_cBestAnt.displayPathName();// 输出最优蚂蚁函数
		System.out.println("costTime: " + (endTimeOne - startTimeOne) + "ms");
		long startTimeTwo = System.currentTimeMillis();
		s.dijkstra(startRoad, endRoad);
		long endTimeTwo = System.currentTimeMillis();
		System.out.println("costTime: " + (endTimeTwo - startTimeTwo) + "ms");
	}
}
