<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>


<nav class="navbar-default navbar-static-side" role="navigation">
	<div class="sidebar-collapse">
		<ul class="nav" id="side-menu">
            <li class="nav-header">
				<div class="dropdown profile-element"> 
					<span><img alt="image" src="<c:url value="/static/images/logobig.png"/>"/></span>
				</div>
				<div class="logo-element">
					<span><img alt="image" src="<c:url value="/static/images/logosmall.png"/>"/></span>
				</div>
			</li>
			<sec:authorize access="hasAnyRole('RECEPTION','ADMIN')">
            <li id="dashboard">
             	<a href="<c:url value="/secure/dashboard"/>">
             		<i class="fa fa-th-large"></i><span class="nav-label"><spring:message code="menu.dashboard" text="!"/></span>
             	</a>
			</li>
			</sec:authorize>
			
			<sec:authorize access="hasAnyRole('MARKETING','RECEPTION','ADMIN')">
			<li id="marketing">
             	<a href="<c:url value="/secure/marketing"/>">
             		<i class="fa fa-university"></i><span class="nav-label"><spring:message code="menu.marketing" text="!"/></span>
             	</a>
			</li>
			</sec:authorize>
			
			<sec:authorize access="hasAnyRole('DOCTOR','ADMIN')">
			<li id="doctor">
             	<a href="<c:url value="/secure/doctor"/>">
             		<i class="fa fa-user-md"></i><span class="nav-label"><spring:message code="menu.doctor" text="!"/></span>
             	</a>
			</li>
			</sec:authorize>
			
			
			<li id="customer">
				<a href="#">
					<i class="fa fa-users"></i><span class="nav-label"><spring:message code="menu.customer" text="!"/></span><span class="fa arrow"></span>
				</a>
				<ul class="nav nav-second-level">
					<li id="manager">
		             	<a href="<c:url value="/secure/customer"/>">
		             		<i class="fa fa-list-ul"></i><span class="nav-label"><spring:message code="menu.customer.manager" text="!"/></span>
		             	</a>
					</li>
					
					<sec:authorize access="hasAnyRole('MARKETING','RECEPTION','ADMIN')">
					<li id="schedule">
		             	<a href="<c:url value="/secure/schedule"/>">
		             		<i class="fa fa-calendar"></i><span class="nav-label"><spring:message code="menu.customer.schedule" text="!"/></span>
		             	</a>
					</li>
					<li id="reality">
		             	<a href="<c:url value="/secure/customer/reality"/>">
		             		<i class="fa fa-user"></i><span class="nav-label"><spring:message code="menu.customer.reality" text="!"/></span>
		             	</a>
					</li>
					</sec:authorize>
				</ul>
			</li>
			
			<sec:authorize access="hasAnyRole('MARKETING','RECEPTION','ADMIN')">
			<li id="sms">
				<a href="#">
					<i class="fa fa-mobile"></i><span class="nav-label"><spring:message code="menu.sms" text="!"/></span><span class="fa arrow"></span>
				</a>
				<ul class="nav nav-second-level">
					<li id="sms_send">
		             	<a href="<c:url value="/secure/sms"/>">
		             		<i class="fa fa-envelope"></i><span class="nav-label"><spring:message code="menu.sms.send" text="!"/></span>
		             	</a>
					</li>
					<li id="sms_statistic">
		             	<a href="<c:url value="/secure/sms/statistic"/>">
		             		<i class="fa fa-list-ul"></i><span class="nav-label"><spring:message code="menu.sms.statistic" text="!"/></span>
		             	</a>
					</li>
				</ul>
			</li>
			</sec:authorize>
			<%-- <li id="invoice">
				<a href="#">
					<i class="fa fa-files-o"></i><span class="nav-label"><spring:message code="menu.invoice" text="!"/></span><span class="fa arrow"></span>
				</a>
				<ul class="nav nav-second-level">
					<li id="invoiceManager">
		             	<a href="<c:url value="/secure/invoice"/>">
		             		<i class="fa fa-list-ul"></i><span class="nav-label"><spring:message code="menu.invoice.manager" text="!"/></span>
		             	</a>
					</li>
					<li id="bill">
		             	<a href="<c:url value="/secure/bill"/>">
		             		<i class="fa fa-calendar"></i><span class="nav-label"><spring:message code="menu.bill" text="!"/></span>
		             	</a>
					</li>
				</ul>
			</li> --%>
            <sec:authorize access="hasRole('ADMIN')">
			<li id="param">
				<a href="#">
					<i class="fa fa-cog"></i><span class="nav-label"><spring:message code="menu.setting" text="!"/></span><span class="fa arrow"></span>
				</a>
				<ul class="nav nav-second-level">
					<li id="param_branch">
						<a href="<c:url value="/secure/branches"/>">
	                     	<i class="fa fa-university"></i> <spring:message code="menu.setting.branch" text="!"/>
	                    </a>
					</li>
					<li id="param_dentist">
						<a href="<c:url value="/secure/dentist"/>">
	                     	<i class="fa fa-users"></i> <spring:message code="menu.setting.dentist" text="!"/>
                     	</a>
					</li>
					<li id="param_user">
						<a href="<c:url value="/secure/user"/>">
	                     	<i class="fa fa-user"></i> <spring:message code="menu.setting.user" text="!"/>
                     	</a>
					</li>
					<li id="param_param">
						<a href="<c:url value="/secure/params"/>">
	                     	<i class="fa fa-user"></i> <spring:message code="menu.setting.params" text="!"/>
                     	</a>
					</li>
				</ul>
			</li>
			</sec:authorize>
         </ul>
	</div>
</nav>