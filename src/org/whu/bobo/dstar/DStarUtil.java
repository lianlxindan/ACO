package org.whu.bobo.dstar;

import java.util.Comparator;

public class DStarUtil {

	// 改变道路权值
	public static void setRoadWeight(DNode node, int flag) {
		double p = 0.1;
		if (flag == 0) {
			node.setRoadWeight(node.getRoadWeight() * p);
		} else if (flag == 1) {
			node.setRoadWeight(node.getRoadWeight() * (5 + p));
		}
	}

	// 计算H值
	public static void countH(DNode node, DNode sNode) {
		node.setH(Math.sqrt(Math.pow(node.getxPos() - sNode.getxPos(), 2)
				+ Math.pow(node.getyPos() - sNode.getyPos(), 2)));
	}

	// 计算key值
	public static void setKey(DNode node) {
		Key key = null;
		if (node.getKey() != null) {
			key = node.getKey();
		} else {
			key = new Key();
		}
		double key1 = Math.min(node.getG(), node.getRhs()) + node.getH();
		double key2 = Math.min(node.getG(), node.getRhs());
		key.setKey1(key1);
		key.setKey2(key2);
		node.setKey(key);
	}
}

// 节点比较类
class DNodeFComparator implements Comparator<DNode> {
	public int compare(DNode o1, DNode o2) {
		double o1key1 = o1.getKey().getKey1();
		double o1key2 = o1.getKey().getKey2();
		double o2key1 = o2.getKey().getKey1();
		double o2key2 = o2.getKey().getKey2();
		if (o1key1 < o2key1 || o1key1 == o2key1 && o1key2 <= o2key2) {
			return -1;
		} else if (o1key1 == o2key1 && o1key2 == o2key2) {
			return 0;
		} else {
			return 1;
		}
	}
}