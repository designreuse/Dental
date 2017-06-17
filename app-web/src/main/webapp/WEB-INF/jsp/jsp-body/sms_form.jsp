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
		 $('#phoneSms').mask("00000000000", { reverse: true });
		 $('.chosen-select').chosen({
	    	"disable_search": true
	    });
	});

	function doSmsValidator() {
		var phoneSms = $('#phoneSms').val();
		var messageSms = $('#messageSms').val();
		var errorEmpty = '<spring:message code="message.empty" text="!"/>';
		var errorValid = '<spring:message code="message.valid" text="!"/>';
		

		$('#err_phoneSms').html('');
		$('#err_messageSms').html('');

		 if (phoneSms == null || phoneSms.trim() == '') {
			 $('#err_phoneSms').html(errorEmpty);
             return false;
         }

		 if (phoneSms.length < 10) {
			 $('#err_phoneSms').html(errorValid);
             return false;
         }

		 if (messageSms == null || messageSms.trim() == '') {
			 $('#err_messageSms').html(errorEmpty);
             return false;
         }

		 $('#form_sms').submit();
	}
</script>

<form:form name="form_sms" id="form_sms" method="post" action="smsSend">
	<input name="action" type="hidden" value="MODIFY">
	
	<div class="form-group">
		<div class="row">
			<div class="col-sm-4">
				<label><spring:message code="customer.telephone" text="!"/></label> <label class="text-danger">*</label>
				<input maxlength="11" value="${phone }" id="phoneSms" name="phoneSms" class="form-control textfield"/>
				<label id="err_phoneSms" class="text-danger"></label>
			</div>
			<div class="col-sm-4">
				<label><spring:message code="sms.type" text="!"/></label>
				<select id="typeSms" name="typeSms" class="chosen-select" style="width:100%">
					<option value="1">
						<spring:message code="sms.type.1" text="!"/>
					</option>
					<option value="2">
						<spring:message code="sms.type.2" text="!"/>
					</option>
				</select>
			</div>
		</div>
	</div>
	
	<div class="form-group">
		<div class="row">
			<div class="col-sm-12">
				<label><spring:message code="sms.message" text="!"/></label> <label class="text-danger">*</label>
				<textarea id="messageSms" maxlength="450" name="messageSms" rows="3" class="form-control textfield"></textarea>
				<label id="err_messageSms" class="text-danger"></label>
			</div>
		</div>
	</div>

	<div class="form-group">
		<div class="row">
			<div class="col-sm-12 text-right">
				<button type="button" class="btn btn-w-m btn-default text-uppercase" data-dismiss="modal">
					<i class="fa fa-close"></i> <spring:message code="button.close" text="!" />
				</button>
				<a onclick="doSmsValidator();" class="btn btn-w-m btn-success text-uppercase"> <i
					class="fa fa-paper-plane"></i> <spring:message code="button.send" text="!" />
				</a>
			</div>
		</div>
	</div>
</form:form>