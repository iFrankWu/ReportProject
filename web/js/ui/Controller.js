
MainController = function($scope,$location,$rootScope,$cookieStore,$http,CommonService){
 $scope.logoImage = "images/LOGO.jpg";
 $scope.width=216;
 $scope.height=62;
 /*
 	监听页面是否跳转，当页面跳转，需要以此来判定页面的Logo图片
 */
$rootScope.$on('$routeChangeSuccess', function(evt, cur, prev) {
    //alert($location.path());
	if($location.path() == "/truscreen/login"){
		$scope.logoImage = "images/login.jpg";
		 $scope.width=162;
         $scope.height=86;
	}else{
		 $scope.logoImage = "images/LOGO.jpg";
		  $scope.width=216;
 		  $scope.height=62;
	}
})


if($cookieStore.get("doctor") == null){
	$("#logout").show();
} else{
	var doctor = $cookieStore.get("doctor");
	if(doctor.type != CommonService.types[1].name){
		$("#doctorLink").show();
	}
	$("#reportLink").show();
	$("#mypageLink").show();
	$("#logout").show();
	if(doctor.type == "系统管理员"){
		$("#reportLink").hide();
		$("#systemsetting").show();
	}
}
	 $scope.unauthorizedCallBack = function(response){
		 if(response.status == 401){
			 alert("登陆已超时，请重新登陆。");
			 $cookieStore.put("doctor",null);// = null;
			 $location.path("/truscreen/login");
		 }else{
			 alert("未知系统错误，请重新登陆");
			 $cookieStore.put("doctor",null);
			 $location.path("/truscreen/login");
		 }
	 }

};


