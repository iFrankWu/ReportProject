@echo off

echo Starting Backup of Mysql Database on server 

set backupDir=c:/truscreen/backup
set doubleBackupDir=d:/truscreen/backup

mkdir  %backupDir%

echo %time%
date /t

For /f "tokens=1-4 delims=/ " %%a in ('date /t') do (set dt=%%c-%%a-%%b)

For /f "tokens=1-4 delims=:." %%a in ('echo %time%') do (set tm=%%a%%b%%c%%d)

set bkupfilename=%dt%-%tm%.sql

echo Backing up to file: %backupDir%\report-%bkupfilename%

mysqldump  --routines -u root -ptruscreen  report > %backupDir%\report-%bkupfilename%

mkdir %doubleBackupDir%

copy %backupDir%\report-%bkupfilename%  %doubleBackupDir%



echo on


echo delete old backup

forfiles /p %backupDir% /s /m *.* /d -3 /c "cmd /c del @file : date >= 3days"

forfiles /p %doubleBackupDir% /s /m *.* /d -3 /c "cmd /c del @file : date >= 3days"



echo Backup Complete! Have A Nice Day
