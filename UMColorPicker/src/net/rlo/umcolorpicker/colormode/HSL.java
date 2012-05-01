package net.rlo.umcolorpicker.colormode;

public class HSL {

	private double h;
	private double s;
	private double l;
	
	public HSL(double h, double s, double l) {
		this.h = h;
		this.s = s;
		this.l = l;
	}
	
	public double getH() {
		return h;
	}
	public void setH(double h) {
		this.h = h;
	}
	public double getS() {
		return s;
	}
	public void setS(double s) {
		this.s = s;
	}
	public double getL() {
		return l;
	}
	public void setL(double l) {
		this.l = l;
	}
	
	public String toString() {
		return String.format("%03d, %03d, %03d", (int)h, (int)(s*100.0), (int)(l*100.0));
	}
	
}
