package com.nettactic.hotelbooking.amap.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.Doorway;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RailwayStationItem;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteBusWalkItem;
import com.amap.api.services.route.RouteRailwayItem;
import com.amap.api.services.route.TMC;
import com.amap.api.services.route.TaxiItem;
import com.amap.api.services.route.WalkStep;
import com.nettactic.hotelbooking.R;
import com.nettactic.hotelbooking.amap.util.AMapUtil;

import java.util.ArrayList;
import java.util.List;

public class RouteOverlay {
	protected List<Marker> stationMarkers = new ArrayList<Marker>();
	protected List<Polyline> allPolyLines = new ArrayList<Polyline>();
	protected Marker startMarker;
	protected Marker endMarker;
	protected LatLng startPoint;
	protected LatLng endPoint;
	protected AMap mAMap;
	private Context mContext;
	private Bitmap startBit, endBit, busBit, walkBit, driveBit;
	protected boolean nodeIconVisible = true;

	public RouteOverlay(Context context) {
		mContext = context;
	}

	/**
	 * 去掉walkRouteOverlay上所有的Marker。
	 */
	public void removeFromMap() {
		if (startMarker != null) {
			startMarker.remove();

		}
		if (endMarker != null) {
			endMarker.remove();
		}
		for (Marker marker : stationMarkers) {
			marker.remove();
		}
		for (Polyline line : allPolyLines) {
			line.remove();
		}
		destroyBit();
	}

