package com.produban;


public class SparkTest {
	public static void main(String[] args) {
		// // Create a StreamingContext with a 1-second batch size from a
		// SparkConf
		// SparkConf conf = new SparkConf().setMaster("local[2]").setAppName(
		// "Test");
		// JavaStreamingContext jssc = new JavaStreamingContext(conf,
		// new Duration(5));
		// // Create a DStream from all the input on port 7777
		// JavaDStream<String> lines = jssc.socketTextStream("localhost",
		// 15000);
		// jssc.checkpoint("c:\\Users\\x100516");
		//
		// JavaDStream<String> linesFiltered = lines
		// .filter(new Function<String, Boolean>() {
		// public Boolean call(String line) {
		// return line.contains("h");
		// }
		// });
		// JavaDStream<Long> javads = lines.count();
		// JavaRDD<Long> rdd = (JavaRDD<Long>)javads;
		//
		// JavaDStream<JavaPairRDD<String, String>> maplinesFiltered =
		// linesFiltered
		// .map(new Function<String, JavaPairRDD<String, String>>() {
		//
		// @Override
		// public JavaPairRDD<String, String> call(String v1) throws Exception {
		// JavaPairRDD<String, String> pair = new JavaPairRDD<>(rdd, kClassTag,
		// vClassTag)
		// return null;
		// }
		// });
		// linesFiltered.countByValue(1).countByValue().t
		// // val errorLinesValue = errorLines.map(line => ("key", line))
		// // val errorLinesValueReduce =
		// // errorLinesValue.groupByKeyAndWindow(Seconds(8), Seconds(4))
		// //
		// // Print out the lines with errors
		// linesFiltered.print();
		//
		// // Start our streaming context and wait for it to "finish"
		// jssc.start();
		// // Wait for the job to finish
		// jssc.awaitTermination();

	}
}
