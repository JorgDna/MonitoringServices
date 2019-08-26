#! /bin/ksh

#
# autor Jorge Espinosa
# version 1.0 - 03/04/2019
#
# ejecutarRastrackServices.sh # 
#

#JAVA_HOME=/home/oracle/Oracle/Middleware/jrockit-jdk1.6.0_45
JAVA_HOME=java
JAVA_RASTRACK_HOME=RastrackServices.jar
APPNAME="RASTRACK SERVICES"
PROPERTIES=configuration.properties

PID=$(ps -ef|grep ${JAVA_RASTRACK_HOME} |grep -v grep)

case $1 in
    start)
        if [ "${PID}" = "" ]; then
	    sed 's/stopMainThread = true/stopMainThread = false/gi' "${PROPERTIES}";
            sed 's/loadConfiguration = true/loadConfiguration = false/gi' "${PROPERTIES}";
		    echo "Starting ${APPNAME} ..."
            nohup $JAVA_HOME -jar "${JAVA_RASTRACK_HOME}" &		
			PID=$(ps -ef|grep "${JAVA_RASTRACK_HOME}" |grep -v grep)
			echo "[Info] -- "${APPNAME}" started!"			
			
            
        else
            echo "${APPNAME} is already running ..."			
        fi
    ;;

    stop)
        if [ "${PID}" = "" ]; then
           echo "${APPNAME} is not running ..."
        else		    
            echo "Stopping ${APPNAME}..."
            sed 's/stopMainThread = false/stopMainThread = true/gi' "${PROPERTIES}";
            sed 's/loadConfiguration = false/loadConfiguration = true/gi' "${PROPERTIES}";
            echo "${APPNAME} stopped!"  	               
            
        fi
    ;;

    load)
       if [ "${PID}" = "" ]; then
           echo "${APPNAME} is not running ..."
       else
           echo "${APPNAME} loading configuration!"
           sed 's/loadConfiguration = false/loadConfiguration = true/gi' "${PROPERTIES}";
           sed 's/stopMainThread = true/stopMainThread = false/gi' "${PROPERTIES}";
           echo "${APPNAME} loaded!" 
       fi
    ;;
    *)
        echo "Choose an option start/stop/load for the service"
    ;;
esac		
	
echo 'OK'
exit 0