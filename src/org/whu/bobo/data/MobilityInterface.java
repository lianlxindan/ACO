package org.whu.bobo.data;


import java.util.List;

public interface MobilityInterface {
	public List<String> getAllEdges();// ��ȡ�������нֵ�

	public double getEdgeLength(String edge);// ��ȡ�ֵ���·����

	public int getVehicleNum(String edge);// ��ȡĳ����·�����еĳ�����Ŀ

	public double getTravelTime(String edge);// ��ȡ��ʻ��ĳ����·�ϳ�������ʱ��

	public void setRoute(List<String> route);
	public List<String> getNextEdges(String edge);// ��ȡ�뱾�ֵ������Ľֵ���
}
