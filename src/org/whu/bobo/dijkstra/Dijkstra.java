package org.whu.bobo.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.whu.bobo.data.Mobility;

/**
 * dijkstra 算法
 * 
 * @author bobo
 *
 */
public class Dijkstra {
	private HashMap<Node, List<Node>> roadMap; // 道路map
	private List<Node> openList;
	private List<Node> closeList;

	/**
	 * 构造函数
	 */
	public Dijkstra() {
		roadMap = new HashMap<Node, List<Node>>();
		openList = new ArrayList<Node>();
		closeList = new ArrayList<Node>();
	}

	// 构建路网
	public void setRoadMap() {
		Mobility m = new Mobility();
		String[] roadInfo = m.getRoadInfo();
		for (int i = 1; i < roadInfo.length; i += 2) {
			Node road = new Node(roadInfo[i],
					Double.parseDouble(roadInfo[i + 1]));
			roadMap.put(road, null);
		}
		Set<Node> set = roadMap.keySet();
		int i = 1;
		for (Iterator<Node> iter = set.iterator(); iter.hasNext();) {
			Node key = (Node) iter.next();
			List<String> edges = m.getNextEdges(key.getRoadName());
			List<Node> nodeLink = new ArrayList<Node>();
			for (int j = 0; j < edges.size(); j++) {
				String edge = edges.get(j);
				Node temp = getNode(edge);
				nodeLink.add(temp);
			}
			roadMap.put(key, nodeLink);
			System.out.println(i + ":" + roadMap.size());
			i++;
		}
	}

	// 初始化
	private void init(Node sNode) {
		closeList.add(sNode); // sNode 加入到closeList 其他加入到openList
		sNode.setShortestWeight(0.0);
		List<Node> nearests = roadMap.get(sNode);
		Set<Node> set = roadMap.keySet();
		for (Iterator<Node> iter = set.iterator(); iter.hasNext();) {
			Node key = (Node) iter.next();
			if (key != sNode) {
				openList.add(key);
			}
		}
		for (Iterator<Node> iter = set.iterator(); iter.hasNext();) {
			Node key = (Node) iter.next();
			if (nearests.contains(key)) {
				key.setShortestWeight(key.getRoadWeight());
			} else {
				key.setShortestWeight(Double.MAX_VALUE);
			}
			key.setParentNode(sNode);
		}
	}

	// 搜索函数
	public void search(String startRoad, String endRoad) {
		Node sNode = getNode(startRoad);
		Node eNode = getNode(endRoad);
		init(sNode); // 初始化
		dijkstra(sNode, eNode); // 核心算法
		displayCost(sNode, eNode); // 打印代价
	}

	private void displayCost(Node sNode, Node eNode) {
		List<Node> resultList = new ArrayList<Node>();
		double totalCost = 0.0;
		Node p = eNode;
		while (p != sNode) {
			resultList.add(p);
			totalCost += p.getRoadWeight();
			p = p.getParentNode();
		}
		totalCost += sNode.getRoadWeight();
		resultList.add(sNode);
		Collections.reverse(resultList);
		System.out.print("bestPath:");
		for (int i = 0; i < resultList.size(); i++) {
			if (i != resultList.size() - 1) {
				System.out.print(resultList.get(i).getRoadName() + "->");
			} else {
				System.out.println(resultList.get(i).getRoadName() + ".");
			}
		}
		System.out.println("minCost: " + totalCost);
	}

	// 核心函数
	public void dijkstra(Node sNode, Node eNode) {
		int n = roadMap.size();
		for (int i = 0; i < n - 1; i++) {
			Node nearest = getShortestPath();
			if (nearest == null
					|| nearest.getRoadName().equals(eNode.getRoadName())) {
				break;
			}
			closeList.add(nearest);
			openList.remove(nearest);
			List<Node> connectList = roadMap.get(nearest);
			for (int k = 0; k < openList.size(); k++) {
				Node temp = openList.get(k);
				double shortestPath = nearest.getShortestWeight()
						+ temp.getRoadWeight();
				if (shortestPath < temp.getShortestWeight()
						&& connectList.contains(temp)) {
					temp.setShortestWeight(shortestPath);
					temp.setParentNode(nearest);
				}
			}
		}
	}

	// 获取最小代价的点
	private Node getShortestPath() {
		Node res = null;
		double minWeight = Double.MAX_VALUE;
		for (int i = 0; i < openList.size(); i++) {
			Node temp = openList.get(i);
			if (temp.getShortestWeight() < minWeight) {
				res = temp;
				minWeight = temp.getShortestWeight();
			}
		}
		return res;
	}

	// 通过名称获取道路
	private Node getNode(String edgeName) {
		Node result = null;
		Set<Node> set = roadMap.keySet();
		for (Iterator<Node> iter = set.iterator(); iter.hasNext();) {
			Node key = (Node) iter.next();
			if (key.getRoadName().equals(edgeName)) {
				result = key;
				break;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		Dijkstra dijkstra = new Dijkstra();
		dijkstra.setRoadMap();
		long beginTime = System.currentTimeMillis();
		dijkstra.search("-10425131", "4006702#2");
		long endTime = System.currentTimeMillis();
		System.out.println("cost time: " + (endTime - beginTime) + "ms");
	}
}
