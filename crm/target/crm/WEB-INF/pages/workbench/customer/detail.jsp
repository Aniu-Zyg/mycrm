<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">

	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<script type="text/javascript">

		//默认情况下取消和保存按钮是隐藏的
		var cancelAndSaveBtnDefault = true;

		$(function(){
			$("#remark").focus(function(){
				if(cancelAndSaveBtnDefault){
					//设置remarkDiv的高度为130px
					$("#remarkDiv").css("height","130px");
					//显示
					$("#cancelAndSaveBtn").show("2000");
					cancelAndSaveBtnDefault = false;
				}
			});

			// 日历实现：bootstrap的datetimepicker插件（给所有class含有my-date的标签赋予日历功能）
			$(".my-date").datetimepicker({
				language:'zh-CN', // 语言设为中文
				format:'yyyy-mm-dd', // 日期格式
				minView:'month', // 可以选择的最小视图
				initialDate:new Date(), // 初始化显示的日期
				autoclose:true, // 选择完日期后是否自动关闭
				todayBtn:true, // 显示‘今天’按钮
				clearBtn:true // 清空按钮
			});

			$("#cancelBtn").click(function(){
				//显示
				$("#cancelAndSaveBtn").hide();
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","90px");
				cancelAndSaveBtnDefault = true;
			});

			$("#remarkDivList").on("mouseover",".remarkDiv",function () {
				$(this).children("div").children("div").show();
			});

			$("#remarkDivList").on("mouseout",".remarkDiv",function () {
				$(this).children("div").children("div").hide();
			});

			$("#remarkDivList").on("mouseover",".myHref",function () {
				$(this).children("span").css("color","red");
			});

			$("#remarkDivList").on("mouseout",".myHref",function () {
				$(this).children("span").css("color","#E6E6E6");
			});

			// 给保存按钮添加单击事件
			$("#saveCreateCustomerRemarkBtn").click(function () {
				// 收集参数
				var noteContent = $.trim($("#remark").val());
				var customerId = '${customer.id}'; // 从request域中收集
				// 表单验证
				if (noteContent == ""){
					alert("备注内容不能为空");
					return;
				}
				// 发送请求
				$.ajax({
					url:'workbench/customer/saveCreateCustomerRemark.do',
					data:{
						noteContent:noteContent,
						customerId:customerId
					},
					type:'post',
					dateType:'json',
					success:function (data) {
						if(data.code == "1"){
							// 清空输入框
							$("#remark").val("");
							// 刷新备注列表
							var htmlStr = "";
							htmlStr += "<div id=\"div_"+data.retData.id+"\" class=\"remarkDiv\" style=\"height: 60px;\">";
							htmlStr += "<img title=\"${sessionScope.sessionUser.name}\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">";
							htmlStr += "<div style=\"position: relative; top: -40px; left: 40px;\" >";
							htmlStr += "<h5>"+data.retData.noteContent+"</h5>";
							htmlStr += "<font color=\"gray\">客户</font> <font color=\"gray\">-</font> <b>${customer.name}</b> <small style=\"color: gray;\"> "+data.retData.createTime+" 由${sessionScope.sessionUser.name}创建</small>";
							htmlStr += "<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
							htmlStr += "<a class=\"myHref\" name=\"editA\" remarkId=\""+data.retData.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
							htmlStr += "&nbsp;&nbsp;&nbsp;&nbsp;";
							htmlStr += "<a class=\"myHref\" name=\"deleteA\" remarkId=\""+data.retData.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
							htmlStr += "</div>";
							htmlStr += "</div>";
							htmlStr += "</div>";
							$("#remarkDiv").before(htmlStr); // 以追加的方式增加备注
						} else {
							// 提示信息
							alert(data.message);
						}
					}
				});
			});

			// 给所有的"删除"图标添加单击事件
			$("#remarkDivList").on("click","a[name='deleteA']",function () {
				// 收集参数
				var id = $(this).attr("remarkId"); // 获取删除选中的备注的id，使用attr()来获取自定义属性remarkId存放的的备注id值
				// 发送请求
				$.ajax({
					url:'workbench/customer/deleteCustomerRemarkById.do',
					data:{
						id:id
					},
					type:'post',
					dataType:'json',
					success:function (data) {
						if (data.code=="1") {
							// 刷新备注列表（直接移除删除的备注，此时数据库已经删除成功，这里是删除前端界面的信息）
							$("#div_"+id).remove(); // remove()会从dom树中删除匹配元素，将该备注模块删除
						} else {
							// 提示信息
							alert(data.message);
						}
					}
				});
			});

			// 给所有客户备注后边的"修改"图标添加单击事件
			$("#remarkDivList").on("click","a[name='editA']",function () {
				// 获取备注的id和noteContent
				var id = $(this).attr("remarkId"); // 通过自定义标签获取
				// 获取div的标签中的h5标签的内容，h5标签中就是备注内容（父子选择器，不要忘了前面的空格）
				var noteContent = $("#div_"+id+" h5").text();
				// 把备注的id和noteContent写到修改备注的模态窗口中
				$("#edit-id").val(id); // 给修改备注的模态窗口的隐藏input标签中写入该备注的id，用于修改
				$("#edit-noteContent").val(noteContent); // 写入备注的内容
				// 弹出修改客户备注的模态窗口
				$("#editRemarkModal").modal("show");
			});

			// 给“更新”按钮添加单击事件
			$("#updateRemarkBtn").click(function () {
				// 收集参数
				var id = $("#edit-id").val(); // 修改备注的模态窗口的备注id
				var noteContent = $.trim($("#edit-noteContent").val()); // 修改备注的模态窗口的备注内容
				//表单验证
				if(noteContent == ""){
					alert("备注内容不能为空");
					return;
				}
				// 发送请求
				$.ajax({
					url:'workbench/customer/saveEditCustomerRemark.do',
					data:{
						id:id,
						noteContent:noteContent
					},
					type:'post',
					dataType:'json',
					success:function (data) {
						if (data.code=="1") {
							// 关闭模态窗口
							$("#editRemarkModal").modal("hide");
							// 刷新备注列表（在前端写数据）
							$("#div_"+data.retData.id+" h5").text(data.retData.noteContent); // 备注前端显示修改后的数据
							$("#div_"+data.retData.id+" small").text(" "+data.retData.editTime+" 由${sessionScope.sessionUser.name}修改");
						} else {
							// 提示信息
							alert(data.message);
							// 模态窗口不关闭
							$("#editRemarkModal").modal("show");
						}
					}
				});
			});

			// 给所有的删除按钮添加单击事件（button标签的单击事件）
			$("#contactsTBody").on("click", "button", function () {
				// 收集参数
				var contactsId = $(this).attr("contactsId"); // 联系人id

				if (window.confirm("您确定要删除该联系人吗？")) {
					// 发送请求
					$.ajax({
						url:'workbench/customer/deleteContactsForDetailById.do',
						data: {
							contactsId:contactsId
						},
						type:'post',
						dataType:'json',
						success:function (data) {
							if(data.code == "1") {
								// 刷新已经关联的市场活动列表（移除对应id的市场活动）
								$("#tr_" + contactsId).remove();
							} else {
								// 提示信息
								alert(data.message);
							}
						}
					});
				}
			});

			// 给所有的删除按钮添加单击事件（button标签的单击事件）
			$("#tranTBody").on("click", "button", function () {
				// 收集参数
				var tranId = $(this).attr("tranId"); // 联系人id

				if (window.confirm("您确定要删除该交易吗？")) {
					// 发送请求
					$.ajax({
						url:'workbench/transaction/deleteTranById.do',
						data: {
							tranId:tranId
						},
						type:'post',
						dataType:'json',
						success:function (data) {
							if(data.code == "1") {
								// 刷新已经关联的市场活动列表（移除对应id的市场活动）
								$("#tr_" + tranId).remove();
							} else {
								// 提示信息
								alert(data.message);
							}
						}
					});
				}
			});

			// 给保存联系人按钮添加单击事件
			$("#saveCreateContactsBtn").click(function () {
				// 收集参数
				var owner = $("#create-owner").val();
				var source = $("#create-source").val();
				var fullname = $.trim($("#create-fullname").val());
				var appellation = $("#create-appellation").val();
				var job = $.trim($("#create-job").val());
				var mphone = $.trim($("#create-mphone").val());
				var email = $.trim($("#create-email").val());
				var customerId = $.trim($("#create-customerId").val());
				var description = $.trim($("#create-description").val());
				var contactSummary = $.trim($("#create-contactSummary").val());
				var nextContactTime = $.trim($("#create-nextContactTime").val());
				var address = $.trim($("#create-address").val());

				// 表单验证
				// 带*的非空
				if (fullname == "") {
					alert("姓名不能为空");
					return;
				}
				if (customerId == "") {
					alert("客户名称不能为空");
					return;
				}
				// 正则表达式验证
				if (email != "") { // 如果邮箱非空开始验证
					var emailRegExp = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/; // 邮件验证正则表达式
					if (!emailRegExp.test(email)) {
						alert("邮箱格式错误");
						return;
					}
				}
				if (mphone != "") {
					var mphoneRegExp = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
					if (!mphoneRegExp.test(mphone)) {
						alert("手机号码格式错误");
						return;
					}
				}
				// 发送请求
				$.ajax({
					url:'workbench/contacts/saveCreateContactsForDetail.do',
					data:{
						owner:owner,
						source:source,
						fullname:fullname,
						appellation:appellation,
						job:job,
						mphone:mphone,
						email:email,
						customerId:customerId,
						description:description,
						contactSummary:contactSummary,
						nextContactTime:nextContactTime,
						address:address
					},
					type:'post',
					dataType:'json',
					success:function (data) {
						if(data.code=="1"){
							// 关闭模态窗口
							$("#createContactsModal").modal("hide");
							window.location.reload();
						} else {
							// 提示信息
							alert(data.message);
							// 模态窗口不关闭
							$("#createContactsModal").modal("show");
						}
					}
				});
			});

		});

	</script>

