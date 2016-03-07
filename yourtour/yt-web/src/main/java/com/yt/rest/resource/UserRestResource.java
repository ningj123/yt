package com.yt.rest.resource;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import com.yt.business.bean.UserAccountBean;
import com.yt.business.bean.UserProfileBean;
import com.yt.business.common.AppException;
import com.yt.business.common.Constants;
import com.yt.business.repository.UserRepository;
import com.yt.core.utils.Base64Utils;
import com.yt.error.StaticErrorEnum;
import com.yt.response.ResponseDataVO;
import com.yt.response.ResponseVO;
import com.yt.utils.FileUtils;
import com.yt.utils.SessionUtils;
import com.yt.vo.member.LoginVO;
import com.yt.vo.member.RegisterVO;
import com.yt.vo.member.UserVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.List;

@Component
@Path("app/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestResource extends RestResource {
	private static final Log LOG = LogFactory.getLog(UserRestResource.class);

	@Autowired
	private UserRepository userRepository;

	/**
	 * 根据用户ID获取注册信息接口
	 * @param id
	 * @return
	 */
	@GET
	@Path("{id}")
	public ResponseDataVO<UserVO> getUser(@PathParam("id") Long id) throws Exception{
		UserProfileBean bean = (UserProfileBean) userRepository.get(UserProfileBean.class, id);
		if (bean == null) {
			LOG.warn(String.format("No UserProfileBean[id=%d] found.", id));
			return new ResponseDataVO<UserVO>(StaticErrorEnum.THE_DATA_NOT_EXIST);
		}

		UserVO vo = UserVO.transform(bean);
		return new ResponseDataVO<UserVO>(vo);
	}

	/**
	 * 用户登录注销接口
	 * @param id
	 * @return
	 */
	@GET
	@Path("logout/{id}")
	public ResponseVO logout(@PathParam("id") Long id)  throws Exception{
		UserProfileBean user = (UserProfileBean) userRepository.get(UserProfileBean.class, id, false);
		if (user == null) {
			LOG.warn(String.format("No UserProfileBean[id=%d] found.", id));
			return new ResponseVO(StaticErrorEnum.USER_NOT_EXIST);
		}

		// 清除当前session登录信息
		SessionUtils.clear();
		return new ResponseVO();
	}

	/**
	 * APP用户登录接口
	 * @param loginVO
	 * @return
	 */
	@POST
	@Path("/login")
	public ResponseDataVO<UserVO> login(LoginVO loginVO) throws Exception{
		try{
			UserProfileBean user = userRepository.getUser(loginVO.getMobile(), loginVO.getPassword());

			UserVO profile = UserVO.transform(user);
			return new ResponseDataVO<UserVO>(profile);
		} catch (AppException ex) {
			return new ResponseDataVO<UserVO>(StaticErrorEnum.AUTHENTICATE_FAIL);
		}
	}

	/**
	 * APP 用户账户注册接口
	 * @param registervo
	 * @return
	 */
	@POST
	@Path("/account/register")
	public ResponseDataVO<UserVO> registerUserAccount(RegisterVO registervo) throws Exception{
		UserProfileBean profile = userRepository.getUserByUserName(registervo.getMobile());
		if(profile != null){
			return new ResponseDataVO<UserVO>(StaticErrorEnum.USER_EXIST);
		}

		UserAccountBean account = new UserAccountBean();
		account.setUserName(registervo.getMobile());
		account.setPwd(Base64Utils.MD5(registervo.getPassword()));

		profile = new UserProfileBean();
		profile.setMobileNo(registervo.getMobile());
		account.setProfile(profile);
		this.userRepository.save(account, String.valueOf(account.getGraphId()));

		profile = userRepository.getUserByUserName(account.getUserName());
		return new ResponseDataVO<UserVO>(UserVO.transform(profile));
	}

	/**
	 * 用户信息注册保存接口
	 * @param profileId
	 * @param nickName
	 * @param gender
	 * @param tags
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/{id}/profile/save")
	public ResponseDataVO<UserVO> registerProfile(@FormDataParam("id") Long profileId,
												  @FormDataParam("nickName") String nickName,
												  @FormDataParam("gender") String gender,
												  @FormDataParam("tags") String tags,
												  FormDataMultiPart form) throws Exception{
		UserProfileBean profile = userRepository.getUserByNickName(nickName);
		if(profile != null){
			return new ResponseDataVO<UserVO>(StaticErrorEnum.NICKNAME_EXIST);
		}

		profile = (UserProfileBean)userRepository.get(UserProfileBean.class, profileId, false);
		if(profile == null){
			return new ResponseDataVO<UserVO>(StaticErrorEnum.USER_NOT_EXIST);
		}

		List<FormDataBodyPart> l= form.getFields("userLogo");
		if(l != null) {
			for (FormDataBodyPart p : l) {
				InputStream is = p.getValueAs(InputStream.class);
				FormDataContentDisposition detail = p.getFormDataContentDisposition();
				profile.setImageUrl(FileUtils.saveFile("images/user", FileUtils.getType(detail.getFileName()), is));
			}
		}

		profile.setNickName(nickName);
		profile.setGender(Constants.GenderType.getEnum(gender));
		profile.setTags(tags);
		this.userRepository.save(profile, false, String.valueOf(profileId));

		profile = (UserProfileBean) userRepository.get(UserProfileBean.class, profileId, false);
		return new ResponseDataVO<UserVO>(UserVO.transform(profile));
	}

	/**
	 * 用户信息修改保存接口
	 * @param id
	 * @param nickName
	 * @param birthday
	 * @param slogan
	 * @param residence
	 * @param gender
	 * @param nativePlace
	 * @param tags
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/{id}/save")
	public ResponseDataVO<UserVO> saveUserProfile(@PathParam("id") Long id,
												  @FormDataParam("nickName") String nickName,
												  @FormDataParam("birthday") Long birthday,
												  @FormDataParam("slogan") String slogan,
												  @FormDataParam("residence") String residence,
												  @FormDataParam("gender") String gender,
												  @FormDataParam("nativePlace") String nativePlace,
												  @FormDataParam("tags") String tags,
												  FormDataMultiPart form) throws Exception{
		UserProfileBean profile = (UserProfileBean) userRepository.get(UserProfileBean.class, id, false);
		if(profile == null){
			return new ResponseDataVO<UserVO>(StaticErrorEnum.USER_NOT_EXIST);
		}

		List<FormDataBodyPart> l= form.getFields("userLogo");
		if(l != null) {
			for (FormDataBodyPart p : l) {
				InputStream is = p.getValueAs(InputStream.class);
				FormDataContentDisposition detail = p.getFormDataContentDisposition();
				profile.setImageUrl(FileUtils.saveFile("images/user", FileUtils.getType(detail.getFileName()), is));
			}
		}

		if(nickName != null) profile.setNickName(nickName);
		if(birthday != null) profile.setBirthday(birthday);
		if(gender != null) profile.setGender(Constants.GenderType.getEnum(gender));
		if(slogan != null) profile.setSlogan(slogan);
		if(nativePlace != null) profile.setNativePlace(nativePlace);
		if(residence != null) profile.setResidence(residence);
		if(tags != null) profile.setTags(tags);

		this.userRepository.save(profile, false, String.valueOf(id));

		profile = (UserProfileBean) userRepository.get(UserProfileBean.class, id, false);
		return new ResponseDataVO<UserVO>(UserVO.transform(profile));
	}
}