DoctorController = function ($scope, $routeParams,$location, $filter,$http,DoctorService,$cookieStore,CommonService,HospitalService,RecordService) {
	$scope.showExport = false;
	if($cookieStore.get("doctor") == null){
		//$("#logout").show();
		$("#reportLink").hide();
		$("#mypageLink").hide();
		$("#doctorLink").hide();
		$("#logout").hide();
	} else{
		var doctor = $cookieStore.get("doctor");
		if(doctor.type != CommonService.types[1].name){// not normal doctor
    		$("#doctorLink").show();
    		$scope.showExport = true;
		}
		$("#mypageLink").show();
		$("#reportLink").show();
		$("#logout").show();
		if(doctor.type == "系统管理员"){
			$("#reportLink").hide();
			$("#systemsetting").show();
			$scope.showExport = true;
		}
	}
	$scope.date5;
	$scope.date6;
	$scope.abletosummit = false;
	$scope.exportData = function(){
		if($scope.date5 !=null &&  $scope.date6!= null){
			if($scope.date5 > $scope.date6){
				alert("请设置正确的时间区间");
				$scope.abletosummit = true;
				return;
			}
			$scope.abletosummit = false;
		}
//		var data = {
//				"date5":$scope.date5,
//				"date6":$scope.date6
//		}
//		 RecordService.exportData(data,function(){
//			 
//		 },function(){
//			 
//		 });
	}
	$scope.statuses = { "values":['可用','禁用']};
	$scope.types =  { "values":['管理员','操作人员','申请医生']};
	$scope.currentDoctor;

    if( $scope.currentDoctor == null){
    	//$scope.currentDoctor = $cookieStore.get("doctor");
    	var doctor = $cookieStore.get("doctor");
    	if(doctor != null){
    		HospitalService.getDoctor(doctor.doctorId,function(result){
	    		if(result != null){
	    			$scope.currentDoctor = result;
	    		} 
	    	},function(response){
	    		 $scope.unauthorizedCallBack(response);
	    	});
    	}
    }
	
	 
	$scope.unauthorizedCallBack = function(response){
		 if(response.status == 401){
			 alert("登陆已超时，请重新登陆。");
			 $cookieStore.put("doctor",null);
			 $location.path("/truscreen/login");
		 }else{
			 alert("未知系统错误，请重新登陆");
			 $cookieStore.put("doctor",null);
			 $location.path("/truscreen/login");
		 }
	 }
 
		$scope.doctorName = "";
		$scope.doctorId;
	    $scope.password = "";
	    $scope.result;
	    $scope.doctors = null;
	    $scope.modifys=false;
		$scope.createDate = "";
		
		$scope.cancle= function(){
			 $scope.doctorId=null;
			 $scope.doctorName = "";
		     $scope.password = "";
		     $scope.result;
		     $scope.doctors;
		     $scope.modifys=false;
			 $scope.createDate = "";
			 $scope.prescribingDoctorName = "";
			 $scope.modifys = false;// !$scope.modifys;
		 }
		 
	   
	    $scope.seek = function(value,arrays){
	    	for(var ele in arrays){
	    		if(arrays[ele].value == value){
	    			return arrays[ele].name;
	    		}
	    	}
	    }
	    
	    $scope.login = function(){
	    	var doctor = {
	    			doctorName:$scope.doctorName,
	    			password:$scope.password
	    	}
	    	var result = DoctorService.login(doctor,function(result){
	    		if(result.isSuccess){
		    		 $cookieStore.put("doctor",result.description);
		    		$("#logout").show();
		    		$("#mypageLink").show();
		    		 $("#reportLink").show();
		    		 $("#systemsetting").hide();
		    		 if(result.description.type==CommonService.types[1].name){
		    			$("#doctorLink").hide();
		    			$location.path("/truscreen/reports");
		    		 }else if(result.description.type=="系统管理员"){
//		    				$("#mypageLink").hide();
				    		 $("#reportLink").hide();
				    		 $("#systemsetting").show();
				    		 $("#doctorLink").show();
				    		 $location.path("/truscreen/hospital");
		    		 }else {
		    				$("#doctorLink").show();
				    		 $location.path("/truscreen/doctors");//.search({'isSuccess':result.isSuccess});  
		    		 }
	    		}else{
		    		alert("登录失败，详情： "+result.description);
		    	}
	    	},function(response){
	    		alert("Response status:"+response.status+", 未知错误，请联系Frank Wu(wushexin@gmail.com)");
	    	});
	    }
	    $scope.checkDoctor = function(doctor){
	    	if(doctor.doctorName == null || doctor.doctorName == ""){
	    		alert("请输入医生名字");return false;
	    	}
	    	if(doctor.type == null){
	    		alert("请设置医生类型");return false;
	    	}
	    	if(doctor.status == null){
	    		alert("请设置医生状态");return false;
	    	}
	    	return true;
	    }
	    $scope.addDoctor = function(){
	    	var doctor = {
	    			doctorName:$scope.doctorName,
	    			password:$scope.password,
	    			type:$scope.type,
	    			status:$scope.status,
	    	}
	    	if(!$scope.checkDoctor(doctor))return;
	    	if($scope.password == null || $scope.password == ""){
	    		alert("请设置密码");return false;
	    	}
	    	DoctorService.addDoctor(doctor,function(result){
	    		if(result.isSuccess){
	    			alert("成功添加医生");
	    			$scope.cancle();
	    			$scope.getDoctorList();
	    		}else{
		    		alert("添加医生失败，详情： "+result.description);
		    	}
	    	},function(response){
	    		 $scope.unauthorizedCallBack(response);
	    	});
	    
	    }
	    $scope.deleteDoctor =  function(doctor){
	    	var r=confirm("确定删除该医生吗?");
	    	if(r == true){
		    	DoctorService.deleteDoctor(doctor.doctorId,function(result){
		    		if(result.isSuccess){
		    			alert("成功除该医生");
		    			$scope.getDoctorList();
		    		}else{
			    		alert("删除医生失败，详情： "+result.description);
			    	}
		    	},function(response){
		    		 $scope.unauthorizedCallBack(response);
		    	});
	    	}
	    }
	    $scope.getDoctorList = function(){
	    	 DoctorService.getDoctorList(function(result){
	    		if(result != null){
//		    		console.log(result);
		    		$scope.doctors = result;
		    	}else{
		    		alert("Get doctor list is null");
		    	}
	    	},function(response){
//	    		 $scope.unauthorizedCallBack(response);
	    		console.log(response);// just log it 
	    	});
	    
	    };
	    
//	    $scope.$watch('confirmed1', function(newValue, oldValue) {
//	        console.log("old value: %s and new value: %s", oldValue, newValue);
	    	 $scope.getDoctorList();
//	    });
	    $scope.modify = function(doctor){
	    	$scope.doctorName = doctor.doctorName;
		    $scope.password = null;//doctor.password;
		    $scope.type = doctor.type;
			$scope.status = doctor.status;
			$scope.createDate = doctor.createDate;
		 
			$scope.doctorId = doctor.doctorId;
			$scope.modifys = !$scope.modifys;
			
			$("#type").val(doctor.type);
			$("#status").val(doctor.status);
			if(doctor.type=="系统管理员"){
				$("#type4System").html(doctor.type);
//				$("#type").attr("disabled","disabled");
			}
	    }
	    $scope.updateDoctor = function(){
	    	var doctor = {
	    			doctorId:$scope.doctorId,
	    			doctorName:$scope.doctorName,
	    			password:$scope.password,
	    			type:$scope.type,
	    			status:$scope.status,
	    			createDate:$scope.createDate,
	    		 
	    	}
	    	if(!$scope.checkDoctor(doctor))return;
	    	DoctorService.updateDoctor($scope.doctorId,doctor,function(result){
	    		if(result.isSuccess){
	    			alert("更新医生成功");
	    		    $scope.cancle();
	    			$scope.getDoctorList();
	    			// update current user
	    			var doctor = $cookieStore.get("doctor");
	    	    	if(doctor != null){
	    	    		HospitalService.getDoctor(doctor.doctorId,function(result){
	    		    		if(result != null){
	    		    			$scope.currentDoctor = result;
	    		    		} 
	    		    	},function(response){
	    		    		 $scope.unauthorizedCallBack(response);
	    		    	});
	    	    	}
	    		}else{
		    		alert("更新医生失败，详情： "+result.description);
		    	}
	    	},function(response){
	    		 $scope.unauthorizedCallBack(response);
	    	});
	    }
	    
	    $scope.formatTime = function(time){
	    	if(time == null)return null;
	    	return $filter('date')(new Date(time), 'yyyy-MM-dd HH:mm:ss');
	    }
	    
	    
	    $scope.formatDate = function(time){
	    	if(time == null)return null;
	    	return $filter('date')(new Date(time), 'yyyy-MM-dd');
	    }
	    
	    $scope.isSelected = function(v1,type,v2){
	    	var r = "";
	    	if("type"==type)
	    		 r =  (v1==v2.type?"selected":"");
	    	else
	    		r= ( v1==v2.status?"":"selected");
	    	 document.write(r);
	    }
	    
	    
	   $scope.records ;
	   $scope.date1;
	   $scope.date2;
	   $scope.logDoctor;//the operation made by he
	   $scope.getRecords = function(){
		   if($scope.date1 != null && $scope.date2 != null){
			   if($scope.date1 > $scope.date2){
				   alert("请设置正确的日期区间");
				   return;
			   }
		   }
		 
		   if($scope.logDoctor == null || $scope.logDoctor ==""){
			   var date = {
					   "date1":$scope.date1,
					   "date2":$scope.date2,
					   "methodName":$scope.methodName,
					   "onlyMyself":true
			   }
		   }else{
			   var date = {
					   "date1":$scope.date1,
					   "date2":$scope.date2,
					   "methodName":$scope.methodName,
					   "onlyMyself":false,
			   		   "doctorName":$scope.logDoctor
			   }
		   }
		   RecordService.getRecords(date,function(result){
	    		if(result.isSuccess){
	    			$scope.records = result.recordList;
	    		}else{
		    		alert("获取日志失败，详情： "+result.description);
		    	}
	    	},function(response){
	    		 $scope.unauthorizedCallBack(response);
	    	});
	   }
	   $scope.date3;
	   $scope.date4;
	   $scope.statisticResult;
	   $scope.doctorNames;
	   $scope.statistic = function(){
		   if($scope.date3 != null && $scope.date4 != null){
			   if($scope.date3 > $scope.date4){
				   alert("请设置正确的日期区间");
				   return;
			   }
		   }
		   var data = {
				   "date3":$scope.date3,
				   "date4":$scope.date4,
				   "doctorName":$scope.doctorNames,
		   }
		   RecordService.statistic(data,function(result){
	    		if(result.isSuccess){
	    			$scope.statisticResult = result.statisticResult;
	    		}else{
		    		alert("获取日志失败，详情： "+result.description);
		    	}
	    	},function(response){
	    		 $scope.unauthorizedCallBack(response);
	    	});
	   }
	   
	   $scope.percent = function(){
		   var re = 0;
		   if($scope.statisticResult == null ){ 
			   re = 0;
		   }else if($scope.statisticResult.normal == 0 ){ 
			   re = 0;
		   }else{
			  re =$scope.statisticResult.normal / ($scope.statisticResult.normal+$scope.statisticResult.exception);
			  re =  (re*100);
		   }
		   return re.toPrecision(4).toString()+"%";
	   }
	 
};