</head>
<body>

<!-- 修改客户备注的模态窗口 -->
<div class="modal fade" id="editRemarkModal" role="dialog">
	<%-- 备注的id --%>
	<input type="hidden" id="remarkId">
	<div class="modal-dialog" role="document" style="width: 40%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">修改备注</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" role="form">
					<input type="hidden" id="edit-id">
					<div class="form-group">
						<label for="edit-noteContent" class="col-sm-2 control-label">内容</label>
						<div class="col-sm-10" style="width: 81%;">
							<textarea class="form-control" rows="3" id="edit-noteContent"></textarea>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
			</div>
		</div>
	</div>
</div>

<!-- 创建联系人的模态窗口 -->
<div class="modal fade" id="createContactsModal" role="dialog">
	<div class="modal-dialog" role="document" style="width: 85%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" onclick="$('#createContactsModal').modal('hide');">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title" id="myModalLabelx">创建联系人</h4>
			</div>
			<div class="modal-body">
				<form id="createContactsForm" class="form-horizontal" role="form">

					<div class="form-group">
						<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<select class="form-control" id="create-owner">
								<c:forEach items="${userList}" var="user">
									<option value="${user.id}">${user.name}</option>
								</c:forEach>
							</select>
						</div>
						<label for="create-source" class="col-sm-2 control-label">来源</label>
						<div class="col-sm-10" style="width: 300px;">
							<select class="form-control" id="create-source">
								<option></option>
								<c:forEach items="${sourceList}" var="source">
									<option value="${source.id}">${source.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div class="form-group">
						<label for="create-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="create-fullname">
						</div>
						<label for="create-appellation" class="col-sm-2 control-label">称呼</label>
						<div class="col-sm-10" style="width: 300px;">
							<select class="form-control" id="create-appellation">
								<option></option>
								<c:forEach items="${appellationList}" var="appellation">
									<option value="${appellation.id}">${appellation.value}</option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div class="form-group">
						<label for="create-job" class="col-sm-2 control-label">职位</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="create-job">
						</div>
						<label for="create-mphone" class="col-sm-2 control-label">手机</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="create-mphone">
						</div>
					</div>

					<div class="form-group" style="position: relative;">
						<label for="create-email" class="col-sm-2 control-label">邮箱</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="create-email">
						</div>
					</div>

					<input type="hidden" id="create-customerId" value=${customer.id}>

					<div class="form-group" style="position: relative;">
						<label for="create-description" class="col-sm-2 control-label">描述</label>
						<div class="col-sm-10" style="width: 81%;">
							<textarea class="form-control" rows="3" id="create-description"></textarea>
						</div>
					</div>

					<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

					<div style="position: relative;top: 15px;">
						<div class="form-group">
							<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
							</div>
						</div>
						<div class="form-group">
							<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control my-date" id="create-nextContactTime" readonly>
							</div>
						</div>
					</div>

					<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

					<div style="position: relative;top: 20px;">
						<div class="form-group">
							<label for="create-address" class="col-sm-2 control-label">详细地址</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="1" id="create-address"></textarea>
							</div>
						</div>
					</div>
				</form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" id="saveCreateContactsBtn">保存</button>
			</div>
		</div>
	</div>
</div>

<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
	<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
	<div class="page-header">
		<h3>${customer.name} <small><a href="https://${customer.website}" target="_blank">${customer.website}</a></small></h3>
	</div>
</div>

<br/>
<br/>
<br/>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
	<div style="position: relative; left: 40px; height: 30px;">
		<div style="width: 300px; color: gray;">所有者</div>
		<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${customer.owner}</b></div>
		<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
		<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${customer.name}</b></div>
		<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
		<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
	</div>
	<div style="position: relative; left: 40px; height: 30px; top: 10px;">
		<div style="width: 300px; color: gray;">公司网站</div>
		<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${customer.website}&nbsp</b></div>
		<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">公司座机</div>
		<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${customer.phone}&nbsp</b></div>
		<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
		<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
	</div>
	<div style="position: relative; left: 40px; height: 30px; top: 20px;">
		<div style="width: 300px; color: gray;">创建者</div>
		<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${customer.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${customer.createTime}</small></div>
		<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
	</div>
	<div style="position: relative; left: 40px; height: 30px; top: 30px;">
		<div style="width: 300px; color: gray;">修改者</div>
		<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${customer.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${customer.editTime}</small></div>
		<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
	</div>
	<div style="position: relative; left: 40px; height: 30px; top: 40px;">
		<div style="width: 300px; color: gray;">联系纪要</div>
		<div style="width: 630px;position: relative; left: 200px; top: -20px;">
			<b>
				${customer.contactSummary}
			</b>
		</div>
		<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
	</div>
	<div style="position: relative; left: 40px; height: 30px; top: 50px;">
		<div style="width: 300px; color: gray;">下次联系时间</div>
		<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${customer.nextContactTime}</b></div>
		<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; "></div>
	</div>
	<div style="position: relative; left: 40px; height: 30px; top: 60px;">
		<div style="width: 300px; color: gray;">描述</div>
		<div style="width: 630px;position: relative; left: 200px; top: -20px;">
			<b>
				${customer.description}
			</b>
		</div>
		<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
	</div>
	<div style="position: relative; left: 40px; height: 30px; top: 70px;">
		<div style="width: 300px; color: gray;">详细地址</div>
		<div style="width: 630px;position: relative; left: 200px; top: -20px;">
			<b>
				${customer.address}&nbsp
			</b>
		</div>
		<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
	</div>
</div>

<!-- 备注 -->
<div id="remarkDivList" style="position: relative; top: 10px; left: 40px;">
	<div class="page-header">
		<h4>备注</h4>
	</div>

	<!-- 遍历线索备注列表 -->
	<c:forEach items="${customerRemarkList}" var="remark">
		<div id="div_${remark.id}" class="remarkDiv" style="height: 60px;">
			<img title="${remark.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>${remark.noteContent}</h5>
				<font color="gray">客户</font> <font color="gray">-</font> <b>${customer.name}</b> <small style="color: gray;">${remark.editFlag=='1'?remark.editTime:remark.createTime}由${remark.editFlag=='1'?remark.editBy:remark.createBy}${remark.editFlag=='1'?'修改':'创建'}</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" name="editA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" name="deleteA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
	</c:forEach>

	<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
		<form role="form" style="position: relative;top: 10px; left: 10px;">
			<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
			<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
				<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
				<button type="button" class="btn btn-primary" id="saveCreateCustomerRemarkBtn">保存</button>
			</p>
		</form>
	</div>
</div>

<!-- 交易 -->
<div>
	<div style="position: relative; top: 20px; left: 40px;">
		<div class="page-header">
			<h4>交易</h4>
		</div>
		<div style="position: relative;top: 0px;">
			<table id="activityTable2" class="table table-hover" style="width: 900px;">
				<thead>
				<tr style="color: #B3B3B3;">
					<td>名称</td>
					<td>金额</td>
					<td>阶段</td>
					<td>预计成交日期</td>
					<td>类型</td>
					<td></td>
				</tr>
				</thead>
				<tbody id="tranTBody">
				<c:forEach items="${tranList}" var="tran">
				<tr id="tr_${tran.id}">
					<td><a href="workbench/transaction/detailTran.do?id=${tran.id}" style="text-decoration: none;">${tran.name}</a></td>
					<td>${tran.money}</td>
					<td>${tran.stage}</td>
					<td>${tran.expectedDate}</td>
					<td>${tran.type}</td>
					<td><button style="border:0px;background-color: white;color:rgb(66,139,202)" onclick="javascript:void(0);" tranId="${tran.id}" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</button></td>
				</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>

		<div>
			<a href="workbench/transaction/toSave.do" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>新建交易</a>
		</div>
	</div>
</div>

<!-- 联系人 -->
<div>
	<div style="position: relative; top: 20px; left: 40px;">
		<div class="page-header">
			<h4>联系人</h4>
		</div>
		<div style="position: relative;top: 0px;">
			<table id="activityTable" class="table table-hover" style="width: 900px;">
				<thead>
				<tr style="color: #B3B3B3;">
					<td>名称</td>
					<td>邮箱</td>
					<td>手机</td>
					<td></td>
				</tr>
				</thead>
				<tbody id="contactsTBody">
				<c:forEach items="${contactsList}" var="contacts">
				<tr id="tr_${contacts.id}">
					<td><a href="workbench/contacts/detailContacts.do?id=${contacts.id}" style="text-decoration: none;">${contacts.fullname}</a></td>
					<td>${contacts.email}</td>
					<td>${contacts.mphone}</td>
					<td><button style="border:0px;background-color: white;color:rgb(66,139,202)" onclick="javascript:void(0);" contactsId="${contacts.id}" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</button></td>
				</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>

		<div>
			<a href="javascript:void(0);" id="bindContactsBtn" data-toggle="modal" data-target="#createContactsModal" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>新建联系人</a>
		</div>
	</div>
</div>

<div style="height: 200px;"></div>
</body>
</html>