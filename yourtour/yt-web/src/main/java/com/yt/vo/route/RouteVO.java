package com.yt.vo.route;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yt.business.bean.PlaceBean;
import com.yt.business.bean.RouteMainBean;
import com.yt.business.bean.RouteScheduleBean;
import com.yt.core.utils.DateUtils;
import com.yt.core.utils.StringUtils;
import com.yt.utils.SessionUtils;
import com.yt.vo.SocialVO;
import com.yt.vo.member.UserVO;

public class RouteVO extends SocialVO {
	private String 	name; // 行程名称
	private String 	lineName;
	private String 	imageUrl;
	private long 	startDate; // 行程开始日期
	private long    endDate;
	private int		duration;

	private int  adultNum;
	private int  childNum;
	private int  olderNum;

	private int  charge; //费用
	private String chargeIncludes;
	private String chargeExcludes;

	private double commentScore;
	private int readNum;
	private int likeNum;
	private int favoriteNum;
	private int shareNum;
	private int commentNum;
	private String tags;

	private String 	feature; //行程特点
	private String 	routeMemo;

	//作为请求参数时，格式为id, 作为相应参数时格式为：id,name
	private String fromPlace; // 行程出发地点

	//作为请求参数时，格式为id1,id2..., 作为相应参数时格式为：id1,id2|name1,name2
	private String toPlaces;

	private List<RouteScheduleVO> schedules; // 行程日程列表

	private UserVO user;

	public static RouteVO transform(RouteMainBean bean) {
		if (bean == null) {
			return null;
		}
		
		RouteVO routeVO = new RouteVO();
		routeVO.fromBean(bean);
		routeVO.setName(bean.getName());
		routeVO.setLineName(bean.getLineName());
		routeVO.setStartDate(new Date(bean.getStartDate()).getTime());
		routeVO.setImageUrl(bean.getImageUrl());
		routeVO.setToPlaces(bean.getToPlaces());

		routeVO.setTags(bean.getTags());
		routeVO.setReadNum(bean.getReadNum());
		routeVO.setCommentScore(bean.getCommentScore());
		routeVO.setCommentNum(bean.getCommentNum());
		routeVO.setFavoriteNum(bean.getFavoriteNum());
		routeVO.setShareNum(bean.getShareNum());
		routeVO.setLikeNum(bean.getLikeNum());

		routeVO.setFeature(bean.getFeature());

		if (bean.getSchedules() != null && bean.getSchedules().size() > 0) {
			for (RouteScheduleBean scheduleBean : bean.getSchedules()) {
				if (scheduleBean == null) {
					continue;
				}
				RouteScheduleVO scheduleVO = RouteScheduleVO.transform(scheduleBean);
				routeVO.getSchedules().add(scheduleVO);
			}
		}
		
		return routeVO;
	}


	public static RouteMainBean transform(RouteVO vo) {
		if (vo == null) {
			return null;
		}
		RouteMainBean bean = new RouteMainBean();
		
		vo.toBean(bean);
		bean.setName(vo.getName());
		bean.setStartDate(vo.getStartDate());
		bean.setEndDate(vo.getEndDate());
		bean.setDuration(vo.getDuration());
		bean.setFromPlace(vo.getFromPlace());
		bean.setToPlaces(vo.getToPlaces());

		bean.setAdultNum(vo.getAdultNum());
		bean.setChildNum(vo.getChildNum());
		bean.setOlderNum(vo.getOlderNum());

		if (StringUtils.isNotNull(vo.getFromPlace())) {
			PlaceBean placeBean = new PlaceBean();
			placeBean.setId(Long.valueOf(vo.getFromPlace().split(",")[0]));
			bean.setFromPlaceBean(placeBean);
		}

		//保存目的地
		Set<PlaceBean> destinations = new HashSet<>();
		String[] places = vo.getToPlaces().split("[|]");
		for(String place : places){
			PlaceBean destination = new PlaceBean();
			destination.setId(Long.valueOf(place.split(",")[0]));
			destinations.add(destination);
		}
		bean.setToPlaceBeans(new ArrayList<>(destinations));

		//保存日程安排
		Date start = new Date(bean.getStartDate()), scheduleDate = null;
		int duration = DateUtils.getDaySub(bean.getStartDate(), bean.getEndDate()) + 2;

		List<RouteScheduleBean> scheduleBeans = new ArrayList<>();
		for(int index = 0; index < duration; index++){
			scheduleDate = DateUtils.add(start, index, Calendar.DATE);

			RouteScheduleBean scheduleBean = new RouteScheduleBean();
			scheduleBean.setIndex(DateUtils.formatDateAsInteger(scheduleDate.getTime()) * 1000);
			scheduleBean.setDate(scheduleDate.getTime());
			scheduleBean.setName(String.format("第%s天-%s", index+1, DateUtils.formatDate(scheduleDate.getTime())));
			scheduleBean.setCreatedUserId(SessionUtils.getCurrentLoginUser());
			scheduleBean.setUpdatedUserId(SessionUtils.getCurrentLoginUser());
			scheduleBean.setCreatedTime(DateUtils.getCurrentTimeMillis());
			scheduleBean.setUpdatedTime(DateUtils.getCurrentTimeMillis());

			scheduleBeans.add(scheduleBean);
		}
		bean.setSchedules(scheduleBeans);

		return bean;
	}

	public RouteVO() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public String getChargeIncludes() {
		return chargeIncludes;
	}

	public void setChargeIncludes(String chargeIncludes) {
		this.chargeIncludes = chargeIncludes;
	}

	public String getChargeExcludes() {
		return chargeExcludes;
	}

	public void setChargeExcludes(String chargeExcludes) {
		this.chargeExcludes = chargeExcludes;
	}

	public int getReadNum() {
		return readNum;
	}

	public void setReadNum(int readNum) {
		this.readNum = readNum;
	}

	public double getCommentScore() {
		return commentScore;
	}

	public void setCommentScore(double commentScore) {
		this.commentScore = commentScore;
	}

	public int getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}

	public int getFavoriteNum() {
		return favoriteNum;
	}

	public void setFavoriteNum(int favoriteNum) {
		this.favoriteNum = favoriteNum;
	}

	public int getShareNum() {
		return shareNum;
	}

	public void setShareNum(int shareNum) {
		this.shareNum = shareNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public String getFromPlace() {
		return fromPlace;
	}

	public void setFromPlace(String fromPlace) {
		this.fromPlace = fromPlace;
	}

	public List<RouteScheduleVO> getSchedules() {
		if(schedules == null){
			schedules = new ArrayList<>();
		}
		return schedules;
	}

	public int getAdultNum() {
		return adultNum;
	}

	public void setAdultNum(int adultNum) {
		this.adultNum = adultNum;
	}

	public int getChildNum() {
		return childNum;
	}

	public void setChildNum(int childNum) {
		this.childNum = childNum;
	}

	public int getOlderNum() {
		return olderNum;
	}

	public void setOlderNum(int olderNum) {
		this.olderNum = olderNum;
	}

	public String getToPlaces() {
		return toPlaces;
	}

	public void setToPlaces(String toPlaces) {
		this.toPlaces = toPlaces;
	}

	public void setSchedules(List<RouteScheduleVO> schedules) {
		this.schedules = schedules;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getRouteMemo() {
		return routeMemo;
	}

	public void setRouteMemo(String routeMemo) {
		this.routeMemo = routeMemo;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}
}
