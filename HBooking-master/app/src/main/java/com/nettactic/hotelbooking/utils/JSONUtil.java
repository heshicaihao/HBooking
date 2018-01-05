package com.nettactic.hotelbooking.utils;

import com.nettactic.hotelbooking.bean.OrderBean;
import com.nettactic.hotelbooking.bean.PointsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JSONUtil {

	/**
	 *解析请求成功后的结果JSONObject
	 * @param json
	 * @return
	 * @throws JSONException
     */
	public static JSONObject resolveResult(String json) throws JSONException {
		JSONObject JSONObject = new JSONObject(json);
		JSONObject result = JSONObject.optJSONObject("result");
		return result;
	}


	/**
	 * 解析 获得订单数据
	 *
	 * @param arr
	 * @return
	 */
	public static List<OrderBean> getOrderList(JSONArray arr) {
		List<OrderBean> orderList = new ArrayList<OrderBean>();

		for (int i = 0; i < arr.length(); i++) {
			OrderBean orderbean = new OrderBean();
			try {
				JSONObject object = (JSONObject) arr.get(i);
				String order_id = object.optString("order_id");
				orderbean.setOrder_id(order_id);

				String createtime = object.optString("createtime");
				orderbean.setCreatetime(createtime);

				String time_in = object.optString("arrival_date");
				orderbean.setTime_in(time_in);

				String time_out = object.optString("departure_date");
				orderbean.setTime_out(time_out);

				String pay_status = object.optString("pay_status");
				orderbean.setPay_status(pay_status);

				String order_status = object.optString("order_status");
				orderbean.setOrder_status(order_status);

				String check_in_status = object.optString("occupancy_status");
				orderbean.setCheck_in_status(check_in_status);

				String user_name = object.optString("user_name");
				orderbean.setUser_name(user_name);

				String room_name = object.optString("room_type");
				orderbean.setRoom_name(room_name);

				orderList.add(orderbean);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return orderList;

	}



	/**
	 * 解析 获得订单数据
	 *
	 * @param arr
	 * @return
	 */
	public static List<PointsBean> getPointsList(JSONArray arr) {
		List<PointsBean> orderList = new ArrayList<PointsBean>();

		for (int i = 0; i < arr.length(); i++) {
			PointsBean orderbean = new PointsBean();
			try {
				JSONObject object = (JSONObject) arr.get(i);
				String order_id = object.optString("id");
				orderbean.setId(order_id);

				String createtime = object.optString("date");
				orderbean.setDate(createtime);

				String time_in = object.optString("integral");
				orderbean.setIntegral(time_in);

				orderList.add(orderbean);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return orderList;

	}


}
