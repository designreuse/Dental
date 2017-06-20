<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<style type="text/css">
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
	$(document).ready(function() {
		$('.chosen-select').chosen({
	    	"disable_search": true
	    });
	    
	    $('.input-group.date').datepicker({
			todayBtn: "linked",
			todayHighlight: true,
            calendarWeeks: false,
            autoclose: true,
			format: "dd/mm/yyyy"
	    });
	});

	function doEdit(action){
		$.ajax({
			url: "<spring:url value='/secure/records/errorEdit.json'/>",
	        data: $('#form_modify').serialize(), 
	        type: "POST",
	        success: function(result){
	        	if(result.errCodeEdit > 0){
	        		$('#err_dateExcuteEdit').html('');
		        	$('#err_teethEdit').html('');
		        	$('#err_contentEdit').html('');
        			$('#err_dateNextEdit').html('');
        			$('#err_grossEdit').html('');
        			$('#err_saleEdit').html('');
        			$('#err_paymentEdit').html('');
        			
	           	 	var len = $.map(result.lstErr, function(n, i) { return i; }).length;
	           	 	for(var i=0;i<len;i++) {
               			$('#err_'+result.lstErr[i].propertyName).html(result.lstErr[i].message); 
              	 	}
	        	}else{
	        		document.forms[1].elements['action'].value = action;
	        		$('#form_modify').submit();
	        	}
	        },
		 	error: function(XMLHttpRequest, textStatus, errorThrown){}
		});
	};
</script>

<form:form name="form_modify" id="form_modify" method="post" commandName="recordsFilter" action="records">
	<input name="action" type="hidden">
	<c:set var="formatPattern" value="#,##0"/>
	
	<form:hidden path="recordId" value="${records.recordId }"/>
	<form:hidden path="customerId" value="${records.customerId }"/>
	
   	<div class="form-group">
		<div class="row">
			<div class="col-sm-6">
				<fmt:formatDate pattern="dd/MM/yyyy" value="${records.dateExcute}" var = "dateExcute"/>
				<label><spring:message code="records.dateExcute" text="!"/></label> <label class="text-danger">*</label>
				<div class="input-group date">
	                <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
	                <form:input path="dateExcuteEdit" value="${dateExcute }" type="text" cssClass="form-control textfield" placeholder="dd/MM/yyyy"/>
				</div>
				<label id="err_dateExcuteEdit" class="text-danger"></label>
			</div>
			<div class="col-sm-6">
				<label><spring:message code="records.dentist" text="!"/></label>
				<form:select path="dentistEdit" class="chosen-select" cssStyle="width:100%">
					<c:forEach items="${dentists}" var="elm">
						<option value="${elm.name }" <c:if test="${records.dentist == elm.name}">selected="selected"</c:if>>${elm.name }</option>
					</c:forEach>
				</form:select>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-6">
				<label><spring:message code="records.teeth" text="!"/></label> <label class="text-danger">*</label>
				<form:input path="teethEdit" value="${records.teeth}" type="text" cssClass="form-control textfield"/>
				<label id="err_teethEdit" class="text-danger"></label>
			</div>
			<div class="col-sm-6">
				<label><spring:message code="records.content" text="!"/></label> <label class="text-danger">*</label>
				<form:input path="contentEdit" value="${records.content}" type="text" cssClass="form-control textfield text-uppercase"/>
				<label id="err_contentEdit" class="text-danger"></label>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-6">
				<fmt:formatNumber pattern="${formatPattern}" value="${records.gross}" var="gross"/>
				<label><spring:message code="records.amount.extra" text="!"/></label> <label class="text-danger">*</label>
				<form:input path="grossEdit" value="${gross }" type="text" cssClass="form-control textfield text-right"/>
				<label id="err_grossEdit" class="text-danger"></label>
			</div>
			<div class="col-sm-6">
				<fmt:formatNumber pattern="${formatPattern}" value="${records.sale}" var="sale"/>
				<label><spring:message code="records.sale" text="!"/></label> <label class="text-danger">*</label>
				<form:input path="saleEdit" value="${sale }" type="text" cssClass="form-control textfield text-right"/>
				<label id="err_saleEdit" class="text-danger"></label>
			</div>
		</div>

		<div class="row">
			<div class="col-sm-6">
				<fmt:formatDate pattern="dd/MM/yyyy" value="${records.dateNext}" var = "dateNext"/>
				<label><spring:message code="records.dateNext" text="!"/></label>
				<div class="input-group date">
	                <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
	                <form:input path="dateNextEdit" value="${dateNext }" type="text" cssClass="form-control textfield" placeholder="dd/MM/yyyy"/>
				</div>
				<label id="err_dateNextEdit" class="text-danger"></label>
			</div>
			<div class="col-sm-6">
				<label><spring:message code="records.contentNext" text="!"/></label>
				<form:input path="contentNextEdit" value="${records.contentNext }" type="text" cssClass="form-control textfield text-uppercase"/>
			</div>
		</div>
		
		<sec:authorize access="hasAnyRole('RECEPTION','ADMIN')">
		<div class="row">
			<div class="col-sm-4">
				<label><spring:message code="invoice.cause" text="!"/></label>
				<form:input path="causePaymentEdit" type="text" cssClass="form-control textfield text-uppercase"/>
			</div>
			<div class="col-sm-4">
				<fmt:formatNumber pattern="${formatPattern}" value="${records.payment}" var="payment"/>
				<label><spring:message code="records.payment" text="!"/></label> <label class="text-danger">*</label>
				<form:input path="paymentEdit" value="${payment }" type="text" cssClass="form-control textfield text-right"/>
				<label id="err_paymentEdit" class="text-danger"></label>
			</div>
			<div class="col-sm-4">
				<label><spring:message code="records.finish" text="!"/></label>
				<form:select path="statusEdit" class="chosen-select" cssStyle="width:100%">
					<option value="W" <c:if test="${'W' == records.status}">selected="selected"</c:if>>
						<spring:message code="customer.status.W" text="!"/>
					</option>
					<option value="F" <c:if test="${'F' == records.status}">selected="selected"</c:if>>
						<spring:message code="customer.status.F" text="!"/>
					</option>
				</form:select>
			</div>
		</div>
		</sec:authorize>
		
		<div class="row">
			<div class="col-sm-12 text-right">
				<button type="button" class="btn btn-w-m btn-default text-uppercase" data-dismiss="modal">
					<i class="fa fa-close"></i> <spring:message code="button.close" text="!"/>
				</button>
				<sec:authorize access="hasAnyRole('DOCTOR','RECEPTION','ADMIN')">
				<button type="button" onclick="doEdit('MODIFY');" class="btn btn-w-m btn-success text-uppercase">
					<i class="fa fa-save"></i> <spring:message code="button.save" text="!"/>
				</button>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('RECEPTION','ADMIN')">
				<button type="button" onclick="doEdit('PRINT');" class="btn btn-w-m btn-success text-uppercase">
					<i class="fa fa-print  "></i> <spring:message code="button.save.printer" text="!"/>
				</button>
				</sec:authorize>
			</div>
		</div>
	</div>
</form:form>