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
	document.getElementById("sms").className = "active";
	document.getElementById("sms_statistic").className = "active";

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
	
	<form:form name="form" id="form" method="post" commandName="smsStatisticFilter">
		<input name="action" type="hidden">
		
		<div class="form-group">
			<div class="row">
				<div class="col-sm-4">
					<label><spring:message code="sms.created.date" text="!"/></label>
					<div class="input-group date">
                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                        <form:input path="dateFrom" type="text" cssClass="form-control textfield" placeholder="dd/MM/yyyy"/>
					</div>
				</div>
				<div class="col-sm-4">
					<label>&nbsp;</label>
					<div class="input-group date">
                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                        <form:input path="dateTo" type="text" cssClass="form-control textfield" placeholder="dd/MM/yyyy"/>
					</div>
				</div>
			</div>
		</div>
		
		<div class="form-group">
			<div class="row">
				<div class="col-sm-12 text-right">
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
							<th><spring:message code="sms.type" text="!"/></th>
							<th class="text-right"><spring:message code="sms.unit" text="!Action"/></th>
							<th class="text-right"><spring:message code="sms.count" text="!"/></th>
							<th class="text-right"><spring:message code="sms.gross" text="!"/></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${smsStatistic}" var="elm" varStatus="stt">
							<tr>
								<td><spring:message code="sms.type.${elm.type}" text="!"/></td>
								<td class="text-right">${elm.unit}</td>
								<td class="text-right">${elm.count}</td>
								<td class="text-right"><fmt:formatNumber pattern="#,##0" value="${elm.gross}"/></td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<th class="text-center text-bold" colspan="2"><spring:message code="customer.total" text="!"/></th>
							<th class="text-right">${totalSms.count }</th>
							<th class="text-right"><fmt:formatNumber pattern="#,##0" value="${totalSms.gross}"/></th>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</form:form>
</div>