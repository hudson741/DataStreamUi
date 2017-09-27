$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/spark/log/list',
        datatype: "json",
        colModel: [			
            { label: '日志ID', name: 'logId', width: 50, key: true },
			{ label: '任务ID', name: 'jobId', width: 50},
			{ label: '任务名称', name: 'jobName', width: 60 },
			{ label: 'AppId', name: 'yarnAppId', width: 60 },
			{ label: '主类', name: 'mainClass', width: 60 },
			{ label: '参数', name: 'params', width: 60 },
			{ label: '状态', name: 'status', width: 50 },
			{ label: '开始时间', name: 'startTime', width: 80 },
			{ label: '结束时间', name: 'endTime', width: 80 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50,100,200],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
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
			jobId: null
		}
	},
	methods: {
		query: function () {
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'jobId': vm.q.jobId},
                page:1 
            }).trigger("reloadGrid");
		},
		showError: function(logId) {
			$.get("../sys/scheduleLog/info/"+logId, function(r){
				parent.layer.open({
				  title:'失败信息',
				  closeBtn:0,
				  content: r.log.error
				});
			});
		},
		back: function (event) {
			history.go(-1);
		}
	}
});

