package com.testmap.logic;

import java.util.ArrayList;

public class Mid {
	private ArrayList<Pointf> pointfs;

	public Mid(Pointf[] pointfs) {
		this.pointfs = new ArrayList<Pointf>();
		for (int i = 0; i < pointfs.length; i++) {
			this.pointfs.add(pointfs[i]);
		}
	}

	public Pointf getPoint() {
		float MaxX = 0;
		float MinX = Float.POSITIVE_INFINITY;
		float MaxY = 0;
		float MinY = Float.POSITIVE_INFINITY;
		for (int i = 0; i < pointfs.size(); i++) {
			if (pointfs.get(i).getX() > MaxX)
				MaxX = pointfs.get(i).getX();
			if (pointfs.get(i).getX() < MinX)
				MinX = pointfs.get(i).getX();
			if (pointfs.get(i).getY() > MaxY)
				MaxY = pointfs.get(i).getY();
			if (pointfs.get(i).getY() < MinY)
				MinY = pointfs.get(i).getY();
		}
		return new Pointf((MaxX+MinX)/2, (MaxY+MinY)/2);
	}
}
