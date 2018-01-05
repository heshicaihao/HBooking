package com.nettactic.hotelbooking.bean;


import com.nettactic.hotelbooking.base.BaseBean;

public class OrderBean  extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String order_id;
	private String createtime;
	private String time_in;
	private String time_out;
	private String pay_status;
	private String order_status;
	private String check_in_status;
	private String user_name;
	private String room_name;

	public OrderBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getTime_in() {
		return time_in;
	}

	public void setTime_in(String time_in) {
		this.time_in = time_in;
	}

	public String getTime_out() {
		return time_out;
	}

	public void setTime_out(String time_out) {
		this.time_out = time_out;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getCheck_in_status() {
		return check_in_status;
	}

	public void setCheck_in_status(String check_in_status) {
		this.check_in_status = check_in_status;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getRoom_name() {
		return room_name;
	}

	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}

	public OrderBean(String order_id, String createtime, String time_in, String time_out, String pay_status, String order_status, String check_in_status, String user_name, String room_name) {
		this.order_id = order_id;
		this.createtime = createtime;
		this.time_in = time_in;
		this.time_out = time_out;
		this.pay_status = pay_status;
		this.order_status = order_status;
		this.check_in_status = check_in_status;
		this.user_name = user_name;
		this.room_name = room_name;
	}

	@Override
	public String toString() {
		return "OrderBean{" +
				"order_id='" + order_id + '\'' +
				", createtime='" + createtime + '\'' +
				", time_in='" + time_in + '\'' +
				", time_out='" + time_out + '\'' +
				", pay_status='" + pay_status + '\'' +
				", order_status='" + order_status + '\'' +
				", check_in_status='" + check_in_status + '\'' +
				", user_name='" + user_name + '\'' +
				", room_name='" + room_name + '\'' +
				'}';
	}
}
