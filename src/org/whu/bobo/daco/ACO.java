package org.whu.bobo.daco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.whu.bobo.data.Mobility;

/**
 * �Ľ���Ⱥ�㷨 1.�ֲ���ȫ����Ϣ�ظ��� 2.��С�������ϵͳ 3.��Ϣ�ظ��²���
 * 
 * @author bobo
 *
 */
public class ACO {
	public static HashMap<AntNode, List<AntNode>> roadMap = new HashMap<AntNode, List<AntNode>>();// ·���ڽӱ�
	public static double ALPHA = 1.0;// �������ӣ���Ϣ�ص���Ҫ�̶�
	public static double BETA = 2.0; // �������ӣ����м�������Ҫ�̶�
	public static double ROU = 0.5; // ��Ϣ�ز�������
	public static double P = 0.2; // // α�����¼�
	public static int N_ANT_COUNT = 10;// ��������
	public static int N_IT_COUNT = 100;// ��������
	public static double DBQ = 100.0;// �ܵ���Ϣ��

	// ����·��
	public void setRoadMap(double pre_trial) {
		Mobility m = new Mobility();
		String[] roadInfo = m.getRoadInfo();
		for (int i = 1; i < roadInfo.length; i += 2) {
			AntNode road = new AntNode(roadInfo[i],
					Double.parseDouble(roadInfo[i + 1]), pre_trial);
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

	// ͨ�����ƻ�ȡ��·
	public static AntNode getAntNode(String edgeName) {
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
