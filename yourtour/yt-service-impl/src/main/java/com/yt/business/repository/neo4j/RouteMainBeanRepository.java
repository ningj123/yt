package com.yt.business.repository.neo4j;

import com.yt.business.bean.*;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface RouteMainBeanRepository extends GraphRepository<RouteMainBean> {
	/**
	 * 判断指定的用户ID是否是指定行程的成员
	 * 
	 * @param routeId
	 *            行程ID
	 * @param userId
	 *            用户ID
	 * @return 如果返回值大于0,则表示是其成员，否则不是。
	 */
	@Query("START route=node({0}), user=node({1}) MATCH route<-[:MEMBER]-user "
			+ "WITH COUNT(user) AS number RETURN number")
	public int isRouteMember(Long routeId, Long userId);

	@QueryResult
	public class OwnerRouteTuple {
		@ResultColumn("r.imageUrl")
		public String imageUrl;
		@ResultColumn("r.impression")
		public String impression;

		@ResultColumn("r.permission")
		public String permission; // R:只读 W:修改

		@ResultColumn("r.role")
		// 参照 Constants.GroupRole枚举定义
		private String role;

		@ResultColumn("route")
		public RouteMainBean route;

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getImpression() {
			return impression;
		}

		public void setImpression(String impression) {
			this.impression = impression;
		}

		public String getPermission() {
			return permission;
		}

		public void setPermission(String permission) {
			this.permission = permission;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public RouteMainBean getRoute() {
			return route;
		}

		public void setRoute(RouteMainBean route) {
			this.route = route;
		}
	}

	/**
	 * 获取行程相关目的地
	 * @param routeId
	 * @return
	 */
	@Query("START route=node({0}) MATCH route-[r:TO]->(place:PlaceBean) RETURN place")
	public List<PlaceBean> getPlaces(Long routeId);

	/**
	 * 获取一个行程的日程安排
	 * @param routeId
	 * @return
	 */
	@Query("START route=node({0}) MATCH route-[r:HAS]->(schedule:RouteScheduleBean) RETURN schedule")
	public List<RouteScheduleBean> getSchedules(Long routeId);

	/**
	 * 获取日程下的活动安排
	 * @param dayId
	 * @return
	 */
	@Query("MATCH (schedule:RouteScheduleBean) WHERE schedule.parentId={0} RETURN schedule")
	public List<RouteScheduleBean> getScheduleActivities(Long dayId);

	/**
	 * 获取和指定用户相关的行程。
	 *
	 * @param userId
	 * @return
	 */
	@Query("START user=node({0}) MATCH user-[r]-(route:RouteMainBean) where route.isDeleted=false and Type(r)={1} RETURN count(route)")
	public int getRouteNum4User(Long userId, String relationship);

	/**
	 * 获取和指定用户相关的行程。
	 *
	 * @param userId
	 * @return
	 */
	@Query("START user=node({0}) MATCH user-[r]-(route:RouteMainBean) where route.isDeleted=false and Type(r)={3} RETURN route")
	public List<RouteMainBean> getRoutes(Long userId, Long startIndex, int limit, String relationship);

	/**
	 *
	 * @param startIndex
	 * @param limit
	 * @param relationship
	 * @return
	 */
	@Query("MATCH (user:UserProfileBean)-[r]-(route:RouteMainBean) where route.isDeleted=false and Type(r)={2} RETURN route, user")
	public List<RouteTuple> getRoutes(Long startIndex, int limit, String relationship);

	/**
	 * 获取所有行程中点评分的前n名
	 * 
	 * @param n
	 *            前n名
	 * @return 符合条件的行程
	 */
	@Query("MATCH (route:RouteMainBean) RETURN route ORDER BY route.commentScore DESC LIMIT {0}")
	public List<RouteMainBean> getRecommendRoutes(int n);

	/**
	 * 获取目的地行程
	 * 
	 * @param placeId
	 * @param nextCursor
	 * @param limit
	 * @return
	 */
	@Query("START user=node({0}) MATCH user-[r:RECOMMEND]->(route:RouteMainBean) where route.isDelete=false RETURN route, user")
	public List<RouteTuple> getRecommendRoutes4Place(Long placeId, Long nextCursor, int limit);

	/**
	 * 获取目的地行程
	 * 
	 * @param placeId
	 * @param nextCursor
	 * @param limit
	 * @return
	 */
	@Query("START user=node({0}) MATCH user-[r:RECOMMEND]->(route:RouteMainBean) RETURN route, user")
	public List<RouteTuple> getRecommendRoutes(Long[] placeId, int duration, Long nextCursor, int limit);

	/**
	 * 根据指定的行程和成员角色，返回该行程中的用户。
	 * 
	 * @param routeId
	 * @return
	 */
	@Query("START route=node({0}) MATCH route -[r:MEMBER]- (user:UserProfileBean) RETURN r.role, user")
	public List<RouteMemberBean> getRouteMember(Long routeId);

	@Query("START route=node({0}) MATCH route -[r:MEMBER]- (user:UserProfileBean) RETURN user")
	public List<UserProfileBean> getRouteMemberUserProfiles(Long routeId);

	/**
	 * 
	 * @param routeId
	 * @param userId
	 * @return
	 */
	@Query("START route=node({0}), user=node({1}) MATCH route<-[:BELONG]-(charge:RouteChargeBean)-[:BELONG]->user RETURN charge")
	public List<RouteChargeBean> getCharges(Long routeId, Long userId);

	/**
	 * 
	 * @param chargeId
	 * @return
	 */
	@Query("START root=node({0}) MATCH root-[:DIVIDED]->(charge:RouteChargeBean)-[:BELONG]->(owner:UserProfileBean) RETURN charge, owner")
	public List<ChargeTuple> getChargeDivisions(Long chargeId);

	/**
	 * 获取和行程相关的资源
	 * @param scheduleId
	 * @return
	 */
	@Query("START schedule=node({0}) MATCH schedule-[:RELATED]->(resource:ResourceBean) RETURN resource")
	public ResourceBean getResource(Long scheduleId);
}
