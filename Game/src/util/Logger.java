package util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Logger {
	
	private PrintStream stream;
	private Date currentTime;
	private DateFormat format = new SimpleDateFormat("MMMMM dd, YYYY [HH:mm:ss]");
	private DateFormat fileName = new SimpleDateFormat("dd-MM-YYYY [HHmmss]");
	private final String prefix = "INFO: ";
	private final String errPrefix = "ERROR: ";
	private final String warnPrefix = "WARNING: ";
	public static final int INFO = 0, WARNING = 1, ERROR = 2;
	private int substring = 0;
	
	public Logger(String logFolderPath, DateFormat fileFormat){
		try {
			if(fileFormat != null)
				this.fileName = fileFormat;
			updateTime();
			if(!new File(logFolderPath).exists())
				new File(logFolderPath).mkdirs();
			stream = new PrintStream(new FileOutputStream(new File(logFolderPath + fileName.format(currentTime) + ".log")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Logger(String logFolderPath){
		try {
			updateTime();
			if(!new File(logFolderPath).exists())
				new File(logFolderPath).mkdirs();
			stream = new PrintStream(new FileOutputStream(new File(logFolderPath + fileName.format(currentTime) + ".log")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setLoggerFormat(DateFormat format){
		this.format = format;
	}
	
	public void println(String line){
		updateTime();
		stream.append(prefix + format.format(currentTime) + " " + line + "\n");
		System.out.println(prefix + format.format(currentTime) + " " + line);
	}
	
	public void print(String text){
		updateTime();
		stream.append(text);
		System.out.print(text);
	}
	
	public void println(String line, int type){
		updateTime();
		if(type == INFO){
			stream.append(prefix + format.format(currentTime) + " " + line + "\n");
			System.out.println(prefix + format.format(currentTime) + " " + line);
		}else if(type == WARNING){
			stream.append(warnPrefix + format.format(currentTime) + " " + line + "\n");
			System.out.println(warnPrefix + format.format(currentTime) + " " + line);
		}else if(type == ERROR){
			stream.append(errPrefix + format.format(currentTime) + " " + line + "\n");
			System.err.println(errPrefix + format.format(currentTime) + " " + line);
		}
	}
	
	public String addSpaces(String text){
		String s = " ";
		for(int i = 0; i < substring; i++){
			s += " ";
		}
		return s + text;
	}
	
	private void updateTime(){
		currentTime = Calendar.getInstance().getTime();
		substring = format.format(currentTime).length() + prefix.length();
	}
}
