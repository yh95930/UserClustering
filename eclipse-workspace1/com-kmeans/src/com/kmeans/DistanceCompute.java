package com.kmeans;

import java.util.HashSet;
import java.util.Set;

/*求两个点的综合距�?*/
public class DistanceCompute {

	public double getDistance(Point point1, Point point2) {

		String[] split1 = point1.getlocalArray()[3].split(",");
		String[] split2 = point2.getlocalArray()[3].split(",");

		Set<String> s1 = new HashSet<>(); // set元素不可重复
		Set<String> s2 = new HashSet<>();

		int count = 0; // 前三列不同的个数
		double sumpre = 0;
		for (int i = 0; i < 3; i++) {
			if (!point1.getlocalArray()[i].equals(point2.getlocalArray()[i]))
				count++;
		}
		if (count == 0)
			sumpre = 0;
		else if (count == 1)
			sumpre = 0.333333;
		else if (count == 2)
			sumpre = 0.666667;
		else
			sumpre = 1;

		for (int i = 0; i < split1.length; i++) {
			s1.add(split1[i]);// 将string里面的元素一个一个按索引放进set集合
		}
		for (int j = 0; j < split2.length; j++) {
			s2.add(split2[j]);
		}

		double mergeNum = 0;// 并集元素个数
		double commonNum = 0;// 相同元素个数（交集）

		for (String ch1 : s1) {
			for (String ch2 : s2) {
				if (ch1.equals(ch2)) {
					commonNum++;
				}
			}
		}
		mergeNum = s1.size() + s2.size() - commonNum;
		double jaccard = 1 - commonNum / mergeNum;
		double sim = jaccard + sumpre * 0.8;
		return sim;
	}

	/*
	 * 求欧式距�?
	 * 
	 * public double getEuclideanDis(Point p1, Point p2) { double count_dis = 0, a =
	 * 0; String[] p1_local_array = p1.getlocalArray(); String[] p2_local_array =
	 * p2.getlocalArray();
	 * 
	 * if (p1_local_array.length != p2_local_array.length) { throw new
	 * IllegalArgumentException("length of array must be equal!"); }
	 * 
	 * for (int i = 0; i < p1_local_array.length; i++) { if(i==0){
	 * 
	 * } a += (Math.pow(p1_local_array[i] - p2_local_array[i], 2) ; } count_dis = 1
	 * - 2 * Math.exp(-a) + 1;
	 * 
	 * return Math.sqrt(count_dis);
	 * 
	 * }
	 */
}
