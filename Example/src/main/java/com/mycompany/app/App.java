package com.mycompany.app;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		System.out.println("jujjuuj");
		//String url = "hdfs://192.168.85.134:8020/tmp/sparkTest/";
		//String file = "file.bin";
		
		if (args.length < 4){
			System.out.println("Error number of parameters");
			System.out.println("Param 1 URL where files are");
			System.out.println("Param 2 File Name");
			System.out.println("Param 3 Fields long separated by , . Ex: 4,3,3,7,50,10,10,10,8,10,1");
			System.out.println("Param 4 Field types separated by ,   Ex: X,X,X,X,X,X,X,X,X,X,X");
		}
		String url = args[0];
		String file = args[1];
		String path = url + file;
		String longFields = args[2];
		String typeFields = args[3];
		
//		String longFields = "4,3,3,7,50,10,10,10,8,10,1";
//		String typeFields = "X,X,X,X,X,X,X,X,X,X,X";
		final Conversor c = new Conversor(null, null, null, longFields, typeFields);
		System.out.println(c.calculaLongBlock());
		SparkConf conf = new SparkConf().setAppName("Simple Application");
		JavaSparkContext sc = new JavaSparkContext(conf);
		//JavaRDD<byte[]> rdd = sc.binaryRecords("c:\\miscosas\\file.bin", 116);	
		JavaRDD<byte[]> rdd = sc.binaryRecords(path, c.calculaLongBlock());
	
		
		JavaRDD<String> rddString = rdd.map(new Function<byte[], String>() {

			@Override
			public String call(byte[] arg0) throws Exception {			
				return c.parse(arg0);
			}
		});
		System.out.println("URL:" + url);
		rddString.saveAsTextFile(url + "/output/" + System.currentTimeMillis() + "/");

		System.out.println("ok");
		sc.close();
	}
	
	public static int byteArrayToInt(byte[] encodedValue) {
	    int index = 0;
	    int value = encodedValue[index++] << Byte.SIZE * 3;
	    value ^= (encodedValue[index++] & 0xFF) << Byte.SIZE * 2;
	    value ^= (encodedValue[index++] & 0xFF) << Byte.SIZE * 1;
	    value ^= (encodedValue[index++] & 0xFF);
	    return value;
	}

}
