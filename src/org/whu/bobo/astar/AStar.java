package org.whu.bobo.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AStar {
	private HashMap<ANode, List<ANode>> roadMap;// ��ǰ�ֵ�������ͨ�Ľֵ�map
	private List<ANode> openList;// �����б�
	private List<ANode> closeList;// �ر��б�

	public AStar() {
		roadMap = new HashMap<ANode, List<ANode>>();
		openList = new ArrayList<ANode>();
		closeList = new ArrayList<ANode>();
	}

	// �����ͼmap
	public void setRoadTempMap() {
		ANode road1 = new ANode("1", 1.0, 1.0, 1.2, null);
		ANode road2 = new ANode("2", 1.0, 2.0, 1.7, null);
		ANode road3 = new ANode("3", 2.0, 2.0, 2.3, null);
		ANode road4 = new ANode("4", 3.0, 2.0, 1.0, null);
		ANode road5 = new ANode("5", 2.0, 3.0, 4.7, null);
		ANode road6 = new ANode("6", 3.0, 4.0, 3.8, null);
		ANode road7 = new ANode("7", 4.0, 2.0, 2.8, null);
		ANode road8 = new ANode("8", 2.0, 4.0, 1.8, null);
		ANode road9 = new ANode("9", 1.0, 3.0, 2.4, null);
		ANode road10 = new ANode("10", 4.0, 4.0, 1.3, null);
		ANode road11 = new ANode("11", 3.0, 5.0, 1.9, null);
		List<ANode> temp1 = new ArrayList<ANode>();
		temp1.add(road2);
		temp1.add(road3);
		roadMap.put(road1, temp1);
		List<ANode> temp2 = new ArrayList<ANode>();
		temp2.add(road9);
		roadMap.put(road2, temp2);
		List<ANode> temp3 = new ArrayList<ANode>();
		temp3.add(road4);
		temp3.add(road5);
		// temp3.add(road10);
		roadMap.put(road3, temp3);
		List<ANode> temp4 = new ArrayList<ANode>();
		temp4.add(road7);
		roadMap.put(road4, temp4);
		List<ANode> temp5 = new ArrayList<ANode>();
		temp5.add(road6);
		temp5.add(road8);
		roadMap.put(road5, temp5);
		List<ANode> temp6 = new ArrayList<ANode>();
		temp6.add(road10);
		temp6.add(road11);
		roadMap.put(road6, temp6);
		List<ANode> temp7 = new ArrayList<ANode>();
		temp7.add(road10);
		roadMap.put(road7, temp7);
		roadMap.put(road8, null);
		roadMap.put(road9, null);
		roadMap.put(road10, null);
		roadMap.put(road11, null);
	}

	public void search(String startRoad, String endRoad) {
		ANode sNode = ANodeUtil.getDNode(roadMap, startRoad);
		ANode eNode = ANodeUtil.getDNode(roadMap, endRoad);
		openList.add(sNode);
		search(sNode, eNode);
	}

	private List<ANode> search(ANode sNode, ANode eNode) {
		List<ANode> resultList = new ArrayList<ANode>();
		boolean isFind = false;
		ANode node = null;
		while (openList.size() > 0) {
			// ȡ�������б������Fֵ������һ���洢��ֵ��FΪ��͵�
			node = openList.get(0);
			// �ж��Ƿ��ҵ�Ŀ���
			if (node.getRoadName().equals(eNode.getRoadName())) {
				isFind = true;
				break;
			}
			List<ANode> allowedRoad = new ArrayList<ANode>();
			allowedRoad = roadMap.get(node);
			for (int i = 0; i < closeList.size(); i++) {
				if (allowedRoad.contains(closeList.get(i))) {
					allowedRoad.remove(closeList.get(i));
				}
			}
			for (int i = 0; i < allowedRoad.size(); i++) {
				ANode temp = allowedRoad.get(i);
				double cost = temp.getRoadWeight();
				checkPath(temp, node, eNode, cost);
			}
			// �ӿ����б���ɾ��
			// ��ӵ��ر��б���
			closeList.add(openList.remove(0));
			// �����б������򣬰�Fֵ��͵ķŵ���׶�
			Collections.sort(openList, new ANodeFComparator());
		}
		if (isFind) {
			getPath(resultList, node);
			display(resultList);
		}
		return resultList;
	}

	// �������Ƿ����ĳ��Ԫ��(-1��û���ҵ������򷵻����ڵ�����)
	private int isListContains(List<ANode> list, double x, double y) {
		for (int i = 0; i < list.size(); i++) {
			ANode node = list.get(i);
			if (node.getxPos() == x && node.getyPos() == y) {
				return i;
			}
		}
		return -1;
	}

	// ��ѯ��·�Ƿ�����ͨ
	private boolean checkPath(ANode node, ANode parentNode, ANode eNode,
			double cost) {
		List<ANode> allowedRoad = roadMap.get(node);
		if ((allowedRoad == null || allowedRoad.size() == 0)
				&& !node.getRoadName().equals(eNode.getRoadName())) {
			closeList.add(node);
			return false;
		}
		// ���ҿ����б����Ƿ����
		int index = -1;
		if ((index = isListContains(openList, node.getxPos(), node.getyPos())) != -1) {
			// Gֵ�Ƿ��С�����Ƿ����G��Fֵ
			if ((parentNode.getG() + cost) < openList.get(index).getG()) {
				node.setParentNode(parentNode);
				ANodeUtil.countG(node, eNode);
				ANodeUtil.countF(node);
				openList.set(index, node);
			}
		} else {
			// ��ӵ������б���
			node.setParentNode(parentNode);
			ANodeUtil.count(node, eNode);
			openList.add(node);
		}
		return true;
	}

	// ���յ������ص����
	private void getPath(List<ANode> resultList, ANode node) {
		if (node.getParentNode() != null) {
			getPath(resultList, node.getParentNode());
		}
		resultList.add(node);
	}

	private void display(List<ANode> resultList) {
		double cost = 0.0;
		for (int i = 0; i < resultList.size(); i++) {
			if (i != resultList.size() - 1) {
				System.out.print(resultList.get(i).getRoadName() + "->");
			} else {
				System.out.println(resultList.get(i).getRoadName());
			}
			cost += resultList.get(i).getRoadWeight();
		}
		System.out.println("cost: " + cost);
	}

	public static void main(String[] args) {
		AStar astar = new AStar();
		astar.setRoadTempMap();
		astar.search("1", "10");
	}
}
