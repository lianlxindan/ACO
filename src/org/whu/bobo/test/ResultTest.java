package org.whu.bobo.test;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.introcs.In;

public class ResultTest {
	public static String filePath = "result.txt";
	public static List<String> cResult;
	public static List<String> bResult;

	public ResultTest() {
		cResult = new ArrayList<String>();
		bResult = new ArrayList<String>();
		initData();
	}
	private void initData() {
		@SuppressWarnings("deprecation")
		String res[] = In.readStrings(filePath);
		for (int i = 0; i < res.length; i++) {
			if (i % 2 == 0) {
				cResult.add(res[i]);
			} else {
				bResult.add(res[i]);
			}
		}
	}

	private double getAverageResult(int flag) {
		double result = 0.0;
		if (flag == 0) {
			for (int i = 0; i < cResult.size(); i++) {
				String temp = cResult.get(i).split(":")[1];
				result += Double.parseDouble(temp);
			}
		} else {
			for (int i = 0; i < bResult.size(); i++) {
				String temp = bResult.get(i).split(":")[1];
				result += Double.parseDouble(temp);
			}
		}
		return result / cResult.size();
	}

	public static void main(String args[]) {
		ResultTest rt = new ResultTest();
		System.out.println(rt.getAverageResult(1));
	}
}
