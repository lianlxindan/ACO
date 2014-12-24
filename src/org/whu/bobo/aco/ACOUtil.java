package org.whu.bobo.aco;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.whu.bobo.data.Mobility;

public class ACOUtil {

	public static Map<String, Integer> roadMaps = convertStringToID(ACOUtil
			.getMobility().getAllEdges());// 街道名称与街道ID相对应的map
	public static ACO aco = null;
	public static Ant ant = null;
	public static Mobility m = null;

	/**
	 * 设置街道名和街道号相对应
	 * 
	 * @param allEdges
	 * @return
	 */
	public static Map<String, Integer> convertStringToID(List<String> allEdges) {
		Map<String, Integer> roadMap = new HashMap<String, Integer>();
		for (int i = 0; i < allEdges.size(); i++) {
			roadMap.put(allEdges.get(i), i + 1);
		}
		return roadMap;
	}

	public static ACO getACOInstance() {
		if (aco == null) {
			aco = new ACO();
		}
		return aco;
	}

	public static Ant getAntInstance() {
		if (ant == null) {
			ant = new Ant();
		}
		return ant;
	}

	public static Mobility getMobility() {
		if (m == null) {
			m = new Mobility();
		}
		return m;
	}

	/**
	 * 返回指定返回内的随机整数
	 * 
	 * @param nLow
	 * @param nUpper
	 * @return
	 */
	public static int rnd(int nLow, int nUpper) {
		return (int) Math.round(Math.random() * (nUpper - nLow) + nLow);
	}

	/**
	 * 
	 * @param roadName
	 * @return
	 */
	public static int getRoadNum(String roadName) {
		int res = -1;
		List<String> allEdges = ACOUtil.getMobility().getAllEdges();
		for (int i = 0; i < allEdges.size(); i++) {
			if (roadName.equals(allEdges.get(i))) {
				res = i + 1;
			}
		}
		return res;
	}

	/**
	 * 返回指定范围内的随机浮点数
	 * 
	 * @param dbLow
	 * @param dbUpper
	 * @return
	 */
	public static double rnd(double dbLow, double dbUpper) {
		return (double) (Math.random() * (dbUpper - dbLow) + dbLow);
	}

	/**
	 * 初始化数组
	 * 
	 * @param array
	 */
	public static void memsetDouble(double[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				array[i][j] = 0.0;
			}
		}
	}

}
