DROP database if exists report;
create database report DEFAULT CHARACTER SET utf8;
use report;

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


create table hospital(
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
department varchar(128),
patientName varchar(128),
age int ,
caseNumber varchar(128) not null,
phone varchar(64),
lastTimeMenstruation timestamp default CURRENT_TIMESTAMP ,
pregnancyNumber int,
childbirthNumber int,
isMenopause boolean,

isLeucorrhea boolean,
isBleed boolean,
unregularBleed varchar(128),
otherComplaints varchar(256),

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

pointNumber int,
isComplete varchar(128),
reason4doesNotComplete varchar(1024),
checkResult varchar(128),

screening boolean,
checking boolean,
otherSuggestion varchar(512),

checkDate timestamp default CURRENT_TIMESTAMP,

doctorName varchar(128),
doctorId int,
isDelete boolean,
address varchar(128),

foreign key(doctorId) REFERENCES doctors(doctorId) on delete  CASCADE
);

ALTER TABLE report ADD prescribingDoctorName VARCHAR(128); 
ALTER TABLE report ADD COLUMN (lct VARCHAR(128), hpv VARCHAR(128) ,touchbleeding VARCHAR(128) );
ALTER TABLE report ADD checkHpv boolean default false;