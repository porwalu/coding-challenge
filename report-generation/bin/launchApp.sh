#!/bin/bash
#Utkarsh Porwal
#Launches the application using their jar files.

echo "Launching key-service at"  `date`
nohup java -jar ../key-service/target/key-service-1.0-SNAPSHOT.jar &
sleep 5
echo "Finished launching key-service."

echo "Launching transaction-service at"  `date`
nohup java -jar ../transaction-service/target/transaction-service-1.0-SNAPSHOT.jar &
sleep 15
echo "Finished launching transaction-service."

echo "Report Generation Scheduler at " `date`
java -jar ../report-executor/target/report-executor-1.0-SNAPSHOT.jar
echo
echo
echo "Report Generation finished. Please check them at `pwd`"
