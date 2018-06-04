#!/bin/sh

if [ -z "$1" ];
then
    echo "Simulation launch script. Missing required parameter simulation class:"
    echo " <jar directory path> : directory containing the performance fat-tests.jar. Required. Example './launch ./target'"
    echo
   exit 0;
 fi

 if [ -z "$2" ];
 then
     echo "Simulation launch script. Missing required parameter simulation class:"
     echo " <simulation class> : fully qualified name of the Simulation class. Example 'uk.gov.hmcts.ccd.simulation.UserProfileSimulation'"
    exit 0;
  fi


COMPILATION_CLASSPATH="$(find -L "$1" -maxdepth 1 -name "*fat-tests.jar" -type f -exec printf :{} ';')"
JAVA_OPTS="-server -XX:+UseThreadPriorities -XX:ThreadPriorityPolicy=42 -Xms512M -Xmx2048M -XX:+HeapDumpOnOutOfMemoryError -XX:+AggressiveOpts -XX:+OptimizeStringConcat -XX:+UseFastAccessorMethods -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv6Addresses=false ${JAVA_OPTS}"
java $JAVA_OPTS -cp $COMPILATION_CLASSPATH io.gatling.app.Gatling -s "$2"