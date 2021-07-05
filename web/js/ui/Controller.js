MainController = function ($scope, $location, $rootScope, $cookieStore, $http, CommonService) {
    $scope.logoImage = "images/LOGO.jpg";
    $scope.width = 216;
    $scope.height = 62;
    /*
        监听页面是否跳转，当页面跳转，需要以此来判定页面的Logo图片
    */
    $rootScope.$on('$routeChangeSuccess', function (evt, cur, prev) {
        //alert($location.path());
        if ($location.path() == "/truscreen/login") {
            $scope.logoImage = "images/login.jpg";
            $scope.width = 162;
            $scope.height = 86;
        } else {
            $scope.logoImage = "images/LOGO.jpg";
            $scope.width = 216;
            $scope.height = 62;
        }
    })


    if ($cookieStore.get("doctor") == null) {
        $("#logout").show();
    } else {
        var doctor = $cookieStore.get("doctor");
        if (doctor.type != CommonService.types[1].name) {
            $("#doctorLink").show();
        }
        $("#reportLink").show();
        $("#reportLink1").show();
        $("#reportLink2").show();
        $("#mypageLink").show();
        $("#logout").show();
        if (doctor.type == "系统管理员") {
            $("#reportLink").hide();
            $("#systemsetting").show();
            $("#reportLink1").hide();
            $("#reportLink2").hide();
        }
    }
    $scope.unauthorizedCallBack = function (response) {
        $("#gotohub").hide();
        if (response.status == 401) {
            alert("登陆已超时，请重新登陆。");
            $cookieStore.put("doctor", null);// = null;
            $location.path("/truscreen/login");
        } else {
            alert("未知系统错误，请重新登陆");
            $cookieStore.put("doctor", null);
            $location.path("/truscreen/login");
        }
    }

    $("#reportLink2").hide();
    $("#reportLink1").hide();
};


