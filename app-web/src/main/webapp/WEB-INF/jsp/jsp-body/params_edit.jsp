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
	function doEditForm(id){
		var key = $('key').val();
		var type = $('type').val();
		if(type == ''){
       	 	$('#err_type').html('Input data');
   		}else if(key == ''){
       	 	$('#err_key').html('Input data');
   		}else $('#form_modify').submit();	
   		
	}
</script>

<form:form name="form_modify" id="form_modify" method="post" modelAttribute="params" action="params/update">
	<input name="paramsId" type="hidden" value="${paramsModel.paramsId }">
   		
	<div class="form-group">
		<div class="row">
			<div class="col-sm-4">
				<label>Key</label>
				<form:input path="key" value="${paramsModel.key }" type="text" cssClass="form-control textfield"/>
				<label id="err_key" class="text-danger"></label>
			</div>
			<div class="col-sm-4">
				<label>Value</label>
				<form:input path="value" value="${paramsModel.value }" type="text" cssClass="form-control textfield"/>			
			</div>
			<div class="col-sm-4">
				<label>Type</label>
				<form:input path="type" value="${paramsModel.type }" type="text" cssClass="form-control textfield"/>
				<label id="err_type" class="text-danger"></label>
			</div>
		</div>
	</div>
	
	
		                           
	<div class="form-group">
		<div class="row">
			<div class="col-lg-12 text-right">
				<button type="button" class="btn btn-w-m btn-default text-uppercase" data-dismiss="modal">
					<i class="fa fa-close"></i> <spring:message code="button.close" text="!Close"/>
				</button>
	  			<a onclick="doEditForm('${paramsModel.paramsId}')" class="btn btn-w-m btn-success text-uppercase" type="button">
	  				<i class="fa fa-check"></i> <spring:message code="button.edit" text="!Go"/>
	  			</a>
			</div>
		</div>
	</div>	
</form:form>