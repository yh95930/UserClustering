package kmodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Main {
	public static void main(String[] args) {

		Assessment ass = new Assessment();

		Main obj = new Main();
		// �˴�Ϊ�Ҵ���Excel·����E:/zhanhj/studysrc/jxl��
		File file = new File("D:/eclipse-workspace4/K-modes/userSet_5000.xls");
		KMeansRun kRun = new KMeansRun(50, obj.readExcel(file));
		List<Cluster> clusterSet = kRun.run();

		for (Cluster cluster : clusterSet) {
			System.out.println(cluster);
		}
		System.out.println("���ε������д�����" + kRun.getIterTimes());
		ass.assessment(clusterSet);
	}

	// ȥ��Excel�ķ���readExcel���÷�������ڲ���Ϊһ��File����
	public ArrayList<String[]> readExcel(File file) {
		ArrayList<String[]> dataSet = new ArrayList<String[]>(200);
		try {
			// ��������������ȡExcel
			InputStream is = new FileInputStream(file.getAbsolutePath());
			// jxl�ṩ��Workbook��
			Workbook wb = Workbook.getWorkbook(is);
			// Excel��ҳǩ����
			int sheet_size = wb.getNumberOfSheets();
			for (int index = 0; index < 1; index++) {
				// ÿ��ҳǩ����һ��Sheet����
				Sheet sheet = wb.getSheet(index);
				// sheet.getRows()���ظ�ҳ�������� sheet.getRows()
				for (int i = 0; i < 5000; i++) {
					String cellinfo = null;
					String[] b = new String[4];
					// sheet.getColumns()���ظ�ҳ��������
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
}