package com.ar_kmeans;

import java.util.ArrayList;
import java.util.List;

public class Assessment {

	private DistanceCompute disC = new DistanceCompute();

	public void assessment(List<Cluster> clusterSet, List<String[]> AssSet) {

		List<Double> avgDis = new ArrayList<Double>();// ���ڵ������ĵ�ƽ������
		List<Double> avgDis1 = new ArrayList<Double>();// �㵽ͬ������������ƽ������
		List<Double> avgDis2 = new ArrayList<Double>();// �㵽��ͬ������������ƽ������
		List<Point> pointList = null; // ��һ���صĵ�
		int number = 0;// ��¼�м����������
		for (Cluster cluster : clusterSet) {
			double C = cluster.getMembers().size();// ���г�Ա����
			double sum = 0;
			double dis = 0;

			pointList = cluster.getMembers();
			for (int i = 0; i < pointList.size(); i++) {
				dis = disC.getDistance(pointList.get(i), cluster.getCenter(), AssSet);
				sum += dis;
			}
			avgDis.add(sum / C);
			// �㵽ͬ������������ƽ������
			for (int i = 0; i < pointList.size(); i++) {
				sum = 0;
				for (int j = 0; j < pointList.size(); j++) {
					dis = disC.getDistance(pointList.get(i), pointList.get(j),AssSet);
					sum += dis;
				}
				avgDis1.add(sum / pointList.size());
			}
		}
		/* �㵽��ͬ������������ƽ������ */
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
					count++;// ��¼�м����뵱ǰ������id��ͬ
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
		System.out.println("����ϵ����" + String.format("%.6f",sum / pointListAll.size()));
		
		sum = 0;
		for (int i = 0; i < clusterSet.size(); i++) {
			sum += avgDis.get(i);
		}
		System.out.println("CPָ����" + String.format("%.6f",sum / clusterSet.size()));

		// DBָ��(ԽСԽ��)
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

				CenterDis.add(disC.getDistance(CenterPointList.get(i), CenterPointList.get(j)));// ��Ci���Cj���ĵ��ľ���
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

		System.out.println("SPָ����" + String.format("%.6f",SP));
		System.out.println("DBָ����" + String.format("%.6f",DBI));
	}

}
