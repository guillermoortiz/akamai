package com.mycompany.app;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Conversor implements Serializable {
	private String FILE;
	private String PATH_IN;
	private String DIR_HDFS;
	private String LONG_IN;
	private String TIPO_IN;
	private static String SEPARATOR = "|";
	private static String SALTO_LINEA = "\n";
	boolean TRACE_DEBUG = true;
	boolean TRACE_INFO = true;
	int[] iCabeceraComp;
	String[] sTiposComp;
	String sDateTime;

	public Conversor(String file, String pathIn, String dirHDFS, String longIn,
			String tipoIn) {
		FILE = file;
		PATH_IN = pathIn;
		DIR_HDFS = dirHDFS;
		LONG_IN = longIn;
		TIPO_IN = tipoIn;

		List<String> aList = new ArrayList<String>(Arrays.asList(LONG_IN
				.split(",")));
		iCabeceraComp = new int[aList.size()];
		for (int i = 0; i < aList.size(); i++) {
			iCabeceraComp[i] = new Integer(aList.get(i).toString());
		}

		aList = new ArrayList<String>(Arrays.asList(TIPO_IN.split(",")));
		sTiposComp = new String[aList.size()];
		for (int i = 0; i < aList.size(); i++) {
			sTiposComp[i] = aList.get(i).toString();
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		sDateTime = dateFormat.format(date);
		printConfiguration();
	}

	private void printConfiguration() {
		System.out.println(" INICIO ---- Conversion ------ " + sDateTime);
		System.out.println(" Parámetros Entrada:");
		System.out.println(" - FILE:     " + FILE);
		System.out.println(" - PATH_IN:  " + PATH_IN);
		System.out.println(" - DIR_HDFS: " + DIR_HDFS);
		System.out.println(" - CABECERA:");
		System.out.print("  + Longitud campos: ");
		for (int i = 0; i < iCabeceraComp.length; i++) {
			System.out.print(" " + iCabeceraComp[i]);
		}
		System.out.println("");
		System.out.print("  + Tipo campos:     ");
		for (int i = 0; i < sTiposComp.length; i++) {
			System.out.print(" " + sTiposComp[i]);
		}

	}

	public int calculaLongBlock() {
		int longRecord = 0;
		for (int i = 0; i < iCabeceraComp.length; i++) {
			if (sTiposComp[i].equals("S") || sTiposComp[i].equals("S99")) {
				longRecord += (iCabeceraComp[i] + 1) / 2;
			} else {
				longRecord += iCabeceraComp[i];
			}
		}
		if (TRACE_DEBUG || TRACE_INFO)
			System.out.println("Longitud Registro: " + longRecord);
		return longRecord;
	}

	public String parseFile(String path) {
		int longRecord = calculaLongBlock();
		FileInputStream fin = null;
		boolean hayRegistros = true;
		String sRegistro = "";
		int iLineas = 0;
		int finLinea = 0;
		int iLineasError = 0;

		try {
			File file = new File(path);
			fin = new FileInputStream(file);
			byte fileContent[] = new byte[longRecord];

			while (hayRegistros) {

				if (TRACE_DEBUG)
					System.out.println("----------- Registro: " + iLineas);

				if (iLineas != 0) {
					finLinea += longRecord;
				}

				fin.read(fileContent);

				System.out.println(" Registros Fichero: " + iLineas);
				System.out.println(" Registros Erroneos:" + iLineasError);
				System.out.println(" FIN ---- Conversion ------ " + sDateTime);

				if (TRACE_INFO || TRACE_DEBUG) {
					System.out.println("sRegistro: " + sRegistro);
				}
				
				sRegistro = parse(fileContent);
				sRegistro = SALTO_LINEA + sRegistro;			

				if ((finLinea + longRecord) > file.length()) {
					if (TRACE_DEBUG) {
						System.out.println("file.length(): " + file.length()
								+ " finLinea: " + finLinea);
					}
					hayRegistros = false;
				}
				iLineas++;
			}

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("java.lang.ArrayIndexOutOfBoundsException");

			System.out.println("sRegistro: " + sRegistro);

		} catch (FileNotFoundException e) {
			System.out.println("File not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading file " + ioe);
		} finally {
			// close the streams using close method
			try {
				if (fin != null) {

					fin.close();
				}
			} catch (IOException ioe) {
				System.out.println("Error while closing stream: " + ioe);
			}
		}

		return sRegistro;

	}

	public String parse(byte[] fileContent) {
		int iniRecord = 0;
		int finRecord = 0;
		int byteRecord = 0;

		String sEvents = "";
		long iEvent = 0;
		float fEvent = 0;
		String record = "";

		try{
			for (int i = 0; i < iCabeceraComp.length; i++) {
	
				if (sTiposComp[i].equals("C")) {
	
					if (i == 0) {
						// iniRecord = finRegistro;
						iniRecord = 0;
						// finRecord = finRegistro + iCabeceraComp[i];
						finRecord = iCabeceraComp[i];
					} else {
						iniRecord = finRecord;
						finRecord = iniRecord + iCabeceraComp[i];
					}
					sEvents = ConversorUtil.getString(fileContent, iniRecord,
							finRecord, "UTF-8");
	
					if (TRACE_DEBUG)
						System.out.println("Campo C: " + sEvents);
	
					if (i == 0) {
						record = sEvents;
					} else {
						record += SEPARATOR + sEvents;
					}
	
				} else if (sTiposComp[i].equals("X")) {
	
					if (i == 0) {
						// iniRecord = finRegistro;
						iniRecord = 0;
						// finRecord = finRegistro + iCabeceraComp[i];
						finRecord = iCabeceraComp[i];
					} else {
						iniRecord = finRecord;
						finRecord = iniRecord + iCabeceraComp[i];
					}
	
					// System.out.println("iniRecord: " +
					// iniRecord+" finRecord: "+finRecord);
	
					sEvents = ConversorUtil.getString(fileContent, iniRecord,
							finRecord, "IBM-500");
	
					if (TRACE_DEBUG)
						System.out.println("Campo X: " + sEvents);
	
					if (i == 0) {
						record = sEvents;
					} else {
						record += SEPARATOR + sEvents;
					}
	
				} else if (sTiposComp[i].equals("D")) {
	
					if (i == 0) {
						// iniRecord = finRegistro;
						iniRecord = 0;
						// finRecord = finRegistro + iCabeceraComp[i];
						finRecord = iCabeceraComp[i];
					} else {
						iniRecord = finRecord;
						finRecord = iniRecord + iCabeceraComp[i];
					}
					sEvents = ConversorUtil.fromZoned(ConversorUtil.getString(
							fileContent, iniRecord, finRecord, "IBM-500"));
	
					if (TRACE_DEBUG)
						System.out.println("Campo D: " + sEvents);
	
					iEvent = Long.parseLong(sEvents);
	
					if (i == 0) {
						record = "" + iEvent;
					} else {
						record += SEPARATOR + fEvent;
					}
	
				} else if (sTiposComp[i].equals("D99")) {
	
					if (i == 0) {
						// iniRecord = finRegistro;
						iniRecord = 0;
						// finRecord = finRegistro + iCabeceraComp[i];
						finRecord = iCabeceraComp[i];
					} else {
						iniRecord = finRecord;
						finRecord = iniRecord + iCabeceraComp[i];
					}
					sEvents = ConversorUtil.fromZoned(ConversorUtil.getString(
							fileContent, iniRecord, finRecord, "IBM-500"));
	
					if (TRACE_DEBUG)
						System.out.println("Campo D99: " + sEvents);
					if (sEvents.substring(0, 1).equals("-"))
						sEvents = sEvents.substring(0, iCabeceraComp[i] - 2 + 1)
								+ "."
								+ sEvents.substring(iCabeceraComp[i] - 2 + 1,
										iCabeceraComp[i]);
					else
						sEvents = sEvents.substring(0, iCabeceraComp[i] - 2)
								+ "."
								+ sEvents.substring(iCabeceraComp[i] - 2,
										iCabeceraComp[i]);
	
					fEvent = Float.parseFloat(sEvents);
	
					if (i == 0) {
						record = "" + fEvent;
					} else {
						record += SEPARATOR + fEvent;
					}
	
				} else if (sTiposComp[i].equals("S")) {
	
					if (i == 0) {
						iniRecord = 0;
					} else {
						iniRecord = finRecord;
					}
					byteRecord = (iCabeceraComp[i] + 1) / 2;
					finRecord = iniRecord + byteRecord;
	
					sEvents = ConversorUtil.getMainframePackedDecimal(fileContent,
							iniRecord, byteRecord);
	
					if (TRACE_DEBUG)
						System.out.println("Campo S: " + sEvents);
	
					iEvent = Long.parseLong(sEvents);
					// System.out.println("Campo S: "+ iEvent);
	
					if (i == 0) {
						record = "" + iEvent;
					} else {
						record += SEPARATOR + iEvent;
					}
	
				} else if (sTiposComp[i].equals("S9")) {
	
					if (i == 0) {
						iniRecord = 0;
					} else {
						iniRecord = finRecord;
					}
	
					byteRecord = (iCabeceraComp[i] + 1) / 2;
					finRecord = iniRecord + byteRecord;
	
					sEvents = ConversorUtil.getMainframePackedDecimal(fileContent,
							iniRecord, byteRecord);
					if (TRACE_DEBUG)
						System.out.println("Campo S9: " + sEvents);
					if (sEvents.substring(0, 1).equals("-"))
						sEvents = sEvents.substring(0, iCabeceraComp[i])
								+ "."
								+ sEvents.substring(iCabeceraComp[i],
										iCabeceraComp[i] + 1);
					else
						sEvents = sEvents.substring(0, iCabeceraComp[i] - 1)
								+ "."
								+ sEvents.substring(iCabeceraComp[i] - 1,
										iCabeceraComp[i]);
	
					fEvent = Float.parseFloat(sEvents);
	
					if (i == 0) {
						record = "" + fEvent;
					} else {
						record += SEPARATOR + fEvent;
					}
	
				} else if (sTiposComp[i].equals("S99")) {
	
					if (i == 0) {
						iniRecord = 0;
					} else {
						iniRecord = finRecord;
					}
	
					byteRecord = (iCabeceraComp[i] + 1) / 2;
					finRecord = iniRecord + byteRecord;
	
					sEvents = ConversorUtil.getMainframePackedDecimal(fileContent,
							iniRecord, byteRecord);
					if (TRACE_DEBUG)
						System.out.println("Campo S99: " + sEvents);
					if (sEvents.substring(0, 1).equals("-"))
						sEvents = sEvents.substring(0, iCabeceraComp[i] - 2 + 1)
								+ "."
								+ sEvents.substring(iCabeceraComp[i] - 2 + 1,
										iCabeceraComp[i]);
					else
						sEvents = sEvents.substring(0, iCabeceraComp[i] - 2)
								+ "."
								+ sEvents.substring(iCabeceraComp[i] - 2,
										iCabeceraComp[i]);
	
					fEvent = Float.parseFloat(sEvents);
	
					// System.out.println("Campo S99:" + fEvent);
	
					if (i == 0) {
						record = "" + fEvent;
					} else {
						record += SEPARATOR + fEvent;
					}
	
				} else {
	
					System.err.println("Tipo de Cabecera desconocido: "
							+ sTiposComp[i]);
					System.err.println("Registro: " + record);
				}
			}
		}catch (Exception e){
			System.out.println("Error exception: " + e);
			e.printStackTrace();
		}
		return record;

	}

	public static void main(String[] args) {
		String longFields = "4,3,3,7,50,10,10,10,8,10,1";
		String typeFields = "X,X,X,X,X,X,X,X,X,X,X";
		Conversor c = new Conversor(null, null, null, longFields, typeFields);
		String result = c.parseFile("c:\\miscosas\\file.bin");
		System.out.println("Resultado:" + result);
	}

}