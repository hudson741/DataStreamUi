$(function () {
	

    $("#jqGrid").jqGrid({
        url: '../sys/spark/list',
        datatype: "json",
        colModel: [			
			{ label: '任务ID', name: 'jobId', width: 60, key: true },
			{ label: '任务名称', name: 'jobName', width: 100 },
			{ label: '任务描述', name: 'jobDescript', width: 150 },
			{ label: 'jar文件名', name: 'jarFileName', width: 100 },
			{ label: '主类class', name: 'mainClass', width: 100 },
			{ label: '用户参数 ', name: 'classArgs', width: 100 },
			{ label: 'driver规格', name: 'driverDockerType', width: 100 },
			{ label: 'executor规格 ', name: 'executorDockerType', width: 100 },
			{ label: 'executor数量 ', name: 'executorNum', width: 120 },
			{ label: 'spark配置', name: 'sparkConf', width: 120 },
			{ label: 'cron表达式', name: 'cronExpression', width: 100 },
			{ label: '状态', name: 'status', width: 60, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-success">正常</span>' : 
					'<span class="label label-danger">暂停</span>';
			}}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			jobName: null
		},
		showList: true,
		title: null,
		schedule: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.schedule = {};
		},
		update: function () {
			var jobId = getSelectedRow();
			if(jobId == null){
				return ;
			}
			
			$.get("../sys/spark/info/"+jobId, function(r){
				vm.showList = false;
                vm.title = "修改";
				vm.schedule = r.schedule;
			});
		},
		saveOrUpdate: function (event) {
			var url = vm.schedule.jobId == null ? "../sys/spark/save" : "../sys/spark/update";
			if(isNaN(vm.schedule.executorNum)){
				alert("executor不是数字")
				return
			}
			$.ajax({
				type: "POST",
			    url: url,
				contentType: "application/json",
			    data: JSON.stringify(vm.schedule),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},

		del: function (event) {
			var jobIds = getSelectedRows();
			if(jobIds == null){
				return ;
			}

			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/spark/delete",
					contentType: "application/json",
				    data: JSON.stringify(jobIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},

		pause: function (event) {
			var jobIds = getSelectedRows();
			if(jobIds == null){
				return ;
			}
			
			confirm('确定要暂停选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/spark/pause",
                    contentType: "application/json",
				    data: JSON.stringify(jobIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		resume: function (event) {
			var jobIds = getSelectedRows();
			if(jobIds == null){
				return ;
			}
			confirm('确定要恢复选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/spark/resume",
                    contentType: "application/json",
				    data: JSON.stringify(jobIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		runOnce: function (event) {
			var jobIds = getSelectedRows();
			if(jobIds == null){
				return ;
			}
			
			confirm('确定要立即执行选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/spark/run",
                    contentType: "application/json",
				    data: JSON.stringify(jobIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'jobName': vm.q.jobName},
                page:page 
            }).trigger("reloadGrid");
		}
		
	}
});