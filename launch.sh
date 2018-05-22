#!/bin/sh

if [ -z "$1" ];
then
    echo "Simulation launch script"
    echo
    echo "Required Parameters : "
    echo
    echo " <jar directory path> : directory containing the performance tests jar. Required. Example './launch ./target'"
    echo
   exit 0;
 fi

COMPILATION_CLASSPATH="$(find -L "$1" -maxdepth 1 -name "*fat-tests.jar" -type f -exec printf :{} ';')"
JAVA_OPTS="-server -XX:+UseThreadPriorities -XX:ThreadPriorityPolicy=42 -Xms512M -Xmx2048M -XX:+HeapDumpOnOutOfMemoryError -XX:+AggressiveOpts -XX:+OptimizeStringConcat -XX:+UseFastAccessorMethods -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv6Addresses=false ${JAVA_OPTS}"
ENVIRONMENT=$2 java $JAVA_OPTS -cp $COMPILATION_CLASSPATH io.gatling.app.Gatling