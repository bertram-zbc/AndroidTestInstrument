package com.testmap.logic;

public class Pointf {
	private float x;
	private float y;

	public Pointf(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y
	 *            要设置的 y
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x
	 *            要设置的 x
	 */
	public void setX(float x) {
		this.x = x;
	}
	
	public static float distance(Pointf a, Pointf b) {
		float result = -1;
		result = (float) Math.sqrt((a.x - b.x) * (a.x - b.x) +(a.y - b.y)
				* (a.y - b.y));
		return result;
	}
	
	static public Pointf midBetween(Pointf x,Pointf y){
		return new Pointf((x.getX()+y.getX())/2,(x.getY()+y.getY())/2);
	}
	
	@Override
	public String toString() {
		String str = "("+x+","+y+")";
		return str;
	}
	
}
