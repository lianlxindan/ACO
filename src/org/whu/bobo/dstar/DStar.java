package org.whu.bobo.dstar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.whu.bobo.data.Mobility;

/**
 * D*�㷨
 * 
 * @author bobo
 *
 */
public class DStar {
	private HashMap<DNode, List<DNode>> roadMap;// ��ǰ�ֵ�������ͨ�Ľֵ�map
	private List<DNode> openList; // �����б�
	private List<DNode> closeList; // �ر��б�
	private List<DNode> uPriority; // ���ȶ���
	private List<DNode> key_node;

	// ���캯��
	public DStar() {
		roadMap = new HashMap<DNode, List<DNode>>();
		openList = new ArrayList<DNode>();
		closeList = new ArrayList<DNode>();
		uPriority = new ArrayList<DNode>();
		key_node = new ArrayList<DNode>();
	}

	// �����ͼmap
	public void setRoadMap() {
		Mobility m = new Mobility();
		String[] roadInfo = m.getRoadInfo();
		for (int i = 1; i < roadInfo.length; i += 2) {
			String roadName = roadInfo[i];
			String roadWeight = roadInfo[i + 1];
			double xPos = m.getCoordData(roadName).get(0);
			double yPos = m.getCoordData(roadName).get(1);
			DNode road = new DNode(roadName, xPos, yPos,
					Double.parseDouble(roadWeight), null);
			roadMap.put(road, null);
		}
		Set<DNode> set = roadMap.keySet();
		int i = 1;
		for (Iterator<DNode> iter = set.iterator(); iter.hasNext();) {
			DNode key = (DNode) iter.next();
			List<String> edges = m.getNextEdges(key.getRoadName());
			List<DNode> nodeLink = new ArrayList<DNode>();
			for (int j = 0; j < edges.size(); j++) {
				String edge = edges.get(j);
				DNode temp = getDNode(edge);
				nodeLink.add(temp);
			}
			roadMap.put(key, nodeLink);
			System.out.println(i + ":" + roadMap.size());
			i++;
		}
	}

	// �����ͼmap
	public void setRoadTempMap() {
		DNode road1 = new DNode("1", 1.0, 1.0, 1.2, null);
		DNode road2 = new DNode("2", 1.0, 2.0, 1.7, null);
		DNode road3 = new DNode("3", 2.0, 2.0, 2.3, null);
		DNode road4 = new DNode("4", 3.0, 2.0, 1.0, null);
		DNode road5 = new DNode("5", 2.0, 3.0, 1.7, null);
		DNode road6 = new DNode("6", 3.0, 4.0, 1.8, null);
		DNode road7 = new DNode("7", 4.0, 2.0, 2.8, null);
		DNode road8 = new DNode("8", 2.0, 4.0, 1.8, null);
		DNode road9 = new DNode("9", 1.0, 3.0, 2.4, null);
		DNode road10 = new DNode("10", 4.0, 4.0, 1.3, null);
		DNode road11 = new DNode("11", 3.0, 5.0, 1.9, null);
		List<DNode> temp1 = new ArrayList<DNode>();
		temp1.add(road2);
		temp1.add(road3);
		roadMap.put(road1, temp1);
		List<DNode> temp2 = new ArrayList<DNode>();
		temp2.add(road9);
		roadMap.put(road2, temp2);
		List<DNode> temp3 = new ArrayList<DNode>();
		temp3.add(road4);
		temp3.add(road5);
		// temp3.add(road10);
		roadMap.put(road3, temp3);
		List<DNode> temp4 = new ArrayList<DNode>();
		temp4.add(road7);
		roadMap.put(road4, temp4);
		List<DNode> temp5 = new ArrayList<DNode>();
		temp5.add(road6);
		temp5.add(road8);
		roadMap.put(road5, temp5);
		List<DNode> temp6 = new ArrayList<DNode>();
		temp6.add(road10);
		temp6.add(road11);
		roadMap.put(road6, temp6);
		List<DNode> temp7 = new ArrayList<DNode>();
		temp7.add(road10);
		roadMap.put(road7, temp7);
		roadMap.put(road8, null);
		roadMap.put(road9, null);
		roadMap.put(road10, null);
		roadMap.put(road11, null);
		initRoadMap();
	}

	// ��ʼ��·��
	private void initRoadMap() {
		double maxValue = Double.MAX_VALUE;
		Set<DNode> set = roadMap.keySet();
		for (Iterator<DNode> iter = set.iterator(); iter.hasNext();) {
			DNode key = (DNode) iter.next();
			key.setG(maxValue);
			key.setRhs(maxValue);
		}
	}

