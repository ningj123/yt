package com.yt.rest.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yt.business.bean.UserBean;
import com.yt.business.repository.UserRepository;
import com.yt.business.utils.AdminUserInitializeService;
import com.yt.business.utils.Neo4jUtils;
import com.yt.error.StaticErrorEnum;
import com.yt.response.ResponseDataVO;
import com.yt.response.ResponsePagingDataVO;
import com.yt.response.ResponseVO;
import com.yt.utils.WebUtils;
import com.yt.vo.member.AuthenticationVO;
import com.yt.vo.member.UserVO;

@Component
@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestResource {
	private static final Log LOG = LogFactory.getLog(UserRestResource.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdminUserInitializeService userService;

	@SuppressWarnings("unchecked")
	@GET
	public ResponseDataVO<List<UserVO>> getAllUsers() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Request fetch all teh UserBean.");
		}
		List<UserVO> list = new ArrayList<UserVO>();
		try {
			List<UserBean> result = (List<UserBean>) userRepository
					.get(UserBean.class);
			for (UserBean bean : result) {
				if (bean == null) {
					continue;
				}
				UserVO vo = UserVO.transform(bean);
				list.add(vo);
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format(
						"Fetch all the UserBean success, total: %d.",
						list.size()));
			}
			return new ResponseDataVO<List<UserVO>>(list);
		} catch (Exception ex) {
			if (LOG.isErrorEnabled()) {
				LOG.error("Fetch all the UserBean fail.", ex);
			}
			return new ResponseDataVO<List<UserVO>>(
					StaticErrorEnum.FETCH_DB_DATA_FAIL);
		}
	}

	@GET
	@Path("{id}")
	public ResponseDataVO<UserVO> getUser(@PathParam("id") String id) {
		long graphId = Neo4jUtils.getGraphIDFromString(id);
		try {
			UserBean bean = null;
			if (graphId != -1) {
				// id是GraphID
				bean = (UserBean) userRepository.get(UserBean.class, graphId);
			} else {
				// id 是rowkey
				bean = (UserBean) userRepository.get(UserBean.class, "rowKey",
						id);
			}
			if (bean == null) {
				return new ResponseDataVO<UserVO>(
						StaticErrorEnum.THE_DATA_NOT_EXIST);
			}
			UserVO vo = UserVO.transform(bean);
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Get UserBean['%s'] success.", id));
			}
			return new ResponseDataVO<UserVO>(vo);
		} catch (Exception ex) {
			if (LOG.isErrorEnabled()) {
				LOG.error(String.format("Fetch UserBean[id='%s'] fail.", id),
						ex);
			}
			return new ResponseDataVO<UserVO>(
					StaticErrorEnum.FETCH_DB_DATA_FAIL);
		}
	}

	@POST
	@Path("import")
	public ResponseVO importData(List<UserVO> vos) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Request import UserBean data.");
		}
		for (UserVO vo : vos) {
			// 默认全部采用admin账户导入数据
			ResponseVO response = save(null, vo, "admin");
			if (response.getErrorCode() != 0) {
				if (LOG.isWarnEnabled()) {
					LOG.warn(String.format(
							"Import UserBean data fail, error: id = '%s'.",
							vo.getRowKey()));
				}
				return response;
			}
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Import UserBean data success, total: %d.",
					vos.size()));
		}
		return new ResponseVO();
	}

	@POST
	@Path("save.json")
	public ResponseVO saveByAdd(UserVO vo, @Context HttpServletRequest request) {
		return save(null, vo, WebUtils.getCurrentLoginUser(request));
	}

	@POST
	@Path("save/{id}.json")
	public ResponseVO saveByUpdate(@PathParam("id") String id, UserVO vo,
			@Context HttpServletRequest request) {
		return save(id, vo, WebUtils.getCurrentLoginUser(request));
	}

	private ResponseVO save(String id, UserVO vo, String operator) {
		if (vo == null) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("The UserVO is null.");
			}
			return new ResponseVO(StaticErrorEnum.THE_INPUT_IS_NULL);
		}
		try {
			UserBean bean = UserVO.transform(vo);
			if (id == null) {
				// 新增
				bean.setGraphId(null);
			} else {
				// 修改
				long graphId = Neo4jUtils.getGraphIDFromString(id);
				if (graphId != -1l) {
					// ID是Graph ID
					bean.setGraphId(graphId);
				} else {
					// ID是Row Key
					bean.setRowKey(id);
				}
			}
			userRepository.save(bean, operator);
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Save UserBean['%s'] success.",
						vo.getRowKey()));
			}
			return new ResponseVO();
		} catch (Exception ex) {
			if (LOG.isErrorEnabled()) {
				LOG.error(
						String.format("Save the UserBean[id='%s'] fail.",
								vo.getId()), ex);
			}
			return new ResponseVO(StaticErrorEnum.DB_OPERATE_FAIL);
		}
	}

	@GET
	@Path("remove/{id}.json")
	public ResponseVO delete(@PathParam("id") String id) {
		long graphId = Neo4jUtils.getGraphIDFromString(id);
		try {
			UserBean bean = null;
			if (graphId != -1) {
				// id是GraphID
				bean = (UserBean) userRepository.get(UserBean.class, graphId);
			} else {
				// id 是rowkey
				bean = (UserBean) userRepository.get(UserBean.class, "rowKey",
						id);
			}
			id = bean.getRowKey();
			userRepository.delete(bean);
			if (LOG.isDebugEnabled()) {
				LOG.debug(String
						.format("Delete UserBean[id='%s'] success.", id));
			}
			return new ResponseVO();
		} catch (Exception ex) {
			if (LOG.isErrorEnabled()) {
				LOG.error(String.format("Fetch UserBean[id='%s'] fail.", id),
						ex);
			}
			return new ResponseVO(StaticErrorEnum.FETCH_DB_DATA_FAIL);
		}
	}

	@POST
	@Path("login")
	public ResponseVO login(AuthenticationVO auth,
			@Context HttpServletRequest request) {
		if (auth == null) {
			return new ResponseVO(StaticErrorEnum.THE_INPUT_IS_NULL);
		}
		try {
			UserBean user = (UserBean) userRepository.get(UserBean.class,
					"code", auth.getCode());
			if (user == null) {
				if (LOG.isWarnEnabled()) {
					LOG.warn(String.format(
							"The user[%s] not exist, login fail.",
							auth.getCode()));
				}
				return new ResponseVO(StaticErrorEnum.USER_NOT_EXIST);
			}
			if (userService.checkPassword(auth.getPassword(), user.getPwd())) {
				// 认证成功
				if (LOG.isDebugEnabled()) {
					LOG.debug(String.format("The user[%s] login success.",
							auth.getCode()));
				}
				WebUtils.setCurrentLoginUser(request, auth.getCode());
				return new ResponseVO();
			}
			return new ResponseVO(StaticErrorEnum.AUTHENTICATE_FAIL);
		} catch (Exception ex) {
			if (LOG.isWarnEnabled()) {
				LOG.warn(
						String.format("The user[%s] login fail.",
								auth.getCode()), ex);
			}
			return new ResponseVO(StaticErrorEnum.AUTHENTICATE_FAIL);
		}
	}

	@GET
	@Path("logout/{username}")
	public ResponseVO logout(@PathParam("username") String username,
			@Context HttpServletRequest request) {
		if (username == null) {
			return new ResponseVO(StaticErrorEnum.THE_INPUT_IS_NULL);
		}
		try {
			UserBean user = (UserBean) userRepository.get(UserBean.class,
					"code", username);
			if (user == null) {
				if (LOG.isWarnEnabled()) {
					LOG.warn(String.format(
							"The user[%s] not exist, logout fail.", username));
				}
				return new ResponseVO(StaticErrorEnum.USER_NOT_EXIST);
			}
			// 清除当前session登录信息
			WebUtils.setCurrentLoginUser(request, null);
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("The user[%s] logout success.",
						username));
			}
			return new ResponseVO();
		} catch (Exception ex) {
			if (LOG.isWarnEnabled()) {
				LOG.warn(String.format("The user[%s] logout fail.", username),
						ex);
			}
			return new ResponseVO(StaticErrorEnum.AUTHENTICATE_FAIL);
		}
	}

	@SuppressWarnings("unchecked")
	@Path("loadPage.json")
	@GET
	public ResponsePagingDataVO<List<UserVO>> loadPage(
			@QueryParam("page") int page, @QueryParam("start") int start,
			@QueryParam("limit") int limit) {
		try {
			long totalSize = userRepository.count(UserBean.class);
			if (start >= totalSize) {
				if (LOG.isWarnEnabled()) {
					LOG.warn(String
							.format("The query parameter is invalid, the total user: %d, but request: page(%d), start(%d), limit(%d).",
									totalSize, page, start, limit));
				}
				return new ResponsePagingDataVO<List<UserVO>>(
						StaticErrorEnum.THE_DATA_NOT_EXIST);
			}

			Vector<UserVO> result = new Vector<UserVO>();
			List<UserBean> users = (List<UserBean>) userRepository.getByPage(
					UserBean.class, start, limit);
			for (UserBean user : users) {
				UserVO vo = UserVO.transform(user);
				if (vo == null) {
					continue;
				}
				result.add(vo);
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug(String
						.format("Query a page user success, total: {%d}, request: page(%d), start(%d), limit(%d).",
								result.size(), page, start, limit));
			}
			return new ResponsePagingDataVO<List<UserVO>>(totalSize, result);
		} catch (Exception ex) {
			if (LOG.isErrorEnabled()) {
				LOG.error(
						String.format(
								"Query a page user fail, request: page(%d), start(%d), limit(%d).",
								page, start, limit), ex);
			}
			return new ResponsePagingDataVO<List<UserVO>>(
					StaticErrorEnum.FETCH_DB_DATA_FAIL);
		}
	}
}
