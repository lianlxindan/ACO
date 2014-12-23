package org.whu.bobo.aco;

import java.util.HashMap;

public class ACO {
	public static HashMap<Integer, HashMap<Integer, Double>> roadNetInfo = new HashMap<Integer, HashMap<Integer, Double>>();// ·���ڽӱ�
	public static double ALPHA = 1.0;// �������ӣ���Ϣ�ص���Ҫ�̶�
	public static double BETA = 2.0;// �������ӣ����м�������Ҫ�̶�
	public static double ROU = 0.5;// ��Ϣ�ز�������

	public static double P = 0.1;// α�����¼�
	public static int N_ANT_COUNT = 5;// ��������
	public static int N_IT_COUNT = 500;// ��������
	public static int N_CITY_COUNT = ACOUtil.getMobility().getAllEdges().size();// �ֵ�������
	public static double DBQ = 100.0;// �ܵ���Ϣ��
	public static double[][] g_Trial = new double[N_CITY_COUNT + 1][N_CITY_COUNT + 1];// �ֵ�֮�����Ϣ��

	/**
	 * ��������·�ڽڵ�֮�����
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
	 * �ڶ��ְ汾
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
	 * ��ȡ����·�ڵľ���
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
