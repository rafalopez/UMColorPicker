package net.rlo.umcolorpicker.colormode;

public class HSV {

	private double h;
	private double s;
	private double v;
	
	public HSV(double h, double s, double v) {
		this.h = h;
		this.s = s;
		this.v = v;
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
	public double getV() {
		return v;
	}
	public void setV(double v) {
		this.v = v;
	}
	
	public String toString() {
		return String.format("%03d, %03d, %03d", (int)h, (int)(s*100.0), (int)(v*100.0));
	}
}
