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
	//构建路网
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
	// 搜索函数
	public void search(String startRoad, String endRoad) {
		Node sNode = getNode(startRoad);
		Node eNode = getNode(endRoad);
		closeList.add(sNode); // sNode 加入到closeList 其他加入到openList
		sNode.setShortestWeight(0.0);
		addNodeToOpen(sNode);
		dijkstra(sNode, eNode);
		displayCost(sNode, eNode);
	}

	private void displayCost(Node sNode, Node eNode) {
		List<Node> resultList = new ArrayList<Node>();
		double totalCost = 0.0;
		Node p = eNode;
		while (p != null) {
			resultList.add(p);
			totalCost += p.getRoadWeight();
			p = p.getParentNode();
		}
		Collections.reverse(resultList);
		for (int i = 0; i < resultList.size(); i++) {
			if (i != resultList.size() - 1) {
				System.out.print(resultList.get(i).getRoadName() + "->");
			} else {
				System.out.println(resultList.get(i).getRoadName());
			}
		}
		System.out.println("cost: " + totalCost);
	}

	private void addNodeToOpen(Node sNode) {
		List<Node> nearests = roadMap.get(sNode);
		Set<Node> set = roadMap.keySet();
		for (Iterator<Node> iter = set.iterator(); iter.hasNext();) {
			Node key = (Node) iter.next();
			if (!key.getRoadName().equals(sNode.getRoadName())) {
				openList.add(key);
			}
		}
		for (int i = 0; i < nearests.size(); i++) {
			Node temp = nearests.get(i);
			temp.setShortestWeight(temp.getRoadWeight());
			temp.setParentNode(sNode);
		}
	}

	// 核心函数
	public void dijkstra(Node sNode, Node eNode) {
		// System.out.println("---->"+roadMap.get(sNode).size());
		Node nearest = getShortestPath(sNode);
		if (nearest == null) {
			return;
		}
		closeList.add(nearest);
		openList.remove(nearest);
		List<Node> allowedRoad = roadMap.get(nearest);
		if (allowedRoad == null || allowedRoad.size() == 0) {
			return;
		}
		for (int i = 0; i < allowedRoad.size(); i++) {
			Node temp = allowedRoad.get(i);
			if (openList.contains(temp)) {
				double shortestPath = nearest.getShortestWeight()
						+ temp.getRoadWeight();
				if (shortestPath < temp.getShortestWeight()) {
					temp.setShortestWeight(shortestPath);
					temp.setParentNode(nearest);
				}
			}
		}
		dijkstra(sNode, eNode);
		dijkstra(nearest, eNode);
	}

	private Node getShortestPath(Node node) {
		Node res = null;
		List<Node> allowedRoad = roadMap.get(node);
		double minWeight = Double.MAX_VALUE;
		for (int i = 0; i < allowedRoad.size(); i++) {
			Node temp = allowedRoad.get(i);
			if (openList.contains(temp)) {
				if (temp.getRoadWeight() < minWeight) {
					res = temp;
					minWeight = temp.getRoadWeight();
				}
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
		dijkstra.search("-23350930", "31777493#1");
		long endTime = System.currentTimeMillis();
		System.out.println("cost time: " + (endTime - beginTime) + "ms");
	}
}
