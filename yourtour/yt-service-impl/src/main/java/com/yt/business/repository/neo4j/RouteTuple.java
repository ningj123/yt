package com.yt.business.repository.neo4j;

import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

import com.yt.business.bean.RouteMainBean;
import com.yt.business.bean.UserProfileBean;

@QueryResult
public class RouteTuple {
	@ResultColumn("route")
	private RouteMainBean route;

	@ResultColumn("user")
	private UserProfileBean user;

	public RouteTuple(){
	}

	public RouteMainBean getRoute() {
		route.setUser(this.getUser());
		return route;
	}

	public void setRoute(RouteMainBean route) {
		this.route = route;
	}

	public UserProfileBean getUser() {
		return user;
	}

	public void setUser(UserProfileBean user) {
		this.user = user;
	}
}