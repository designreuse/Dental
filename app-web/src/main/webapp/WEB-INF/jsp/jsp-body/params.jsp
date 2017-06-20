<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<link href="<c:url value="/static/inspinia/css/plugins/sweetalert/sweetalert.css"/>" rel="stylesheet">
<script src="<c:url value="/static/inspinia/js/plugins/sweetalert/sweetalert.min.js"/>"></script>

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
	document.getElementById("param").className = "active";
	document.getElementById("param_param").className = "active";

	function doEditParam(id) {
	    $.ajax({ 
	     	url: "<spring:url value='/secure/params/edit/'/>" + id,
	        cache: false,
	        success: function (reponse) {
	            $("#modify-result").html(reponse);
	        }
	    });
	}
	
	function ConformDelete(id) {
	    swal({
	        title: "<spring:message code="message.conform.delete" text="!"/>" + " [" + id +"] ?",
	        type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: "<spring:message code="button.delete" text="!"/>",
	        cancelButtonText: "<spring:message code="button.cancel" text="!"/>",
	        closeOnConfirm: false
	    }, function () {
			 document.forms[0].elements['id'].value=id;
			 document.forms[0].submit();
	    });
	};
	
</script>
<div class="row">
	<div class="col-sm-12">
		<button type="button" class="btn btn-w-m btn-success text-uppercase" onclick="doEditParam('0')" data-toggle="modal" data-target="#formEdit">
			<i class="fa fa-plus-square"> <spring:message code="button.add" text="!Create Role"></spring:message></i>
		</button>
	</div>
</div>
<div id="body">
	<form:form id="form" name="form" method="post" modelAttribute="userFilter" action="params/delete">
		<input name="action" type="hidden">
		<input name="id" type="hidden">
	
		<div class="row">
			<div class="col-sm-12">&nbsp;</div>
			<div class="col-sm-12">
				<div id="_results">
					<table class="table table-striped table-bordered table-hover table-manifest" style="width:100%;">
						<thead>
							<tr>
								<th class="text-center">#</th>
								<th class="text-center"></th>
								<th><spring:message code="params.key" text="!"/></th>
								<th><spring:message code="params.value" text="!"/></th>
								<th><spring:message code="params.type" text="!"/></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${listParams}" var="elm" varStatus="stt">
							<tr>
								<td class="text-center">${stt.index + 1}</td>
								<td class="text-center text-nowrap">
									<a onclick='doEditParam("${elm.paramsId}")' data-toggle="modal" data-target="#formEdit"
										title="<spring:message code="user.table.modify" text="!Modify user"/>"><i class="fa fa-2x fa-edit"></i>
									</a>
									<a onclick="ConformDelete('${elm.paramsId}')" title="<spring:message code="button.delete" text="!Delete"/>"><i class="fa fa-2x fa-trash-o"></i></a>
								</td>
								<td>${elm.key }</td>
								<td>${elm.value }</td>
								<td>${elm.type }</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</form:form>
	<div class="modal inmodal" id="formEdit" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog" style="width:800px">
			<div class="modal-content animated bounceInRight">
				<div class="modal-body">
					<div id="modify-result"></div>
				</div>
			</div>
		</div>
	</div>	
</div>


