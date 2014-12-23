package org.whu.bobo.data;


import java.util.List;

public interface MobilityInterface {
	public List<String> getAllEdges();// 获取城市所有街道

	public double getEdgeLength(String edge);// 获取街道道路长度

	public int getVehicleNum(String edge);// 获取某条道路上所有的车辆数目

	public double getTravelTime(String edge);// 获取行驶在某条道路上车的运行时间

	public void setRoute(List<String> route);
	public List<String> getNextEdges(String edge);// 获取与本街道相连的街道口
}