	/**
	 * ��ʼ��������·���ƶ�
	 * 
	 * @param startRoad
	 * @param endRoad
	 */
	public void move(String startRoad, String endRoad) {
		DNode sNode = getDNode(startRoad);
		DNode eNode = getDNode(endRoad);
		initRoad(startRoad, endRoad);
		search(eNode, sNode);
		boolean flag = false;
		while (sNode != eNode) { // ��������·���ƶ� ��·���仯ʱ�ٴι滮·��
			sNode = sNode.getParentNode();
			if (sNode.getRoadName() == "3") {
				DNode changeNode = getDNode("6");
				if (closeList.contains(changeNode)) {
					closeList.remove(changeNode);
				}
				DStarUtil.setRoadWeight(changeNode, 1);
				openList.add(changeNode);
				uPriority.add(changeNode);
				while (openList.size() > 0) {
					DNode node = openList.get(0);
					if (node.getG() > node.getRhs()) {
						node.setG(node.getRhs());
						if (node.getRoadName().equals(sNode.getRoadName())) {
							flag = true;
							break;
						}
						List<DNode> allowedRoad = isRoadMapContainsNode(node);
						for (int i = 0; i < allowedRoad.size(); i++) {
							DNode temp = allowedRoad.get(i);
							DStarUtil.countH(temp, sNode);
							updateRoad(temp, eNode);
						}
					} else {
						node.setG(Double.MAX_VALUE);
						updateRoad(node, eNode);
						List<DNode> allowedRoad = isRoadMapContainsNode(node);
						for (int i = 0; i < allowedRoad.size(); i++) {
							DNode temp = allowedRoad.get(i);
							DStarUtil.countH(temp, sNode);
							updateRoad(temp, eNode);
						}
					}
					closeList.add(openList.remove(0));
					uPriority.remove(0);
					Collections.sort(uPriority, new DNodeFComparator());
					if (uPriority.size() == 0) {
						System.out.println("��ǰ��Ϊ: " + sNode.getRoadName());
						System.out.println("�ع滮��·��Ϊ: ");
						display(sNode, eNode);
					} else {
						openList.add(uPriority.get(0));
					}
				}
				if (flag) {
					System.out.println("��ǰ��Ϊ: " + sNode.getRoadName());
					System.out.print("�ع滮��·��Ϊ: ");
					display(sNode, eNode);
				}
			}
		}
	}

	/**
	 * ���·�������仯
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<DNode> checkRoadChange() {
		List<DNode> result = new ArrayList<DNode>();
		return result;// ����Ҫ�ع滮�ͷ���null
	}

	/**
	 * �������ͨ�ĵ�
	 * 
	 * @param node
	 * @return
	 */
	private List<DNode> isRoadMapContainsNode(DNode node) {
		List<DNode> result = new ArrayList<DNode>();
		Set<DNode> set = roadMap.keySet();
		for (Iterator<DNode> iter = set.iterator(); iter.hasNext();) {
			DNode key = (DNode) iter.next();
			List<DNode> temp = roadMap.get(key);
			if (temp != null && temp.contains(node)) {
				result.add(key);
			}
		}
		return result;
	}

	/**
	 * �� openList��closeList ���뵽key_node
	 */
	public void addNodeTokeyNode() {
		for (int i = 0; i < openList.size(); i++) {
			key_node.add(openList.get(i));
		}
		for (int i = 0; i < closeList.size(); i++) {
			if (!key_node.contains(closeList.get(i))) {
				key_node.add(closeList.get(i));
			}
		}
	}

	/**
	 * ��ʼ��������·����ʼ��
	 * 
	 * @param startRoad
	 * @param endRoad
	 */
	private void initRoad(String startRoad, String endRoad) {
		DNode sNode = getDNode(startRoad);
		DNode eNode = getDNode(endRoad);
		DStarUtil.countH(eNode, sNode);
		eNode.setRhs(0.0);
		DStarUtil.setKey(eNode);
		uPriority.add(eNode);
	}

