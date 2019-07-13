package com.ar_kmeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/*求两个点的综合距离*/
public class DistanceCompute {

	public double getDistance(Point point1, Point point2,List<String[]> AssSet) {

		List<String[]> table = null;
		List<String[]> relSet = null;// 为循环中删除频繁项集引入的辅助变量?

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

		double A = 0;// Pt累加值
		mergeNum = s1.size() + s2.size() - commonNum;
		if (mergeNum != commonNum) {
			
			table = AssSet;
			relSet = table;

			for (int i = 0; i < relSet.size(); i++) {
				// 从集合中获取数组
				String[] ob1 = (String[]) relSet.get(i);
				String[] pre1 = ob1[0].split(","); // 获取前件
				String[] con1 = ob1[1].split(","); // 获取后件
				String freSet1 = ob1[0] + "," + ob1[1];// 频繁项集

				double Pt = 0;
				double maxCt = Double.parseDouble(ob1[2]); // 存最大置信度

				if (containArray(pre1, split1) && containArray(pre1, split2))// a,b同时存在pre_t
				{
					if (containArray(con1, split1) && !containArray(con1, split2)
							|| !containArray(con1, split1) && containArray(con1, split2)) {

						for (int j = i + 1; j < relSet.size(); j++) {
							String[] ob2 = (String[]) relSet.get(j);
							String[] pre2 = ob2[0].split(","); // 获取前件
							String[] con2 = ob2[1].split(","); // 获取后件
							String freSet2 = ob2[0] + "," + ob2[1];//频繁项集
							if (containArray(pre2, split1) && containArray(pre2, split2) && Arrays.equals(con2, con1)) {

								if (maxCt < Double.parseDouble(ob2[2])) {
									maxCt = Double.parseDouble(ob2[2]);
									remove(relSet, freSet1);
									remove(relSet, freSet2);

								} else if (maxCt >= Double.parseDouble(ob2[2])) {
									remove(relSet, freSet1);
									remove(relSet, freSet2);
								}

							}
						}
						Pt = maxCt;
					}

				} else if (containArray(pre1, split1) && !containArray(pre1, split2) && !containArray(con1, split1)
						&& containArray(con1, split2)
						|| containArray(pre1, split2) && !containArray(pre1, split1) && !containArray(con1, split2)
								&& containArray(con1, split1)) {
					Pt = (double) 0.5 * maxCt;
					remove(relSet, freSet1);// 删除子频繁项集对应的关联规则
				} else {
					Pt = 0;
				}
				A = A + Pt;
			}
		} else
			A = 0;

		double jaccard = 1 - (commonNum + A) / mergeNum;
		double sim = jaccard + sumpre * 0.8;
		return sim;
	}

	// 删除频繁项集
	public static List<String[]> remove(List<String[]> table, String target) {
		int size = table.size();
		for (int i = size - 1; i >= 0; i--) {
			String[] t = table.get(i);
			String freSet = t[0] + "," + t[1];
			if (containArray(freSet.split(","), target.split(",")))
				table.remove(t);
		}
		return table;
	}

	public static boolean containArray(String[] a, String[] b) { // a小b�?
		boolean flag = false;
		int k = 0;
		/**
		 * 统计b中包含a中的元素是否与a的元素个数相�?
		 */
		if (a.length <= b.length) {
			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < b.length; j++) {
					if (a[i].equals(b[j])) {
						k++;
						continue;
					}
				}
			}
		}
		if (k == a.length) {
			flag = true;
		}
		return flag;
	}

	// 保存待赋值的关联规则对应的频繁项集，与后续关联规则进行比较
	public static List<String[]> save(List<String[]> table, String target) {
		int size = table.size();
		for (int i = size - 1; i >= 0; i--) {
			String[] t = table.get(i);
			String freSet = t[0] + "," + t[1];
			if (containArray(freSet.split(","), target.split(",")))
				table.remove(t);
		}
		return table;
	}
	
	
	
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
		double sim = jaccard + sumpre;
		return sim;
	}
	
	
	
}
