# His系统配置文件

配置文件是 bin/config/db.properties 文件

具体his的配置分两部分

## 第一部分是医院的数据库配置

如果医院是sqlserver 那么需要按照如下的格式进行配置
`
sqlserver.type=sqlserver
sqlserver.driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
sqlserver.username=xxx
sqlserver.password=xxx
sqlserver.url=jdbc:sqlserver://xxx;DatabaseName=xxx
sqlserver.maxConn=50
`

1、其中 sqlserver.type 和 sqlserver.driver 不需要修改 <br>
2、sqlserver.username和sqlserver.password 以及sqlserver.url 需要根据医院给出信息进行调整




## 第二部分是系统获取对应字段的sql


`
default.patientInfoSql=select top 1 * from [WK_Interface].[dbo].[WK_ZYBRJBXX] where MZH = ?
default.patientXM=XM
default.patientZYID=ZYID
default.patientCSRQ=csrq
default.patientMZH=MZH
default.patientJTZZ=jtzz
default.patientLXDH=lxdh
`

这些配置是根据医院his表中的字段来进行的相关配置，具体到时候根据情况再定


