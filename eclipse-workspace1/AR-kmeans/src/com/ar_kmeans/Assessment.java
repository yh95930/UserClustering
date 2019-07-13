package com.ar_kmeans;

import java.util.ArrayList;
import java.util.List;

public class Assessment {

	private DistanceCompute disC = new DistanceCompute();

	public void assessment(List<Cluster> clusterSet, List<String[]> AssSet) {

		List<Double> avgDis = new ArrayList<Double>();// 簇内点与中心的平均距离
		List<Double> avgDis1 = new ArrayList<Double>();// 点到同簇其他样本的平均距离
		List<Double> avgDis2 = new ArrayList<Double>();// 点到不同簇其他样本的平均距离
		List<Point> pointList = null; // 存一个簇的点
		int number = 0;// 记录有几个中心组合
		for (Cluster cluster : clusterSet) {
			double C = cluster.getMembers().size();// 簇中成员数量
			double sum = 0;
			double dis = 0;

			pointList = cluster.getMembers();
			for (int i = 0; i < pointList.size(); i++) {
				dis = disC.getDistance(pointList.get(i), cluster.getCenter(), AssSet);
				sum += dis;
			}
			avgDis.add(sum / C);
			// 点到同簇其他样本的平均距离
			for (int i = 0; i < pointList.size(); i++) {
				sum = 0;
				for (int j = 0; j < pointList.size(); j++) {
					dis = disC.getDistance(pointList.get(i), pointList.get(j),AssSet);
					sum += dis;
				}
				avgDis1.add(sum / pointList.size());
			}
		}
		/* 点到不同簇其他样本的平均距离 */
		List<Point> pointListAll = new ArrayList<Point>();
		for (Cluster cluster : clusterSet) {
			pointListAll.addAll(cluster.getMembers());
		}
		for (int i = 0; i < pointListAll.size(); i++) {
			double dis = 0, sum = 0, count = 0;
			for (int j = 0; j < pointListAll.size(); j++) {
				if (pointListAll.get(i).getClusterid() != pointListAll.get(j).getClusterid()) {
					dis = disC.getDistance(pointListAll.get(i), pointListAll.get(j));
					sum += dis;
					count++;// 记录有几个与当前点所属id不同
				}
			}
			avgDis2.add(sum / count);
		}
		double sum = 0;
		for (int i = 0; i < pointListAll.size(); i++)
			if (avgDis1.get(i) < avgDis2.get(i))
				sum += 1 - avgDis1.get(i) / avgDis2.get(i);
			else if (avgDis1.get(i) > avgDis2.get(i))
				sum += avgDis2.get(i) / avgDis1.get(i) - 1;
			else
				sum += 0;
		System.out.println("轮廓系数：" + String.format("%.6f",sum / pointListAll.size()));
		
		sum = 0;
		for (int i = 0; i < clusterSet.size(); i++) {
			sum += avgDis.get(i);
		}
		System.out.println("CP指数：" + String.format("%.6f",sum / clusterSet.size()));

		// DB指数(越小越好)
		List<Point> CenterPointList = new ArrayList<Point>();
		List<Double> CenterDis = new ArrayList<Double>();
		double DBI = 0;
		double A = 0, MaxA = 0;
		sum = 0;
		for (Cluster cluster : clusterSet) {
			CenterPointList.add(cluster.getCenter());
		}
		for (int i = 0; i < CenterPointList.size(); i++) {
			for (int j = i + 1; j < CenterPointList.size(); j++) {

				CenterDis.add(disC.getDistance(CenterPointList.get(i), CenterPointList.get(j)));// 簇Ci与簇Cj中心点间的距离
				number++;

			}
		}

		for (int i = 0; i < CenterPointList.size(); i++) {
			for (int j = 0; j < CenterPointList.size(); j++) {
				if (i != j) {
					A = (avgDis.get(i) + avgDis.get(j))
							/ disC.getDistance(CenterPointList.get(i), CenterPointList.get(j));

					if (MaxA < A)
						MaxA = A;
				}
			}
			sum += MaxA;
		}
		DBI = sum / clusterSet.size();
		// SP
		sum = 0;
		double SP = 0;
		for (int i = 0; i < number; i++) {
			sum += CenterDis.get(i);
		}
		SP = sum / number;

		System.out.println("SP指数：" + String.format("%.6f",SP));
		System.out.println("DB指数：" + String.format("%.6f",DBI));
	}

}
