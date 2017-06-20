<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<style type="text/css">
	.datepicker { 
		z-index: 9999 !important;
	}
	
	.chosen-container-single .chosen-single {
         background: #fff;
         border-radius: 3px;
         color: #444;
         display: block;
         height: 23px;
         line-height: 24px;
         overflow: hidden;
         padding: 0 0 0 8px;
         position: relative;
         text-decoration: none;
         white-space: nowrap;
         box-shadow: none;
         border: 1px solid #e5e6e7;
     }
     .chosen-container {
        width: 100% !important;
    }
</style>

<script type="text/javascript">
	document.getElementById("doctor").className = "active";
	
	$(function(){
	    $('.input-group.date').datepicker({
			todayBtn: "linked",
			todayHighlight: true,
            calendarWeeks: false,
            autoclose: true,
			format: "dd/mm/yyyy"
	    });
	    
	    $('.chosen-select').chosen({
	    	"disable_search": true
	    });
	});
</script>

<div id="body">
	
	<form:form name="form" id="form" method="post" commandName="doctorFilter">
		<input name="action" type="hidden">
		
		<div class="form-group">
			<div class="row">
				<div class="col-sm-4">
					<label><spring:message code="customer.id" text="!"/></label>
					<form:input path="serial" class="form-control textfield"/>
				</div>
				<div class="col-sm-4">
					<label><spring:message code="customer.name" text="!"/></label>
					<form:input path="fullName" class="form-control textfield"/>
				</div>
			</div>
		</div>
		
		<div class="form-group">
			<div class="row">
				<div class="col-sm-4">
					<a href='<c:url value="/secure/doctor"></c:url>' class="btn btn-w-m btn-success text-uppercase">
						<i class="fa fa-refresh"></i> <spring:message code="button.refresh" text="!"/>
					</a>
				</div>
				<div class="col-sm-8 text-right">
					<a onclick="javascript:doSubmit('RESET');" class="btn btn-w-m btn-default text-uppercase">
						<i class="fa fa-undo"></i> <spring:message code="button.reset" text="!"/>
					</a>
					<a onclick="javascript:doSubmit('GO');" class="btn btn-w-m btn-success text-uppercase">
						<i class="fa fa-search"></i> <spring:message code="button.search" text="!"/>
					</a>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-sm-12">
				<table class="table table-striped table-bordered table-hover table-lta" style="width:100%;">
					<thead>
						<tr>
							<th class="text-center">#</th>
							<th><spring:message code="schedule.serial" text="!"/></th>
							<th><spring:message code="schedule.name" text="!"/></th>
							<th><spring:message code="schedule.date" text="!Action"/></th>
							<th><spring:message code="schedule.content" text="!Action"/></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${records}" var="elm" varStatus="stt">
							<c:url value="/secure/records"  var="linkCustomer">
								<c:param name="id" value="${elm.customerId}"/>
							</c:url>
							
							<tr>
								<td class="text-center">${row + stt.index + 1}</td>
								<td><a href="${linkCustomer }">${elm.serial}</a></td>
								<td><a href="${linkCustomer }">${elm.fullName}</a></td>
								<td><fmt:formatDate pattern="dd/MM/yyyy" value="${elm.dateExcute}" /></td>
								<td>${elm.content}</td>
							</tr>
						</c:forEach>	
					</tbody>
				</table>
			</div>
		</div>
	</form:form>
</div>