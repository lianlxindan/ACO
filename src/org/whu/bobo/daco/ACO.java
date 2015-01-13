package org.whu.bobo.daco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.whu.bobo.data.Mobility;

/**
 * 改进蚁群算法 1.局部、全局信息素更新 2.最小最大蚂蚁系统 3.信息素更新策略 4.双向蚁群算 5.加入方向性 6.用与终点的距离作为期望因子
 * 7.引入节点活跃度:活跃度=分支数/节点总数 8.概率边计数值改进点 9 更新挥发系数 10 信息数每次更新公式的改进？ 11.自适应阈值
 * 12.如何跳出局部最优？ 引入遗传算法中的交叉算子？混沌搅动？ 13. 信息素和启发信息标准化 14.带方向的信息素更新策略 15.引入逆向蚂蚁
 * 
 * 
 * Question? 1.转向延误模型？ 2.实验对比得出什么？对比什么？ 设计自适应算法？
 * 
 * @author bobo
 * @version 2.0
 */
public class ACO {
	public static HashMap<AntNode, List<AntNode>> roadMap = new HashMap<AntNode, List<AntNode>>();// 路网邻接表
	public static double ALPHA = 1.5;// 启发因子，信息素的重要程度
	public static double BETA = 2.5; // 期望因子，城市间距离的重要程度 需要调整
	public static double ROU = 0.5; // 信息素残留参数
	public static double P = 0.2; // 伪概率事件
	public static int N_ANT_COUNT = 5;// 蚂蚁数量
	public static int N_IT_COUNT = 10;// 迭代次数
	public static double DBQ = 100.0;// 总的信息量
	// 构建路网
	public void setRoadMap(double pre_trial) {
		Mobility m = new Mobility();
		String[] roadInfo = m.getRoadInfo();
		for (int i = 1; i < roadInfo.length; i += 2) {
			String roadName = roadInfo[i];
			String roadWeight = roadInfo[i + 1];
			double xPos = m.getCoordData(roadName).get(0);
			double yPos = m.getCoordData(roadName).get(1);
			AntNode road = new AntNode(roadName,
					Double.parseDouble(roadWeight), xPos, yPos, pre_trial);
			roadMap.put(road, null);
		}
		Set<AntNode> set = roadMap.keySet();
		int i = 1;
		for (Iterator<AntNode> iter = set.iterator(); iter.hasNext();) {
			AntNode key = (AntNode) iter.next();
			List<String> edges = m.getNextEdges(key.getRoadName());
			List<AntNode> nodeLink = new ArrayList<AntNode>();
			for (int j = 0; j < edges.size(); j++) {
				String edge = edges.get(j);
				AntNode temp = getAntNode(edge);
				nodeLink.add(temp);
			}
			roadMap.put(key, nodeLink);
			System.out.println(i + ":" + roadMap.size());
			i++;
		}
	}

	// 通过名称获取道路
	public AntNode getAntNode(String edgeName) {
		AntNode result = null;
		Set<AntNode> set = roadMap.keySet();
		for (Iterator<AntNode> iter = set.iterator(); iter.hasNext();) {
			AntNode key = (AntNode) iter.next();
			if (key.getRoadName().equals(edgeName)) {
				result = key;
				break;
			}
		}
		return result;
	}
}
