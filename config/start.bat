@echo off

if "%1"=="h" goto begin

start mshta vbscript:createobject("wscript.shell").run("""%~nx0"" h",0)(window.close)&&exit

:begin

set LIB_HOME=D:\truscreen\lib
set JAVA_HOME=d:\truscreen\jre64\jre7
set Path=%JAVA_HOME%/bin;%PATH%
set PR_CLASSPATH=%LIB_HOME%/asm-3.1.jar;%LIB_HOME%/commons-lang-2.5.jar;%LIB_HOME%/datasourse.jar;%LIB_HOME%/jackson-core-asl-1.7.1.jar;%LIB_HOME%/jackson-jaxrs-1.7.1.jar;%LIB_HOME%/jackson-mapper-asl-1.7.1.jar;%LIB_HOME%/jackson-xc-1.7.1.jar;%LIB_HOME%/javax.servlet-3.0.0.v201112011016.jar;%LIB_HOME%/javax.servlet.jar;%LIB_HOME%/jersey-client-1.8.jar;%LIB_HOME%/jersey-core-1.8.jar;%LIB_HOME%/jersey-json-1.8.jar;%LIB_HOME%/jersey-server-1.8.jar;%LIB_HOME%/jersey-servlet-1.13-b01.jar;%LIB_HOME%/jettison-1.1.jar;%LIB_HOME%/jetty-continuation-8.1.7.v20120910.jar;%LIB_HOME%/jetty-http-8.1.7.v20120910.jar;%LIB_HOME%/jetty-io-8.1.7.v20120910.jar;%LIB_HOME%/jetty-security-8.1.7.v20120910.jar;%LIB_HOME%/jetty-server-8.1.7.v20120910.jar;%LIB_HOME%/jetty-servlet-8.1.7.v20120910.jar;%LIB_HOME%/jetty-util-8.1.7.v20120910.jar;%LIB_HOME%/json_simple-1.1.jar;%LIB_HOME%/jsr311-api-1.1.1.jar;%LIB_HOME%/junit-4.5.jar;%LIB_HOME%/log4j-1.2.15.jar;%LIB_HOME%/mysql-connector-java-5.1.17-bin.jar;%LIB_HOME%/oauth-server-1.6.jar;%LIB_HOME%/oauth-signature-1.1.5.1.jar;%LIB_HOME%/poi-3.9-20121203.jar;%LIB_HOME%/poi-excelant-3.9-20121203.jar;%LIB_HOME%/server-side.jar;%LIB_HOME%/slf4j-api-1.4.3.jar;%LIB_HOME%/mimepull.jar;%LIB_HOME%/jersey-multipart.jar;%LIB_HOME%/sqljdbc4.jar

java -cp %PR_CLASSPATH% com.tibco.test.Jetty
