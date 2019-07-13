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
	private int kNum; // 簇的个数
	private int iterNum = 10; // 迭代次数

	private int iterMaxTimes = 100000; // 单次迭代最大运行次数
	private int iterRunTimes = 0; // 单次迭代实际运行次数
	private double disDiff = 0.0000000000001d; // 单次迭代终止条件，两次运行中类中心的距离差

	private List<String[]> original_data = null; // 用于存放，原始数据集
	private List<String[]> AssSet = null;// 用于存放关联规则集
	private static List<Point> pointList = null; // 用于存放，原始数据集所构建的点集
	private DistanceCompute disC = new DistanceCompute();
	private int len = 0; // 用于记录每个数据点的维度
	private double disMax = 0; // 记录两点最大距离
	static double oldSUM = 0; // 记录上次迭代目标函数总和

	public KMeansRun(int k, List<String[]> original_data, List<String[]> AssSet) {
		this.kNum = k;
		this.original_data = original_data;
		this.len = original_data.get(0).length;
		this.AssSet = AssSet;
		// 检查规范
		check();
		// 初始化点集
		init();
	}

	/**
	 * 检查规范
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
	 * 初始化数据集，把数组转化为Point类型。
	 */
	private void init() {
		pointList = new ArrayList<Point>(200);
		for (int i = 0, j = original_data.size(); i < j; i++) {
			pointList.add(new Point(i, original_data.get(i)));
		}
	}

	/** list转string */
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
	 * 随机选取中心点，构建成中心类。
	 */
	private List<Cluster> chooseCenterCluster() {
		List<Cluster> clusterSet = new ArrayList<Cluster>();
		HashMap<int[], Double> map = new HashMap<int[], Double>(); // 存放两点，与距离
		Random random = new Random();

		// 计算两两距离
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
			// 前两个距离最远的点作为中心
			if (id <= 1) {
				int[] maxKey = getMaxKey1(map);
				for (Point point : pointList) {
					if (point.getId() == maxKey[0] || point.getId() == maxKey[1]) {
						// 把两点添加到clusterset
						Cluster cluster = new Cluster(id, point);
						clusterSet.add(cluster);
						id++;
					}
				}
			} else {
				Point point = pointList.get(random.nextInt(pointList.size()));// 随机选取点作为中心
				// 用于标记是否已经选择过该数据
				boolean flag = false;
				for (Cluster cluster : clusterSet) {
					if (cluster.getCenter().equals(point)) {
						flag = true;
					}
				}
				// 如果随机选取的点没有被选中过，则生成一个cluster
				if (flag == false) {
					Cluster cluster = new Cluster(id, point);
					clusterSet.add(cluster);
					id++;
				}
			}
		}

		/*
		 * for (int id = 0; id < kNum;) { Point point =
		 * pointList.get(random.nextInt(pointList.size()));// 随机选取点作为中心 //
		 * 用于标记是否已经选择过该数据 boolean flag = false; for (Cluster cluster : clusterSet) { if
		 * (cluster.getCenter().equals(point)) { flag = true; } } //
		 * 如果随机选取的点没有被选中过，则生成一个cluster if (flag == false) { Cluster cluster = new
		 * Cluster(id, point); clusterSet.add(cluster); id++; } }
		 */

		return clusterSet;
	}

	/**
	 * 为每个点分配一个类！
	 */
	public void cluster(List<Cluster> clusterSet) {
		// 计算每个点到K个中心的距离，并且为每个点标记类别号
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
		// 新清除原来所有的类中成员。把所有的点，分别加入每个类别
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
	 * 计算每个类的中心位置！
	 */
	public boolean calculateCenter(List<Cluster> clusterSet) {
		boolean ifNeedIter = false;
		double SUM = 0;

		for (Cluster cluster : clusterSet) {
			List<Point> point_list = cluster.getMembers();
			double[] sumAll = new double[len];
			String[] newCenter = new String[len]; // 记录新中心（求平均值得来）
			List<String> sum1 = new ArrayList<String>();// 存当前簇内所有人的性别
			HashMap<String, Double> map1 = new HashMap<>();// 存年龄
			HashMap<String, Double> map2 = new HashMap<>();// 存学历人数
			HashMap<String, Double> map3 = new HashMap<>();// 存兴趣人数

			// 所有点，对应各个维度进行求和
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

				} else if (j == 1) {// 属性为性别
					int count1 = 0, count2 = 0;// 男女计数
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
					
				} else if (j == 2) {// 属性为学历
					for (int i = 0; i < point_list.size(); i++) { // 找众数
						Double count = map2.get(point_list.get(i).getlocalArray()[j]);
						if (count == null)
							count = 1d;
						else
							count++;
						map2.put(point_list.get(i).getlocalArray()[j], count);

					}
					newCenter[2] = getMaxKey(map2);
				} else if (j == 3) {
					List<String[]> list = new ArrayList<String[]>();// 存该簇中所有人的兴趣
					for (int i = 0; i < point_list.size(); i++) {
						list.add(point_list.get(i).getlocalArray()[j].split(","));
					}

					for (int i = 0; i < list.size(); i++) { // 找兴趣众数
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
					List<String> list1 = new ArrayList<String>();// 存满足map.size()/2 的结果
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

			// 计算两个新、旧中心的距离，如果任意一个类中心移动的距离大于dis_diff则继续迭代。
			if (disC.getDistance(cluster.getCenter(), new Point(newCenter), AssSet) !=0) {
				ifNeedIter = true;
			}
			// 设置新的类中心位置
			cluster.setCenter(new Point(newCenter));
		}
		return ifNeedIter;
	}

	// 输出最大值的键
	public static String getMaxKey(Map<String, Double> hashMap) {
		String key = null;
		double value = 0;
		String flagKey = null;
		double flagValue = 0;
		Set<Entry<String, Double>> entrySet = hashMap.entrySet();
		for (Entry<String, Double> entry : entrySet) {
			// key value 代表每轮遍历出来的值
			key = entry.getKey();
			value = entry.getValue();
			if (flagValue < value) {
				// flagKey flagValue 当判断出最大值是将最大值赋予该变量
				flagKey = key;
				flagValue = value;
			}
		}
		return flagKey;
	}

	// 排序
	public static int[] getMaxKey1(Map<int[], Double> hashMap) {
		int[] key = null;
		double value = 0;
		int[] flagKey = null;
		double flagValue = 0;
		Set<Entry<int[], Double>> entrySet = hashMap.entrySet();
		for (Entry<int[], Double> entry : entrySet) {
			// key value 代表每轮遍历出来的值
			key = entry.getKey();
			value = entry.getValue();
			if (flagValue < value) {
				// flagKey flagValue 当判断出最大值是将最大值赋予该变量
				flagKey = key;
				flagValue = value;
			}
		}
		return flagKey;
	}

	/**
	 * 运行 k-means
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
	 * 返回实际运行次数
	 */
	public int getIterTimes() {
		return iterRunTimes;
	}
}
