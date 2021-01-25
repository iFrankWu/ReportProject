# 外部开放接口


## 获取报告单检查结果
-----
#### 接口名称： 获取报告单检查结果  
#### 接口协议：HTTP  
#### 请求方法：GET  
#### 接口地址：http://localhost:8081/truscreen/openapi/detail/{out_order_no}  
#### 例子：http://localhost:8081/truscreen/openapi/detail/23  
#### 返回值：

````
{
    "description":"获取报告单成功",
    "report":{
        "address":null,
        "doctorId":11,
        "reportId":4826,
        "checkResult":"正常",
        "doctorName":"a",
        "patientName":"32",
        "age":23,
        "uid":null,
        "pnorValueResult":null,
        "reason4doesNotComplete":null,
        "caseNumber":"23",
        "lastTimeMenstruation":1610980178000,
        "pregnancyNumber":23,
        "childbirthNumber":23,
        "isMenopause":false,
        "isLeucorrhea":false,
        "isBleed":true,
        "unregularBleed":"false",
        "otherComplaints":null,
        "isSmooth":false,
        "isAcuteInflammation":false,
        "isHypertrophy":true,
        "isPolyp":false,
        "erosion":null,
        "isTear":false,
        "isNesslersGlandCyst":false,
        "isWhite":false,
        "isCancer":false,
        "otherClinical":null,
        "pointNumber":0,
        "isComplete":"完成",
        "screening":true,
        "checking":false,
        "otherSuggestion":null,
        "checkDate":1610980178000,
        "phone":"23",
        "prescribingDoctorName":"王燕",
        "checkHpv":false,
        "lct":"HSIL",
        "hpv":"33+",
        "touchbleeding":"false",
        "department":"妇科",
        "modifyDate":1610980178000,
        "isDelete":true,
        "outpatientNo":"23",
        "admissionNo":"23",
        "pregnancyStatus":false,
        "pregnancyTime":0,
        "transformArea":null,
        "visableCancer":false
    },
    "isSuccess":true
}
````

#### 各字段含义

````
科室="hospital.department"
订单号="report.outpatientNo"
TS检查号="report.admissionNo"
姓名="report.patientName"
年龄="report.age"
病历号="report.caseNumber"
地址="report.address"
电话="report.phone"
末次月经="report.lastTimeMenstruation"
绝经="report.isMenopause"
孕次="report.pregnancyNumber"
产次="report.childbirthNumber" 

白带多="report.isLeucorrhea"
性交出血="report.isBleed"
不规则流血="report.unregularBleed"
LCT="report.lct"
HPV="report.hpv"

光滑="report.isSmooth"
慢性炎症="report.isAcuteInflammation"
肥大="report.isHypertrophy"
柱状上皮异位="report.erosion"
息肉="report.isPolyp"
撕裂="report.isTear"
纳博特囊="report.isNesslersGlandCyst"
白斑="report.isWhite"
接触性出血="report.touchbleeding"
点探数量="checkPointNumber(event,this)"
正常type="radio" name="result" value="正常" ng-model="search.checkResult"
异常type="radio" name="result" value="异常" ng-model="search.checkResult" 
检查结果="doesCheckCompleted"

进一步处理意见：
按照国家宫颈癌筛查指南定期筛查
type="checkbox" name="regular" ng-model="search.screening"
建议进行HPV筛查
type="checkbox" name="colposcopy" ng-model="report.checkHpv"
进行阴道镜或阴道镜下活检
type="checkbox" name="colposcopy" ng-model="search.checking"

申请医生="report.prescribingDoctorName"
报告单ID="report.reportId"
操作人员="report.doctorName"
检查日期="checkDatePrint"

````

### 报告单参考版面



