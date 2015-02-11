#!/bin/bash

export HADOOP_CONF_DIR=/etc/hadoop/conf
SPARK_CLASSPATH=""
for lib in `ls /user/etc/lib/*.jar`
do
        if [ -z "$SPARK_CLASSPATH" ]; then
		SPARK_CLASSPATH=$lib
	else
		SPARK_CLASSPATH=$lib,$SPARK_CLASSPATH
	fi
done

spark-submit --name "Akamai Streaming Java" --master yarn-client --class com.produban.bin.AkamaiJavaBin --jars $SPARK_CLASSPATH --executor-memory 1g /user/etc/executor/produban-akamai-bin.jar
