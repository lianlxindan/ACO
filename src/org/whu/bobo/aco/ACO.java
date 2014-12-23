package org.whu.bobo.aco;

import java.util.HashMap;

public class ACO {
	public static HashMap<Integer, HashMap<Integer, Double>> roadNetInfo = new HashMap<Integer, HashMap<Integer, Double>>();// 路网邻接表
	public static double ALPHA = 1.0;// 启发因子，信息素的重要程度
	public static double BETA = 2.0;// 期望因子，城市间距离的重要程度
	public static double ROU = 0.5;// 信息素残留参数

	public static double P = 0.1;// 伪概率事件
	public static int N_ANT_COUNT = 5;// 蚂蚁数量
	public static int N_IT_COUNT = 500;// 迭代次数
	public static int N_CITY_COUNT = ACOUtil.getMobility().getAllEdges().size();// 街道口数量
	public static double DBQ = 100.0;// 总的信息量
	public static double[][] g_Trial = new double[N_CITY_COUNT + 1][N_CITY_COUNT + 1];// 街道之间的信息素

	/**
	 * 设置两个路口节点之间距离
	 * 
	 * @param a
	 * @param b
	 * @param dis
	 */
	public void setDistance(int a, int b, double dis, double trial) {
		HashMap<Integer, Double> temp = roadNetInfo.get(a);
		if (temp == null) {
			temp = new HashMap<Integer, Double>();
		}
		temp.put(b, dis);
		roadNetInfo.put(a, temp);
		g_Trial[a][b] = trial;
	}

	/**
	 * 第二种版本
	 * 
	 * @param aName
	 * @param bName
	 * @param dis
	 * @param trial
	 */
	public void setDistanceTwo(String aName, String bName, double trial) {
		int aID = ACOUtil.roadMaps.get(aName);
		int bID = ACOUtil.roadMaps.get(bName);
		double _dis = ACOUtil.getMobility().getEdgeLength(bName);
		HashMap<Integer, Double> temp = roadNetInfo.get(aID);
		if (temp == null) {
			temp = new HashMap<Integer, Double>();
		}
		temp.put(bID, _dis);
		roadNetInfo.put(aID, temp);
		// System.out.println(aID+" "+roadNetInfo.get(aID));
		g_Trial[aID][bID] = trial;
	}

	/**
	 * 获取两个路口的距离
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public double getDistance(int a, int b) {
		double res = Double.MAX_VALUE;
		HashMap<Integer, Double> temp = roadNetInfo.get(a);
		if (temp != null) {
			if (temp.containsKey(b)) {
				res = temp.get(b);
			}
		}
		return res;
	}
}
