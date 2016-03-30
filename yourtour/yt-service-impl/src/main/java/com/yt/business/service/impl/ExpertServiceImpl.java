package com.yt.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yt.business.bean.ExpertApplicationBean;
import com.yt.business.bean.ExpertServiceBean;
import com.yt.business.bean.RouteMainBean;
import com.yt.business.bean.UserProfileBean;
import com.yt.business.repository.neo4j.ExpertBeanRepository;
import com.yt.business.repository.neo4j.ExpertTuple;
import com.yt.business.repository.neo4j.RouteMainBeanRepository;
import com.yt.business.repository.neo4j.RouteTuple;
import com.yt.business.service.IExpertService;
import com.yt.core.utils.CollectionUtils;
import com.yt.core.utils.StringUtils;
import com.yt.neo4j.repository.CrudOperate;

@Service
public class ExpertServiceImpl extends ServiceBase implements IExpertService {
	private static final Log LOG = LogFactory.getLog(ExpertServiceImpl.class);

	@Autowired
	private CrudOperate<ExpertApplicationBean> applicationCrudOperate;

	@Autowired
	private CrudOperate<ExpertServiceBean> serviceCrudOperate;
	
	@Autowired
	private CrudOperate<UserProfileBean> profileCrudOperate;

	@Autowired
	private ExpertBeanRepository expertBeanRepository;

	@Autowired
	private RouteMainBeanRepository routeRepository;

	public ExpertServiceImpl() {
		super();
	}

	@Override
	public List<UserProfileBean> getExperts(String placeIds, String pServices) throws Exception {
		Long[] places = null, services = null;
		List<ExpertTuple> tuples = null;

		if(StringUtils.isNotNull(placeIds)){
			String[] ids = placeIds.split(",");
			places = new Long[ids.length];
			for(int index = 0; index < ids.length; index++){
				places[index] = Long.valueOf(ids[index]);
			}
		}

		if(StringUtils.isNull(pServices)){
			tuples = expertBeanRepository.getExperts(places);
		}else{
			String[] ids = pServices.split(",");
			services = new Long[ids.length];
			for(int index = 0; index < ids.length; index++){
				services[index] = Long.valueOf(ids[index]);
			}

			tuples = expertBeanRepository.getExperts(places,services);
		}

		List<UserProfileBean> experts = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(tuples)){
			for(ExpertTuple tuple : tuples){
				UserProfileBean expert = tuple.getProfile();

				experts.add(expert);
			}
		}

		return experts;
	}

	@Override
	public List<RouteMainBean> getRecommendRoutes(Long uid, Long nextCursor, int limit) throws Exception {
		List<RouteMainBean> routes = new ArrayList<>();

		List<RouteTuple> tuples = routeRepository.getRecommendRoutes(uid, nextCursor, limit);
		if(CollectionUtils.isNotEmpty(tuples)){
			for(RouteTuple tuple : tuples){
				routes.add(tuple.getRoute());
			}
		}

		return routes;
	}

	@Override
	public List<RouteMainBean> getServicedRoutes(Long expertId, Long nextCursor, int limit) throws Exception {
		List<RouteMainBean> routes = new ArrayList<>();

		List<RouteMainBeanRepository.OwnerRouteTuple> tuples = this.routeRepository.getRoutes(expertId, nextCursor, limit, "EXPERT");
		if(CollectionUtils.isNotEmpty(tuples)){
			for(RouteMainBeanRepository.OwnerRouteTuple tuple : tuples){
				routes.add(tuple.getRoute());
			}
		}

		return routes;
	}

	@Override
	public void saveApplication(ExpertApplicationBean application, Long uid) throws Exception {
		this.applicationCrudOperate.save(application);
	}

	@Override
	public ExpertApplicationBean getApplication(Long userId) throws Exception {
		return expertBeanRepository.getApplication(userId);
	}

	@Override
	public UserProfileBean getExpert(Long userId) throws Exception {
		UserProfileBean expert = this.profileCrudOperate.get(userId);

		return expert;
	}


}
