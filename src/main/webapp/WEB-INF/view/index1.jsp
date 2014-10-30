<%@page import="com.mob.googleoauth.GoogleAuthHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Mobiquity | Login</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="icon" href="resource/img/mob-logo-small.png"
	type="image/x-icon">
	<link href="resource/css/bootstrap.min.css" rel="stylesheet">
<style>
body {
	font-family: Sans-Serif;
	margin: 1em;
}
#connect {
    text-align: center;
    padding-top: 15px
}
#connect p {
    color: #AAA;
    margin-top: 20px
}
#res {
    display: none
}
.panel {
    width: 500px;
    margin: 10px auto;
}
#info, .activity-author-info {
    position: relative;
    left: 10px;
    width: calc(100% - 45px);
}

#info p, .activity-author-info p {
    font-size: 0.9em
}


/* spinner by @tobiasahlin: http://tobiasahlin.com/spinkit/ */
.spinner {
  width: 40px;
  height: 40px;

  position: relative;
  margin: 20px auto;
}

.double-bounce1, .double-bounce2 {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: #333;
  opacity: 0.6;
  position: absolute;
  top: 0;
  left: 0;
  
  -webkit-animation: bounce 2.0s infinite ease-in-out;
  animation: bounce 2.0s infinite ease-in-out;
}

.double-bounce2 {
  -webkit-animation-delay: -1.0s;
  animation-delay: -1.0s;
}

@-webkit-keyframes bounce {
  0%, 100% { -webkit-transform: scale(0.0) }
  50% { -webkit-transform: scale(1.0) }
}

@keyframes bounce {
  0%, 100% { transform: scale(0.0) }
  50% { transform: scale(1.0) }
}
/* 
.oauthDemo a {
	display: block;
	border-style: solid;
	border-color: #bbb #888 #666 #aaa;
	border-width: 1px 2px 2px 1px;
	background: #ccc;
	color: #333;
	line-height: 2;
	text-align: center;
	text-decoration: none;
	font-weight: 900;
	width: 13em;
} */

.oauthDemo pre {
	background: #ccc;
}
/* 
.oauthDemo a:active {
	border-color: #666 #aaa #bbb #888;
	border-width: 2px 1px 1px 2px;
	color: #000;
}
 */
.readme {
	padding: .5em;
	background-color: #F9AD81;
	color: #333;
}
</style>
</head>
<body>
	<div class="oauthDemo" id="connect">
		<%
			/*
			 * The GoogleAuthHelper handles all the heavy lifting, and contains all "secrets"
			 * required for constructing a google login url.
			 */
			final GoogleAuthHelper helper = new GoogleAuthHelper();

			if (request.getParameter("code") == null
					|| request.getParameter("state") == null) {

				/*
				 * initial visit to the page
				 */
				out.println("<a class='btn btn-primary' href='" + helper.buildLoginUrl()
						+ "'>Connect with Google+</a>");

				/*
				 * set the secure state token in session to be able to track what we sent to google
				 */
				session.setAttribute("state", helper.getStateToken());

			} else if (request.getParameter("code") != null
					&& request.getParameter("state") != null
					&& request.getParameter("state").equals(
							session.getAttribute("state"))) {

				session.removeAttribute("state");

				out.println("<pre>");
				/*
				 * Executes after google redirects to the callback url.
				 * Please note that the state request parameter is for convenience to differentiate
				 * between authentication methods (ex. facebook oauth, google oauth, twitter, in-house).
				 * 
				 * GoogleAuthHelper()#getUserInfoJson(String) method returns a String containing
				 * the json representation of the authenticated user's information. 
				 * At this point you should parse and persist the info.
				 */

			}
		%>
	</div>
	<br />
</body>
</html>