	private void destroyBit() {
		if (startBit != null) {
			startBit.recycle();
			startBit = null;
		}
		if (endBit != null) {
			endBit.recycle();
			endBit = null;
		}
		if (busBit != null) {
			busBit.recycle();
			busBit = null;
		}
		if (walkBit != null) {
			walkBit.recycle();
			walkBit = null;
		}
		if (driveBit != null) {
			driveBit.recycle();
			driveBit = null;
		}
	}
	/**
	 * 给起点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
	 * @return 更换的Marker图片。
	 * @since V2.1.0
	 */
	protected BitmapDescriptor getStartBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.amap_start);
	}
	/**
	 * 给终点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
	 * @return 更换的Marker图片。
	 * @since V2.1.0
	 */
	protected BitmapDescriptor getEndBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.amap_end);
	}
	/**
	 * 给公交Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
	 * @return 更换的Marker图片。
	 * @since V2.1.0
	 */
	protected BitmapDescriptor getBusBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.amap_bus);
	}
	/**
	 * 给步行Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
	 * @return 更换的Marker图片。
	 * @since V2.1.0
	 */
	protected BitmapDescriptor getWalkBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.amap_man);
	}

	protected BitmapDescriptor getDriveBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.amap_car);
	}

	protected BitmapDescriptor getRideBitmapDescriptor(){
		return BitmapDescriptorFactory.fromResource(R.drawable.amap_ride);
	}

	protected void addStartAndEndMarker() {
		startMarker = mAMap.addMarker((new MarkerOptions())
				.position(startPoint).icon(getStartBitmapDescriptor())
				.title("\u8D77\u70B9"));
		// startMarker.showInfoWindow();

		endMarker = mAMap.addMarker((new MarkerOptions()).position(endPoint)
				.icon(getEndBitmapDescriptor()).title("\u7EC8\u70B9"));
		// mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint,
		// getShowRouteZoom()));
	}
	/**
	 * 移动镜头到当前的视角。
	 * @since V2.1.0
	 */
	public void zoomToSpan() {
		if (startPoint != null) {
			if (mAMap == null)
				return;
			try {
				LatLngBounds bounds = getLatLngBounds();
				mAMap.animateCamera(CameraUpdateFactory
						.newLatLngBounds(bounds, 50));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	protected LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder b = LatLngBounds.builder();
		b.include(new LatLng(startPoint.latitude, startPoint.longitude));
		b.include(new LatLng(endPoint.latitude, endPoint.longitude));
		for (Polyline polyline : allPolyLines){
			for (LatLng point : polyline.getPoints()){
				b.include(point);
			}
		}
		return b.build();
	}
	/**
	 * 路段节点图标控制显示接口。
	 * @param visible true为显示节点图标，false为不显示。
	 * @since V2.3.1
	 */
	public void setNodeIconVisibility(boolean visible) {
		try {
			nodeIconVisible = visible;
			if (this.stationMarkers != null && this.stationMarkers.size() > 0) {
				for (int i = 0; i < this.stationMarkers.size(); i++) {
					this.stationMarkers.get(i).setVisible(visible);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	protected void addStationMarker(MarkerOptions options) {
		if(options == null) {
			return;
		}
		Marker marker = mAMap.addMarker(options);
		if(marker != null) {
			stationMarkers.add(marker);
		}
		
	}

	protected void addPolyLine(PolylineOptions options) {
		if(options == null) {
			return;
		}
		Polyline polyline = mAMap.addPolyline(options);
		if(polyline != null) {
			allPolyLines.add(polyline);
		}
	}
	
	protected float getRouteWidth() {
		return 18f;
	}

	protected int getWalkColor() {
		return Color.parseColor("#6db74d");
	}

	/**
	 * 自定义路线颜色。
	 * return 自定义路线颜色。
	 * @since V2.2.1
	 */
	protected int getBusColor() {
		return Color.parseColor("#537edc");
	}

	protected int getDriveColor() {
		return Color.parseColor("#537edc");
	}

	protected int getRideColor(){
		return Color.parseColor("#537edc");
	}

	// protected int getShowRouteZoom() {
	// return 15;
	// }

    /**
     * 公交路线图层类。在高德地图API里，如果需要显示公交路线，可以用此类来创建公交路线图层。如不满足需求，也可以自己创建自定义的公交路线图层。
     * @since V2.1.0
     */
    public static class BusRouteOverlay extends com.nettactic.hotelbooking.amap.RouteOverlay {

        private BusPath busPath;
        private LatLng latLng;

        /**
         * 通过此构造函数创建公交路线图层。
         * @param context 当前activity。
         * @param amap 地图对象。
         * @param path 公交路径规划的一个路段。详见搜索服务模块的路径查询包（com.amap.api.services.route）中的类<strong> <a href="../../../../../../Search/com/amap/api/services/route/BusPath.html" title="com.amap.api.services.route中的类">BusPath</a></strong>。
         * @param start 起点坐标。详见搜索服务模块的核心基础包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/LatLonPoint.html" title="com.amap.api.services.core中的类">LatLonPoint</a></strong>。
         * @param end 终点坐标。详见搜索服务模块的核心基础包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/LatLonPoint.html" title="com.amap.api.services.core中的类">LatLonPoint</a></strong>。
         * @since V2.1.0
         */
        public BusRouteOverlay(Context context, AMap amap, BusPath path,
                               LatLonPoint start, LatLonPoint end) {
            super(context);
            this.busPath = path;
            startPoint = AMapUtil.convertToLatLng(start);
            endPoint = AMapUtil.convertToLatLng(end);
            mAMap = amap;
        }

        /**
         * 添加公交路线到地图上。
         * @since V2.1.0
         */

        public void addToMap() {
            /**
             * 绘制节点和线<br>
             * 细节情况较多<br>
             * 两个step之间，用step和step1区分<br>
             * 1.一个step内可能有步行和公交，然后有可能他们之间连接有断开<br>
             * 2.step的公交和step1的步行，有可能连接有断开<br>
             * 3.step和step1之间是公交换乘，且没有步行，需要把step的终点和step1的起点连起来<br>
             * 4.公交最后一站和终点间有步行，加入步行线路，还会有一些步行marker<br>
             * 5.公交最后一站和终点间无步行，之间连起来<br>
             */
            try {
                List<BusStep> busSteps = busPath.getSteps();
                for (int i = 0; i < busSteps.size(); i++) {
                    BusStep busStep = busSteps.get(i);
                    if (i < busSteps.size() - 1) {
                        BusStep busStep1 = busSteps.get(i + 1);// 取得当前下一个BusStep对象
                        // 假如步行和公交之间连接有断开，就把步行最后一个经纬度点和公交第一个经纬度点连接起来，避免断线问题
                        if (busStep.getWalk() != null
                                && busStep.getBusLine() != null) {
                            checkWalkToBusline(busStep);
                        }

                        // 假如公交和步行之间连接有断开，就把上一公交经纬度点和下一步行第一个经纬度点连接起来，避免断线问题
                        if (busStep.getBusLine() != null
                                && busStep1.getWalk() != null
                                && busStep1.getWalk().getSteps().size() > 0) {
                            checkBusLineToNextWalk(busStep, busStep1);
                        }
                        // 假如两个公交换乘中间没有步行，就把上一公交经纬度点和下一步公交第一个经纬度点连接起来，避免断线问题
                        if (busStep.getBusLine() != null
                                && busStep1.getWalk() == null
                                && busStep1.getBusLine() != null) {
                            checkBusEndToNextBusStart(busStep, busStep1);
                        }
                        // 和上面的很类似
                        if (busStep.getBusLine() != null
                                && busStep1.getWalk() == null
                                && busStep1.getBusLine() != null) {
                            checkBusToNextBusNoWalk(busStep, busStep1);
                        }
                        if (busStep.getBusLine() != null
                                && busStep1.getRailway() != null ) {
                            checkBusLineToNextRailway(busStep, busStep1);
                        }
                        if (busStep1.getWalk() != null &&
                                busStep1.getWalk().getSteps().size() > 0 &&
                                busStep.getRailway() != null) {
                            checkRailwayToNextWalk(busStep, busStep1);
                        }

                        if ( busStep1.getRailway() != null &&
                                busStep.getRailway() != null) {
                            checkRailwayToNextRailway(busStep, busStep1);
                        }

                        if (busStep.getRailway() != null &&
                            busStep1.getTaxi() != null ){
                            checkRailwayToNextTaxi(busStep, busStep1);
                        }


                    }

                    if (busStep.getWalk() != null
                            && busStep.getWalk().getSteps().size() > 0) {
                        addWalkSteps(busStep);
                    } else {
                        if (busStep.getBusLine() == null && busStep.getRailway() == null && busStep.getTaxi() == null) {
                            addWalkPolyline(latLng, endPoint);
                        }
                    }
                    if (busStep.getBusLine() != null) {
                        RouteBusLineItem routeBusLineItem = busStep.getBusLine();
                        addBusLineSteps(routeBusLineItem);
                        addBusStationMarkers(routeBusLineItem);
                        if (i == busSteps.size() - 1) {
                            addWalkPolyline(AMapUtil.convertToLatLng(getLastBuslinePoint(busStep)), endPoint);
                        }
                    }
                    if (busStep.getRailway() != null) {
                        addRailwayStep(busStep.getRailway());
                        addRailwayMarkers(busStep.getRailway());
                        if (i == busSteps.size() - 1) {
                            addWalkPolyline(AMapUtil.convertToLatLng(busStep.getRailway().getArrivalstop().getLocation()), endPoint);
                        }
                    }
                    if (busStep.getTaxi() != null) {
                        addTaxiStep(busStep.getTaxi());
                        addTaxiMarkers(busStep.getTaxi());
                    }
                }
                addStartAndEndMarker();

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }



        private void checkRailwayToNextTaxi(BusStep busStep, BusStep busStep1) {
            LatLonPoint railwayLastPoint = busStep.getRailway().getArrivalstop().getLocation();
            LatLonPoint taxiFirstPoint = busStep1.getTaxi().getOrigin();
            if (!railwayLastPoint.equals(taxiFirstPoint)) {
                addWalkPolyLineByLatLonPoints(railwayLastPoint, taxiFirstPoint);
            }
        }

        private void checkRailwayToNextRailway(BusStep busStep, BusStep busStep1) {
            LatLonPoint railwayLastPoint = busStep.getRailway().getArrivalstop().getLocation();
            LatLonPoint railwayFirstPoint = busStep1.getRailway().getDeparturestop().getLocation();
            if (!railwayLastPoint.equals(railwayFirstPoint)) {
                addWalkPolyLineByLatLonPoints(railwayLastPoint, railwayFirstPoint);
            }

        }

        private void checkBusLineToNextRailway(BusStep busStep, BusStep busStep1) {
            LatLonPoint busLastPoint = getLastBuslinePoint(busStep);
            LatLonPoint railwayFirstPoint = busStep1.getRailway().getDeparturestop().getLocation();
            if (!busLastPoint.equals(railwayFirstPoint)) {
                addWalkPolyLineByLatLonPoints(busLastPoint, railwayFirstPoint);
            }

        }

        private void checkRailwayToNextWalk(BusStep busStep, BusStep busStep1) {
            LatLonPoint railwayLastPoint = busStep.getRailway().getArrivalstop().getLocation();
            LatLonPoint walkFirstPoint = getFirstWalkPoint(busStep1);
            if (!railwayLastPoint.equals(walkFirstPoint)) {
                addWalkPolyLineByLatLonPoints(railwayLastPoint, walkFirstPoint);
            }

        }

        private void addRailwayStep(RouteRailwayItem railway) {
            List<LatLng> railwaylistpoint = new ArrayList<LatLng>();
            List<RailwayStationItem> railwayStationItems = new ArrayList<RailwayStationItem>();
            railwayStationItems.add(railway.getDeparturestop());
            railwayStationItems.addAll(railway.getViastops());
            railwayStationItems.add(railway.getArrivalstop());
            for (int i = 0; i < railwayStationItems.size(); i++) {
                railwaylistpoint.add(AMapUtil.convertToLatLng(railwayStationItems.get(i).getLocation()));
            }
            addRailwayPolyline(railwaylistpoint);
        }

        private void addTaxiStep(TaxiItem taxi){
            addPolyLine(new PolylineOptions().width(getRouteWidth())
                    .color(getBusColor())
                    .add(AMapUtil.convertToLatLng(taxi.getOrigin()))
                    .add(AMapUtil.convertToLatLng(taxi.getDestination())));
        }

        /**
         * @param busStep
         */
        private void addWalkSteps(BusStep busStep) {
            RouteBusWalkItem routeBusWalkItem = busStep.getWalk();
            List<WalkStep> walkSteps = routeBusWalkItem.getSteps();
            for (int j = 0; j < walkSteps.size(); j++) {
                WalkStep walkStep = walkSteps.get(j);
                if (j == 0) {
                    LatLng latLng = AMapUtil.convertToLatLng(walkStep
                            .getPolyline().get(0));
                    String road = walkStep.getRoad();// 道路名字
                    String instruction = getWalkSnippet(walkSteps);// 步行导航信息
                    addWalkStationMarkers(latLng, road, instruction);
                }

                List<LatLng> listWalkPolyline = AMapUtil
                        .convertArrList(walkStep.getPolyline());
                this.latLng = listWalkPolyline.get(listWalkPolyline.size() - 1);

                addWalkPolyline(listWalkPolyline);

                // 假如步行前一段的终点和下的起点有断开，断画直线连接起来，避免断线问题
                if (j < walkSteps.size() - 1) {
                    LatLng lastLatLng = listWalkPolyline.get(listWalkPolyline
                            .size() - 1);
                    LatLng firstlatLatLng = AMapUtil
                            .convertToLatLng(walkSteps.get(j + 1).getPolyline()
                                    .get(0));
                    if (!(lastLatLng.equals(firstlatLatLng))) {
                        addWalkPolyline(lastLatLng, firstlatLatLng);
                    }
                }

            }
        }

        /**
         * 添加一系列的bus PolyLine
         *
         * @param routeBusLineItem
         */
        private void addBusLineSteps(RouteBusLineItem routeBusLineItem) {
            addBusLineSteps(routeBusLineItem.getPolyline());
        }

        private void addBusLineSteps(List<LatLonPoint> listPoints) {
            if (listPoints.size() < 1) {
                return;
            }
            addPolyLine(new PolylineOptions().width(getRouteWidth())
                    .color(getBusColor())
                    .addAll(AMapUtil.convertArrList(listPoints)));
        }

        /**
         * @param latLng
         *            marker
         * @param title
         * @param snippet
         */
        private void addWalkStationMarkers(LatLng latLng, String title,
                                           String snippet) {
            addStationMarker(new MarkerOptions().position(latLng).title(title)
                    .snippet(snippet).anchor(0.5f, 0.5f).visible(nodeIconVisible)
                    .icon(getWalkBitmapDescriptor()));
        }

        /**
         * @param routeBusLineItem
         */
        private void addBusStationMarkers(RouteBusLineItem routeBusLineItem) {
            BusStationItem startBusStation = routeBusLineItem
                    .getDepartureBusStation();
            LatLng position = AMapUtil.convertToLatLng(startBusStation
                    .getLatLonPoint());
            String title = routeBusLineItem.getBusLineName();
            String snippet = getBusSnippet(routeBusLineItem);

            addStationMarker(new MarkerOptions().position(position).title(title)
                    .snippet(snippet).anchor(0.5f, 0.5f).visible(nodeIconVisible)
                    .icon(getBusBitmapDescriptor()));
        }

        private void addTaxiMarkers(TaxiItem taxiItem) {

            LatLng position = AMapUtil.convertToLatLng(taxiItem
                    .getOrigin());
            String title = taxiItem.getmSname()+"打车";
            String snippet = "到终点";

            addStationMarker(new MarkerOptions().position(position).title(title)
                    .snippet(snippet).anchor(0.5f, 0.5f).visible(nodeIconVisible)
                    .icon(getDriveBitmapDescriptor()));
        }

        private void addRailwayMarkers(RouteRailwayItem railway) {
            LatLng Departureposition = AMapUtil.convertToLatLng(railway
                    .getDeparturestop().getLocation());
            String Departuretitle = railway.getDeparturestop().getName()+"上车";
            String Departuresnippet = railway.getName();

            addStationMarker(new MarkerOptions().position(Departureposition).title(Departuretitle)
                    .snippet(Departuresnippet).anchor(0.5f, 0.5f).visible(nodeIconVisible)
                    .icon(getBusBitmapDescriptor()));


            LatLng Arrivalposition = AMapUtil.convertToLatLng(railway
                    .getArrivalstop().getLocation());
            String Arrivaltitle = railway.getArrivalstop().getName()+"下车";
            String Arrivalsnippet = railway.getName();

            addStationMarker(new MarkerOptions().position(Arrivalposition).title(Arrivaltitle)
                    .snippet(Arrivalsnippet).anchor(0.5f, 0.5f).visible(nodeIconVisible)
                    .icon(getBusBitmapDescriptor()));
        }
        /**
         * 如果换乘没有步行 检查bus最后一点和下一个step的bus起点是否一致
         *
         * @param busStep
         * @param busStep1
         */
        private void checkBusToNextBusNoWalk(BusStep busStep, BusStep busStep1) {
            LatLng endbusLatLng = AMapUtil
                    .convertToLatLng(getLastBuslinePoint(busStep));
            LatLng startbusLatLng = AMapUtil
                    .convertToLatLng(getFirstBuslinePoint(busStep1));
            if (startbusLatLng.latitude - endbusLatLng.latitude > 0.0001
                    || startbusLatLng.longitude - endbusLatLng.longitude > 0.0001) {
                drawLineArrow(endbusLatLng, startbusLatLng);// 断线用带箭头的直线连?
            }
        }

        /**
         *
         * checkBusToNextBusNoWalk 和这个类似
         *
         * @param busStep
         * @param busStep1
         */
        private void checkBusEndToNextBusStart(BusStep busStep, BusStep busStep1) {
            LatLonPoint busLastPoint = getLastBuslinePoint(busStep);
            LatLng endbusLatLng = AMapUtil.convertToLatLng(busLastPoint);
            LatLonPoint busFirstPoint = getFirstBuslinePoint(busStep1);
            LatLng startbusLatLng = AMapUtil.convertToLatLng(busFirstPoint);
            if (!endbusLatLng.equals(startbusLatLng)) {
                drawLineArrow(endbusLatLng, startbusLatLng);//
            }
        }

        /**
         * 检查bus最后一步和下一各step的步行起点是否一致
         *
         * @param busStep
         * @param busStep1
         */
        private void checkBusLineToNextWalk(BusStep busStep, BusStep busStep1) {
            LatLonPoint busLastPoint = getLastBuslinePoint(busStep);
            LatLonPoint walkFirstPoint = getFirstWalkPoint(busStep1);
            if (!busLastPoint.equals(walkFirstPoint)) {
                addWalkPolyLineByLatLonPoints(busLastPoint, walkFirstPoint);
            }
        }

        /**
         * 检查 步行最后一点 和 bus的起点 是否一致
         *
         * @param busStep
         */
        private void checkWalkToBusline(BusStep busStep) {
            LatLonPoint walkLastPoint = getLastWalkPoint(busStep);
            LatLonPoint buslineFirstPoint = getFirstBuslinePoint(busStep);

            if (!walkLastPoint.equals(buslineFirstPoint)) {
                addWalkPolyLineByLatLonPoints(walkLastPoint, buslineFirstPoint);
            }
        }

        /**
         * @param busStep1
         * @return
         */
        private LatLonPoint getFirstWalkPoint(BusStep busStep1) {
            return busStep1.getWalk().getSteps().get(0).getPolyline().get(0);
        }

        /**
         *
         */
        private void addWalkPolyLineByLatLonPoints(LatLonPoint pointFrom,
                LatLonPoint pointTo) {
            LatLng latLngFrom = AMapUtil.convertToLatLng(pointFrom);
            LatLng latLngTo = AMapUtil.convertToLatLng(pointTo);

            addWalkPolyline(latLngFrom, latLngTo);
        }

        /**
         * @param latLngFrom
         * @param latLngTo
         * @return
         */
        private void addWalkPolyline(LatLng latLngFrom, LatLng latLngTo) {
            addPolyLine(new PolylineOptions().add(latLngFrom, latLngTo)
                    .width(getRouteWidth()).color(getWalkColor()).setDottedLine(true));
        }

        /**
         * @param listWalkPolyline
         */
        private void addWalkPolyline(List<LatLng> listWalkPolyline) {

            addPolyLine(new PolylineOptions().addAll(listWalkPolyline)
                    .color(getWalkColor()).width(getRouteWidth()).setDottedLine(true));
        }

        private void addRailwayPolyline(List<LatLng> listPolyline) {

            addPolyLine(new PolylineOptions().addAll(listPolyline)
                    .color(getDriveColor()).width(getRouteWidth()));
        }


        private String getWalkSnippet(List<WalkStep> walkSteps) {
            float disNum = 0;
            for (WalkStep step : walkSteps) {
                disNum += step.getDistance();
            }
            return "\u6B65\u884C" + disNum + "\u7C73";
        }

        public void drawLineArrow(LatLng latLngFrom, LatLng latLngTo) {

            addPolyLine(new PolylineOptions().add(latLngFrom, latLngTo).width(3)
                    .color(getBusColor()).width(getRouteWidth()));// 绘制直线
        }

        private String getBusSnippet(RouteBusLineItem routeBusLineItem) {
            return "("
                    + routeBusLineItem.getDepartureBusStation().getBusStationName()
                    + "-->"
                    + routeBusLineItem.getArrivalBusStation().getBusStationName()
                    + ") \u7ECF\u8FC7" + (routeBusLineItem.getPassStationNum() + 1)
                    + "\u7AD9";
        }

        /**
         * @param busStep
         * @return
         */
        private LatLonPoint getLastWalkPoint(BusStep busStep) {

            List<WalkStep> walkSteps = busStep.getWalk().getSteps();
            WalkStep walkStep = walkSteps.get(walkSteps.size() - 1);
            List<LatLonPoint> lonPoints = walkStep.getPolyline();
            return lonPoints.get(lonPoints.size() - 1);
        }

        private LatLonPoint getExitPoint(BusStep busStep) {
            Doorway doorway = busStep.getExit();
            if (doorway == null) {
                return null;
            }
            return doorway.getLatLonPoint();
        }

        private LatLonPoint getLastBuslinePoint(BusStep busStep) {
            List<LatLonPoint> lonPoints = busStep.getBusLine().getPolyline();

            return lonPoints.get(lonPoints.size() - 1);
        }

        private LatLonPoint getEntrancePoint(BusStep busStep) {
            Doorway doorway = busStep.getEntrance();
            if (doorway == null) {
                return null;
            }
            return doorway.getLatLonPoint();
        }

        private LatLonPoint getFirstBuslinePoint(BusStep busStep) {
            return busStep.getBusLine().getPolyline().get(0);
        }
    }

    /**
     * 导航路线图层类。
     */
    public static class DrivingRouteOverlay extends com.nettactic.hotelbooking.amap.RouteOverlay {

        private DrivePath drivePath;
        private List<LatLonPoint> throughPointList;
        private List<Marker> throughPointMarkerList = new ArrayList<Marker>();
        private boolean throughPointMarkerVisible = true;
        private List<TMC> tmcs;
        private PolylineOptions mPolylineOptions;
        private PolylineOptions mPolylineOptionscolor;
        private Context mContext;
        private boolean isColorfulline = true;
        private float mWidth = 25;
        private List<LatLng> mLatLngsOfPath;

        public void setIsColorfulline(boolean iscolorfulline) {
            this.isColorfulline = iscolorfulline;
        }

        /**
         * 根据给定的参数，构造一个导航路线图层类对象。
         *
         * @param amap      地图对象。
         * @param path 导航路线规划方案。
         * @param context   当前的activity对象。
         */
        public DrivingRouteOverlay(Context context, AMap amap, DrivePath path,
                                   LatLonPoint start, LatLonPoint end, List<LatLonPoint> throughPointList) {
            super(context);
            mContext = context;
            mAMap = amap;
            this.drivePath = path;
            startPoint = AMapUtil.convertToLatLng(start);
            endPoint = AMapUtil.convertToLatLng(end);
            this.throughPointList = throughPointList;

            initBitmapDescriptor();
        }

        public float getRouteWidth() {
            return mWidth;
        }

        /**
         * 设置路线宽度
         *
         * @param mWidth 路线宽度，取值范围：大于0
         */
        public void setRouteWidth(float mWidth) {
            this.mWidth = mWidth;
        }

        /**
         * 添加驾车路线添加到地图上显示。
         */
        public void addToMap() {
            initPolylineOptions();
            try {
                if (mAMap == null) {
                    return;
                }

                if (mWidth == 0 || drivePath == null) {
                    return;
                }
                mLatLngsOfPath = new ArrayList<LatLng>();
                tmcs = new ArrayList<TMC>();
                List<DriveStep> drivePaths = drivePath.getSteps();
                mPolylineOptions.add(startPoint);
                for (int i = 0; i < drivePaths.size(); i++) {
                    DriveStep step = drivePaths.get(i);
                    List<LatLonPoint> latlonPoints = step.getPolyline();
                    List<TMC> tmclist = step.getTMCs();
                    tmcs.addAll(tmclist);
                    addDrivingStationMarkers(step, convertToLatLng(latlonPoints.get(0)));
                    for (LatLonPoint latlonpoint : latlonPoints) {
                        mPolylineOptions.add(convertToLatLng(latlonpoint));
                        mLatLngsOfPath.add(convertToLatLng(latlonpoint));
                    }
                }
                mPolylineOptions.add(endPoint);
                if (startMarker != null) {
                    startMarker.remove();
                    startMarker = null;
                }
                if (endMarker != null) {
                    endMarker.remove();
                    endMarker = null;
                }
                addStartAndEndMarker();
                addThroughPointMarker();
                if (isColorfulline && tmcs.size()>0 ) {
                    colorWayUpdate(tmcs);
                    showcolorPolyline();
                }else {
                    showPolyline();
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        /**
         * 初始化线段属性
         */
        private void initPolylineOptions() {

            mPolylineOptions = null;

            mPolylineOptions = new PolylineOptions();
            mPolylineOptions.color(getDriveColor()).width(getRouteWidth());
        }

        private void showPolyline() {
            addPolyLine(mPolylineOptions);
        }

        private void showcolorPolyline() {
            addPolyLine(mPolylineOptionscolor);

        }

        /**
         * 根据不同的路段拥堵情况展示不同的颜色
         *
         * @param tmcSection
         */
        private void colorWayUpdate(List<TMC> tmcSection) {
            if (mAMap == null) {
                return;
            }
            if (tmcSection == null || tmcSection.size() <= 0) {
                return;
            }
            TMC segmentTrafficStatus;
            mPolylineOptionscolor = null;
            mPolylineOptionscolor = new PolylineOptions();
            mPolylineOptionscolor.width(getRouteWidth());
            List<Integer> colorList = new ArrayList<Integer>();
            List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();
            List<LatLng> points = new ArrayList<>();
            List<Integer> texIndexList = new ArrayList<Integer>();
    //        mPolylineOptionscolor.add(startPoint);
    //        mPolylineOptionscolor.add(AMapUtil.convertToLatLng(tmcSection.get(0).getPolyline().get(0)));

            points.add(startPoint);
            points.add(AMapUtil.convertToLatLng(tmcSection.get(0).getPolyline().get(0)));
            colorList.add(getDriveColor());
            bitmapDescriptors.add(defaultRoute);

            BitmapDescriptor bitmapDescriptor = null;
            int textIndex = 0;
            texIndexList.add(textIndex);
            texIndexList.add(++textIndex);
            for (int i = 0; i < tmcSection.size(); i++) {
                segmentTrafficStatus = tmcSection.get(i);
                int color = getcolor(segmentTrafficStatus.getStatus());
                bitmapDescriptor = getTrafficBitmapDescriptor(segmentTrafficStatus.getStatus());
                List<LatLonPoint> mployline = segmentTrafficStatus.getPolyline();
                for (int j = 1; j < mployline.size(); j++) {
    //				mPolylineOptionscolor.add(AMapUtil.convertToLatLng(mployline.get(j)));
                    points.add(AMapUtil.convertToLatLng(mployline.get(j)));

                    colorList.add(color);

                    texIndexList.add(++textIndex);
                    bitmapDescriptors.add(bitmapDescriptor);
                }

            }


            points.add(endPoint);
            colorList.add(getDriveColor());
            bitmapDescriptors.add(defaultRoute);
            texIndexList.add(++textIndex);
            mPolylineOptionscolor.addAll(points);
            mPolylineOptionscolor.colorValues(colorList);

    //        mPolylineOptionscolor.setCustomTextureIndex(texIndexList);
    //        mPolylineOptionscolor.setCustomTextureList(bitmapDescriptors);
        }

        private BitmapDescriptor defaultRoute = null;
        private BitmapDescriptor unknownTraffic = null;
        private BitmapDescriptor smoothTraffic = null;
        private BitmapDescriptor slowTraffic = null;
        private BitmapDescriptor jamTraffic = null;
        private BitmapDescriptor veryJamTraffic = null;
        private void initBitmapDescriptor() {
            defaultRoute = BitmapDescriptorFactory.fromResource(R.mipmap.amap_route_color_texture_6_arrow);
            smoothTraffic = BitmapDescriptorFactory.fromResource(R.mipmap.amap_route_color_texture_4_arrow);
            unknownTraffic = BitmapDescriptorFactory.fromResource(R.mipmap.amap_route_color_texture_0_arrow);
            slowTraffic = BitmapDescriptorFactory.fromResource(R.mipmap.amap_route_color_texture_3_arrow);
            jamTraffic = BitmapDescriptorFactory.fromResource(R.mipmap.amap_route_color_texture_2_arrow);
            veryJamTraffic = BitmapDescriptorFactory.fromResource(R.mipmap.amap_route_color_texture_9_arrow);

        }

        private BitmapDescriptor getTrafficBitmapDescriptor(String status) {
    //        if (status.trim().equals("未知")) {
    //            return unknownTraffic;
    //        } else

            Log.e("ggb", "==> 路况信息 is " + status);
            if (status.equals("畅通")) {
                return smoothTraffic;
            } else if (status.equals("缓行")) {
                return slowTraffic;
            } else if (status.equals("拥堵")) {
                return jamTraffic;
            } else if (status.equals("严重拥堵")) {
                return veryJamTraffic;
            } else {
                return defaultRoute;
            }
        }

        private int getcolor(String status) {

            if (status.equals("畅通")) {
                return Color.GREEN;
            } else if (status.equals("缓行")) {
                 return Color.YELLOW;
            } else if (status.equals("拥堵")) {
                return Color.RED;
            } else if (status.equals("严重拥堵")) {
                return Color.parseColor("#990033");
            } else {
                return Color.parseColor("#537edc");
            }
        }

        public LatLng convertToLatLng(LatLonPoint point) {
            return new LatLng(point.getLatitude(),point.getLongitude());
      }

        /**
         * @param driveStep
         * @param latLng
         */
        private void addDrivingStationMarkers(DriveStep driveStep, LatLng latLng) {
            addStationMarker(new MarkerOptions()
                    .position(latLng)
                    .title("\u65B9\u5411:" + driveStep.getAction()
                            + "\n\u9053\u8DEF:" + driveStep.getRoad())
                    .snippet(driveStep.getInstruction()).visible(nodeIconVisible)
                    .anchor(0.5f, 0.5f).icon(getDriveBitmapDescriptor()));
        }

        @Override
        protected LatLngBounds getLatLngBounds() {
            LatLngBounds.Builder b = LatLngBounds.builder();
            b.include(new LatLng(startPoint.latitude, startPoint.longitude));
            b.include(new LatLng(endPoint.latitude, endPoint.longitude));
            if (this.throughPointList != null && this.throughPointList.size() > 0) {
                for (int i = 0; i < this.throughPointList.size(); i++) {
                    b.include(new LatLng(
                            this.throughPointList.get(i).getLatitude(),
                            this.throughPointList.get(i).getLongitude()));
                }
            }
            return b.build();
        }

        public void setThroughPointIconVisibility(boolean visible) {
            try {
                throughPointMarkerVisible = visible;
                if (this.throughPointMarkerList != null
                        && this.throughPointMarkerList.size() > 0) {
                    for (int i = 0; i < this.throughPointMarkerList.size(); i++) {
                        this.throughPointMarkerList.get(i).setVisible(visible);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        private void addThroughPointMarker() {
            if (this.throughPointList != null && this.throughPointList.size() > 0) {
                LatLonPoint latLonPoint = null;
                for (int i = 0; i < this.throughPointList.size(); i++) {
                    latLonPoint = this.throughPointList.get(i);
                    if (latLonPoint != null) {
                        throughPointMarkerList.add(mAMap
                                .addMarker((new MarkerOptions())
                                        .position(
                                                new LatLng(latLonPoint
                                                        .getLatitude(), latLonPoint
                                                        .getLongitude()))
                                        .visible(throughPointMarkerVisible)
                                        .icon(getThroughPointBitDes())
                                        .title("\u9014\u7ECF\u70B9")));
                    }
                }
            }
        }

        private BitmapDescriptor getThroughPointBitDes() {
            return BitmapDescriptorFactory.fromResource(R.drawable.amap_through);

        }

        /**
         * 获取两点间距离
         *
         * @param start
         * @param end
         * @return
         */
        public static int calculateDistance(LatLng start, LatLng end) {
            double x1 = start.longitude;
            double y1 = start.latitude;
            double x2 = end.longitude;
            double y2 = end.latitude;
            return calculateDistance(x1, y1, x2, y2);
        }

        public static int calculateDistance(double x1, double y1, double x2, double y2) {
            final double NF_pi = 0.01745329251994329; // 弧度 PI/180
            x1 *= NF_pi;
            y1 *= NF_pi;
            x2 *= NF_pi;
            y2 *= NF_pi;
            double sinx1 = Math.sin(x1);
            double siny1 = Math.sin(y1);
            double cosx1 = Math.cos(x1);
            double cosy1 = Math.cos(y1);
            double sinx2 = Math.sin(x2);
            double siny2 = Math.sin(y2);
            double cosx2 = Math.cos(x2);
            double cosy2 = Math.cos(y2);
            double[] v1 = new double[3];
            v1[0] = cosy1 * cosx1 - cosy2 * cosx2;
            v1[1] = cosy1 * sinx1 - cosy2 * sinx2;
            v1[2] = siny1 - siny2;
            double dist = Math.sqrt(v1[0] * v1[0] + v1[1] * v1[1] + v1[2] * v1[2]);

            return (int) (Math.asin(dist / 2) * 12742001.5798544);
        }


        //获取指定两点之间固定距离点
        public static LatLng getPointForDis(LatLng sPt, LatLng ePt, double dis) {
            double lSegLength = calculateDistance(sPt, ePt);
            double preResult = dis / lSegLength;
            return new LatLng((ePt.latitude - sPt.latitude) * preResult + sPt.latitude, (ePt.longitude - sPt.longitude) * preResult + sPt.longitude);
        }
        /**
         * 去掉DriveLineOverlay上的线段和标记。
         */
        @Override
        public void removeFromMap() {
            try {
                super.removeFromMap();
                if (this.throughPointMarkerList != null
                        && this.throughPointMarkerList.size() > 0) {
                    for (int i = 0; i < this.throughPointMarkerList.size(); i++) {
                        this.throughPointMarkerList.get(i).remove();
                    }
                    this.throughPointMarkerList.clear();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
