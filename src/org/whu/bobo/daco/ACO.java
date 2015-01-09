package org.whu.bobo.daco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.whu.bobo.data.Mobility;

/**
 * �Ľ���Ⱥ�㷨 1.�ֲ���ȫ����Ϣ�ظ��� 2.��С�������ϵͳ 3.��Ϣ�ظ��²��� 4.˫����Ⱥ�� 5.���뷽���� 6.�����յ�ľ�����Ϊ��������
 * 7.����ڵ��Ծ��:��Ծ��=��֧��/�ڵ����� 8.���ʱ߼���ֵ�Ľ��� 9 ���»ӷ�ϵ�� 10 ��Ϣ��ÿ�θ��¹�ʽ�ĸĽ��� 11.����Ӧ��ֵ
 * 12.��������ֲ����ţ� �����Ŵ��㷨�еĽ������ӣ���������� 13. ��Ϣ�غ�������Ϣ��׼�� 14.���������Ϣ�ظ��²��� 15.������������
 * 
 * 
 * Question? 1.ת������ģ�ͣ� 2.ʵ��Աȵó�ʲô���Ա�ʲô��
 * 
 * @author bobo
 * @version 2.0
 */
public class ACO {
	public static HashMap<AntNode, List<AntNode>> roadMap = new HashMap<AntNode, List<AntNode>>();// ·���ڽӱ�
	public static double ALPHA = 1.5;// �������ӣ���Ϣ�ص���Ҫ�̶�
	public static double BETA = 2.5; // �������ӣ����м�������Ҫ�̶� ��Ҫ����
	public static double ROU = 0.5; // ��Ϣ�ز�������
	public static double P = 0.2; // α�����¼�
	public static int N_ANT_COUNT = 10;// ��������
	public static int N_IT_COUNT = 100;// ��������
	public static double DBQ = 100.0;// �ܵ���Ϣ��

	// ��������·��
	public void setTempMap(double pre_trial) {
		AntNode road1 = new AntNode("1", 0.1, 1.0, 1.0, pre_trial);
		AntNode road2 = new AntNode("2", 0.2, 1.0, 2.0, pre_trial);
		AntNode road3 = new AntNode("3", 0.2, 2.0, 1.0, pre_trial);
		AntNode road4 = new AntNode("4", 0.2, 4.0, 1.0, pre_trial);
		AntNode road5 = new AntNode("5", 0.2, 2.0, 3.0, pre_trial);
		AntNode road6 = new AntNode("6", 0.2, 3.0, 2.0, pre_trial);
		AntNode road7 = new AntNode("7", 0.2, 5.0, 2.0, pre_trial);
		AntNode road8 = new AntNode("8", 0.2, 1.0, 4.0, pre_trial);
		AntNode road9 = new AntNode("9", 0.2, 3.0, 4.0, pre_trial);
		AntNode road10 = new AntNode("10", 0.2, 5.0, 4.0, pre_trial);
		List<AntNode> temp1 = new ArrayList<AntNode>();
		temp1.add(road2);
		temp1.add(road3);
		roadMap.put(road1, temp1);
		List<AntNode> temp2 = new ArrayList<AntNode>();
		temp2.add(road8);
		roadMap.put(road2, temp2);
		List<AntNode> temp3 = new ArrayList<AntNode>();
		temp3.add(road4);
		temp3.add(road5);
		temp3.add(road6);
		roadMap.put(road3, temp3);
		List<AntNode> temp4 = new ArrayList<AntNode>();
		temp4.add(road7);
		roadMap.put(road4, temp4);
		List<AntNode> temp5 = new ArrayList<AntNode>();
		temp5.add(road9);
		roadMap.put(road5, temp5);
		List<AntNode> temp6 = new ArrayList<AntNode>();
		temp6.add(road10);
		roadMap.put(road6, temp6);
		List<AntNode> temp7 = new ArrayList<AntNode>();
		roadMap.put(road7, temp7);
		List<AntNode> temp8 = new ArrayList<AntNode>();
		roadMap.put(road8, temp8);
		List<AntNode> temp9 = new ArrayList<AntNode>();
		temp9.add(road10);
		roadMap.put(road9, temp9);
		List<AntNode> temp10 = new ArrayList<AntNode>();
		roadMap.put(road10, temp10);
	}

	// ����·��
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

	// ͨ�����ƻ�ȡ��·
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
