package edu.psu.mob_apps.lab5.persistence;

public class BitmapWorkerTaskArgs {
	public BitmapWorkerTaskArgs(String fileName, int maxWidth, int maxHeight) {
		this.fileName = fileName;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}
	
	public String fileName;
	public int maxWidth;
	public int maxHeight;
}
