package com.nettactic.hotelbooking.bean;


import com.nettactic.hotelbooking.base.BaseBean;

public class PointsBean extends BaseBean {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String date;
	private String integral;

	public PointsBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	@Override
	public String toString() {
		return "PointsBean{" +
				"id='" + id + '\'' +
				", date='" + date + '\'' +
				", integral='" + integral + '\'' +
				'}';
	}

	public PointsBean(String id, String date, String integral) {
		this.id = id;
		this.date = date;
		this.integral = integral;
	}
}
