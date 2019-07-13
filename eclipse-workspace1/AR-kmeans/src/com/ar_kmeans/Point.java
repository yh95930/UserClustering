package com.ar_kmeans;

/*分装Point类*/
public class Point {
	private String[] localArray;
    private int id;
    private int clusterId;  // 标识属于哪个类中心。
    private double dist;     // 标识和所属类中心的距离。
 
    public Point(int id, String[] localArray) {
        this.id = id;
        this.localArray = localArray;
    }
 
    public Point(String[] localArray) {
        this.id = -1; //表示不属于任意一个类
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
            if (point.localArray[i].compareTo(localArray[i])!= 0) { //若两个不同
                return false;
            }
        }
        return true;
    }
 
}
