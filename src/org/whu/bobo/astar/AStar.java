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
	private HashMap<ANode, List<ANode>> roadMap;// 当前街道可以连通的街道map
	private List<ANode> openList;// 开启列表
	private List<ANode> closeList;// 关闭列表

	public AStar() {
		roadMap = new HashMap<ANode, List<ANode>>();
		openList = new ArrayList<ANode>();
		closeList = new ArrayList<ANode>();
	}

	// 构建地图map
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
			// 取出开启列表中最低F值，即第一个存储的值的F为最低的
			node = openList.get(0);
			// 判断是否找到目标点
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
			// 从开启列表中删除
			// 添加到关闭列表中
			closeList.add(openList.remove(0));
			// 开启列表中排序，把F值最低的放到最底端
			Collections.sort(openList, new ANodeFComparator());
		}
		if (isFind) {
			getPath(resultList, node);
			display(resultList);
		}
		return resultList;
	}

	// 集合中是否包含某个元素(-1：没有找到，否则返回所在的索引)
	private int isListContains(List<ANode> list, double x, double y) {
		for (int i = 0; i < list.size(); i++) {
			ANode node = list.get(i);
			if (node.getxPos() == x && node.getyPos() == y) {
				return i;
			}
		}
		return -1;
	}

	// 查询此路是否能走通
	private boolean checkPath(ANode node, ANode parentNode, ANode eNode,
			double cost) {
		List<ANode> allowedRoad = roadMap.get(node);
		if ((allowedRoad == null || allowedRoad.size() == 0)
				&& !node.getRoadName().equals(eNode.getRoadName())) {
			closeList.add(node);
			return false;
		}
		// 查找开启列表中是否存在
		int index = -1;
		if ((index = isListContains(openList, node.getxPos(), node.getyPos())) != -1) {
			// G值是否更小，即是否更新G，F值
			if ((parentNode.getG() + cost) < openList.get(index).getG()) {
				node.setParentNode(parentNode);
				ANodeUtil.countG(node, eNode);
				ANodeUtil.countF(node);
				openList.set(index, node);
			}
		} else {
			// 添加到开启列表中
			node.setParentNode(parentNode);
			ANodeUtil.count(node, eNode);
			openList.add(node);
		}
		return true;
	}

	// 从终点往返回到起点
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