DoctorController = function ($scope, $routeParams, $location, $filter, $http, DoctorService, $cookieStore, CommonService, HospitalService, RecordService) {
    $scope.showExport = false;
    if ($cookieStore.get("doctor") == null) {
        //$("#logout").show();
        $("#reportLink").hide();
        $("#mypageLink").hide();
        $("#doctorLink").hide();
        $("#reportLink1").hide();
        $("#reportLink2").hide();
        $("#logout").hide();
        $("#gotohub").hide();
    } else {
        var doctor = $cookieStore.get("doctor");
        if (doctor.type != CommonService.types[1].name) {// not normal doctor
            $("#doctorLink").show();
            $scope.showExport = true;
        }
        $("#mypageLink").show();
        $("#reportLink").show();
        $("#reportLink1").show();
        $("#reportLink2").show();
        $("#logout").show();
        if (doctor.type == "系统管理员") {
            $("#reportLink").hide();
            $("#systemsetting").show();
            $scope.showExport = true;
        }
        $("#gotohub").show();
    }


    $("#reportLink2").hide();
    $("#reportLink1").hide();

    $scope.date5;
    $scope.date6;
    $scope.abletosummit = false;
    $scope.exportData = function () {
        if ($scope.date5 != null && $scope.date6 != null) {
            if ($scope.date5 > $scope.date6) {
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
    $scope.statuses = {"values": ['可用', '禁用']};
    $scope.types = {"values": ['管理员', '操作人员', '申请医生']};
    $scope.currentDoctor;

    if ($scope.currentDoctor == null) {
        //$scope.currentDoctor = $cookieStore.get("doctor");
        var doctor = $cookieStore.get("doctor");
        if (doctor != null) {
            HospitalService.getDoctor(doctor.doctorId, function (result) {
                if (result != null) {
                    $scope.currentDoctor = result;
                }
            }, function (response) {
                $scope.unauthorizedCallBack(response);
            });
        }
    }


    $scope.unauthorizedCallBack = function (response) {
        $("#gotohub").hide();
        if (response.status == 401) {
            alert("登陆已超时，请重新登陆。");
            $cookieStore.put("doctor", null);
            $location.path("/truscreen/login");
        } else {
            alert("未知系统错误，请重新登陆");
            $cookieStore.put("doctor", null);
            $location.path("/truscreen/login");
        }
    }

    $scope.doctorName = "";
    $scope.doctorId;
    $scope.password = "";
    $scope.result;
    $scope.doctors = null;
    $scope.modifys = false;
    $scope.createDate = "";

    $scope.cancle = function () {
        $scope.doctorId = null;
        $scope.doctorName = "";
        $scope.password = "";
        $scope.result;
        $scope.doctors;
        $scope.modifys = false;
        $scope.createDate = "";
        $scope.prescribingDoctorName = "";
        $scope.modifys = false;// !$scope.modifys;
    }


    $scope.seek = function (value, arrays) {
        for (var ele in arrays) {
            if (arrays[ele].value == value) {
                return arrays[ele].name;
            }
        }
    }

    $scope.login = function () {
        var doctor = {
            doctorName: $scope.doctorName,
            password: $scope.password
        }
        var result = DoctorService.login(doctor, function (result) {
            if (result.isSuccess) {
                $cookieStore.put("doctor", result.description);
                $("#logout").show();
                $("#mypageLink").show();
                $("#reportLink").show();
                $("#reportLink1").show();
                $("#reportLink2").show();
                $("#systemsetting").hide();
                if (result.description.type == CommonService.types[1].name) {
                    $("#doctorLink").hide();
                    $location.path("/truscreen/reports");
                } else if (result.description.type == "系统管理员") {
//		    				$("#mypageLink").hide();
                    $("#reportLink").hide();
                    $("#reportLink1").hide();
                    $("#reportLink2").hide();
                    $("#systemsetting").show();
                    $("#doctorLink").show();
                    $location.path("/truscreen/hospital");
                } else {
                    $("#doctorLink").show();
                    $location.path("/truscreen/hub");//.search({'isSuccess':result.isSuccess});
                }
            } else {
                alert("登录失败，详情： " + result.description);
            }
        }, function (response) {
            alert("Response status:" + response.status + ", 未知错误，请联系Frank Wu(wushexin@gmail.com)");
        });
    }
    $scope.checkDoctor = function (doctor) {
        if (doctor.doctorName == null || doctor.doctorName == "") {
            alert("请输入医生名字");
            return false;
        }
        if (doctor.type == null) {
            alert("请设置医生类型");
            return false;
        }
        if (doctor.status == null) {
            alert("请设置医生状态");
            return false;
        }
        return true;
    }
    $scope.addDoctor = function () {
        var doctor = {
            doctorName: $scope.doctorName,
            password: $scope.password,
            type: $scope.type,
            status: $scope.status,
        }
        if (!$scope.checkDoctor(doctor)) return;
        if ($scope.password == null || $scope.password == "") {
            alert("请设置密码");
            return false;
        }
        DoctorService.addDoctor(doctor, function (result) {
            if (result.isSuccess) {
                alert("成功添加医生");
                $scope.cancle();
                $scope.getDoctorList();
            } else {
                alert("添加医生失败，详情： " + result.description);
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });

    }
    $scope.deleteDoctor = function (doctor) {
        var r = confirm("确定删除该医生吗?");
        if (r == true) {
            DoctorService.deleteDoctor(doctor.doctorId, function (result) {
                if (result.isSuccess) {
                    alert("成功除该医生");
                    $scope.getDoctorList();
                } else {
                    alert("删除医生失败，详情： " + result.description);
                }
            }, function (response) {
                $scope.unauthorizedCallBack(response);
            });
        }
    }
    $scope.getDoctorList = function () {
        DoctorService.getDoctorList(function (result) {
            if (result != null) {
//		    		console.log(result);
                $scope.doctors = result;
            } else {
                alert("Get doctor list is null");
            }
        }, function (response) {
//	    		 $scope.unauthorizedCallBack(response);
            console.log(response);// just log it
        });

    };

//	    $scope.$watch('confirmed1', function(newValue, oldValue) {
//	        console.log("old value: %s and new value: %s", oldValue, newValue);
    $scope.getDoctorList();
//	    });
    $scope.modify = function (doctor) {
        $scope.doctorName = doctor.doctorName;
        $scope.password = null;//doctor.password;
        $scope.type = doctor.type;
        $scope.status = doctor.status;
        $scope.createDate = doctor.createDate;

        $scope.doctorId = doctor.doctorId;
        $scope.modifys = !$scope.modifys;

        $("#type").val(doctor.type);
        $("#status").val(doctor.status);
        if (doctor.type == "系统管理员") {
            $("#type4System").html(doctor.type);
//				$("#type").attr("disabled","disabled");
        }
    }
    $scope.updateDoctor = function () {
        var doctor = {
            doctorId: $scope.doctorId,
            doctorName: $scope.doctorName,
            password: $scope.password,
            type: $scope.type,
            status: $scope.status,
            createDate: $scope.createDate,

        }
        if (!$scope.checkDoctor(doctor)) return;
        DoctorService.updateDoctor($scope.doctorId, doctor, function (result) {
            if (result.isSuccess) {
                alert("更新医生成功");
                $scope.cancle();
                $scope.getDoctorList();
                // update current user
                var doctor = $cookieStore.get("doctor");
                if (doctor != null) {
                    HospitalService.getDoctor(doctor.doctorId, function (result) {
                        if (result != null) {
                            $scope.currentDoctor = result;
                        }
                    }, function (response) {
                        $scope.unauthorizedCallBack(response);
                    });
                }
            } else {
                alert("更新医生失败，详情： " + result.description);
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }

    $scope.formatTime = function (time) {
        if (time == null) return null;
        return $filter('date')(new Date(time), 'yyyy-MM-dd HH:mm:ss');
    }


    $scope.formatDate = function (time) {
        if (time == null) return null;
        return $filter('date')(new Date(time), 'yyyy-MM-dd');
    }

    $scope.isSelected = function (v1, type, v2) {
        var r = "";
        if ("type" == type)
            r = (v1 == v2.type ? "selected" : "");
        else
            r = ( v1 == v2.status ? "" : "selected");
        document.write(r);
    }


    $scope.records;
    $scope.date1;
    $scope.date2;
    $scope.logDoctor;//the operation made by he
    $scope.getRecords = function () {
        if ($scope.date1 != null && $scope.date2 != null) {
            if ($scope.date1 > $scope.date2) {
                alert("请设置正确的日期区间");
                return;
            }
        }

        if ($scope.logDoctor == null || $scope.logDoctor == "") {
            var date = {
                "date1": $scope.date1,
                "date2": $scope.date2,
                "methodName": $scope.methodName,
                "onlyMyself": true
            }
        } else {
            var date = {
                "date1": $scope.date1,
                "date2": $scope.date2,
                "methodName": $scope.methodName,
                "onlyMyself": false,
                "doctorName": $scope.logDoctor
            }
        }
        RecordService.getRecords(date, function (result) {
            if (result.isSuccess) {
                $scope.records = result.recordList;
            } else {
                alert("获取日志失败，详情： " + result.description);
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }
    $scope.date3;
    $scope.date4;
    $scope.statisticResult;
    $scope.doctorNames;
    $scope.statistic = function () {
        if ($scope.date3 != null && $scope.date4 != null) {
            if ($scope.date3 > $scope.date4) {
                alert("请设置正确的日期区间");
                return;
            }
        }
        var data = {
            "date3": $scope.date3,
            "date4": $scope.date4,
            "doctorName": $scope.doctorNames,
        }
        RecordService.statistic(data, function (result) {
            if (result.isSuccess) {
                $scope.statisticResult = result.statisticResult;
            } else {
                alert("获取日志失败，详情： " + result.description);
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }

    $scope.percent = function () {
        var re = 0;
        if ($scope.statisticResult == null) {
            re = 0;
        } else if ($scope.statisticResult.normal == 0) {
            re = 0;
        } else {
            re = $scope.statisticResult.normal / ($scope.statisticResult.normal + $scope.statisticResult.exception);
            re = (re * 100);
        }
        return re.toPrecision(4).toString() + "%";
    }

};


ReportController = function ($scope, $routeParams, $location, $filter, $http, DoctorService, $cookieStore, CommonService, ReportService, $compile, HospitalService, HHDService, OpenApiService, $timeout) {

    $scope.commands = ["current", "socket_status", "system_report", "terminate", "connect", "login", "exit"];
    $scope.command = "current";
    $scope.hhdStatus = "INIT";
    $scope.executeCommand = function () {
        HHDService.executeHHDCommand($scope.command, function (result) {
            $scope.hhdStatus = result.description;
        }, function (error) {
            console.log(error)
            $scope.hhdStatus = "ERROR";
        });
    }


    $scope.lcts = ["无","CA","NILM", "ASCUS", "LSIL", "HSIL", "ASC-H", "AGC"];
    $scope.hpvs = ["16+", "18+", "31+", "33+", "52+", "58+", "其他高危型+", "其他低危型", "阴性", "无"];

    if ($cookieStore.get("doctor") == null) {
        //$("#logout").show();
    } else {
        var doctor = $cookieStore.get("doctor");
        if (doctor.type != CommonService.types[1].name) {// not normal doctor
            $("#doctorLink").show();
            $("#createbtn").css("margin-left", "570px");
        }
        $("#mypageLink").show();
        $("#reportLink2").show();
        $("#reportLink1").show();
        $("#gotohub").show();

        $("#logout").show();
//		$scope.getPageNumber();
        if (doctor.type == "系统管理员") {
            $("#reportLink").hide();
            $("#systemsetting").show();
        }
    }

    $scope.doctor = $cookieStore.get("doctor");
    $scope.hospital;
    $scope.showLogo = false;
    $scope.pnormalThreshold = function ()  {
        //hpv 和 lct 都为🈳️空 默认 0.5
        if(!$scope.report.hpv && !$scope.report.lct){
            if($scope.report.erosion){
                return 0.3;
            }
            return 0.5;
        }

        //lCT  未检测时 下列值默认0.6
        if(!$scope.report.lct){
            if( $scope.report.hpv.includes("16+")  || $scope.report.hpv.includes("18+") || $scope.report.hpv.includes("31+")
                || $scope.report.hpv.includes("33+")  || $scope.report.hpv.includes("52+")  || $scope.report.hpv.includes("58+")){
                return 0.6;
            }
            if($scope.report.hpv.includes("其他高危型+")){
                return 0.5;
            }
            if($scope.report.hpv.includes("阴性")){
                return 0.3;
            }

        }

        if($scope.report.lct  =="CA" && !$scope.report.hpv){
            return 1;
        }


        //["NILM", "ASCUS", "LSIL", "HSIL", "ASC-H", "SCC", "AGC"]
        if($scope.report.lct  =="ASC-H" || $scope.report.lct ==  "HSIL" || $scope.report.lct ==  "AGC" ){
            //HPV 未检测时 默认 0.9
            if(!$scope.report.hpv){
                return 0.9;
            }
            // $scope.hpvs = ["阴性", "16+","18+","其他高危型+"];
            if( $scope.report.hpv.includes("16+")  || $scope.report.hpv.includes("18+") || $scope.report.hpv.includes("31+")
                || $scope.report.hpv.includes("33+")  || $scope.report.hpv.includes("52+")  || $scope.report.hpv.includes("58+")){
                return 1;
            }
            if( $scope.report.hpv.length == 1 &&  $scope.report.hpv[0] == "其他高危型+" ){
                return 1;
            }
            if( $scope.report.hpv.length == 1 &&  $scope.report.hpv[0] == "阴性" ){
                return 0.8;
            }

        }
        if($scope.report.lct  =="LSIL" || $scope.report.lct ==  "ASCUS"){
            if(!$scope.report.hpv){
                return 0.5;
            }
            // $scope.hpvs = ["阴性", "16+","18+","其他高危型+"];
            if( $scope.report.hpv.includes("16+")  || $scope.report.hpv.includes("18+") || $scope.report.hpv.includes("31+")
                || $scope.report.hpv.includes("33+")  || $scope.report.hpv.includes("52+")  || $scope.report.hpv.includes("58+")){
                return 0.65;
            }
            if( $scope.report.hpv.length == 1 &&  $scope.report.hpv[0] == "其他高危型+" ){
                return 0.6;
            }
            if( $scope.report.hpv.length == 1 &&  $scope.report.hpv[0] == "阴性" ){
                return 0.3;
            }
        }

        if($scope.report.lct  =="NILM"){
            if(!$scope.report.hpv){
                return 0.4;
            }
            // $scope.hpvs = ["阴性", "16+","18+","其他高危型+"];
            if( $scope.report.hpv.includes("16+")  || $scope.report.hpv.includes("18+") || $scope.report.hpv.includes("31+")
                || $scope.report.hpv.includes("33+")  || $scope.report.hpv.includes("52+")  || $scope.report.hpv.includes("58+")){
                return 0.6;
            }
            if( $scope.report.hpv.length == 1 &&  $scope.report.hpv[0] == "其他高危型+" ){
                return 0.5;
            }
            if( $scope.report.hpv.length == 1 &&  $scope.report.hpv[0] == "阴性" ){
                return 0;
            }
        }

        //柱状上皮易位 0.3
        if($scope.report.erosion){
            return 0.3;
        }

        return 0.5;
    }
    $scope.getPNorm = function () {
        if (!$scope.report) {
            return;
        }
        var uid = $scope.report.uid;
        if (!uid) {
            // alert("请输入UID");
            return;
        }
        if (uid.length < 6) {
            return;
        }

        if ($scope.report.hpv) {
            if ($scope.report.hpv instanceof Array && $scope.report.hpv.length > 1 && $scope.report.hpv.includes("阴性")) {
                alert("HPV 阴性和其他配置冲突");
                return;
            }
        }

        // ReportService.getPNorm(uid, function (response) {
        //         if (response.isSuccess) {
        if ($scope.report.visableCancer) {
            $scope.report.checkResult = "异常"
            $scope.doesCheckCompleted = true;
            $scope.report.isComplete = '完成';
        } else {
            // $scope.report.pnorValueResult = response.description;
            //非孕妇
            if (!$scope.report.pregnancyStatus) {
                if ($scope.report.pnorValueResult >= $scope.pnormalThreshold()) {
                    $scope.report.checkResult = "正常"
                } else {
                    $scope.report.checkResult = "异常"
                }
            } else {
                //孕妇
                if ($scope.report.pnorValueResult >= 0.25) {
                    $scope.report.checkResult = "正常"
                } else {
                    $scope.report.checkResult = "异常"
                }
            }
            $scope.isNormal();
            $scope.doesCheckCompleted = true;
            $scope.report.isComplete = '完成';
        }

        $scope.isVisableCancer();
        //     }
        //     else {
        //         alert(response.description);
        //     }
        // }, function (response) {
        //     console.log(response);// just log it
        // }
        // )
    }

    /**
     * 当hhd设备未能产出结果时 ， 手动填入7位数字，前6位是uid，第七位是检测结果 1 表示 正常 ， 0 表示异常
     */
    $scope.getPNormManual = function () {
        if (!$scope.report) {
            return;
        }
        var uid = $scope.report.uid;
        if (!uid) {
            // alert("请输入UID");
            return;
        }
        if (uid.length < 6) {
            return;
        }

        // if (uid.length != 7) {
        //     //alert("uid长度不为7位");
        //     return;
        // }


        //第七位 0 为 失败 1 为成功
        // var result = uid.substr(6, 1);
        //
        // if (result === '1') {
        //     $scope.report.isComplete = '完成';
        //     $scope.doesCheckCompleted = true;
        //     $scope.report.checkResult = "异常"
        //
        // } else if (result === '0') {
        //     $scope.report.isComplete = '完成';
        //     $scope.doesCheckCompleted = true;
        //     $scope.report.checkResult = "正常"
        //
        // } else {
        //     // alert("uid格式不对");
        // }

        //
        if (uid.length >= 7) {
            //第七位 0 为 失败 1 为成功
            var result = uid.substr(6, 1);

            if (result === '1') {
                $scope.report.isComplete = '完成';
                $scope.doesCheckCompleted = true;
                $scope.report.checkResult = "异常"

            } else if (result === '0') {
                $scope.report.isComplete = '完成';
                $scope.doesCheckCompleted = true;
                $scope.report.checkResult = "正常"

            } 
        }

        var clinical = $scope.report.otherClinical ;
        if(clinical != null){
            if(clinical.endWith("无.")){
                $scope.report.isComplete = '完成';
                $scope.doesCheckCompleted = true;
                $scope.report.checkResult = "正常"
            }

            if(clinical.endWith("/.")){
                $scope.report.isComplete = '完成';
                $scope.doesCheckCompleted = true;
                $scope.report.checkResult = "异常"
            }
        }
    }


    $scope.getPNormPregenancy = function () {
        if (!$scope.report) {
            return;
        }
        var uid = $scope.report.uid;
        if (!uid) {
            // alert("请输入UID");
            return;
        }
        if (uid.length < 6) {
            return;
        }

        if ($scope.report.hpv) {
            if ($scope.report.hpv instanceof Array && $scope.report.hpv.length > 1 && $scope.report.hpv.includes("阴性")) {
                alert("HPV 阴性和其他配置冲突");
                return;
            }
        }

        // ReportService.getPNorm(uid, function (response) {
        //         if (response.isSuccess) {
        if ($scope.report.visableCancer) {
            $scope.report.checkResult = "异常"
            $scope.doesCheckCompleted = true;
            $scope.report.isComplete = '完成';
        } else {
            // $scope.report.pnorValueResult = response.description;
            //非孕妇
            if (!$scope.report.pregnancyStatus) {
                if ($scope.report.pnorValueResult >= 0.5) {
                    $scope.report.checkResult = "正常"
                } else {
                    $scope.report.checkResult = "异常"
                }
            } else {
                //孕妇
                if ($scope.report.pnorValueResult >= 0.25) {
                    $scope.report.checkResult = "正常"
                } else {
                    $scope.report.checkResult = "异常"
                }
            }
            $scope.isNormal();
            $scope.doesCheckCompleted = true;
            $scope.report.isComplete = '完成';
        }
        //         }
        //         else {
        //             alert(response.description);
        //         }
        //     }, function (response) {
        //         console.log(response);// just log it
        //     }
        // )
    }

    $scope.isVisableCancer = function () {
        var report = $scope.report;
        if (!report) {
            return;
        }

        if (report.age >= 35) {
            if (report.uid && report.uid.length >= 6) {
                //可见癌 不规则流血 可疑癌 接触性流血
                if (Boolean(report.visableCancer) || Boolean(report.isCancer) || $scope.getBoolean(report.touchbleeding)) {
                    $scope.report.checkResult = "异常";
                }
                // else{
                //      $scope.report.checkResult = "正常";
                // }
            } else {
                $scope.report.checkResult = null;
            }
        } else {
            if (report.uid && report.uid.length >= 6) {
                if (Boolean(report.visableCancer) || Boolean(report.isCancer)) {
                    $scope.report.checkResult = "异常";
                }
                // else{
                //     $scope.report.checkResult = "正常";
                // }
            } else {
                $scope.report.checkResult = null;
            }
        }

        $scope.getPNormManual();
    }

    $scope.getPatientInfo = function () {
        var patientName = "defaultName";
        var outpatientNo = "-2";
        var admissionNo = "-2";
        if ($scope.report && $scope.report.patientName) {
            patientName = $scope.report.patientName;
        }

        if ($scope.report && $scope.report.outpatientNo) {
            outpatientNo = $scope.report.outpatientNo;
        }

        if ($scope.report && $scope.report.admissionNo) {
            admissionNo = $scope.report.admissionNo;
        }

        ReportService.getPatientInfo(patientName, outpatientNo, admissionNo, function (result) {
            console.log(result);// just log it

            if(result.description.patientName){
                $scope.report = result.description;
            }
        }, function (response) {
            console.log(response);// just log it
        });
    }

    $scope.getHospital = function () {
        HospitalService.getHospital($scope.hospital, function (result) {
            if (result != null) {
                $scope.hospital = result;
                if (result.hospitalLogo == null || result.hospitalLogo == "") {
                    $scope.showLogo = false;
                } else {
                    $scope.showLogo = true;
                }
            } else {
                //alert("删除报告单失败，详情： "+result.description);
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }

    $scope.getHospital();
    $scope.applyDoctorList = [];
    DoctorService.getDoctorList(function (result) {
        if (result != null) {
            var j = 0;
            for (var i = 0; i < result.length; i++) {
                if (result[i].type == '申请医生')
                    $scope.applyDoctorList[j++] = result[i].doctorName;
            }
            ;

        } else {
            alert("Get doctor list is null");
        }
    }, function (response) {
        console.log(response);// just log it
    });

    $scope.newCreateReport = false;
    $scope.newCreate = function () {
        $scope.report = null;//也可以赋值 比如 $scope.report = {'key1':'vaule1'}
        $scope.doesCheckCompleted = true;
        $scope.newCreateReport = !$scope.newCreateReport;
    }


// $scope.report = {'hpv':'vaule1'}
// $scope.onChangeOption = function(){
// 	document.getElementById("inputSelect").value=$scope.report.hpv;
// 	console.log(document.getElementById("inputSelect").value);
// }

    $scope.checkReport = function () {
        if ($scope.report == null || $scope.report.patientName == null || $scope.report.caseNumber == null || $scope.report.patientName == "" || $scope.report.caseNumber == "") {
            alert("报告单中的姓名、病历号等信息不能够为空！");
            return false;
        }
        if ($scope.report.department == null || $scope.report.department == "") {
            $scope.report.department = $scope.hospital.department;
            //alert("请输入报告单中的科室字段");
            //return false;
        }
        if ($scope.getComplaints($scope.report) == "") {
            alert("请填写报告单主诉信息");
            return false;
        }

        if ($scope.getClinical($scope.report) == "") {
            alert("请填写报告单临床表现信息");
            return false;
        }

        if ($scope.report.isComplete == null || $scope.report.isComplete == "") {
            alert("请设置检查是否完成；");
            return false;
        }
        if ($scope.report.isComplete == "完成") {
            // if($scope.report.pointNumber == null || $scope.report.pointNumber == ""){
            // 	alert("请填写报告单点探数！");
            // 	return false;
            // }
            // if($scope.report.pointNumber <15 || $scope.report.pointNumber > 32){
            // 	alert("点探数必须在是15至32间的数值；");return false;
            // }
            if ($scope.report.checkResult == null || $scope.report.checkResult == "") {
                alert("请填写报告单中检查结果字段！");
                return false;
            }
        } else {
            if ($scope.report.reason4doesNotComplete == null || $scope.report.reason4doesNotComplete == "") {
                alert("请填写未完成失败原因");
                return false;
            }
        }


        return true;
    }
    $scope.intervalId = null;
    $scope.saveReport = function (doNotCheck) {
        if (!doNotCheck && !$scope.checkReport()) return false;
        console.log($scope.report);
        if ($scope.report.hpv instanceof Array) {
            $scope.report.hpv = $scope.report.hpv.join(',');
        }
        ReportService.addReport($scope.report, function (result) {
            console.log("the result of add report : " + JSON.stringify(result));

            if (result.isSuccess) {
                if (!doNotCheck) {
                    $scope.newCreateReport = false;
                    $scope.report = null;
                    $scope.stashDone = false;
                    $scope.getTopPage(1);
                }
                $scope.report.reportId = result.description;

                $scope.intervalId = $timeout($scope.getDetail, 5000);

                alert("新建报告单成功");
            } else {
                alert("新建报告单败，详情： " + result.description);
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });

    }
    $scope.cancleReport = function () {
        $scope.newCreateReport = false;
        $scope.report = null;
    }

// $scope.report = null;

    $scope.unauthorizedCallBack = function (response) {
        $("#gotohub").hide();
        if (response.status == 401) {
            alert("登陆已超时，请重新登陆。");
            $cookieStore.put("doctor", null);
            $location.path("/truscreen/login");
        } else {
            alert("未知系统错误，请重新登陆");
            $cookieStore.put("doctor", null);
            $location.path("/truscreen/login");
        }
    }

    $scope.getComplaints = function (report) {
        var complaints = "";
        if (Boolean(report.isLeucorrhea)) {
            complaints += "白带多/";
        }
        if (Boolean(report.isBleed)) {
            complaints += "性交出血/";
        }
        report.unregularBleed = $scope.getBoolean(report.unregularBleed);
        if (Boolean(report.unregularBleed)) {
            complaints += "不规则流血/";
        }
        if (report.otherComplaints != null) {
            complaints += report.otherComplaints;
        }
        return $scope.spliceString(complaints);
    }

    $scope.getClinical = function (report) {
        var clinical = "";
        if (Boolean(report.isSmooth)) {
            clinical += "光滑/";
        }
        if (Boolean(report.isAcuteInflammation)) {
            clinical += "慢性炎症/";
        }
        if (Boolean(report.isHypertrophy)) {
            clinical += "肥大/";
        }
        if (Boolean(report.isPolyp)) {
            clinical += "息肉/";
        }
        if (report.erosion != null) {
            clinical += "柱状上皮异位:" + report.erosion + "/";
        }
        if (Boolean(report.isTear)) {
            clinical += "撕裂/";
        }
        if (Boolean(report.isNesslersGlandCyst)) {
            clinical += "纳氏腺囊肿/";
        }
        if (Boolean(report.isWhite)) {
            clinical += "白斑/";
        }
        if (Boolean(report.isCancer)) {
            clinical += "阴道排液/";
        }

        report.touchbleeding = $scope.getBoolean(report.touchbleeding);
        if (Boolean(report.touchbleeding)) {
            clinical += "接触性出血/";
        }
        if (Boolean(report.visableCancer)) {
            clinical += "肉眼可见病变/";
        }
        if (report.otherClinical != null) {
            clinical += report.otherClinical;
        }
        return $scope.spliceString(clinical);
    }
    $scope.isComplete = function (report) {
        return report.isComplete ? "是" : "否";
    }
    $scope.canBeEdit = false;
    $scope.suggestion = function (report) {
        var suggestion = "";
        if (report.screening) {
            suggestion += "筛查/";
        }
        if (report.checking) {
            suggestion += "检查/";
        }
        if (report.otherSuggestion) {
            suggestion += report.otherSuggestion;
        }

        $scope.doesEditable(report);

        return $scope.spliceString(suggestion);
    }

    $scope.doesEditable = function (report) {
        var doctor = $cookieStore.get("doctor");
        if (doctor.type == CommonService.types[1].name) {// if is a normal doctor
            if (report.doctorId == doctor.doctorId) {
                $scope.canBeEdit = false;
            } else {// normal doctor can't edit other doctor's report
                $scope.canBeEdit = true;
            }
        } else {
            $scope.canBeEdit = false;
        }

    }


    $scope.spliceString = function (str) {
        if (str != null) {
            var length = str.length;
            if (length > 10) {
                return str.substr(0, 7) + "...";
            }
            if (str.charAt(length - 1) == '/') {
                return str.substr(0, length - 1);
            }
        }
        return str;
    }


    $scope.reports = null;

    $scope.maxId = 1;
    $scope.minId = 1;
    $scope.size = 10;
    $scope.sortColumn = "reportId";
    $scope.gopage = 0;
    $scope.currentPage = 0;
    $scope.allRecordNumber = 10;
    $scope.getPrePage = function (pages) {
        $("#showPageTr").css("display", "");
        if ($scope.currentPage <= 1) {
            alert("已到第一页");
            return;
        }
        ReportService.getPrePage($scope.maxId, $scope.size, pages, $scope.sortColumn, function (result) {
            if (result != null) {
                for (var i = 0; i < result.reportList.length; i++) {
                    var rept = result.reportList[i];
                    rept.unregularBleed = 'true' === rept.unregularBleed;
                    rept.touchbleeding = "true" === rept.touchbleeding;
                }
                $scope.reports = result.reportList;
                $scope.maxId = result.maxId;
                $scope.minId = result.minId;
                $scope.allRecordNumber = result.allRecordNumber;
                $scope.currentPage = $scope.currentPage - 1;
                $scope.showPreNextPage();
                $scope.getPageNumber();
            } else {
                alert("获取报告单失败");
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }
    $scope.getCurrentPage = function (pages) {
        $("#showPageTr").css("display", "");
        if ($scope.maxId == null) return;
        ReportService.getCurrentPage($scope.maxId, $scope.size, pages, $scope.sortColumn, function (result) {
            if (result != null) {
                for (var i = 0; i < result.reportList.length; i++) {
                    var rept = result.reportList[i];
                    rept.unregularBleed = 'true' === rept.unregularBleed;
                    rept.touchbleeding = "true" === rept.touchbleeding;
                }
                $scope.reports = result.reportList;
                $scope.maxId = result.maxId;
                $scope.minId = result.minId;
                $scope.allRecordNumber = result.allRecordNumber;
                $scope.showPreNextPage();
                $scope.getPageNumber();
            } else {
                alert("获取报告单失败");
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }

    $scope.pageSizeChange = function () {
        if ($scope.size <= 0) {
            alert("每页显示记录数必须大于等于1，请设置正确的值");
            $scope.size = 1;
            return;
        }
        $scope.getCurrentPage(1);
    }
    $scope.showPreNextPage = function () {
        if ($scope.currentPage <= 1) {
            $("#prepage").css("display", "none");
        } else {
            $("#prepage").css("display", "");
        }
        if ($scope.currentPage == $scope.getPageNumber()) {
            $("#nextpage").css("display", "none");
        } else {
            $("#nextpage").css("display", "");
        }
    }
    $scope.getDetail = function (showDetailFunction) {
        var reportId = $scope.report.reportId;
        ReportService.getDetail(reportId, 1, 10, 'getDetail', function (result) {
            console.log(result);
            if (result.isSuccess) {
                if (result.data.pnorValueResult && result.data.pnorValueResult > 0) {
                    console.log("stop timer");
                    $scope.report = result.data;
                    if (location.href.endsWith("reports_pregnancy")) {
                        $scope.getPNormPregenancy();
                    } else {
                        $scope.getPNorm();
                    }
                    $scope.report.isComplete = '完成';
                    showDetailFunction($scope.report);
                } else {
                    $timeout($scope.getDetail, 15000);
                }
            } else {
                alert(result.data);
            }
        }, function (response) {
            alert("返回错误" + response);
        })
    }

    $scope.getNextPage = function (pages) {
        $("#showPageTr").css("display", "");
        if ($scope.currentPage >= $scope.getPageNumber()) {
            alert("已到最后一页");
            return;
        }
        ReportService.getNextPage($scope.minId, $scope.size, pages, $scope.sortColumn, function (result) {
            for (var i = 0; i < result.reportList.length; i++) {
                var rept = result.reportList[i];
                rept.unregularBleed = 'true' === rept.unregularBleed;
                rept.touchbleeding = "true" === rept.touchbleeding;
            }
            $scope.reports = result.reportList;
            $scope.maxId = result.maxId;
            $scope.minId = result.minId;
            $scope.allRecordNumber = result.allRecordNumber;
            $scope.currentPage = $scope.currentPage + 1;
            $scope.showPreNextPage();
            $scope.getPageNumber();
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }
    $scope.getTopPage = function (pages) {
        $("#showPageTr").css("display", "");
        ReportService.getTopPage($scope.maxId, $scope.size, pages, $scope.sortColumn, function (result) {
            for (var i = 0; i < result.reportList.length; i++) {
                var rept = result.reportList[i];
                rept.unregularBleed = 'true' === rept.unregularBleed;
                rept.touchbleeding = "true" === rept.touchbleeding;
            }
            $scope.reports = result.reportList;
            $scope.maxId = result.maxId;
            $scope.minId = result.minId;
            $scope.allRecordNumber = result.allRecordNumber;
            $scope.currentPage = $scope.currentPage + 1;
            $scope.showPreNextPage();
            $scope.getPageNumber();
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }
    $scope.paginationUI = function () {
        var html = "";
        $("pagination").html(html);
    }
    $scope.getPageNumber = function () {
        if ($scope.allRecordNumber == null || $scope.allRecordNumber == 0) {
            return 0;
        }
        //console.log("$scope.allRecordNumber%$scope.size:"+($scope.allRecordNumber%$scope.size));
        if ($scope.allRecordNumber % $scope.size == 0) {

            return $scope.allRecordNumber / $scope.size;
        }
        return Math.floor($scope.allRecordNumber / $scope.size) + 1;
    }
    $scope.goPage = function () {
        $("#showPageTr").css("display", "");
        if ($scope.gopage != 0) {
            var pages = 1;
            if ($scope.gopage > $scope.getPageNumber()) {
                alert("输入的页码值必须小于最大页数 " + $scope.getPageNumber() + " ，请输入正确的页码值");
                return;
            } else if ($scope.gopage <= 0) {
                alert("输入的页码值必须大于 0 ，请输入正确的页码值");
                return;
            }
            if (Math.abs($scope.gopage - $scope.currentPage) != 0) {
                pages = Math.abs($scope.gopage - $scope.currentPage);
            }
        }
        if ($scope.currentPage > $scope.gopage) {
            $scope.getPrePage(pages);
            $scope.currentPage = $scope.gopage + 1;
        } else if ($scope.currentPage == $scope.gopage) {
            return;
        } else {
            $scope.getNextPage(pages);
            $scope.currentPage = $scope.gopage - 1;
        }
//		$scope.currentPage = $scope.gopage;
    }

    $scope.modifyFlag = false;
    $scope.modify = function (rep, $event) {
        $scope.modifyFlag = true;
        $scope.report = rep;
        $scope.modifyUnderDetails = false;
        $scope.readonly = false;
        $scope.disabled = false;

        if ($scope.report.hpv == null) {
            $scope.report.hpv = [];
        } else {
            if (!($scope.report.hpv instanceof Array)) {
                $scope.report.hpv = $scope.report.hpv.split(',');
            }
        }

        $scope.doesCheckComplete();
        $scope.isNormal();
        $scope.report.lastTimeMenstruation = $scope.formatDate($scope.report.lastTimeMenstruation);
        $scope.checkDate4Detial = $scope.formatTime($scope.report.checkDate);
        $scope.modifyDate4Detial = $scope.formatTime($scope.report.modifyDate);
        $scope.doesCheckComplete();
        $scope.isAvaiableWhileModify();


        $compile($("#midfyReport").contents())($scope);

        // var ele =  $("#midfyReport .abcToDel").contents();
        // console.log(ele.html())

        // for(var i = 0; i < ele.length; i++){
        //     if(i > 1 && i < ele.length-1){
        //         ele[i].remove();
        //     }
        // }


        if ($event != null) {
            $event.stopPropagation();
        }
    }
    $scope.cancleModify = function () {
        $scope.modifyFlag = false;
        $scope.readonly = true;
        $scope.disabled = true;
        $scope.modifyUnderDetails = false;
        $scope.closeDetails();
    }
    $scope.updateReport = function ($event) {
        if (!$scope.checkReport()) return false;
        if ($scope.report.hpv && $scope.report.hpv instanceof Array) {
            $scope.report.hpv = $scope.report.hpv.join(',');
        }
        ReportService.updateReport($scope.report.reportId, $scope.report, function (result) {
            if (result.isSuccess) {
                if (!$scope.report.reportId) {
                    return;
                }
                alert("修改报告单成功");
                //update locoal updated report value
                for (var ele in $scope.reports) {
                    if ($scope.reports[ele].reportId == $scope.report.reportId) {
                        $scope.reports[ele] = $scope.report;
                        break;
                    }
                }
                // $scope.report = null;
                $scope.cancleModify();
            } else {
                alert("修改报告单失败，详情： " + result.description);
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
        if ($event != null) {
            $event.stopPropagation();
        }
    }
    $scope.deleteReport = function (rep, $event) {
        var r = confirm("确实删除该报告单吗？");
        if (r == true) {
            ReportService.deleteReport(rep.reportId, function (result) {
                if (result.isSuccess) {
                    $scope.closeDetails();
                    //UI界面需要删除这行数据
                    for (var ele in $scope.reports) {
                        if ($scope.reports[ele].reportId == rep.reportId) {
                            $scope.reports.splice(ele, 1);
                        }
                    }
                    alert("删除报告单成功");
                } else {
                    alert("删除报告单失败，详情： " + result.description);
                }
            }, function (response) {
                $scope.unauthorizedCallBack(response);
            });
        }
        if ($event != null) {
            $event.stopPropagation();
        }
    }
    $scope.checkDatePrint;
    $scope.lastTimeMenstruationPrint;
    $scope.printReport = function (rep, $event) {

        $scope.report = rep;

        var uid = rep.uid;
        if (!(uid == null)) {
            if (uid.length == 7) {
                $scope.report.uid = uid.substr(0, 6);
            } else {
                $scope.report.uid = uid.substr(uid.length - 6, 6);
            }
        }


        $scope.doesCheckComplete();
        $scope.checkDatePrint = $scope.formatChineseDate(rep.checkDate);
        $scope.lastTimeMenstruationPrint = $scope.formatChineseDate(rep.lastTimeMenstruation);
        var window = $('#printframe').get(0).contentWindow;
        // window.location.href =  window.location.href+"_"+rep.reportId;
        var doc = window.document;
        doc.title = rep.reportId;

        if ($scope.report.hpv) {
            if (!($scope.report.hpv instanceof Array)) {
                $scope.report.hpv = $scope.report.hpv.split(',');
            }
        }

        if($scope.report.pregnancyNumber == 0 ){
            $scope.report.pregnancyNumber = "-";
        }
        if($scope.report.childbirthNumber == 0 ){
            $scope.report.childbirthNumber = "-";
        }




        var $body = $('body', doc);
        var prefix = '<div style="width:1050px;margin:auto;">';
        var suffix = "<div>";
        var modifyHtml = $("#printdiv").html();
        var html = prefix + modifyHtml + suffix;
        $body.html(html);
        $compile($("#printframe").contents())($scope);
        $scope.doesCheckComplete();
        setTimeout(function () {
            $('#printframe').get(0).contentWindow.focus();
            $('#printframe').get(0).contentWindow.print();
        }, 1000);

        if ($event != null) {
            $event.stopPropagation();
        }
        return false;
    }
    $scope.formatTime = function (time) {
        if (time == null) return null;
        return $filter('date')(new Date(time), 'yyyy-MM-dd HH:mm:ss');//date.toLocaleString();
    }
    $scope.formatDate = function (time) {
        if (time == null) return null;
        return $filter('date')(new Date(time), 'yyyy-MM-dd');
    }
    $scope.formatChineseDate = function (time) {
        if (time == null) return null;
        return $filter('date')(new Date(time), 'yyyy-MM-dd');
    }
    $scope.searchText;

    $scope.isNormal = function () {
        console.log($scope.report.checkResult);
        if ($scope.report.checkResult == "正常") {
            $(".normal").css("display", "");
            $(".exception").css("display", "none");
        } else {
            $(".normal").css("display", "none");
            $(".exception").css("display", "");
        }
    }

    $scope.getBoolean = function (value) {
        var type = typeof value;
        if (type === 'boolean') {
            return Boolean(value);
        } else {
            return value === 'true';
        }
    }
    $scope.doesCheckCompleted = false;
    $scope.doesCheckComplete = function () {
        if ($scope.report.isComplete == "undefined" || $scope.report.isComplete == null) {
            $scope.doesCheckCompleted = false;
        } else if ($scope.report.isComplete == "未完成") {
            $scope.doesCheckCompleted = false;
            // $scope.report.checkResult = '';
        } else {
            $scope.doesCheckCompleted = true;
            // $scope.checkResult = '';
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
    $scope.details = function ($index, report, element) {
        $scope.readonly = true;
        $scope.disabled = true;
        $scope.report = report;
        report.unregularBleed = $scope.getBoolean(report.unregularBleed);
        report.touchbleeding = $scope.getBoolean(report.touchbleeding);

        $scope.checkDate4Detial = $scope.formatTime($scope.report.checkDate);
        $scope.modifyDate4Detial = $scope.formatTime($scope.report.modifyDate);

        var doctor = $cookieStore.get("doctor");
        if (doctor.type == CommonService.types[1].name) {// if is a normal doctor
            if (report.doctorId == doctor.doctorId) {
                $scope.disabledModify = false;
                $scope.disabledDelete = false;
            } else {// normal doctor can't edit other doctor's report
                $scope.disabledModify = true;
                $scope.disabledDelete = true;
            }
        } else {
            $scope.disabledModify = false;
            $scope.disabledDelete = false;
        }

        if ($("#details") == null || $("#details").text() == "") {

        } else {
            $("#details").remove();
            if ($scope.lastIndex == $index) return;
        }
        $scope.lastIndex = $index;
        var ele = "#reportListTable tr:eq(" + ($index + 1) + ")";
        var modifyHtml = $("#midfyReport").html();
        var prefix = "<tr id='details'><td width='100%' colspan='14' style='text-align:left'>";
        var suffix = "</td></tr>";
//		var middle = '{{report.age}}<input type="button" ng-click="test1()" /><input type="text" ng-model="report.age"/><input type="date" value="{{formatDate(report.lastTimeMenstruation)}}"/><input type="checkbox" ng-model="report.isCompleted" />';
        var html = prefix + modifyHtml + suffix;


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

    $scope.isAvaiableWhileModify = function () {
        if ($scope.doesCheckCompleted) {
            $scope.isDisableWhileModify = $scope.disabled;
            $scope.isReadonlyWhileModify = $scope.readonly;
        } else {
            $scope.isDisableWhileModify = true;
            $scope.isReadonlyWhileModify = true;
        }
    }

    $scope.readonly = true;
    $scope.disabled = true;
    $scope.modifyUnderDetails = false;

    $scope.activeUpdateReport = function () {
        $scope.readonly = false;
        $scope.disabled = false;
        $scope.modifyUnderDetails = true;
        $scope.isAvaiableWhileModify();
    }

    $scope.closeDetails = function () {
        $scope.modifyUnderDetails = false;
        if ($("#details") == null || $("#details").text() == "") {

        } else {
            $("#details").remove();
        }
        $scope.report = null;
    }


    $scope.showAdvance = false;
    $scope.openAdvanceSearch = function () {
        $scope.showAdvance = true;
    }
    $scope.search;// the advance serach object

    $scope.advanceSearch = function () {
        if ($scope.search == null) {
            alert("请输入查询条件");
            return;
        }
        if ($scope.search.age != null && $scope.search.age2 != null) {
            if ($scope.search.age2 < $scope.search.age) {
                alert("请输入正确的年龄区间");
                $("#age").focus();
                return;
            }
        }

        if ($scope.search.lastTimeMenstruation != null && $scope.search.lastTimeMenstruation2 != null) {
            if ($scope.search.lastTimeMenstruation > $scope.search.lastTimeMenstruation2) {
                alert("请输入正确的末次月经区间");
                $("#lastTimeMenstruation").focus();
                return;
            }
        }

        if ($scope.search.checkDate != null && $scope.search.checkDate2 != null) {
            if ($scope.search.checkDate > $scope.search.checkDate2) {
                alert("请输入正确的检查日期区间");
                $("#lastTimeMenstruation").focus();
                return;
            }
        }
        ReportService.advanceSearch($scope.search, function (result) {
            if (result.isSuccess) {
                for (var i = 0; i < result.reportList.length; i++) {
                    var rept = result.reportList[i];
                    rept.unregularBleed = 'true' === rept.unregularBleed;
                    rept.touchbleeding = "true" === rept.touchbleeding;
                }
                $scope.reports = result.reportList;
                $scope.cancelAdvanceSearch();
                $("#showPageTr").css("display", "none");
                $('#showResultSize').css("display", "");
            } else {
                alert("搜索报告单失败，详情： " + result.description);
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }
    $scope.cancelAdvanceSearch = function () {
        $scope.showAdvance = false;
        $scope.search = null;
        $("#showPageTr").css("display", "");
        $('#showResultSize').css("display", "none");
    }

    $scope.stash = function () {
        var report = $scope.report;
        if (report == null) {
            alert("报告单不完整,请先填写完整的报告单");
            return;
        }

        //report.outpatientNo && report.admissionNo &&
        var isFull = report.patientName;
        if (!isFull) {
            alert("报告单不完整,请先填写完整的报告单");
            return;
        }


        if ($scope.report.hpv instanceof Array && $scope.report.hpv.length > 1 && $scope.report.hpv.includes("阴性")) {
            alert("HPV 阴性和其他配置冲突");
            return;
        }

        if ($scope.report == null || $scope.report.patientName == null || $scope.report.caseNumber == null || $scope.report.patientName == "" || $scope.report.caseNumber == "") {
            alert("报告单中的姓名、病历号等信息不能够为空！");
            return false;
        }
        if ($scope.report.department == null || $scope.report.department == "") {
            $scope.report.department = $scope.hospital.department;
            //alert("请输入报告单中的科室字段");
            //return false;
        }
        if ($scope.getComplaints($scope.report) == "") {
            alert("请填写报告单主诉信息");
            return;
        }

        if ($scope.getClinical($scope.report) == "") {
            alert("请填写报告单临床表现信息");
            return false;
        }


        var r = confirm("点击录入完成，以上材料不允许变更，确认吗？");
        if (r == true) {
            $scope.stashDone = true;

            $scope.saveReport(true);
        }
    }

    $("#reportLink2").hide();
    $("#reportLink1").hide();

    console.log("refer:" + $cookieStore.get("refer"));
    if ("hub" == $cookieStore.get("refer")) {
        $scope.newCreate();
        $cookieStore.put("refer", null);
        $("#createbtn").show();
    } else if ("search" == $cookieStore.get("refer")) {
        $("#createbtn").hide();
    }


    $scope.getQueryVariable = function (variable) {
        var query = window.location.href.substring(location.href.indexOf("?") + 1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            if (pair[0] == variable) {
                return pair[1];
            }
        }
        return (false);
    }


    $scope.showDetail = function (rep, $event) {

        $scope.report = rep;

        var uid = rep.uid;
        if (!(uid == null)) {
            if (uid.length == 7) {
                $scope.report.uid = uid.substr(0, 6);
            } else {
                $scope.report.uid = uid.substr(uid.length - 6, 6);
            }
        }


        $scope.doesCheckComplete();
        $scope.report.checkDate = $scope.formatChineseDate($scope.report.checkDate);


        $scope.lastTimeMenstruationPrint = $scope.formatChineseDate(rep.lastTimeMenstruation);
        // var window = $('#printframe').get(0).contentWindow;
        // window.location.href =  window.location.href+"_"+rep.reportId;
        // var doc = window.document;
        // doc.title = rep.reportId;

        if ($scope.report.hpv) {
            if (!($scope.report.hpv instanceof Array)) {
                $scope.report.hpv = $scope.report.hpv.split(',');
            }
        }


        $scope.report.unregularBleed = 'true' === $scope.report.unregularBleed;
        $scope.report.touchbleeding = "true" === $scope.report.touchbleeding;

        // var $body = $('body', doc);
        // var prefix = '<div style="width:1050px;margin:auto;">';
        // var suffix = "<div>";
        // var modifyHtml = $("#printdiv").html();
        // var html = prefix + modifyHtml + suffix;
        // $body.html(html);
        // $compile($("#printframe").contents())($scope);
        $scope.doesCheckComplete();
        // setTimeout(function () {
        //     $('#printframe').get(0).contentWindow.focus();
        //      // $('#printframe').get(0).contentWindow.print();
        // }, 1000);

        // if ($event != null) {
        //     $event.stopPropagation();
        // }
        return false;
    }

    $scope.getReportDetail = function (no, showDetailFunction) {
        OpenApiService.getReport(no, function (result) {
            console.log(result);
            if (result.isSuccess) {
                $scope.report = result.report;
                showDetailFunction($scope.report);
            } else {
                alert(result.description);
            }
        }, function (response) {
            alert("返回错误" + response);
        })
    }

    if (location.href.indexOf("detail") >= 0) {

        $("#navigation").hide();
        if (!$scope.report) {
            $scope.report = {};
        }

        var no = $scope.getQueryVariable('no');
        $scope.getReportDetail(no, $scope.showDetail);
    }else{
        //init page
        $scope.getTopPage(1);
    }


}
;
HospitalController = function ($scope, $location, HospitalService, $locale, $cookieStore, $http, CommonService) {
    if ($cookieStore.get("doctor") == null) {
        $("#logout").show();
    } else if (location.href.endsWith("hub")) {
        $("#logout").hide();
        $("#doctorLink").hide();
        $("#gotohub").hide();
        $("#mypageLink").hide();
        $("#systemsetting").hide();

        var doctor = $cookieStore.get("doctor");
        if (doctor.type == CommonService.types[0].name) {
            $("#doctorHub").show();
        } else {
            $("#doctorHub").hide();
        }

    } else {

        var doctor = $cookieStore.get("doctor");
        if (doctor.type != CommonService.types[1].name) {
            $("#doctorLink").show();
        }
        $("#reportLink").show();
        $("#mypageLink").show();
        $("#logout").show();
        if (doctor.type == "系统管理员") {
            $("#reportLink").hide();
            $("#systemsetting").show();
        }
    }

    $("#reportLink2").hide();
    $("#reportLink1").hide();

    $scope.hospital;
    $scope.readonly = true;
    $scope.isModify = false;
    $scope.checkHospital = function () {
        if ($scope.hospital == null) {
            alert("系统新不能够为空");
            return false;
        }
        if ($scope.hospital.name == null || $scope.hospital.name == "") {
            alert("请输入医院名称");
            return false;
        }
        if ($scope.hospital.department == null || $scope.hospital.department == "") {
            alert("请输入科室名称");
            return false;
        }
        if ($scope.hospital.machineNumber == null || $scope.hospital.machineNumber == "") {
            alert("请输入主机序列号");
            return false;
        }
        if ($scope.hospital.handController == null || $scope.hospital.handController == "") {
            alert("请输入手控器序列号");
            return false;
        }
        if ($scope.hospital.firmwareVersion == null || $scope.hospital.firmwareVersion == "") {
            alert("请输入韧体版本");
            return false;
        }
        if ($scope.hospital.prescribingDoctorName == null || $scope.hospital.prescribingDoctorName == "") {
            alert("请输入开方医生，多个以逗号分隔");
            return false;
        }
        return true;
    }
    $scope.addHospital = function () {
        if (!$scope.checkHospital()) return;
        HospitalService.addHospital($scope.hospital, function (result) {
            if (result.isSuccess) {
                alert("删除报告单成功");
            } else {
                alert("删除报告单失败，详情： " + result.description);
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }
    $scope.updateHospital = function () {
        if (!$scope.checkHospital()) return;
        HospitalService.updateHospital($scope.hospital, function (result) {
            if (result.isSuccess) {
                alert("更新系统信息成功");
                $scope.cancelModify();
            } else {
                alert("更新系统信息失败，详情： " + result.description);
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }
    $scope.getHospital = function () {
        HospitalService.getHospital($scope.hospital, function (result) {
            if (result != null) {
                $scope.hospital = result;
            } else {
                //alert("删除报告单失败，详情： "+result.description);
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }

    $scope.deleteLogo = function () {
        HospitalService.deleteLogo($scope.hospital.hospitalId, function (result) {
            if (result.isSuccess) {
                alert("删除Logo成功");
//    			$scope.getHospital();
//    			 $location.path("/truscreen/hospital");
                location.reload();
            } else {
                alert("删除Logo成功，详情： " + result.description);
            }
        }, function (response) {
            $scope.unauthorizedCallBack(response);
        });
    }
    $scope.activeModify = function () {
        $scope.isModify = true;
        $scope.readonly = false;
        $scope.doesNotCancleModify = true;
        return false;
    }
    $scope.doesCancleModify = true;
    $scope.cancelModify = function ($event) {
        $scope.isModify = false;
        $scope.readonly = true;
        $scope.doesCancleModify = false;
        if ($event != null) {
            $event.stopPropagation();
        }
        return false;
    }
    $scope.getHospital();

    $scope.gotoReport = function (path) {
        $cookieStore.put("refer", "hub");
        $location.path(path);
        $cookieStore.put("path", path);
    }

    $scope.gotoFind = function (path) {
        $cookieStore.put("refer", "search");
        $location.path(path);
    }

    $scope.unauthorizedCallBack = function (response) {
        $("#gotohub").hide();
        if (response.status == 401) {
            alert("登陆已超时，请重新登陆。");
            $cookieStore.put("doctor", null);
            $location.path("/truscreen/login");
        } else {
            alert("未知系统错误，请重新登陆");
            $cookieStore.put("doctor", null);
            $location.path("/truscreen/login");
        }
    }
}