	// ��һ�׶ι滮������·��
	private void search(DNode eNode, DNode sNode) {
		openList.add(uPriority.remove(0));
		boolean isFind = false;
		DNode node = null;
		while (openList.size() > 0) {
			node = openList.get(0);
			if (node.getG() > node.getRhs()) {
				node.setG(node.getRhs());
				if (node.getRoadName() == sNode.getRoadName()) {
					openList.remove(0);
					isFind = true;
					break;
				}
				List<DNode> allowedRoad = isRoadMapContainsNode(node);
				for (int i = 0; i < closeList.size(); i++) { // �ߵ�����ͬ��ʱ��
					DNode temp = closeList.get(i);
					if (allowedRoad.contains(temp)) {
						allowedRoad.remove(temp);
					}
				}
				for (int i = 0; i < allowedRoad.size(); i++) {
					DNode temp = allowedRoad.get(i);
					DStarUtil.countH(temp, sNode);
					updateRoad(temp, eNode);
				}
			} else {
				node.setG(Double.MAX_VALUE);
				updateRoad(node, eNode);
				List<DNode> allowedRoad = isRoadMapContainsNode(node);
				for (int i = 0; i < closeList.size(); i++) { // �ߵ�����ͬ��ʱ��
					DNode temp = closeList.get(i);
					if (allowedRoad.contains(temp)) {
						allowedRoad.remove(temp);
					}
				}
				for (int i = 0; i < allowedRoad.size(); i++) {
					DNode temp = allowedRoad.get(i);
					DStarUtil.countH(temp, sNode);
					updateRoad(temp, eNode);
				}
			}
			closeList.add(openList.remove(0));
			Collections.sort(uPriority, new DNodeFComparator());
			openList.add(uPriority.remove(0));
		}
		if (isFind) {
			System.out.println("���Ϊ: " + sNode.getRoadName());
			System.out.print("��һ�ι滮��·��Ϊ: ");
			display(sNode, eNode);
		}
	}

	/**
	 * ��·�������仯ʱ ����rhs
	 * 
	 * @param node
	 */
	private void updateRoad(DNode paramNode, DNode eNode) {
		DNode localRoad = null;
		if (paramNode != eNode) {
			paramNode.setRhs(Double.MAX_VALUE);
			List<DNode> allowedRoad = roadMap.get(paramNode);
			double min;
			for (int i = 0; i < allowedRoad.size(); i++) { // ����rhsֵ Ȼ��ѡ�������С��·
				if (allowedRoad.get(i).getG() == Double.MAX_VALUE) {
					min = Double.MAX_VALUE;
				} else {
					min = allowedRoad.get(i).getG()
							+ allowedRoad.get(i).getRoadWeight();
				}
				if (paramNode.getRhs() <= min) {
					continue;
				}
				paramNode.setRhs(min);
				localRoad = allowedRoad.get(i);
			}
		}
		paramNode.setParentNode(localRoad);
		if (paramNode.getG() != paramNode.getRhs()) {
			DStarUtil.setKey(paramNode);
			uPriority.add(paramNode);
		}
	}

	/**
	 * ��ӡһ�ι滮�������·��
	 * 
	 * @param node
	 */
	private void display(DNode sNode, DNode eNode) {
		double totalCost = 0.0;
		DNode p = sNode;
		while (p != null) {
			if (p != eNode) {
				System.out.print(p.getRoadName() + "->");
			} else {
				System.out.println(p.getRoadName());
			}
			totalCost += p.getRoadWeight();
			// System.out.println("-->"+ totalCost);
			p = p.getParentNode();
		}
		System.out.println("totalCost: " + totalCost);
	}

	// ��ȡ�ֵ��ĳ���
	@SuppressWarnings("unused")
	private double getDistance(DNode node, DNode eNode) {
		double result = Math.sqrt(Math.pow(node.getxPos() - eNode.getxPos(), 2)
				+ Math.pow(node.getyPos() - eNode.getyPos(), 2));
		return result;
	}

	/**
	 * ��ȡ���·��
	 * 
	 * @param resultList
	 * @param node
	 */
	@SuppressWarnings("unused")
	private void getPath(List<DNode> resultList, DNode node) {
		if (node.getParentNode() != null) {
			getPath(resultList, node.getParentNode());
		}
		resultList.add(node);
	}

	// ͨ�����ƻ�ȡ��·
	public DNode getDNode(String edgeName) {
		DNode result = null;
		Set<DNode> set = roadMap.keySet();
		for (Iterator<DNode> iter = set.iterator(); iter.hasNext();) {
			DNode key = (DNode) iter.next();
			if (key.getRoadName().equals(edgeName)) {
				result = key;
				break;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		DStar dstar = new DStar();
		dstar.setRoadTempMap();
		dstar.move("1", "10");
	}
}
