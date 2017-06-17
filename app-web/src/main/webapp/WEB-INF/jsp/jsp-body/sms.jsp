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
	document.getElementById("sms_send").className = "active";

	var pageNo = ${smsFilter.page};
	
	$(function(){
	    $.ajax({
	    	url : "<spring:url value='/secure/sms/loadpage'/>",
	        data: { pageNo: pageNo },
	        cache : false,
	        success: function (response) {
	            $("#_results").html(response);
	        }
	    });

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
	
	<form:form name="form" id="form" method="post" commandName="smsFilter">
		<input name="action" type="hidden">
		
		<div class="form-group">
			<div class="row">
				<div class="col-sm-4">
					<label><spring:message code="customer.telephone" text="!"/></label>
					<form:input path="phone" class="form-control textfield"/>
				</div>
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
					<label><spring:message code="sms.type" text="!"/></label>
					<form:select path="type" class="chosen-select" cssStyle="width:100%">
						<option value="" <c:if test="${'' == smsFilter.type}">selected="selected"</c:if>>
							<spring:message code="commom.all" text="!"/>
						</option>
						<option value="1" <c:if test="${'W' == smsFilter.type}">selected="selected"</c:if>>
							<spring:message code="sms.type.1" text="!"/>
						</option>
						<option value="2" <c:if test="${'F' == smsFilter.type}">selected="selected"</c:if>>
							<spring:message code="sms.type.2" text="!"/>
						</option>
					</form:select>
				</div>
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
				<div class="col-sm-4">
					<label><spring:message code="sms.status" text="!"/></label>
					<form:select path="status" class="chosen-select" cssStyle="width:100%">
						<option value="" <c:if test="${'' == smsFilter.status}">selected="selected"</c:if>>
							<spring:message code="commom.all" text="!"/>
						</option>
						<option value="W" <c:if test="${'W' == smsFilter.status}">selected="selected"</c:if>>
							<spring:message code="sms.status.W" text="!"/>
						</option>
						<option value="F" <c:if test="${'F' == smsFilter.status}">selected="selected"</c:if>>
							<spring:message code="sms.status.F" text="!"/>
						</option>
					</form:select>
				</div>
			</div>
		</div>
		
		<div class="form-group">
			<div class="row">
				<div class="col-sm-4">
					<button type="button" class="btn btn-w-m btn-success text-uppercase" data-toggle="modal" data-target="#formCreate">
						<i class="fa fa-envelope"> <spring:message code="sms.create" text="!"/></i>
					</button>
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
				<div id="_results"></div>
			</div>
		</div>
	</form:form>
	
	<!-- ------------------------------------------- CREATE ----------------------------------------------------- -->
	<div class="modal inmodal" id="formCreate" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog" style="width:900px">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true"><i class="fa fa-2x fa-times-circle"></i></span></button>
					<h5 class="modal-title text-uppercase"><spring:message code="sms.create" text="!"/></h5>
				</div>
				<div class="modal-body">
					<form:form id="form-add" name="form-add" method="post" commandName="smsFilter" action="sms">
						<input type="hidden" name="action" value="SEND">
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-4">
									<label><spring:message code="sms.type" text="!"/></label>
									<form:select path="typeSend" class="chosen-select" cssStyle="width:100%">
										<option value="1" <c:if test="${'1' == smsFilter.typeSend}">selected="selected"</c:if>>
											<spring:message code="sms.type.1" text="!"/>
										</option>
										<option value="2" <c:if test="${'1' == smsFilter.typeSend}">selected="selected"</c:if>>
											<spring:message code="sms.type.2" text="!"/>
										</option>
									</form:select>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-12">
									<spring:message code="sms.phone.note" text="!" var = "notePhone"/>
									<label><spring:message code="customer.telephone" text="!"/></label> <label class="text-danger">*</label>
									<form:textarea placeholder="${notePhone }" path="phoneSend" rows="2" class="form-control textfield"/>
									<label id="err_phoneSend" class="text-danger"></label>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-12">
									<label><spring:message code="sms.message" text="!"/></label>
									<form:textarea maxlength="450" path="messageSend" rows="3" class="form-control textfield"/>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-12 text-right">
									<button type="button" class="btn btn-w-m btn-default text-uppercase" data-dismiss="modal">
										<i class="fa fa-close"></i> <spring:message code="button.close" text="!"/>
									</button>
									<button type="button" onclick="doCreate();" class="btn btn-w-m btn-success text-uppercase">
										<i class="fa fa-paper-plane"></i> <spring:message code="button.send" text="!"/>
									</button>
								</div>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
</div>