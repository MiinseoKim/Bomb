package org.androidtown.myapplication123;


public class TimeTableModel {
	private int id;
	private int startnum;
	private int endnum;
	private int week;
	private String name="";
	private String classroom="";


	@Override
	public String toString() {
		return "TimeTableModel [ startnum=" + startnum
				+ ", endnum=" + endnum + ", week=" + week +  ", name=" + name
				+ ", classroom=" + classroom+ "]";
	}


	public int getId(){return id;}

	public int getStartnum() {
		return startnum;
	}

	public int getEndnum() {
		return endnum;
	}

	public int getWeek() {
		return week;
	}

	public String getName() {	return name;	}

	public String getClassroom() {
		return classroom;
	}


	public void setStartnum(int startnum) {
		this.startnum = startnum;
	}

	public void setEndnum(int endnum) {
		this.endnum = endnum;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}



	public TimeTableModel(int id,int startnum, int endnum, int week, String name,
						  String classroom) {
		super();
		this.id=id;
		this.startnum = startnum;
		this.endnum = endnum;
		this.week = week;
		this.name = name;
		this.classroom = classroom;

	}

}
