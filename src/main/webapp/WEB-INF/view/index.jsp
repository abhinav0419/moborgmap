<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.mob.googleoauth.GoogleAuthHelper"%>
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="img/favicon.ico"
	type="image/vnd.microsoft.icon" />
<title>Mobiquity | Login</title>
<link rel="icon" href="resource/img/mob-logo-small.png"
	type="image/x-icon">
<!-- Bootstrap Core CSS -->
<link href="resource/css/bootstrap.min.css" rel="stylesheet">

<!-- MetisMenu CSS -->
<link href="resource/css/plugins/metisMenu/metisMenu.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="resource/css/sb-admin-2.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="resource/font-awesome-4.1.0/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

	<div class="container">
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="login-panel panel panel-default">
					<div class="panel-heading" style="height: 50px">
						<span><img src="resource/img/mob-logo-small.png"
							style="float: left; margin-right: 5px;"></span>
						<h3 class="panel-title" style="float: left;">Please Sign In</h3>
					</div>
					<div class="panel-body">
						<div class="oauthDemo col-xs-offset-3" id="connect">
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
									out.println("<a class='btn btn-danger' href='"
											+ helper.buildLoginUrl() + " '>Connect with Google+</a>");

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
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="navbar navbar-default navbar-fixed-bottom">
		<div class="container">
			<span class="navbar-text"> Copyright © Mobiquity, Inc. 2014.
				All Rights Reserved. </span>
		</div>
	</div>

	<!-- jQuery Version 1.11.0 -->
	<script src="resource/js/jquery-1.11.0.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="resource/js/bootstrap.min.js"></script>

	<!-- Metis Menu Plugin JavaScript -->
	<script src="resource/js/plugins/metisMenu/metisMenu.min.js"></script>

	<!-- Custom Theme JavaScript -->
	<script src="resource/js/sb-admin-2.js"></script>

</body>

</html>
