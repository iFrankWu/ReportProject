CommonServiceImpl = function(CommonResource) {
    return {
        doctorId: "",
        doctor:null,
        discountInfo:[
	                     {name:'月付', value:'month'},
	                     {name:'季付', value:'season'},
	                     {name:'半年付', value:'halfyear'},
	                     {name:'年付', value:'year'},
	                   ],
	     types :[
	           	                     {name:'管理员', value:'admin'},
	           	                      {name:'申请医生', value:'disabled'},
	           	                     {name:'操作人员', value:'normal'}

	           	                   ],
	     statuses: [
	           	                     {name:'可用', value:'useable'},
	           	                     {name:'禁用', value:'unuseable'}
	           	                   ],
	     postRequest:function(params,callback,failurecallback){
	    	 CommonResource.postRequest(params,callback,failurecallback);
	     },
	     putRequest:function(params,callback,failurecallback){
	    	 CommonResource.putRequest(params,callback,failurecallback);
	     }
    }
}; 

DoctorServiceImpl = function(DoctorResource){
	return {
		  login : function(doctor,callback,failurecallback){
			  DoctorResource.login(doctor,callback,failurecallback);
	       },
	       logout : function(ids,callback){
				  DoctorResource.logout({id:ids},callback);
		       },
	       getDoctorList : function(callback,failurecallback){
	    	   DoctorResource.getList(callback,failurecallback);
		    },
		    addDoctor : function(doctor,callback,failurecallback){
		    	DoctorResource.addDoctor(doctor,callback,failurecallback);
		    },
		    deleteDoctor:	function(ids,callback,failurecallback){
		    	DoctorResource.deleteDoctor({id:ids},callback,failurecallback);
		    },
		    updateDoctor:function(ids,doctor,callback,failurecallback){
		    	DoctorResource.updateDoctor({id:ids},doctor,callback,failurecallback);
		    }
	}
};
ReportServiceImpl = function(ReportResource){
	return {	
			getNextPage:function(startId,sizes,pages,sortColumns,callback,failurecallback){
				if(startId == null) startId = 0;
	    	   ReportResource.getNextPage({reportId:startId,preOrNext:'Next',size:sizes,page:pages,sortColumn:sortColumns},callback,failurecallback);
		    },
		    getPrePage:function(startId,sizes,pages,sortColumns,callback,failurecallback){
				if(startId == null) startId = 0;
	    	   ReportResource.getPrePage({reportId:startId,preOrNext:'Pre',size:sizes,page:pages,sortColumn:sortColumns},callback,failurecallback);
		    },
		    getCurrentPage:function(startId,sizes,pages,sortColumns,callback,failurecallback){
				if(startId == null) startId = 0;
	    	   ReportResource.getCurrentPage({reportId:startId,preOrNext:'Current',size:sizes,page:pages,sortColumn:sortColumns},callback,failurecallback);
		    },
		    getTopPage:function(startId,sizes,pages,sortColumns,callback,failurecallback){
				if(startId == null) startId = 1;
	    	   ReportResource.getTopPage({reportId:startId,preOrNext:'Top',size:sizes,page:pages,sortColumn:sortColumns},callback,failurecallback);
		    },
		    addReport:function(report,callback,failurecallback){
		    	ReportResource.addReport(report,callback,failurecallback);
		    },
		    deleteReport:function(reportIds,callback,failurecallback){
		    	ReportResource.deleteReport({reportId:reportIds},callback,failurecallback);
		    },
		    updateReport:function(reportIds,report,callback,failurecallback){
		    	ReportResource.updateReport({reportId:reportIds},report,callback,failurecallback);
		    },
		    advanceSearch:function(serach,callback,failurecallback){
		    	ReportResource.advanceSearch(serach,callback,failurecallback);
		    }
		}
};

HospitalServiceImpl = function(HospitalResource){
	return {	
			getHospital:function(callback,failurecallback){
				HospitalResource.getHospital(callback,failurecallback);
		    },
		    getDoctor:function(ids,callback,failurecallback){
		    	HospitalResource.getDoctor({id:ids},callback,failurecallback);
		    },
		    deleteLogo:function(hospitalIds,callback,failurecallback){
		    	HospitalResource.deleteLogo({hospitalId:hospitalIds},callback,failurecallback);
		    },
		    updateHospital:function(hospital,callback,failurecallback){
		    	HospitalResource.updateHospital(hospital,callback,failurecallback);
		    }
	    }
};

RecordServiceImpl = function(RecordResource){
	return {	
			getRecords:function(date,callback,failurecallback){
				RecordResource.getRecords(date,callback,failurecallback);
		    },
			statistic:function(data,callback,failurecallback){
				RecordResource.statistic(data,callback,failurecallback);
		    },
		    exportData:function(data){
				RecordResource.exportData({exports:"exports"},data,callback,failurecallback);
		    }
		    
	    }
};