package org.whu.bobo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.princeton.cs.introcs.In;

public class Mobility implements MobilityInterface {
	public static String filePath = "con-connect-three.txt";
	public static HashMap<String, Double> EdgeLength = new HashMap<String, Double>();

	public static List<String> dataSource;

	public static List<String> shapeData;
	public static HashMap<String, List<Double>> shapeCoord;

	public static HashMap<String, List<Double>> lastShapeCoord; // ��·β������

	public Mobility() {
		dataSource = new ArrayList<String>();
		shapeData = new ArrayList<String>();
		shapeCoord = new HashMap<String, List<Double>>();
		lastShapeCoord = new HashMap<String, List<Double>>();
		initData();
	}

	// ��ʼ������
	private void initData() {
		@SuppressWarnings("deprecation")
		String res[] = In.readStrings(filePath);
		for (int i = 0; i < res.length; i++) {
			if (res[i].startsWith("shape")) {
				shapeData.add(res[i]);
			} else {
				dataSource.add(res[i]);
			}
		}
		int index = dataSource.size() - 2;
		String allLength = dataSource.get(index);
		String everyLength[] = allLength.split(",");
		for (int i = 1; i < everyLength.length; i += 2) {
			EdgeLength.put(everyLength[i],
					Double.parseDouble(everyLength[i + 1]));
		}
		setMapCoord();
		setLastMapCoord();
	}

	// �õ���·ƽ������
	public List<Double> getCoordData(String edge) {
		return shapeCoord.get(edge);
	}

	// �����·ƽ������
	private double getAvgCoord(List<Double> res, int flag) {
		double total = 0.0;
		if (flag == 0) {
			for (int i = 0; i < res.size() - 1; i += 2) {
				total += res.get(i);
			}
			return total / res.size() * 2;
		} else if (flag == 1) {
			for (int i = 1; i < res.size(); i += 2) {
				total += res.get(i);
			}
			return total / res.size() * 2;
		} else {
			return 0.0;
		}
	}

	// �����·ƽ������Map
	public void setMapCoord() {
		List<Double> data = new ArrayList<Double>();
		for (int i = 0; i < shapeData.size(); i++) {
			data.clear();
			String[] res = shapeData.get(i).split(",");
			String edgeName = res[1];
			for (int j = 2; j < res.length; j++) {
				data.add(Double.parseDouble(res[j]));
			}
			List<Double> coord = new ArrayList<Double>();
			double xPos = getAvgCoord(data, 0);
			double yPos = getAvgCoord(data, 1);
			coord.add(xPos);
			coord.add(yPos);
			shapeCoord.put(edgeName, coord);
		}
	}

	// �����·��������Map
	public void setLastMapCoord() {
		List<Double> data = new ArrayList<Double>();
		for (int i = 0; i < shapeData.size(); i++) {
			data.clear();
			String[] res = shapeData.get(i).split(",");
			String edgeName = res[1];
			for (int j = 2; j < res.length; j++) {
				data.add(Double.parseDouble(res[j]));
			}
			List<Double> coord = new ArrayList<Double>();
			double xPos = data.get(data.size() - 2);
			double yPos = data.get(data.size() - 1);
			coord.add(xPos);
			coord.add(yPos);
			lastShapeCoord.put(edgeName, coord);
		}
	}

	// �õ���·��������
	public List<Double> getLastCoordData(String edge) {
		return lastShapeCoord.get(edge);
	}

	// ��ȡ��·���еı�
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

	// ��ȡ��·��Ϣ
	public String[] getRoadInfo() {
		int index = dataSource.size() - 2;
		String allLength = dataSource.get(index);
		String roadInfo[] = allLength.split(",");
		return roadInfo;
	}

	// ��ȡ��·�ߵ�Ȩֵ
	public double getEdgeLength(String edge) {
		return EdgeLength.get(edge);
	}

	// ��ȡ�����������бߵļ���
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

	// ��ȡ��·�ϳ�������
	public int getVehicleNum(String edge) {
		return 0;
	}

	// ��ȡͨ��������·��ʱ��
	public double getTravelTime(String edge) {
		return 0;
	}

	// ��ȡ��··��
	public void setRoute(List<String> route) {

	}

	public static void main(String[] args) {
		Mobility m = new Mobility();
		System.out.println(m.getCoordData("--5558").get(0));
		System.out.println(m.getLastCoordData("--5558"));
	}
}
