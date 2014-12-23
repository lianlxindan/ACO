package org.whu.bobo.daco;

public class ACOUtil {
	public static ACO aco = null;
	
	//获取ACO类的实例
	public static ACO getAOInstance() {
		if (aco == null) {
			aco = new ACO();
		}
		return aco;
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
