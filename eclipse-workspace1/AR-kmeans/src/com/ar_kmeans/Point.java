package com.ar_kmeans;

/*��װPoint��*/
public class Point {
	private String[] localArray;
    private int id;
    private int clusterId;  // ��ʶ�����ĸ������ġ�
    private double dist;     // ��ʶ�����������ĵľ��롣
 
    public Point(int id, String[] localArray) {
        this.id = id;
        this.localArray = localArray;
    }
 
    public Point(String[] localArray) {
        this.id = -1; //��ʾ����������һ����
        this.localArray = localArray;
    }
 
    public String[] getlocalArray() {
        return localArray;
    }
 
    public int getId() {
        return id;
    }
 
    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }
 
    public int getClusterid() {
        return clusterId;
    }
 
    public double getDist() {
        return dist;
    }
 
    public void setDist(double min_dis) {
        this.dist = min_dis;
    }
 
    @Override
    public String toString() {
        String result = "Point_id=" + id + "  [";
        for (int i = 0; i < localArray.length; i++) {
            result += localArray[i] + " ";
        }
        return result.trim()+"] clusterId: "+clusterId+" dist: "+dist;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
 
        Point point = (Point) obj;
        if (point.localArray.length != localArray.length)
            return false;
 
        for (int i = 0; i < localArray.length; i++) {
            if (point.localArray[i].compareTo(localArray[i])!= 0) { //��������ͬ
                return false;
            }
        }
        return true;
    }
 
}
