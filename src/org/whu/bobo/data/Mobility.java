package org.whu.bobo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.princeton.cs.introcs.In;

public class Mobility implements MobilityInterface {
	public static String filePath = "con-connect.txt";
	public static HashMap<String, Double> EdgeLength = new HashMap<String, Double>();

	public static List<String> dataSource;

	public Mobility() {
		dataSource = new ArrayList<String>();
		initData();
	}

	public String[] getDataFromFile() {
		@SuppressWarnings("deprecation")
		String res[] = In.readStrings(filePath);
		return res;
	}

	// 初始化数据
	private void initData() {
		@SuppressWarnings("deprecation")
		String res[] = In.readStrings(filePath);
		for (int i = 0; i < res.length; i++) {
			dataSource.add(res[i]);
		}
		int index = dataSource.size() - 2;
		String allLength = dataSource.get(index);
		String everyLength[] = allLength.split(",");
		for (int i = 1; i < everyLength.length; i += 2) {
			EdgeLength.put(everyLength[i],
					Double.parseDouble(everyLength[i + 1]));
		}
	}

	// 获取道路所有的边
	public List<String> getAllEdges() {
		List<String> allEdges = new ArrayList<String>();
		int index = dataSource.size() - 1;
		String allEdge = dataSource.get(index);
		String everyEdge[] = allEdge.split(",");
		for (int i = 1; i < everyEdge.length; i++) {
			allEdges.add(everyEdge[i]);
		}
		return allEdges;
	}

	// 获取道路信息
	public String[] getRoadInfo() {
		int index = dataSource.size() - 2;
		String allLength = dataSource.get(index);
		String roadInfo[] = allLength.split(",");
		return roadInfo;
	}

	// 获取道路边的权值
	public double getEdgeLength(String edge) {
		return EdgeLength.get(edge);
	}

	// 获取边相连的所有边的集合
	public List<String> getNextEdges(String edge) {
		List<String> edges = new ArrayList<String>();
		for (int i = 0; i < dataSource.size() - 2; i++) {
			String temp = dataSource.get(i);
			String[] everyEdge = temp.split(",");
			if (edge.equals(everyEdge[0])) {
				for (int j = 1; j < everyEdge.length; j++) {
					edges.add(everyEdge[j]);
				}
			}
		}
		return edges;
	}

	// 获取道路上车的数量
	public int getVehicleNum(String edge) {
		return 0;
	}

	// 获取通过该条道路的时间
	public double getTravelTime(String edge) {
		return 0;
	}

	// 获取道路路由
	public void setRoute(List<String> route) {

	}

	public static void main(String[] args) {
		Mobility m = new Mobility();
		int size = m.getAllEdges().size();
		System.out.println(size);
		System.out.println(dataSource.size());
		System.out.println(m.getEdgeLength("0/0to0/1"));
		System.out.println(m.getNextEdges("0/1to1/1").size());
	}
}
