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
	function sendSMS(){
		var typeSMS = $('#smsType').val();
		var phoneNumber = $('#phoneNumber').val();
		$.ajax({
			url: "<spring:url value='/sms/send/'/>",
			type: "POST",
			cache: false,
			data: {typeSMS : typeSMS, phoneNumber: phoneNumber},
			success: function(result){
				alert("result");
			}
		});
	}
</script>
<div class="modal inmodal" id="smsPopup" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog" style="width:900px">
			<div class="modal-content animated bounceInRight">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true"><i class="fa fa-2x fa-times-circle"></i></span></button>
					<h5 class="modal-title text-uppercase">SMS</h5>
				</div>
				<div class="modal-body">
					<form id="form-add" name="form-add" action="marketing" method="post">
						<input type="hidden" name="phoneNumber" value="0973393310">
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-3">
									<label>Khach Hang</label> <label class="text-danger">*</label>
									<div id="fullNameCreate" name="fullNameCreate" class="text-uppercase"  value="">Pham Duy Thong</div>
									
								</div>
								<div class="col-sm-3">
									<label>Dien thoai</label>
									<div id="phoneCreate" name="phoneCreate" class="text-uppercase"  value="">0973393310</div>
								</div>
								<div class="col-sm-6">
									<label>Dia Chi</label><br>
									<div id="phoneCreate" name="phoneCreate" class="text-uppercase"  value="">14 ton dan p13 quan 4</div>
								</div>
							</div>
						</div>
						
					
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-4">
									<label>Loai Tin Nhan</label>
									<select id="branchCreate" name="branchCreate" style="width: 100%; display: none;" class="chosen-select">
										<option>so ngau nhien</option>
										<option>Tin nhan thuong hieu</option>
									</select>
								</div>
								
								
							</div>
						</div>
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-12">
									<label>Noi dung tin nhan</label>
									<textarea id="noteCreate" name="noteCreate" class="form-control textfield" rows="2"></textarea>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="row">
								<div class="col-sm-12 text-right">
									<button type="button" class="btn btn-w-m btn-default text-uppercase" data-dismiss="modal">
										<i class="fa fa-close"></i> Close
									</button>
									<button type="button" onclick="sendSMS();" class="btn btn-w-m btn-success text-uppercase">
										<i class="fa fa-save"></i> Gui
									</button>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>