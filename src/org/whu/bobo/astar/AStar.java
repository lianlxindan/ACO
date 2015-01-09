package org.whu.bobo.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.whu.bobo.data.Mobility;

/**
 * 1.31982182#1:0 2.4319349:0
 * 
 * @author bobo
 *
 */
public class AStar {
	private HashMap<ANode, List<ANode>> roadMap;// ��ǰ�ֵ�������ͨ�Ľֵ�map
	private List<ANode> openList;// �����б�
	private List<ANode> closeList;// �ر��б�

	public AStar() {
		roadMap = new HashMap<ANode, List<ANode>>();
		openList = new ArrayList<ANode>();
		closeList = new ArrayList<ANode>();
	}

	// ������ͼmap
	public void setRoadMap() {
		Mobility m = new Mobility();
		String[] roadInfo = m.getRoadInfo();
		for (int i = 1; i < roadInfo.length; i += 2) {
			String roadName = roadInfo[i];
			String roadWeight = roadInfo[i + 1];
			double xPos = m.getCoordData(roadName).get(0);
			double yPos = m.getCoordData(roadName).get(1);
			ANode road = new ANode(roadName, xPos, yPos,
					Double.parseDouble(roadWeight), null);
			roadMap.put(road, null);
		}
		Set<ANode> set = roadMap.keySet();
		int i = 1;
		for (Iterator<ANode> iter = set.iterator(); iter.hasNext();) {
			ANode key = (ANode) iter.next();
			List<String> edges = m.getNextEdges(key.getRoadName());
			List<ANode> nodeLink = new ArrayList<ANode>();
			for (int j = 0; j < edges.size(); j++) {
				String edge = edges.get(j);
				ANode temp = ANodeUtil.getANode(roadMap, edge);
				nodeLink.add(temp);
			}
			roadMap.put(key, nodeLink);
			System.out.println(i + ":" + roadMap.size());
			i++;
		}
	}
	public void search(String startRoad, String endRoad) {
		ANode sNode = ANodeUtil.getANode(roadMap, startRoad);
		ANode eNode = ANodeUtil.getANode(roadMap, endRoad);
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
		System.out.print("bestPath:");
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
		astar.setRoadMap();
		long timeOne = System.currentTimeMillis();
		astar.search("-10425131", "4006702#2");
		long timeTwo = System.currentTimeMillis();
		System.out.println("cost time: " + (timeTwo - timeOne) + "ms");
	}
}
