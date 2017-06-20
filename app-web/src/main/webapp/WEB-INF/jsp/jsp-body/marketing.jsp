<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<link href="<c:url value="/static/inspinia/css/plugins/sweetalert/sweetalert.css"/>" rel="stylesheet">
<script src="<c:url value="/static/inspinia/js/plugins/sweetalert/sweetalert.min.js"/>"></script>

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
	document.getElementById("marketing").className = "active";

	var pageNo = ${marketingFilter.page};
	
	$(function(){
		 $('#phoneCreate').mask("00000000000", { reverse: true });
		 $('#phoneEdit').mask("00000000000", { reverse: true });
		 $('#phoneAdd').mask("00000000000", { reverse: true });
		 
		fnLoadPage();

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

	function fnLoadPage() {
		$.ajax({
			url : "<spring:url value='/secure/marketing/loadpage'/>",
		    data: { pageNo: pageNo },
		    cache : false,
		    success: function (response) {
		        $("#_results").html(response);
		    }
		});
	};

	function doCreate(){
		$.ajax({
			url: "<spring:url value='/secure/marketing/errorCreate.json'/>",
	        data: $('#form-add').serialize(), 
	        type: "POST",
	        success: function(result){
	        	if(result.errCodeCreate > 0){
	        		$('#err_fullNameCreate').html('');
		        	$('#err_arrivalDateCreate').html('');
		        	$('#err_birthdayCreate').html('');
		        	
	           	 	var len = $.map(result.lstErr, function(n, i) { return i; }).length;
	           	 	for(var i=0;i<len;i++) {
               			$('#err_'+result.lstErr[i].propertyName).html(result.lstErr[i].message); 
              	 	}
	        	}else{
	        		$('#form-add').submit();
	        	}
	        },
		 	error: function(XMLHttpRequest, textStatus, errorThrown){}
		});
	};

	function ConformDelete(id, fullName) {
	    swal({
	        title: "<spring:message code="message.conform.delete" text="!"/>" + " [" + fullName + "] ?",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: "<spring:message code="button.delete" text="!"/>",
	        cancelButtonText: "<spring:message code="button.cancel" text="!"/>",
	        closeOnConfirm: false
	      },function () {
	   	 	document.forms[0].elements['action'].value='DELETE';
	   	 	document.forms[0].elements['id'].value=id;
			document.forms[0].submit();
	    });
	};

	function doEdit(id) {
 	    $.ajax({ 
 	    	url: "<spring:url value='/secure/marketing/edit/'/>" + id,
 	        cache: false,
 	        success: function (reponse) {
 	            $("div#edit-result").html(reponse);
 	        }
 	    });
 	}

	function doCustomer(id) {
 	    $.ajax({ 
 	    	url: "<spring:url value='/secure/marketing/create/'/>" + id,
 	        cache: false,
 	        success: function (reponse) {
 	            $("div#customer-result").html(reponse);
 	        }
 	    });
 	}
	
	function sendMultiSms(){
		var phones = '';
		$('input#sendSms:checked').each(function(){
			if($(this).is(':checked')){
				phones += $(this).parent().parent().parent().find('a').text().trim() + ';';
			}
		});
		phones = phones.substring(0,phones.length - 1);
		$('textarea#phoneSend').val(phones);
	}
	function doSendSMS(){
		$("#sms-send-form").submit();
	}
</script>

<div id="body">
	<form:form name="form" id="form" method="post" commandName="marketingFilter">
		<input name="action" type="hidden">
		<input name="id" type="hidden">
		
		<div class="form-group">
			<div class="row">
				<div class="col-sm-4">
					<label><spring:message code="customer.name" text="!"/></label>
					<form:input path="fullName" class="form-control textfield"/>
				</div>
				<div class="col-sm-4">
					<label><spring:message code="customer.telephone" text="!"/></label>
					<form:input path="phone" maxlength="11" class="form-control textfield"/>
				</div>
				<div class="col-sm-4">
					<label><spring:message code="customer.address" text="!"/></label>
					<form:input path="address" class="form-control textfield"/>
				</div>
			</div>
		</div>
		
		<div class="form-group">
			<div class="row">
				<div class="col-sm-4">
					<label><spring:message code="customer.status" text="!"/></label>
					<form:select path="status" class="chosen-select" cssStyle="width:100%">
						<option value="" <c:if test="${'' == marketingFilter.status}">selected="selected"</c:if>>
							<spring:message code="commom.all" text="!"/>
						</option>
						<option value="WATTING" <c:if test="${'WATTING' == marketingFilter.status}">selected="selected"</c:if>>
							<spring:message code="marketing.status_WATTING" text="!"/>
						</option>
						<option value="FINISH" <c:if test="${'FINISH' == marketingFilter.status}">selected="selected"</c:if>>
							<spring:message code="marketing.status_FINISH" text="!"/>
						</option>
					</form:select>
				</div>
				
				<sec:authorize access="hasAnyRole('MARKETING','ADMIN')">
				<div class="col-sm-4">
					<label><spring:message code="customer.branch" text="!"/></label>
					<form:select path="branch" class="chosen-select" cssStyle="width:100%">
						<option value="" <c:if test="${'' == marketingFilter.branch}">selected="selected"</c:if>>
							<spring:message code="commom.all" text="!"/>
						</option>
						<c:forEach items="${branches}" var="elm">
							<option value="${elm.id}"<c:if test="${elm.id == marketingFilter.branch}">selected="selected"</c:if>>
								${elm.name }
							</option>
						</c:forEach>
					</form:select>
				</div>
				</sec:authorize>
			</div>
		</div>
		
		<div class="form-group">
			<div class="row">
				<div class="col-sm-6">
					<sec:authorize access="hasAnyRole('MARKETING','ADMIN')">
						<button type="button" class="btn btn-w-m btn-success text-uppercase" data-toggle="modal" data-target="#formCreate">
							<i class="fa fa-plus-square"> <spring:message code="marketing.create" text="!"/></i>
						</button>
					</sec:authorize>
						
					<sec:authorize access="hasAnyRole('RECEPTION','MARKETING','ADMIN')">
						<button type="button" onclick="sendMultiSms();" class="btn btn-w-m btn-success text-uppercase" data-toggle="modal" data-target="#sendMultiSMS">
							<i class="fa fa-envelope"> <spring:message code="sms.create" text="!"/></i>
						</button>
					</sec:authorize>
				</div>
				<div class="col-sm-6 text-right">
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
	<div class="modal inmodal" id="formCreate" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" style="width:900px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true"><i class="fa fa-2x fa-times-circle"></i></span></button>
					<h5 class="modal-title text-uppercase"><spring:message code="marketing.create" text="!"/></h5>
				</div>
				<div class="modal-body">
					<form:form id="form-add" name="form-add" method="post" commandName="marketingFilter" action="marketing">
						<input type="hidden" name="action" value="CREATE">
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-4">
									<label><spring:message code="customer.name" text="!"/></label> <label class="text-danger">*</label>
									<form:input path="fullNameCreate" type="text" cssClass="form-control textfield text-uppercase"/>
									<label id="err_fullNameCreate" class="text-danger"></label>
								</div>
								<div class="col-sm-4">
									<label><spring:message code="customer.telephone" text="!"/></label>
									<form:input path="phoneCreate" maxlength="11" type="text" cssClass="form-control textfield text-uppercase"/>
								</div>
								<div class="col-sm-4">
									<label><spring:message code="customer.sex" text="!"/></label><br>
									<div class="radio radio-primary radio-inline">
                                         <input type="radio" id="sexCreate1" value="1" name="sexCreate" checked="checked">
                                         <label for="sexCreate1"> <spring:message code="customer.sex.men" text="!"/> </label>
                                    </div>
                                    <div class="radio radio-primary radio-inline">
                                        <input type="radio" id="sexCreate2" value="2" name="sexCreate">
                                        <label for="sexCreate2"> <spring:message code="customer.sex.women" text="!"/> </label>
                                    </div>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-4">
									<label><spring:message code="customer.date.birth" text="!"/></label>
									<div class="input-group date">
				                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
				                        <form:input path="birthdayCreate" type="text" cssClass="form-control textfield" placeholder="dd/MM/yyyy"/>
									</div>
									<label id="err_birthdayCreate" class="text-danger"></label>
								</div>
								<div class="col-sm-4">
									<label><spring:message code="customer.email" text="!"/></label>
									<form:input path="emailCreate" type="text" cssClass="form-control textfield"/>
								</div>
								<div class="col-sm-4">
									<label><spring:message code="customer.address" text="!"/></label><br>
									<form:input path="addressCreate" type="text" cssClass="form-control textfield text-uppercase"/>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-4">
									<label><spring:message code="customer.date.start" text="!"/></label>
									<div class="input-group date">
				                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
				                        <form:input path="arrivalDateCreate" type="text" cssClass="form-control textfield" placeholder="dd/MM/yyyy"/>
									</div>
									<label id="err_arrivalDateCreate" class="text-danger"></label>
								</div>
								<div class="col-sm-4">
									<label><spring:message code="customer.content" text="!"/></label>
									<form:input path="contentCreate" type="text" cssClass="form-control textfield text-uppercase"/>
								</div>
								
								<div class="col-sm-4">
									<label><spring:message code="customer.branch" text="!"/></label>
									<form:select path="branchCreate" class="chosen-select" cssStyle="width:100%">
										<c:forEach items="${branches}" var="elm">
											<option value="${elm.id }" <c:if test="${'DN10' == elm.id}">selected="selected"</c:if>>${elm.name }</option>
										</c:forEach>
									</form:select>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-12">
									<label><spring:message code="customer.note" text="!"/></label>
									<form:textarea path="noteCreate" rows="2" class="form-control textfield"/>
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
										<i class="fa fa-save"></i> <spring:message code="button.save" text="!"/>
									</button>
								</div>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	
	<!-- ------------------------------------------- EDIT ----------------------------------------------------- -->
	<div class="modal inmodal" id="formEdit" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" style="width: 900px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true"><i class="fa fa-2x fa-times-circle"></i></span></button>
					<h5 class="modal-title text-uppercase"><spring:message code="marketing.edit" text="!"/></h5>
				</div>
				<div class="modal-body">
					<div id="edit-result"></div>
				</div>
			</div>
		</div>
	</div>
	
	<!-- ------------------------------------------- ADD CUSTOMER  ----------------------------------------------------- -->
	<div class="modal inmodal" id="formCustomer" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" style="width: 900px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true"><i class="fa fa-2x fa-times-circle"></i></span></button>
					<h5 class="modal-title text-uppercase"><spring:message code="customer.create" text="!"/></h5>
				</div>
				<div class="modal-body">
					<div id="customer-result"></div>
				</div>
			</div>
		</div>
	</div>
	
	<!-- ------------------------------------------- ADD CUSTOMER  ----------------------------------------------------- -->
	<div class="modal inmodal" id="formSendSms" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" style="width: 900px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true"><i class="fa fa-2x fa-times-circle"></i></span></button>
					<h5 class="modal-title text-uppercase"><spring:message code="sms.create" text="!"/></h5>
				</div>
				<div class="modal-body">
					<div id="sms-send-result"></div>
				</div>
			</div>
		</div>
	</div>
	
	<!-- SEND SMS FORM -->
	<div class="modal inmodal" id="sendMultiSMS" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" style="width:900px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true"><i class="fa fa-2x fa-times-circle"></i></span></button>
					<h5 class="modal-title text-uppercase"><spring:message code="sms.create" text="!"/></h5>
				</div>
				<div class="modal-body">
					<form:form id="sms-send-form" name="form-add" method="post" commandName="smsFilter" action="sms">
						<input type="hidden" name="action" value="SEND">
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-4">
									<label><spring:message code="sms.type" text="!"/></label>
									<form:select path="typeSend" class="chosen-select" cssStyle="width:100%">
										<option value="7" <c:if test="${'7' == smsFilter.typeSend}">selected="selected"</c:if>>
											<spring:message code="sms.type.7" text="!"/>
										</option>
										<option value="0" <c:if test="${'0' == smsFilter.typeSend}">selected="selected"</c:if>>
											<spring:message code="sms.type.0" text="!"/>
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
									<button type="button" onclick="doSendSMS();" class="btn btn-w-m btn-success text-uppercase">
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