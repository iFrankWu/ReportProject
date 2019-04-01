DROP database if exists report;
create database report DEFAULT CHARACTER SET utf8;
use report;
#set names gbk;

create table doctors(
doctorId int auto_increment not null primary key,
doctorName varchar(128) not null,
password varchar(128) not null,
type varchar(16),
status varchar(16),
modifyDate timestamp default CURRENT_TIMESTAMP ,
createDate timestamp default CURRENT_TIMESTAMP,
isDelete boolean not null DEFAULT false
);

#drop table logrecord;
create table logrecord(
recordId int auto_increment not null primary key,
doctorId int,
ip varchar(64),
methodName varchar(128),
methodParam varchar(4096),
operateTime timestamp default now(),
foreign key(doctorId) REFERENCES doctors(doctorId) on delete  CASCADE
);
delete from doctors;
insert into doctors(doctorName,password,type,status,createDate)values('truscreen',password('truscreen'),'系统管理员','可用',now());


create table hospital(/** 这个表的数据 是静态数据*/
	hospitalId int auto_increment not null  primary key,
	name varchar(128),
	machineNumber varchar(128),
	handController varchar(128),
	firmwareVersion varchar(128),
    hospitalLogo varchar(512),
    department varchar(256)
);

create table report(
reportId int auto_increment not null primary key,
modifyDate timestamp DEFAULT  now(),
/**资料*/
department varchar(128),
patientName varchar(128),
age int ,
caseNumber varchar(128) not null,
phone varchar(64),
lastTimeMenstruation timestamp default CURRENT_TIMESTAMP ,
pregnancyNumber int,
childbirthNumber int,
isMenopause boolean,

/**主诉*/
isLeucorrhea boolean,
isBleed boolean,
unregularBleed varchar(128),
otherComplaints varchar(256),

/**临床表现*/
isSmooth boolean,
isAcuteInflammation boolean,
isHypertrophy boolean,
isPolyp boolean,
erosion varchar(128),
isTear boolean,
isNesslersGlandCyst boolean,
isWhite boolean,
isCancer boolean,
otherClinical varchar(256),

/**检查内容*/
pointNumber int,
isComplete varchar(128),
reason4doesNotComplete varchar(1024),
checkResult varchar(128),

/**进一步处理意见*/
screening boolean,/* 筛查*/
checking boolean,/**检查*/
otherSuggestion varchar(512),

checkDate timestamp default CURRENT_TIMESTAMP,

doctorName varchar(128),
doctorId int,
isDelete boolean,
address varchar(128),/**add patientAddress in 20130720*/

foreign key(doctorId) REFERENCES doctors(doctorId) on delete  CASCADE
);

ALTER TABLE report ADD prescribingDoctorName VARCHAR(128); /** prescribingDoctorName varchar(128), add prescibing doctor name V1.2 2014.4.9 */
ALTER TABLE report ADD COLUMN (lct VARCHAR(128), hpv VARCHAR(128) ,touchbleeding VARCHAR(128) );/** add lct hpv and touch bleeding V1.3 12/25/2015 */
ALTER TABLE report ADD checkHpv boolean default false;/** add checkHpv suggestion V1.4 2016.12.25 */
ALTER TABLE report ADD outpatientNo VARCHAR(128);/** add 门诊号 V1.5 2017.07.11 */
ALTER TABLE report ADD admissionNo VARCHAR(128);/** add 住院号 suggestion V1.5 2017.07.11 */

ALTER TABLE report ADD pregnancyStatus boolean;/** add 妊娠状态 V1.6 2018.02.09 */
ALTER TABLE report ADD pregnancyTime int;/** add 怀孕周数 V1.6 2018.02.09 */
ALTER TABLE report ADD pnorValueResult FLOAT ;/** add PNOR值  V1.6 2018.02.09 */

ALTER TABLE report ADD transformArea VARCHAR(128);/** add 可转换区域 V1.7 2018.09.05 */
ALTER TABLE report ADD uid VARCHAR(128);/** add 患者id  V1.7 2018.09.05 */

ALTER TABLE report ADD visableCancer boolean;/** add 是否肉眼可见癌 V1.8 2019.04.01 */

