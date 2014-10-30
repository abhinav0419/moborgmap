package com.mob.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.mob.googleoauth.GoogleAuthHelper;
import com.mob.model.OfficeAddress;
import com.mob.model.Title;
import com.mob.model.User;
import com.mob.service.OfficeAddressService;
import com.mob.service.TitleService;
import com.mob.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private TitleService titleService;
	@Autowired
	private OfficeAddressService officeAddressService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String userMainPage() {
		return "index";
	}

	@RequestMapping(value = "/oauthRedirect", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView user(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response1)
			throws IOException, JSONException {
		try {
			HttpSession session = request.getSession();
			// Oauth called
			// HttpSession session = request.getSession();
			GoogleAuthHelper helper = new GoogleAuthHelper();
			// get user data from google token
			List ls = helper.getUserInfoJson(request.getParameter("code"),
					request.getSession());
			// session.setAttribute("email", ls.get(0));
			// session.setAttribute("name", ls.get(1));
			// session.setAttribute("picture", ls.get(2));

			// request.getSession().setAttribute("userToken",
			// request.getParameter("code"));
			request.getSession().setAttribute("email", ls.get(0));
			request.getSession().setAttribute("name", ls.get(1));
			request.getSession().setAttribute("picture", ls.get(2));
			request.getSession().setAttribute("flag", "flag");
			String userEmail = (String) request.getSession().getAttribute(
					"email");
			User user = new User();
			user.setEmailId((String) ls.get(0));
			user.setFullName((String) ls.get(1));
			user.setUserPicture((String) ls.get(2));

			// check user is avail or not
			boolean isUserAvailable = userService.isUserAvailable(user);
			System.out.println(isUserAvailable);
			if (isUserAvailable == false) {

				List<Title> titleNameList = titleService.getTitleName();
				List<OfficeAddress> officeList = officeAddressService
						.getofficeName();
				List<User> managerList = userService.getAllResourceManager();
				System.out.println(titleNameList.get(0).getTitleName());
				model.put("titleNameList", titleNameList);
				model.put("officeList", officeList);
				model.put("managerList", managerList);

				model.put("userEntity", user);
				ModelAndView modelAndView = new ModelAndView("registration");
				return modelAndView;
			} else {
				User userEntity = userService.getUserByEmailId(userEmail);
				session.setAttribute("loggedUser", userEntity);
				session.setAttribute("loggedUserId", userEntity.getUserId());
				session.setAttribute("loggedUserName", userEntity.getFullName());
				model.put("userEntity", userService.loadLocation(user));
				ModelAndView modelAndView = new ModelAndView("mobmap");
				System.out.println("Redirecting to MobMap Page");
				return modelAndView;
			}
		} catch (NullPointerException np) {
			logoutUser(request.getSession(), response1);
		} catch (TokenResponseException e) {
			
			ModelAndView modelAndView = new ModelAndView("mobmap");
			return modelAndView;
		}
		ModelAndView modelAndView = new ModelAndView("index");
		return modelAndView;

	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String saveUser(@Valid @ModelAttribute("userEntity") User user,
			BindingResult bindingResult,
			@RequestParam("officeLocation") String officeLocation,
			@RequestParam("title") String title,
			@RequestParam("reportTo") String reportTo,
			@RequestParam("userPicture") String userPicture,
			Map<String, Object> model, HttpSession session,HttpServletRequest request,
			HttpServletResponse response) {

		OfficeAddress address = null;
		Title title1 = null;

		String emailId = user.getEmailId();
		System.out.println(officeLocation);
		User resourceManager = userService.getResourceManager(reportTo);

		// String emailId = user.getEmailId();
		User userVerified = userService.getUserByEmailId(emailId);

		if (userVerified == null) {
			address = userService.getOfficeAddressId(officeLocation);
			title1 = userService.getTitleId(title);
			user.setEmailId(emailId);
			// System.out.println(user.getEmailId() + "lol"
			// + (String) session.getAttribute("email"));
			user.setResourceManagerId(resourceManager.getUserId());
			user.setOfficeLocationId(address);
			user.setTitleId(title1);
			user.setActive(true);
			session.setAttribute("registered", user.getFullName());
			String register = "register";
			request.setAttribute("register", register);
			model.put("name", user.getFullName());
			user.setUserPicture(userPicture);

			userService.saveUser(user);
			User userEntity = userService.getUserByEmailId(emailId);
			session.setAttribute("loggedUser", userEntity);
			session.setAttribute("loggedUserId", userEntity.getUserId());
			return "mobmap";
		} else {
			System.out.println("error in officelocation");
			model.put("User already exists", userVerified);
			return "null";
		}

	}

	@RequestMapping(value = "/updateprofile", method = RequestMethod.POST)
	public String updateUserProfile(
			@ModelAttribute("userEntity") User user,
			@RequestParam("fullName") String fullName,
			@RequestParam("contactNo") String contactNo,
			@RequestParam("officeContactNo") String officeContactNo,
			@RequestParam("emailId") String emailId,
			@RequestParam("jobDescription") String jobDescription,
			@RequestParam("hobbies") String hobbies,
			@RequestParam("officeLocation") String officeLocation,
			@RequestParam("title") String title,
			@RequestParam("reportTo") String reportTo,
			Map<String, Object> model, HttpSession session,
			Map<String, String> map, HttpServletRequest request) {

		OfficeAddress address = null;
		Title title1 = null;
		
		System.out.println("00000000000000000000000");
		User resourceManager = userService.getResourceManager(reportTo);

		System.out.println(user.getFullName());
		//String emailId = user.getEmailId();

		User userVerified = userService.getUserByEmailId(emailId);

		if (userVerified != null) {
			address = userService.getOfficeAddressId(officeLocation);
			title1 = userService.getTitleId(title);
			userVerified.setEmailId(emailId);
			userVerified.setFullName(user.getFullName());
			userVerified.setContactNo(user.getContactNo());
			userVerified.setOfficeContactNo(user.getOfficeContactNo());
			userVerified.setResourceManagerId(resourceManager.getUserId());
			userVerified.setOfficeLocationId(address);
			userVerified.setTitleId(title1);
			userVerified.setJobDescription(user.getJobDescription());
			userVerified.setHobbies(user.getHobbies());
			userVerified.setJobDescription(jobDescription);
			userVerified.setHobbies(hobbies);
			System.out.println((String) session.getAttribute("picture"));
			userVerified.setUserPicture((String) session.getAttribute("picture"));
			
			userService.saveUser(userVerified);
			
			System.out.println("doing update");
			request.setAttribute("updated", userVerified.getFullName());
			System.out.println("success in updating");

			String updated = "updated";
			request.setAttribute("updated", updated);
			model.put("userEntity", userService.loadLocation(user));
			map.put("name", userVerified.getFullName());
			return "mobmap";
		} else {
			model.put("User not exists", userVerified);
			System.out.println("error in updating");
			return "mobmap";
		}

	}

	@RequestMapping(value = "/getuserprofile", method = RequestMethod.GET)
	public ModelAndView getUserProfileReturnCurrent(
			@RequestParam("userId") Long userId,
			@RequestParam("currentId") Long currentId,
			HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("userprofile");
		User user = userService.getUserById(userId);
		if (user.getUserPicture() == null) {
			user.setUserPicture("resource/img/mob.png");
		}
		request.setAttribute("singleNoBack", true);
		modelAndView.addObject("user", user);
		modelAndView.addObject("currentId", currentId);
		return modelAndView;
	}
	// get user profile by userId
	@RequestMapping(value = "/getupdateprofile", method = RequestMethod.GET)
	public ModelAndView getUserUpdateProfile(
			@RequestParam("userId") Long userId, Map<String, Object> model,
			@ModelAttribute("userEntity") User user123) {

		ModelAndView modelAndView = new ModelAndView("updateprofile");
		User user = userService.getUserById(userId);
		modelAndView.addObject("user", user);
		Title currentTitle = userService.getCurrentTitle(user.getUserId());
		User resourceManager = userService.getManager(user
				.getResourceManagerId());
		OfficeAddress officeName = officeAddressService.getofficeAddress(user
				.getUserId());

		List<Title> titleNameList = titleService.getTitleName();
		List<OfficeAddress> officeList = officeAddressService.getofficeName();
		List<User> managerList = userService.getAllResourceManager();
		model.put("titleNameList", titleNameList);
		model.put("officeList", officeList);
		model.put("managerList", managerList);
		model.put("currentTitle", currentTitle);
		model.put("resourceManager", resourceManager);
		model.put("officeName", officeName);
		return modelAndView;
	}

	@RequestMapping(value = "/getuserpro", method = RequestMethod.GET)
	public ModelAndView getUserProfile(@RequestParam("userId") Long userId,
			HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView("userprofile");
		User user = userService.getUserById(userId);

		if (user.getUserPicture() == null) {
			user.setUserPicture("resource/img/mob.png");
		}
		request.setAttribute("singleNoBack", true);
		modelAndView.addObject("user", user);

		return modelAndView;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutUser(HttpSession session, HttpServletResponse response1)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"https://accounts.google.com/o/oauth2/revoke?token="
						+ session.getAttribute("userToken"));
		org.apache.http.HttpResponse response = client.execute(post);
		session.invalidate();
		return "index";
	}

}
