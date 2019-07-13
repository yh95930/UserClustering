package com.ar_kmeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Main {
	public static void main(String[] args) {

		Assessment ass = new Assessment();

		Main obj = new Main();
		/** 此处为创建Excel路径 */
		File file = new File("D:/eclipse-workspace/AR-kmeans/userSet_50000.xls");
		File file1 = new File("D:/eclipse-workspace/AR-kmeans/123.xls");
		List<String[]> original_data = obj.readExcel(file);
		List<String[]> AssSet = obj.readExcel1(file1);
		KMeansRun kRun = new KMeansRun(50, original_data, AssSet);
		List<Cluster> clusterSet = kRun.run();

		for (Cluster cluster : clusterSet) {
			System.out.println(cluster);
		}
		System.out.println("单次迭代运行次数：" + kRun.getIterTimes());
		ass.assessment(clusterSet, AssSet);
	}

	/** 去读Excel的方法readExcel，该方法的入口参数为一个File对象 */
	public ArrayList<String[]> readExcel(File file) {
		ArrayList<String[]> dataSet = new ArrayList<String[]>(200);
		try {
			// 创建输入流，读取Excel
			InputStream is = new FileInputStream(file.getAbsolutePath());
			// jxl提供的Workbook类
			Workbook wb = Workbook.getWorkbook(is);
			// Excel的页签数量
			int sheet_size = wb.getNumberOfSheets();
			for (int index = 0; index < 1; index++) {
				// 每个页签创建一个Sheet对象
				Sheet sheet = wb.getSheet(index);
				// sheet.getRows()返回该页的总行数 sheet.getRows()
				for (int i = 0; i < 10000; i++) {
					String cellinfo = null;
					String[] b = new String[4];
					// sheet.getColumns()返回该页的总列数
					for (int j = 0; j < sheet.getColumns(); j++) {
						cellinfo = sheet.getCell(j, i).getContents();
						try {
							String a = cellinfo;
							b[j] = a;
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
					dataSet.add(b);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataSet;
	}

	public ArrayList<String[]> readExcel1(File file) {
		ArrayList<String[]> dataSet = new ArrayList<String[]>();
		try {
			// 创建输入流，读取Excel
			InputStream is = new FileInputStream(file.getAbsolutePath());
			// jxl提供的Workbook�?
			Workbook wb = Workbook.getWorkbook(is);
			// Excel的页签数�?
			int sheet_size = wb.getNumberOfSheets();
			for (int index = 0; index < sheet_size; index++) {
				// 每个页签创建Sheet对象
				Sheet sheet = wb.getSheet(index);
				// sheet.getRows()返回该页的行数
				// for (int i = 0; i < sheet.getRows(); i++)
				for (int i = 0; i < 140; i++) { // 当前excel行数
					String cellinfo = null;
					String[] b = new String[4];
					// sheet.getColumns()返回该页的列数
					for (int j = 0; j < 3; j++) {
						cellinfo = sheet.getCell(j, i).getContents();
						try {
							String a = cellinfo;
							b[j] = a;
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
					dataSet.add(b);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataSet;
	}
}
