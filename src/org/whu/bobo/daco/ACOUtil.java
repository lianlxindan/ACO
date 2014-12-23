package org.whu.bobo.daco;

public class ACOUtil {
	public static ACO aco = null;
	
	//��ȡACO���ʵ��
	public static ACO getAOInstance() {
		if (aco == null) {
			aco = new ACO();
		}
		return aco;
	}

	/**
	 * ����ָ�������ڵ��������
	 * 
	 * @param nLow
	 * @param nUpper
	 * @return
	 */
	public static int rnd(int nLow, int nUpper) {
		return (int) Math.round(Math.random() * (nUpper - nLow) + nLow);
	}

	/**
	 * ����ָ����Χ�ڵ����������
	 * 
	 * @param dbLow
	 * @param dbUpper
	 * @return
	 */
	public static double rnd(double dbLow, double dbUpper) {
		return (double) (Math.random() * (dbUpper - dbLow) + dbLow);
	}
	/**
	 * ��ʼ������
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
