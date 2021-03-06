DoctorResourceImpl = function ($resource) {
    return $resource('truscreen/doctor/:id/:passoword', {}, {
        login:{method:'POST',params:{}},
        logout:{method:'GET',params:{id:'id'}},
        getList:{method:'GET',params:{},isArray:true},
        addDoctor:{method:"PUT",params:{}},
        deleteDoctor:{method:"DELETE",params:{id:'id'}},
        updateDoctor:{method:"PUT",params:{id:"id"}}
    });
};
 
CommonResourceImpl = function ($resource) {
    return $resource('truscreen/comm/', {}, {
//        getList:{method:'GET',params:{id:'id',withStudent:'withStudent'},isArray:true},
//        addClazz:{method:'PUT',params:{}},
//        updateClazz:{method:"PUT",params:{id:'id'}},
//        deleteClazz:{method:'DELETE',params:{id:'id'}},
//        getClazz:{method:'GET',params:{id:'id'}}
    	postRequest:{method:'POST',isArray:true},
    	putRequest:{method:'PUT'}
    });
};

ReportResourceImpl = function ($resource) {
    return $resource('truscreen/report/:reportId/:preOrNext/:size/:page/:sortColumn/:uid/:patientName/:outpatientNo/:admissionNo', {}, {
      //  login:{method:'POST',params:{}},
        //logout:{method:'GET',params:{id:'id'}},
        getDetail:{method:'GET',params:{reportId:'reportId',preOrNext:'preOrNext',size:'size',page:'page',sortColumn:'sortColumn'}},
        getNextPage:{method:'GET',params:{reportId:'reportId',preOrNext:'preOrNext',size:'size',page:'page',sortColumn:'sortColumn'}},
        getPrePage:{method:'GET',params:{reportId:'reportId',preOrNext:'preOrNext',size:'size',page:'page',sortColumn:'sortColumn'}},
        getCurrentPage:{method:'GET',params:{reportId:'reportId',preOrNext:'preOrNext',size:'size',page:'page',sortColumn:'sortColumn'}},
        getTopPage:{method:'GET',params:{reportId:'reportId',preOrNext:'preOrNext',size:'size',page:'page',sortColumn:'sortColumn'}},
        addReport:{method:"PUT",params:{}},
        deleteReport:{method:"DELETE",params:{reportId:'reportId'}},
        updateReport:{method:"PUT",params:{reportId:"reportId"}},
        advanceSearch:{method:"POST",params:{}},
        getPNorm:{method:"GET",params:{reportId:'1',preOrNext:'Next',size:'1',page:'1',sortColumn:'1',uid:'uid'}},
        getPatientInfo:{method:"GET",params:{reportId:'1',preOrNext:'Next',size:'1',page:'1',sortColumn:'1',uid:'1',patientName:'patientName',outpatientNo:'outpatientNo',admissionNo:'admissionNo'}}
    });
};
HospitalResourceImpl = function ($resource) {
    return $resource('truscreen/hospital/:hospitalId', {}, {
        getHospital:{method:'GET',params:{}},
        updateHospital:{method:'POST',params:{}},
        deleteLogo:{method:'DELETE',params:{hospitalId:'hospitalId'}},
        getDoctor:{method:"PUT",params:{id:'id'}},
    });
};

RecordResourceImpl = function ($resource) {
    return $resource('truscreen/record/:exports', {}, {
        getRecords:{method:'POST',params:{}},
        statistic:{method:'PUT',params:{}},
        exportData:{method:'POST',params:{exports:"exports"}}
    });
};

HHDResourceImpl = function ($resource) {
    return $resource('truscreen/hdd/:command', {}, {
         executeHHDCommand:{method:'GET',params:{ command:'command' }}
    });
};

OpenApiResourceImpl = function ($resource) {
    return $resource('truscreen/openapi/detail/:no', {}, {
        getReport:{method:'GET',params:{ no:'no' }}
    });
};
 

 