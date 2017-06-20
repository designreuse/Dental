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

	function doEditValidator() {
		$.ajax({
			url : "<spring:url value='/secure/marketing/errorEdit.json'/>",
			data : $('#form_modify').serialize(),
			type : "POST",
			success : function(result) {
				if (result.errCodeEdit > 0) {
					$('#err_fullNameEdit').html('');
					$('#err_arrivalDateEdit').html('');

					var len = $.map(result.lstErr, function(n, i) {
						return i;
					}).length;
					for (var i = 0; i < len; i++) {
						$('#err_' + result.lstErr[i].propertyName).html(
								result.lstErr[i].message);
					}
				} else {
					$('#form_modify').submit();
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			}
		});
	};
</script>

<form:form name="form_modify" id="form_modify" method="post" commandName="marketingFilter" action="marketing">
	<input name="action" type="hidden" value="MODIFY">
	<input name="id" type="hidden" value="${marketing.marketingId }">

	<div class="form-group">
		<div class="row">
			<div class="col-sm-4">
				<label><spring:message code="customer.name" text="!" /></label> <label class="text-danger">*</label>
				<form:input path="fullNameEdit" value="${marketing.fullName }" type="text" cssClass="form-control textfield text-uppercase" />
				<label id="err_fullNameEdit" class="text-danger"></label>
			</div>
			<div class="col-sm-4">
				<label><spring:message code="customer.telephone" text="!" /></label>
				<form:input path="phoneEdit" value="${marketing.phone }" maxlength="11" type="text" cssClass="form-control textfield text-uppercase" />
			</div>
			<div class="col-sm-4">
				<label><spring:message code="customer.sex" text="!" /></label><br>
				<div class="radio radio-primary radio-inline">
					<input type="radio" id="sexEdit1" value="1" name="sexEdit" <c:if test="${marketing.sex == '1'}">checked="checked"</c:if>> 
					<label for="sexEdit1"> <spring:message code="customer.sex.men" text="!" /></label>
				</div>
				<div class="radio radio-primary radio-inline">
					<input type="radio" id="sexEdit2" value="2" name="sexEdit" <c:if test="${marketing.sex == '2'}">checked="checked"</c:if>>
					<label for="sexEdit2"> <spring:message code="customer.sex.women" text="!" /></label>
				</div>
			</div>
		</div>
	</div>

	<div class="form-group">
		<div class="row">
			<div class="col-sm-4">
				<fmt:formatDate pattern="dd/MM/yyyy" value="${marketing.birthday}" var="birthday"/>
				<label><spring:message code="customer.date.birth" text="!"/></label>
				<div class="input-group date">
                       <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                       <form:input path="birthdayEdit" value="${birthday }" type="text" cssClass="form-control textfield" placeholder="dd/MM/yyyy"/>
				</div>
				<label id="err_birthdayEdit" class="text-danger"></label>
			</div>
			<div class="col-sm-4">
				<label><spring:message code="customer.email" text="!" /></label>
				<form:input path="emailEdit" value="${marketing.email }" type="text" cssClass="form-control textfield" />
			</div>
			<div class="col-sm-4">
				<label><spring:message code="customer.address" text="!" /></label>
				<form:input path="addressEdit" value="${marketing.address }" type="text" cssClass="form-control textfield text-uppercase" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<div class="row">
			<div class="col-sm-4">
				<fmt:formatDate pattern="dd/MM/yyyy" value="${marketing.arrivalDate}" var="arrivalDate"/>
				<label><spring:message code="customer.date.start" text="!" /></label>
				<div class="input-group date">
					<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
					<form:input path="arrivalDateEdit" value="${arrivalDate }" type="text" cssClass="form-control textfield" placeholder="dd/MM/yyyy" />
				</div>
				<label id="err_arrivalDateEdit" class="text-danger"></label>
			</div>
			<div class="col-sm-4">
				<label><spring:message code="customer.content" text="!" /></label>
				<form:input path="contentEdit" value="${marketing.content }" type="text" cssClass="form-control textfield text-uppercase" />
			</div>

			<div class="col-sm-4">
				<label><spring:message code="customer.branch" text="!" /></label>
				<form:select path="branchEdit" class="chosen-select" cssStyle="width:100%">
					<c:forEach items="${branches}" var="elm">
						<option value="${elm.id }" <c:if test="${elm.id == marketing.branch}">selected="selected"</c:if>>${elm.name }</option>
					</c:forEach>
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
				<a onclick="doEditValidator();" class="btn btn-w-m btn-success text-uppercase"> <i
					class="fa fa-save"></i> <spring:message code="button.save" text="!" />
				</a>
			</div>
		</div>
	</div>
</form:form>