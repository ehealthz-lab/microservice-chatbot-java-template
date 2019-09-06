package main.java.objects;

import java.util.ArrayList;
import java.util.Date;

public class Notification {
	
	public static class Timer {
		
		int year;
		int month;
		int day;
		int hour;
		int minute;
		int second;
		
		public Timer (int year, int month, int day, int hour, int minute, int second) {
			this.year = year;
			this.month = month;
			this.day = day;
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		}
		
		public Timer (int month, int day, int hour, int minute, int second) {
			year = 0;
			this.month = month;
			this.day = day;
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		}
		
		public Timer (int day, int hour, int minute, int second) {
			year = 0;
			month = 0;
			this.day = day;
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		}
		
		public Timer (int hour, int minute, int second) {
			year = 0;
			month = 0;
			day = 0;
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		}
		
		public Timer (int minute, int second) {
			year = 0;
			month = 0;
			day = 0;
			hour = 0;
			this.minute = minute;
			this.second = second;
		}
		
		public Timer (int second) {
			year = 0;
			month = 0;
			day = 0;
			hour = 0;
			minute = 0;
			this.second = second;
		}
		
		public Timer () {
			year = 0;
			month = 0;
			day = 0;
			hour = 0;
			minute = 0;
			second = 0;
			
		}

		public int getYear() {
			return year;
		}
		
		public void setYear(int year) {
			this.year = year;
		}
		
		public int getMonth() {
			return month;
		}
		
		public void setMonth(int month) {
			this.month = month;
		}
		
		public int getDay() {
			return day;
		}
		
		public void setDay(int day) {
			this.day = day;
		}
		
		public int getHour() {
			return hour;
		}
		
		public void setHour(int hour) {
			this.hour = hour;
		}
		
		public int getMinute() {
			return minute;
		}
		
		public void setMinute(int minute) {
			this.minute = minute;
		}
		
		public int getSecond() {
			return second;
		}
		
		public void setSecond(int second) {
			this.second = second;
		}
		
	}
	
	Date date;
	String message;
	
	String destinatary;
	String microservice;
	
	Double repetitions;
	Timer time_repetitions;
	
	
	private ArrayList<String> idResource;
	
	/*
	 * Class constructor
	 */
	public Notification() {		
		date = null;
		message = "";
		
		destinatary = "";
		microservice = null;
		
		repetitions = 0d;
		time_repetitions = new Timer();
		
		idResource = new ArrayList<String>();
	}
	
	/*
	 * Class constructor
	 */
	public Notification(Notification n) {		
		date = n.getDate();
		message = n.getMessage();
		
		destinatary = n.getDestinatary();
		microservice = n.getMicroservice();
		
		repetitions = n.getRepetitions();
		time_repetitions = n.getTime_repetitions();
		
		idResource = n.getIdResource();
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getDestinatary() {
		return destinatary;
	}
	
	public void setDestinatary(String destinatary) {
		this.destinatary = destinatary;
	}
	
	public String getMicroservice() {
		return microservice;
	}
	
	public void setMicroservice(String microservice) {
		this.microservice = microservice;
	}
	
	public Double getRepetitions() {
		return repetitions;
	}
	
	public void setRepetitions(Double repetitions) {
		this.repetitions = repetitions;
	}
	
	public Timer getTime_repetitions() {
		return time_repetitions;
	}
	
	public void setTime_repetitions(Timer time_repetitions) {
		this.time_repetitions = time_repetitions;
	}
	
	public ArrayList<String> getIdResource() {
		return idResource;
	}
	
	public void setIdResource(ArrayList<String> id) {
		this.idResource = id;
	}
	
}
