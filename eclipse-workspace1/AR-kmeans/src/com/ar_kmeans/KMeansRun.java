package com.ar_kmeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class KMeansRun {
	private int kNum; // �صĸ���
	private int iterNum = 10; // ��������

	private int iterMaxTimes = 100000; // ���ε���������д���
	private int iterRunTimes = 0; // ���ε���ʵ�����д���
	private double disDiff = 0.0000000000001d; // ���ε�����ֹ���������������������ĵľ����

	private List<String[]> original_data = null; // ���ڴ�ţ�ԭʼ���ݼ�
	private List<String[]> AssSet = null;// ���ڴ�Ź�������
	private static List<Point> pointList = null; // ���ڴ�ţ�ԭʼ���ݼ��������ĵ㼯
	private DistanceCompute disC = new DistanceCompute();
	private int len = 0; // ���ڼ�¼ÿ�����ݵ��ά��
	private double disMax = 0; // ��¼����������
	static double oldSUM = 0; // ��¼�ϴε���Ŀ�꺯���ܺ�

	public KMeansRun(int k, List<String[]> original_data, List<String[]> AssSet) {
		this.kNum = k;
		this.original_data = original_data;
		this.len = original_data.get(0).length;
		this.AssSet = AssSet;
		// ���淶
		check();
		// ��ʼ���㼯
		init();
	}

	/**
	 * ���淶
	 */
	private void check() {
		if (kNum == 0) {
			throw new IllegalArgumentException("k must be the number > 0");
		}
		if (original_data == null) {
			throw new IllegalArgumentException("program can't get real data");
		}
	}

	/**
	 * ��ʼ�����ݼ���������ת��ΪPoint���͡�
	 */
	private void init() {
		pointList = new ArrayList<Point>(200);
		for (int i = 0, j = original_data.size(); i < j; i++) {
			pointList.add(new Point(i, original_data.get(i)));
		}
	}

	/** listתstring */
	public static String listToString(List<String> mList) {
		String convertedListStr = "";
		if (null != mList && mList.size() > 0) {
			String[] mListArray = mList.toArray(new String[mList.size()]);
			for (int i = 0; i < mListArray.length; i++) {
				if (i < mListArray.length - 1) {
					convertedListStr += mListArray[i] + ",";
				} else {
					convertedListStr += mListArray[i];
				}
			}
			return convertedListStr;
		} else
			return "List is null!!!";
	}

	/**
	 * ���ѡȡ���ĵ㣬�����������ࡣ
	 */
	private List<Cluster> chooseCenterCluster() {
		List<Cluster> clusterSet = new ArrayList<Cluster>();
		HashMap<int[], Double> map = new HashMap<int[], Double>(); // ������㣬�����
		Random random = new Random();

		// ������������
		int[] value = new int[2];
		double Dis = 0;
		for (int i = 0; i < pointList.size(); i++) {
			for (int j = i + 1; j < pointList.size(); j++) {
				Dis = (double) disC.getDistance(pointList.get(i), pointList.get(j), AssSet);
				value[0] = pointList.get(i).getId();
				value[1] = pointList.get(j).getId();
				if (disMax < Dis) {
					disMax = Dis;
					map.put(value, Dis);
				}
			}
		}
		
		for (int id = 0; id < kNum;) {
			// ǰ����������Զ�ĵ���Ϊ����
			if (id <= 1) {
				int[] maxKey = getMaxKey1(map);
				for (Point point : pointList) {
					if (point.getId() == maxKey[0] || point.getId() == maxKey[1]) {
						// ��������ӵ�clusterset
						Cluster cluster = new Cluster(id, point);
						clusterSet.add(cluster);
						id++;
					}
				}
			} else {
				Point point = pointList.get(random.nextInt(pointList.size()));// ���ѡȡ����Ϊ����
				// ���ڱ���Ƿ��Ѿ�ѡ���������
				boolean flag = false;
				for (Cluster cluster : clusterSet) {
					if (cluster.getCenter().equals(point)) {
						flag = true;
					}
				}
				// ������ѡȡ�ĵ�û�б�ѡ�й���������һ��cluster
				if (flag == false) {
					Cluster cluster = new Cluster(id, point);
					clusterSet.add(cluster);
					id++;
				}
			}
		}

		/*
		 * for (int id = 0; id < kNum;) { Point point =
		 * pointList.get(random.nextInt(pointList.size()));// ���ѡȡ����Ϊ���� //
		 * ���ڱ���Ƿ��Ѿ�ѡ��������� boolean flag = false; for (Cluster cluster : clusterSet) { if
		 * (cluster.getCenter().equals(point)) { flag = true; } } //
		 * ������ѡȡ�ĵ�û�б�ѡ�й���������һ��cluster if (flag == false) { Cluster cluster = new
		 * Cluster(id, point); clusterSet.add(cluster); id++; } }
		 */

		return clusterSet;
	}

	/**
	 * Ϊÿ�������һ���࣡
	 */
	public void cluster(List<Cluster> clusterSet) {
		// ����ÿ���㵽K�����ĵľ��룬����Ϊÿ����������
		for (Point point : pointList) {
			double min_dis = Integer.MAX_VALUE;
			for (Cluster cluster : clusterSet) {
				double tmp_dis = (double) Math.min(disC.getDistance(point, cluster.getCenter(), AssSet), min_dis);
				if (tmp_dis != min_dis) {
					min_dis = tmp_dis;
					point.setClusterId(cluster.getId());
					point.setDist(min_dis);
				}
			}
		}
		// �����ԭ�����е����г�Ա�������еĵ㣬�ֱ����ÿ�����
		for (Cluster cluster : clusterSet) {
			cluster.getMembers().clear();
			for (Point point : pointList) {
				if (point.getClusterid() == cluster.getId()) {
					cluster.addPoint(point);
				}
			}
		}

	}

	/**
	 * ����ÿ���������λ�ã�
	 */
	public boolean calculateCenter(List<Cluster> clusterSet) {
		boolean ifNeedIter = false;
		double SUM = 0;

		for (Cluster cluster : clusterSet) {
			List<Point> point_list = cluster.getMembers();
			double[] sumAll = new double[len];
			String[] newCenter = new String[len]; // ��¼�����ģ���ƽ��ֵ������
			List<String> sum1 = new ArrayList<String>();// �浱ǰ���������˵��Ա�
			HashMap<String, Double> map1 = new HashMap<>();// ������
			HashMap<String, Double> map2 = new HashMap<>();// ��ѧ������
			HashMap<String, Double> map3 = new HashMap<>();// ����Ȥ����

			// ���е㣬��Ӧ����ά�Ƚ������
			for (int j = 0; j < len; j++) {
				if (j == 0) {
					for (int i = 0; i < point_list.size(); i++) {
						Double count = map1.get(point_list.get(i).getlocalArray()[j]);
						if (count == null)
							count = 1d;
						else
							count++;
						map1.put(point_list.get(i).getlocalArray()[j], count);
					}

					newCenter[0] = getMaxKey(map1);

				} else if (j == 1) {// ����Ϊ�Ա�
					int count1 = 0, count2 = 0;// ��Ů����
					for (int i = 0; i < point_list.size(); i++) {
						if (point_list.get(i).getlocalArray()[j].equals("1"))
							count1++;
						if (point_list.get(i).getlocalArray()[j].equals("2"))
							count2++;
					}
					if (count1 >= count2)
						newCenter[j] = "1";			
					else
						newCenter[j] = "2";
					
				} else if (j == 2) {// ����Ϊѧ��
					for (int i = 0; i < point_list.size(); i++) { // ������
						Double count = map2.get(point_list.get(i).getlocalArray()[j]);
						if (count == null)
							count = 1d;
						else
							count++;
						map2.put(point_list.get(i).getlocalArray()[j], count);

					}
					newCenter[2] = getMaxKey(map2);
				} else if (j == 3) {
					List<String[]> list = new ArrayList<String[]>();// ��ô��������˵���Ȥ
					for (int i = 0; i < point_list.size(); i++) {
						list.add(point_list.get(i).getlocalArray()[j].split(","));
					}

					for (int i = 0; i < list.size(); i++) { // ����Ȥ����
						for (int t = 0; t < list.get(i).length; t++) {
							Double count = map3.get(list.get(i)[t]);
							if (count == null)
								count = 1d;
							else
								count++;
							map3.put(list.get(i)[t], count);
						}
					}
					Iterator<Entry<String, Double>> it = map3.entrySet().iterator();
					List<String> list1 = new ArrayList<String>();// ������map.size()/2 �Ľ��
					while (it.hasNext()) {
						Entry<String, Double> entry = (Entry<String, Double>) it.next();
						Object obj = entry.getValue();
						if (obj != null && (Double) obj >= list.size() / 2) {
							list1.add((String) entry.getKey());
						}
					}
					newCenter[3] = listToString(list1);
				}
			}

			// ���������¡������ĵľ��룬�������һ���������ƶ��ľ������dis_diff�����������
			if (disC.getDistance(cluster.getCenter(), new Point(newCenter), AssSet) !=0) {
				ifNeedIter = true;
			}
			// �����µ�������λ��
			cluster.setCenter(new Point(newCenter));
		}
		return ifNeedIter;
	}

	// ������ֵ�ļ�
	public static String getMaxKey(Map<String, Double> hashMap) {
		String key = null;
		double value = 0;
		String flagKey = null;
		double flagValue = 0;
		Set<Entry<String, Double>> entrySet = hashMap.entrySet();
		for (Entry<String, Double> entry : entrySet) {
			// key value ����ÿ�ֱ���������ֵ
			key = entry.getKey();
			value = entry.getValue();
			if (flagValue < value) {
				// flagKey flagValue ���жϳ����ֵ�ǽ����ֵ����ñ���
				flagKey = key;
				flagValue = value;
			}
		}
		return flagKey;
	}

	// ����
	public static int[] getMaxKey1(Map<int[], Double> hashMap) {
		int[] key = null;
		double value = 0;
		int[] flagKey = null;
		double flagValue = 0;
		Set<Entry<int[], Double>> entrySet = hashMap.entrySet();
		for (Entry<int[], Double> entry : entrySet) {
			// key value ����ÿ�ֱ���������ֵ
			key = entry.getKey();
			value = entry.getValue();
			if (flagValue < value) {
				// flagKey flagValue ���жϳ����ֵ�ǽ����ֵ����ñ���
				flagKey = key;
				flagValue = value;
			}
		}
		return flagKey;
	}

	/**
	 * ���� k-means
	 */
	public List<Cluster> run() {
		List<Cluster> clusterSet = chooseCenterCluster();
		boolean ifNeedIter = true;
		while (ifNeedIter) {
			cluster(clusterSet);
			ifNeedIter = calculateCenter(clusterSet);
			iterRunTimes++;
		}
		return clusterSet;
	}

	/**
	 * ����ʵ�����д���
	 */
	public int getIterTimes() {
		return iterRunTimes;
	}
}
