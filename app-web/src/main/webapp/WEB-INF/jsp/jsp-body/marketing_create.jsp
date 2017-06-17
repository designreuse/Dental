<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

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
		$('.input-group.date').datepicker({
			todayBtn : "linked",
			todayHighlight : true,
			calendarWeeks : false,
			autoclose : true,
			format : "dd/mm/yyyy"
		});

		 $('.chosen-select').chosen({
	    	"disable_search": true
	    });
	});

	function doCustomerValidator() {
		$.ajax({
			url : "<spring:url value='/secure/marketing/errorCustomer.json'/>",
			data : $('#form_customer').serialize(),
			type : "POST",
			success : function(result) {
				if (result.errCode > 0) {
					$('#err_fullNameAdd').html('');
					$('#err_birthdayAdd').html('');
					$('#err_arrivalDateAdd').html('');

					var len = $.map(result.lstErr, function(n, i) {
						return i;
					}).length;
					for (var i = 0; i < len; i++) {
						$('#err_' + result.lstErr[i].propertyName).html(result.lstErr[i].message);
					}
				} else {
					$('#form_customer').submit();
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});
	};
</script>

<form:form name="form_customer" id="form_customer" method="post" commandName="CMFilter" action="marketingcustomer">
	<input name="action" type="hidden" value="ADD_CUSTOMER">
	<input name="id" type="hidden" value="${marketing.marketingId }">

	<div class="form-group">
		<div class="row">
			<div class="col-sm-4">
				<label><spring:message code="customer.name" text="!" /></label> <label class="text-danger">*</label>
				<form:input path="fullNameAdd" value="${marketing.fullName }" type="text" cssClass="form-control textfield text-uppercase" />
				<label id="err_fullNameAdd" class="text-danger"></label>
			</div>
			<div class="col-sm-4">
				<label><spring:message code="customer.telephone" text="!" /></label>
				<form:input path="phoneAdd" value="${marketing.phone }" maxlength="11" type="text" cssClass="form-control textfield text-uppercase" />
			</div>
			<div class="col-sm-4">
				<label><spring:message code="customer.sex" text="!" /></label><br>
				<div class="radio radio-primary radio-inline">
					<input type="radio" id="sexAdd1" value="1" name="sexAdd" <c:if test="${marketing.sex == '1'}">checked="checked"</c:if>> 
					<label for="sexAdd1"> <spring:message code="customer.sex.men" text="!" /></label>
				</div>
				<div class="radio radio-primary radio-inline">
					<input type="radio" id="sexAdd2" value="2" name="sexAdd" <c:if test="${marketing.sex == '2'}">checked="checked"</c:if>>
					<label for="sexAdd2"> <spring:message code="customer.sex.women" text="!" /></label>
				</div>
			</div>
		</div>
	</div>
	
	<div class="form-group">
		<div class="row">
			<div class="col-sm-4">
				<label><spring:message code="customer.type" text="!"/></label><br>
				<div class="radio radio-primary radio-inline">
                     <input type="radio" id="typeAdd1" value="N" name="typeAdd" checked="checked">
                     <label for="typeAdd1"> <spring:message code="customer.type.new" text="!"/> </label>
                </div>
                <div class="radio radio-primary radio-inline">
                    <input type="radio" id="typeAdd2" value="G" name="typeAdd">
                    <label for="typeAdd2"> <spring:message code="customer.type.guarantee" text="!"/> </label>
                </div>
			</div>
			<div class="col-sm-4">
				<fmt:formatDate pattern="dd/MM/yyyy" value="${marketing.birthday}" var="birthday"/>
				<label><spring:message code="customer.date.birth" text="!"/></label> <label class="text-danger">*</label>
				<div class="input-group date">
                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                	<form:input path="birthdayAdd" value="${birthday }" type="text" cssClass="form-control textfield" placeholder="dd/MM/yyyy"/>
				</div>
				<label id="err_birthdayAdd" class="text-danger"></label>
			</div>
			<div class="col-sm-4">
				<label><spring:message code="customer.email" text="!" /></label>
				<form:input path="emailAdd" value="${marketing.email }" type="text" cssClass="form-control textfield" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<div class="row">
			<div class="col-sm-8">
				<label><spring:message code="customer.address" text="!" /></label>
				<form:input path="addressAdd" value="${marketing.address }" type="text" cssClass="form-control textfield text-uppercase" />
			</div>
			<div class="col-sm-4">
				<label><spring:message code="customer.date.start" text="!" /></label> <label class="text-danger">*</label>
				<div class="input-group date">
					<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
					<form:input path="arrivalDateAdd" type="text" cssClass="form-control textfield" placeholder="dd/MM/yyyy" />
				</div>
				<label id="err_arrivalDateAdd" class="text-danger"></label>
			</div>
		</div>
	</div>

	<div class="form-group">
		<div class="row">
			<div class="col-sm-4">
				<label><spring:message code="customer.cause" text="!"/></label>
				<form:input path="causeAdd" value="${marketing.content}" type="text" cssClass="form-control textfield text-uppercase"/>
			</div>
			<div class="col-sm-4">
				<label><spring:message code="customer.dentist" text="!"/></label>
				<form:select path="dentistAdd" class="chosen-select" cssStyle="width:100%">
					<c:forEach items="${dentists}" var="elm">
						<option value="${elm.name }">${elm.name }</option>
					</c:forEach>
				</form:select>
			</div>
			<div class="col-sm-4">
				<label><spring:message code="customer.status" text="!"/></label>
				<form:select path="statusAdd" class="chosen-select" cssStyle="width:100%">
					<option value="W" selected="selected">
						<spring:message code="customer.status.W" text="!"/>
					</option>
					<option value="T">
						<spring:message code="customer.status.T" text="!"/>
					</option>
					<option value="F">
						<spring:message code="customer.status.F" text="!"/>
					</option>
				</form:select>
			</div>
		</div>
	</div>

	<div class="form-group">
		<div class="row">
			<div class="col-sm-12">
				<label><spring:message code="customer.note" text="!" /></label>
				<textarea name="noteEdit" rows="2" class="form-control textfield">${marketing.note }</textarea>
			</div>
		</div>
	</div>

	<div class="form-group">
		<div class="row">
			<div class="col-sm-12 text-right">
				<button type="button" class="btn btn-w-m btn-default text-uppercase" data-dismiss="modal">
					<i class="fa fa-close"></i> <spring:message code="button.close" text="!" />
				</button>
				<a onclick="doCustomerValidator();" class="btn btn-w-m btn-success text-uppercase"> <i
					class="fa fa-save"></i> <spring:message code="button.save" text="!" />
				</a>
			</div>
		</div>
	</div>
</form:form>