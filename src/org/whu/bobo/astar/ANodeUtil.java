package org.whu.bobo.astar;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class ANodeUtil {
	// ����G,H,Fֵ
	public static void count(ANode node, ANode eNode) {
		countG(node, eNode);
		countH(node, eNode);
		countF(node);// �Ĺ�
	}

	// ����Gֵ
	public static void countG(ANode node, ANode eNode) {
		double cost = node.getRoadWeight();
		if (node.getParentNode() == null) {
			node.setG(cost);
		} else {
			node.setG(node.getParentNode().getG() + cost);
		}
	}

	// ����Hֵ
	public static void countH(ANode node, ANode eNode) {
		node.setH(Math.sqrt(Math.pow(node.getxPos() - eNode.getxPos(), 2)
				+ Math.pow(node.getyPos() - eNode.getyPos(), 2)));
	}

	// ����Fֵ
	public static void countF(ANode node) {
		node.setF(node.getG() + node.getH());
	}

	// ͨ�����ƻ�ȡ��·
	public static ANode getANode(HashMap<ANode, List<ANode>> roadMap,
			String edgeName) {
		ANode result = null;
		Set<ANode> set = roadMap.keySet();
		for (Iterator<ANode> iter = set.iterator(); iter.hasNext();) {
			ANode key = (ANode) iter.next();
			if (key.getRoadName().equals(edgeName)) {
				result = key;
				break;
			}
		}
		return result;
	}
}

// �ڵ�Ƚ���
class ANodeFComparator implements Comparator<ANode> {
	@Override
	public int compare(ANode o1, ANode o2) {
		if (o1.getF() > o2.getF()) {
			return 1;
		} else if (o1.getF() == o2.getF()) {
			return 0;
		} else {
			return -1;
		}
	}
}
