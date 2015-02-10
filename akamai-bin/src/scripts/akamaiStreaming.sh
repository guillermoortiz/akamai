#!/bin/bash

export HADOOP_CONF_DIR=/etc/hadoop/conf
SPARK_CLASSPATH=""
for lib in `ls /user/local/etc/lib/*.jar`
do
        if [ -z "$SPARK_CLASSPATH" ]; then
		SPARK_CLASSPATH=$lib
	else
		SPARK_CLASSPATH=$lib,$SPARK_CLASSPATH
	fi
done

spark-submit --name "Akamai Streaming" --master yarn-client --class com.produban.bin.AkamaiBin --jars $SPARK_CLASSPATH --executor-memory 1g /user/local/etc/executor/produban-akamai-bin.jar
