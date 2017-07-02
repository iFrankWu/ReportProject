'use strict';

var userControl = angular.module('userControl', ['ui', 'ngResource','ngCookies'] ,function ($routeProvider,$compileProvider) {
//    console.log("Configure route provider");
  //  $locationProvider.html5Mode(true).hashPrefix('!');
    
//    $compileProvider.directive('compile', function($compile) {
//        // directive factory creates a link function
//        return function(scope, element, attrs) {
//          scope.$watch(
//            function(scope) {
//               // watch the 'compile' expression for changes
//              return scope.$eval(attrs.compile);
//            },
//            function(value) {
//              // when the 'compile' expression changes
//              // assign it into the current DOM
//              element.html(value);
//     
//              // compile the new DOM and link it to the current
//              // scope.
//              // NOTE: we only compile .childNodes so that
//              // we don't get into infinite loop compiling ourselves
//              $compile(element.contents())(scope);
//            }
//          );
//        };
//      });
    
    
    $routeProvider
    	.when("/truscreen/main/",{
	    	templateUrl:"partials/main.html",
	    	controller:MainController
    	})
         .when("/truscreen/doctors",{
        	templateUrl:"partials/doctor.html",
        	controller:DoctorController
        })
         .when("/truscreen/login",{
        	templateUrl:"partials/userLogin.html",
        	controller:DoctorController
        })
        .when("/truscreen/reports",{
	    	templateUrl:"partials/report.html",
	    	controller:ReportController
    	})
    	 .when("/truscreen/mypage",{
	    	templateUrl:"partials/mypage.html",
	    	controller:DoctorController
    	})
    	.when("/truscreen/hospital",{
	    	templateUrl:"partials/hospital.html",
	    	controller:HospitalController
    	})
        .when("/", {
            redirectTo: "/truscreen/login"
        })
        .otherwise({
            redirectTo: "/truscreen/login"
        });
})
    .factory('DoctorResource',DoctorResourceImpl)
    .factory("CommonResource",CommonResourceImpl)
    .factory("ReportResource",ReportResourceImpl)  
    .factory("HospitalResource",HospitalResourceImpl)
     .factory("RecordResource",RecordResourceImpl)
    .service('DoctorService',DoctorServiceImpl)
    .service('ReportService',ReportServiceImpl)
    .service('CommonService',CommonServiceImpl)
    .service('HospitalService',HospitalServiceImpl)
    .service('RecordService',RecordServiceImpl);


//var app = angular.module('myApp', []);

//userControl.directive('contentItem', function ($compile) {
//    var getTemplate = function() {
//    	var html ="<input type='text' ng-model='report.age'/>";
//    	return html;
////    	return $("#midfyReport").html();
//    }
//
//    var linker = function(scope, element, attrs) {
//        scope.rootDirectory = 'images/';
//
//        element.html(getTemplate()).show();
//
//        $compile(element.contents())(scope);
//    }
//
//    return {
//        restrict: "E",
//        rep1ace: true,
//        link: linker,
//        scope: {
//            report:'='
//        }
//    };
//});




 