ReportController = function ($scope, $routeParams,$location, $filter,$http,DoctorService,$cookieStore,CommonService,ReportService,$compile,HospitalService) {
	if($cookieStore.get("doctor") == null){
		//$("#logout").show();
	} else{
		var doctor = $cookieStore.get("doctor");
		if(doctor.type != CommonService.types[1].name){// not normal doctor
    		$("#doctorLink").show();
    		$("#createbtn").css("margin-left","289px");
		} 
		$("#mypageLink").show();
		$("#reportLink").show();
		$("#logout").show();
//		$scope.getPageNumber();
		if(doctor.type == "系统管理员"){
			$("#reportLink").hide();
			$("#systemsetting").show();
		}
	}
	
	$scope.doctor = $cookieStore.get("doctor");
	$scope.hospital ;
	$scope.showLogo = false;
 	$scope.getHospital = function(){
		HospitalService.getHospital($scope.hospital,function(result){
    		if(result != null){
    			$scope.hospital = result;
    			if(result.hospitalLogo == null || result.hospitalLogo == ""){
    				$scope.showLogo = false;
    			}else {
    				$scope.showLogo = true;
    			}
    		}else{
	    		//alert("删除报告单失败，详情： "+result.description);
	    	}
    	},function(response){
    		 $scope.unauthorizedCallBack(response);
    	});
	}

	$scope.getHospital();
	$scope.applyDoctorList = [];
	DoctorService.getDoctorList(function(result){
	    		if(result != null){
		    		 var j = 0;
		    		 for (var i = 0; i < result.length ; i++) {
		    		 	if(result[i].type == '申请医生')
		    		 		$scope.applyDoctorList[j++] = result[i].doctorName;
		    		 };

		    	}else{
		    		alert("Get doctor list is null");
		    	}
	    	},function(response){
	    		console.log(response);// just log it 
	    	});
	
	$scope.newCreateReport = false;
	$scope.newCreate = function(){
		$scope.report = null;//也可以赋值 比如 $scope.report = {'key1':'vaule1'}
		$scope.doesCheckCompleted = true;
		$scope.newCreateReport = !$scope.newCreateReport;
	}
	

	$scope.lcts = ["NILM","ASCUS","LSIL","HSIL","ASC-H","SCC","AGC"];
	$scope.hpvs = ["阴性","阳性"];

	// $scope.report = {'hpv':'vaule1'}
	// $scope.onChangeOption = function(){
	// 	document.getElementById("inputSelect").value=$scope.report.hpv;
	// 	console.log(document.getElementById("inputSelect").value);
	// }

	$scope.checkReport = function(){
		if($scope.report == null || $scope.report.patientName == null || $scope.report.caseNumber == null || $scope.report.patientName == "" ||  $scope.report.caseNumber == ""){
			alert("报告单中的姓名、病历号等信息不能够为空！");
			return false;
		}
		if($scope.report.department == null || $scope.report.department ==""){
			$scope.report.department = $scope.hospital.department;
			//alert("请输入报告单中的科室字段");
			//return false;
		}
		if($scope.getComplaints($scope.report) == ""){
			alert("请填写报告单主诉信息");
			return false;
		}
		
		if($scope.getClinical($scope.report) == "" ){
			alert("请填写报告单临床表现信息");
			return false;
		}
		
		if($scope.report.isComplete == null || $scope.report.isComplete ==""){
			alert("请设置检查是否完成；");return false;
		}
		if($scope.report.isComplete == "完成"){
			if($scope.report.pointNumber == null || $scope.report.pointNumber == ""){
				alert("请填写报告单点探数！");
				return false;
			}
			if($scope.report.pointNumber <15 || $scope.report.pointNumber > 32){
				alert("点探数必须在是15至32间的数值；");return false;
			}
			if($scope.report.checkResult == null ||  $scope.report.checkResult == "" ){
				alert("请填写报告单中检查结果字段！");
				return false;
			}
		}else{
			if($scope.report.reason4doesNotComplete == null || $scope.report.reason4doesNotComplete ==""){
				alert("请填写未完成失败原因");
				return false;
			}
		}
		
	
		return true;
	}
	$scope.saveReport = function(){
		if(!$scope.checkReport())return false;
		console.log($scope.report);
		ReportService.addReport($scope.report,function(result){
    		if(result.isSuccess){
    			alert("新建报告单成功");
    		    $scope.report = null;
    			$scope.newCreateReport = false;
    			$scope.getTopPage(1);
//    			$scope.getReportListByPage();
    		}else{
	    		alert("新建报告单败，详情： "+result.description);
	    	}
    	},function(response){
    		 $scope.unauthorizedCallBack(response);
    	});
		
	}
	$scope.cancleReport = function(){
		$scope.newCreateReport = false;
$scope.report = null;
	}
	
	// $scope.report = null;
	 
	$scope.unauthorizedCallBack = function(response){
		 if(response.status == 401){
			 alert("登陆已超时，请重新登陆。");
			 $cookieStore.put("doctor",null);
			 $location.path("/truscreen/login");
		 }else{
			 alert("未知系统错误，请重新登陆");
			 $cookieStore.put("doctor",null);
			 $location.path("/truscreen/login");
		 }
	 }
	 
	$scope.getComplaints = function(report){
		var complaints = "";
		if(report.isLeucorrhea){
			complaints += "白带多/";
		}
		if(report.isBleed){
			complaints += "性交出血/";
		}
		if(report.unregularBleed != null){
			complaints += "不规则流血:"+report.unregularBleed+"/";
		}
		if(report.otherComplaints != null){
			complaints += report.otherComplaints;
		}
		return $scope.spliceString(complaints);
	}
	
	$scope.getClinical = function(report){
		var clinical = "";
		if(report.isSmooth){
			clinical += "光滑/";
		}
		if(report.isAcuteInflammation){
			clinical += "急性炎症/";
		}
		if(report.isHypertrophy){
			clinical += "肥大/";
		}
		if(report.isPolyp){
			clinical += "息肉/";
		}
		if(report.erosion != null){
			clinical += "糜烂:"+report.erosion+"/";
		}
		if(report.isTear){
			clinical += "撕裂/";
		}
		if(report.isNesslersGlandCyst){
			clinical += "纳氏腺囊肿/";
		}
		if(report.isWhite){
			clinical += "白斑/";
		}
		if(report.isCancer){
			clinical += "可疑癌/";
		}
		if(report.otherClinical != null){
			clinical += report.otherClinical;
		}
		return $scope.spliceString(clinical);
	}
	$scope.isComplete = function(report){
		return report.isComplete?"是":"否";
	}
	$scope.canBeEdit = false;
	$scope.suggestion = function(report){
		var suggestion = "";
		if(report.screening){
			suggestion += "筛查/";
		}
		if(report.checking){
			suggestion += "检查/";
		}
		if(report.otherSuggestion){
			suggestion += report.otherSuggestion;
		}
	
		$scope.doesEditable(report);
		
		return $scope.spliceString(suggestion);
	}
	
	$scope.doesEditable = function(report){
		var doctor = $cookieStore.get("doctor");
		if(doctor.type == CommonService.types[1].name){// if is a normal doctor
			if(report.doctorId == doctor.doctorId){
				$scope.canBeEdit = false;
			}else{// normal doctor can't edit other doctor's report
				$scope.canBeEdit = true;
			}
		}else{
			$scope.canBeEdit= false;
		}
	
	}
	
	
	$scope.spliceString = function(str){
		if(str!= null){
			var length = str.length;
			if(length > 10){
				return  str.substr(0,7)+"...";
			}
			 if(str.charAt(length-1) == '/'){
				return str.substr(0,length-1);
			 }
		}
		return str;
	}
	
	
	$scope.reports =null;
	
	$scope.maxId = 1;
	$scope.minId = 1;
	$scope.size=10;
	$scope.sortColumn = "reportId";	
	$scope.gopage=0;
	$scope.currentPage = 0;
	$scope.allRecordNumber=10;
	$scope.getPrePage = function(pages){
		$("#showPageTr").css("display","");
		if($scope.currentPage <= 1){
			alert("已到第一页");
			return;
		}
		ReportService.getPrePage($scope.maxId,$scope.size,pages,$scope.sortColumn,function(result){
			if(result != null){
				$scope.reports = result.reportList;
				$scope.maxId = result.maxId;
				$scope.minId = result.minId;
				$scope.allRecordNumber = result.allRecordNumber;
				$scope.currentPage = $scope.currentPage -1;
				$scope.showPreNextPage();
				$scope.getPageNumber();
			}else{
				alert("获取报告单失败");
			}
		},function(response){
			 $scope.unauthorizedCallBack(response);
		});
	}
	$scope.getCurrentPage = function(pages){
		$("#showPageTr").css("display","");
		if($scope.maxId == null )return;
		ReportService.getCurrentPage($scope.maxId,$scope.size,pages,$scope.sortColumn,function(result){
			if(result != null){
				$scope.reports = result.reportList;
				$scope.maxId = result.maxId;
				$scope.minId = result.minId;
				$scope.allRecordNumber = result.allRecordNumber;
				$scope.showPreNextPage();
				$scope.getPageNumber();
			}else{
				alert("获取报告单失败");
			}
		},function(response){
			 $scope.unauthorizedCallBack(response);
		});
	}
	
	$scope.pageSizeChange = function(){
		if($scope.size <= 0){
			alert("每页显示记录数必须大于等于1，请设置正确的值");
			$scope.size = 1;
			return;
		}
		$scope.getCurrentPage(1);
	}
	$scope.showPreNextPage = function(){
		if($scope.currentPage <= 1 ){
			$("#prepage").css("display","none");
		}else{
			$("#prepage").css("display","");
		}
		if($scope.currentPage == $scope.getPageNumber() ){
			$("#nextpage").css("display","none");
		}else{
			$("#nextpage").css("display","");
		}
	}
	$scope.getNextPage = function(pages){
		$("#showPageTr").css("display","");
		if($scope.currentPage >= $scope.getPageNumber()){
			alert("已到最后一页");
			return;
		}
		ReportService.getNextPage($scope.minId,$scope.size,pages,$scope.sortColumn,function(result){
			$scope.reports = result.reportList;
			$scope.maxId = result.maxId;
			$scope.minId = result.minId;
			$scope.allRecordNumber = result.allRecordNumber;
			$scope.currentPage = $scope.currentPage +1;
			$scope.showPreNextPage();
			$scope.getPageNumber();
		},function(response){
			 $scope.unauthorizedCallBack(response);
		});
	}	
	$scope.getTopPage = function(pages){
		$("#showPageTr").css("display","");
		ReportService.getTopPage($scope.maxId,$scope.size,pages,$scope.sortColumn,function(result){
			$scope.reports = result.reportList;
			$scope.maxId = result.maxId;
			$scope.minId = result.minId;
			$scope.allRecordNumber = result.allRecordNumber;
			$scope.currentPage = $scope.currentPage +1;
			$scope.showPreNextPage();
			$scope.getPageNumber();
		},function(response){
			 $scope.unauthorizedCallBack(response);
		});
	}	
	$scope.paginationUI = function(){
		var html = "";
		$("pagination").html(html);
	}
	$scope.getPageNumber = function(){ 
		if($scope.allRecordNumber == null || $scope.allRecordNumber == 0){
			return 0;
		}
		//console.log("$scope.allRecordNumber%$scope.size:"+($scope.allRecordNumber%$scope.size));
		if($scope.allRecordNumber%$scope.size == 0){
			
			return $scope.allRecordNumber/$scope.size;
		}
		return Math.floor($scope.allRecordNumber/$scope.size)+1;
	}
	$scope.goPage = function(){
		$("#showPageTr").css("display","");
		if($scope.gopage != 0){
			var pages = 1;
			if($scope.gopage > $scope.getPageNumber() ){
				alert("输入的页码值必须小于最大页数 "+$scope.getPageNumber()+" ，请输入正确的页码值");
				return;
			}else if( $scope.gopage <= 0){
				alert("输入的页码值必须大于 0 ，请输入正确的页码值");return;
			}
			if(Math.abs($scope.gopage-$scope.currentPage) != 0){
				pages =  Math.abs($scope.gopage-$scope.currentPage);
			}
		}
		if($scope.currentPage >  $scope.gopage){
			$scope.getPrePage(pages);
			$scope.currentPage = $scope.gopage+1;
		}else if($scope.currentPage ==  $scope.gopage){
			return;
		}else {
			$scope.getNextPage(pages);
			$scope.currentPage = $scope.gopage-1;
		}
//		$scope.currentPage = $scope.gopage;
	}

	$scope.modifyFlag = false;
	$scope.modify = function(rep,$event){
		$scope.modifyFlag = true;
		$scope.report = rep;
		$scope.modifyUnderDetails = false;
		$scope.readonly = false;
		$scope.disabled = false;
		
		$scope.doesCheckComplete();
		$scope.isNormal();
		$scope.report.lastTimeMenstruation = $scope.formatDate($scope.report.lastTimeMenstruation);
		$scope.checkDate4Detial = $scope.formatTime($scope.report.checkDate);
		$scope.modifyDate4Detial = $scope.formatTime($scope.report.modifyDate);
		$scope.doesCheckComplete();
		$scope.isAvaiableWhileModify();
		
		$compile($("#midfyReport").contents())($scope);
		
		if($event != null){
				$event.stopPropagation();
		}
	}
	$scope.cancleModify = function(){
		$scope.modifyFlag = false;
		$scope.readonly = true;
		$scope.disabled = true;
		$scope.modifyUnderDetails = false;
		 $scope.closeDetails();
	}
	$scope.updateReport = function($event){
		if(!$scope.checkReport())return false;
		ReportService.updateReport($scope.report.reportId,$scope.report,function(result){
    		if(result.isSuccess){
    			alert("修改报告单成功");
    			//update locoal updated report value
    			for(var ele in $scope.reports ){
		    		if($scope.reports[ele].reportId == $scope.report.reportId){
		    			$scope.reports[ele] = $scope.report;
		    		}
    			}
    		    $scope.report = null;
    			$scope.cancleModify();
    		}else{
	    		alert("修改报告单失败，详情： "+result.description);
	    	}
    	},function(response){
    		 $scope.unauthorizedCallBack(response);
    	});
		if($event != null){
			$event.stopPropagation();
		}
	}
	$scope.deleteReport = function(rep,$event){
		var r=confirm("确实删除该报告单吗？");
		if(r == true){
			ReportService.deleteReport(rep.reportId,function(result){
	    		if(result.isSuccess){
	    			 $scope.closeDetails();
	    			//UI界面需要删除这行数据
	    			for(var ele in $scope.reports ){
    			    		if($scope.reports[ele].reportId == rep.reportId){
	    			    			$scope.reports.splice(ele,1);
    			    		}
	    			 }
	    			alert("删除报告单成功");
	    		}else{
		    		alert("删除报告单失败，详情： "+result.description);
		    	}
	    	},function(response){
	    		 $scope.unauthorizedCallBack(response);
	    	});
		}
		if($event != null){
			$event.stopPropagation();
		}
	}
	$scope.checkDatePrint;
	$scope.lastTimeMenstruationPrint;
	$scope.printReport = function(rep,$event){
		$scope.report = rep;
		$scope.doesCheckComplete();
		$scope.checkDatePrint = $scope.formatChineseDate(rep.checkDate);
		$scope.lastTimeMenstruationPrint = $scope.formatChineseDate(rep.lastTimeMenstruation);
        var doc = $('#printframe').get(0).contentWindow.document;
        var $body = $('body',doc);
		var prefix = '<div style="width:1050px;margin:auto;">';
		var suffix = "<div>";
		var modifyHtml = $("#printdiv").html();
		var html = prefix+modifyHtml+suffix;
        $body.html(html);
        $compile($("#printframe").contents())($scope);
        $scope.doesCheckComplete();
        setTimeout(function(){
            $('#printframe').get(0).contentWindow.focus();
            $('#printframe').get(0).contentWindow.print();
        }, 1000); 
		
		if($event != null){
			$event.stopPropagation();
		}
		 return false;
	}
	$scope.formatTime = function(time){
    	if(time == null)return null;
    	return $filter('date')(new Date(time), 'yyyy-MM-dd HH:mm:ss');//date.toLocaleString();
    }
    $scope.formatDate = function(time){
    	if(time == null)return null;
    	return $filter('date')(new Date(time), 'yyyy-MM-dd');
    }
    $scope.formatChineseDate = function(time){
    	if(time == null)return null;
    	return $filter('date')(new Date(time), 'yyyy-MM-dd');
    }
    $scope.searchText;
 
	$scope.isNormal = function(){
		console.log($scope.report.checkResult);
		if($scope.report.checkResult == "正常"){
			$(".normal").css("display","");
			$(".exception").css("display","none");
		}else{
			$(".normal").css("display","none");
			$(".exception").css("display","");
		}
	}
	$scope.doesCheckCompleted = false;
	$scope.doesCheckComplete = function(){
		if($scope.report.isComplete == "undefined" || $scope.report.isComplete == null ){
			$scope.doesCheckCompleted = false;
		}else if($scope.report.isComplete == "未完成"){
			$scope.doesCheckCompleted = false;
		}else{
			$scope.doesCheckCompleted = true;
		}
		$scope.isAvaiableWhileModify();
//		return true;
	}
	
	
	$scope.lastIndex = null;
	$scope.disabledModify = false;
	$scope.disabledDelete = false;
	$scope.checkDate4Detial;
	$scope.modifyDate4Detial;
	$scope.isDisableWhileModify = false;
	$scope.isReadonlyWhileModify = false;
	$scope.details = function($index,report,element){
		 $scope.readonly = true;
		 $scope.disabled = true;
		$scope.report = report;
		$scope.checkDate4Detial = $scope.formatTime($scope.report.checkDate);
		$scope.modifyDate4Detial = $scope.formatTime($scope.report.modifyDate);
		
		var doctor = $cookieStore.get("doctor");
		if(doctor.type == CommonService.types[1].name){// if is a normal doctor
			if(report.doctorId == doctor.doctorId){
				$scope.disabledModify = false;
				$scope.disabledDelete = false;
			}else{// normal doctor can't edit other doctor's report
				$scope.disabledModify = true;
				$scope.disabledDelete = true;
			}
		}else{
			$scope.disabledModify = false;
			$scope.disabledDelete = false;
		}
		
		if($("#details") == null || $("#details").text() == ""){
			
		}else{
			$("#details").remove();
			if($scope.lastIndex == $index)return;
		}
		$scope.lastIndex = $index;
		var ele = "#reportListTable tr:eq("+($index+1)+")";
		var modifyHtml = $("#midfyReport").html();
		var prefix = "<tr id='details'><td width='100%' colspan='14' style='text-align:left'>";
		var suffix = "</td></tr>";
//		var middle = '{{report.age}}<input type="button" ng-click="test1()" /><input type="text" ng-model="report.age"/><input type="date" value="{{formatDate(report.lastTimeMenstruation)}}"/><input type="checkbox" ng-model="report.isCompleted" />';
		var html = prefix+modifyHtml+suffix;
		
		

		$(ele).after(html).show();
		$compile($("#details").contents())($scope);
		$scope.isNormal();
		$scope.report.lastTimeMenstruation = $scope.formatDate($scope.report.lastTimeMenstruation);
		$scope.checkDate4Detial = $scope.formatTime($scope.report.checkDate);
		$scope.modifyDate4Detial = $scope.formatTime($scope.report.modifyDate);
		$scope.doesCheckComplete();
		$scope.isAvaiableWhileModify();
		$compile($("#details").contents())($scope);
//		$compile($("#midfyReport").contents())($scope);
//		$scope.isDisableWhileModify =$scope.disabled && !$scope.doesCheckCompleted;
//		$scope.isReadonlyWhileModify = $scope.readonly  && !$scope.doesCheckCompleted;
//		$scope.$apply($scope.charsRemaining);
//		alert($("#readonly").val());
//		$("#readyonly").removeAttr("readonly");

//		$("#readyonly").attr("readonly",false);
	}
	
	$scope.isAvaiableWhileModify = function(){
		if($scope.doesCheckCompleted){
			$scope.isDisableWhileModify =$scope.disabled;
			$scope.isReadonlyWhileModify = $scope.readonly;
		}else{
			$scope.isDisableWhileModify  =true;
			$scope.isReadonlyWhileModify = true;
		}
	}
	
	$scope.readonly = true;
	$scope.disabled = true;
	$scope.modifyUnderDetails = false;
	
	 $scope.activeUpdateReport = function(){
		 $scope.readonly = false;
		 $scope.disabled = false;
		 $scope.modifyUnderDetails = true;
		 $scope.isAvaiableWhileModify();
	 }
	  
	 $scope.closeDetails = function(){
		 $scope.modifyUnderDetails = false;
			if($("#details") == null || $("#details").text() == ""){
				
			}else{
				$("#details").remove();
			}
			$scope.report = null;
	 }
	 //init page
	 $scope.getTopPage(1);
	 
	 $scope.showAdvance = false;
	 $scope.openAdvanceSearch = function(){
		 $scope.showAdvance = true;
	 }
	 $scope.search;// the advance serach object
	 
	 $scope.advanceSearch = function(){
		 if($scope.search == null){
			 alert("请输入查询条件");
			 return;
		 }
		 if($scope.search.age != null && $scope.search.age2 != null ){
			 if($scope.search.age2 < $scope.search.age){
				 alert("请输入正确的年龄区间");
				 $("#age").focus();
				 return;
			 }
		 }
		 
		 if($scope.search.lastTimeMenstruation != null && $scope.search.lastTimeMenstruation2 != null ){
			 if($scope.search.lastTimeMenstruation > $scope.search.lastTimeMenstruation2){
				 alert("请输入正确的末次月经区间");
				 $("#lastTimeMenstruation").focus();
				 return;
			 }
		 }
		 
		 if($scope.search.checkDate != null && $scope.search.checkDate2 != null ){
			 if($scope.search.checkDate > $scope.search.checkDate2){
				 alert("请输入正确的检查日期区间");
				 $("#lastTimeMenstruation").focus();
				 return;
			 }
		 }
		 ReportService.advanceSearch($scope.search,function(result){
	    		if(result.isSuccess){
	    			$scope.reports = result.reportList;
	    			$scope.cancelAdvanceSearch();
	    			$("#showPageTr").css("display","none");
	    			$('#showResultSize').css("display","");
	    		}else{
		    		alert("搜索报告单失败，详情： "+result.description);
		    	}
	    	},function(response){
	    		 $scope.unauthorizedCallBack(response);
	    	});
	 }
	 $scope.cancelAdvanceSearch = function(){
		 $scope.showAdvance = false;
		 $scope.search = null;
		 $("#showPageTr").css("display","");
		 $('#showResultSize').css("display","none");
	 }
};
HospitalController = function($scope,$location,HospitalService,$locale,$cookieStore,$http,CommonService){
	if($cookieStore.get("doctor") == null){
		$("#logout").show();
	} else{
		var doctor = $cookieStore.get("doctor");
		if(doctor.type != CommonService.types[1].name){
			$("#doctorLink").show();
		}
		$("#reportLink").show();
		$("#mypageLink").show();
		$("#logout").show();
		if(doctor.type == "系统管理员"){
			$("#reportLink").hide();
			$("#systemsetting").show();
		}
	}
	$scope.hospital;
	$scope.readonly = true;
	$scope.isModify = false;
	$scope.checkHospital = function(){
		if($scope.hospital == null){
			alert("系统新不能够为空");return false;
		}
		if($scope.hospital.name == null || $scope.hospital.name == ""){
			alert("请输入医院名称");return false;
		}
		if($scope.hospital.department == null || $scope.hospital.department == ""){
			alert("请输入科室名称");return false;
		}
		if($scope.hospital.machineNumber == null || $scope.hospital.machineNumber == ""){
			alert("请输入主机序列号");return false;
		}
		if($scope.hospital.handController == null || $scope.hospital.handController == ""){
			alert("请输入手控器序列号");return false;
		}
		if($scope.hospital.firmwareVersion == null || $scope.hospital.firmwareVersion == ""){
			alert("请输入韧体版本");return false;
		}
		if($scope.hospital.prescribingDoctorName == null || $scope.hospital.prescribingDoctorName == ""){
			alert("请输入开方医生，多个以逗号分隔");return false;
		}
		return true;
	}
	$scope.addHospital = function(){
		if(!$scope.checkHospital())return;
		HospitalService.addHospital($scope.hospital,function(result){
    		if(result.isSuccess){
    			alert("删除报告单成功");
    		}else{
	    		alert("删除报告单失败，详情： "+result.description);
	    	}
    	},function(response){
    		 $scope.unauthorizedCallBack(response);
    	});
	}
	$scope.updateHospital = function(){
		if(!$scope.checkHospital())return;
		HospitalService.updateHospital($scope.hospital,function(result){
    		if(result.isSuccess){
    			alert("更新系统信息成功");
    			$scope.cancelModify();
    		}else{
	    		alert("更新系统信息失败，详情： "+result.description);
	    	}
    	},function(response){
    		 $scope.unauthorizedCallBack(response);
    	});
	}
	$scope.getHospital = function(){
		HospitalService.getHospital($scope.hospital,function(result){
    		if(result != null){
    			$scope.hospital = result;
    		}else{
	    		//alert("删除报告单失败，详情： "+result.description);
	    	}
    	},function(response){
    		 $scope.unauthorizedCallBack(response);
    	});
	}
	
	$scope.deleteLogo = function(){
		HospitalService.deleteLogo($scope.hospital.hospitalId,function(result){
    		if(result.isSuccess){
    			alert("删除Logo成功");
//    			$scope.getHospital();
//    			 $location.path("/truscreen/hospital");
    			location.reload();
    		}else{
	    		alert("删除Logo成功，详情： "+result.description);
	    	}
    	},function(response){
    		 $scope.unauthorizedCallBack(response);
    	});
	}
	$scope.activeModify = function(){
		$scope.isModify  = true;
		$scope.readonly = false;
		$scope.doesNotCancleModify = true;
		 return false;
	}
	$scope.doesCancleModify = true;
	$scope.cancelModify = function($event){
		$scope.isModify  = false;
		$scope.readonly = true;
		$scope.doesCancleModify = false;
		if($event != null){
			$event.stopPropagation();
		}
		 return false;
	}
	$scope.getHospital();

	$scope.unauthorizedCallBack = function(response){
		 if(response.status == 401){
			 alert("登陆已超时，请重新登陆。");
			 $cookieStore.put("doctor",null);
			 $location.path("/truscreen/login");
		 }else{
			 alert("未知系统错误，请重新登陆");
			 $cookieStore.put("doctor",null);
			 $location.path("/truscreen/login");
		 }
	 }
}
