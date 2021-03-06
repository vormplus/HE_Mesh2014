package wblut.external.ProGAL;

/**
 * Part of ProGAL: http://www.diku.dk/~rfonseca/ProGAL/
 * 
 * Original copyright notice:
 * 
 * Copyright (c) 2013, Dept. of Computer Science - Univ. of Copenhagen. All
 * rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * A triangle in (x,y,z)-space represented by the three corner-points.
 */
public class Triangle implements Simplex {
	protected Point p1, p2, p3;

	/** Construct a triangle using the three specified points as corners */
	public Triangle(Point p1, Point p2, Point p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	/** Get the first corner */
	public Point getP1() {
		return p1;
	}

	/** Get the second corner */
	public Point getP2() {
		return p2;
	}

	/** Get the third corner */
	public Point getP3() {
		return p3;
	}

	/** Get the specified corner of this triangle */
	public Point getCorner(int c) {
		return getPoint(c);
	}

	/** Get the specified corner-point of this triangle */
	@Override
	public Point getPoint(int c) {
		switch (c) {
		case 0:
			return p1;
		case 1:
			return p2;
		case 2:
			return p3;
		}
		throw new Error("Badly specified point number (" + c
				+ "). Should be between 0 and 2");
	}

	/**
	 * Return the 'dimension' of this object. Required by the interface Simplex.
	 */
	@Override
	public int getDimension() {
		return 2;
	}

	/** Return the center of the triangle. Here average of the corners is used. */
	@Override
	public Point getCenter() {
		return new Point((p1.x() + p2.x() + p3.x()) / 3,
				(p1.y() + p2.y() + p3.y()) / 3, (p1.z() + p2.z() + p3.z()) / 3);
	}

	/** Return the area of one side of the triangle. */
	public double getArea() {
		return 0.5 * p1.vectorTo(p2).crossThis(p1.vectorTo(p3)).length();
	}

	/** Return a vector that is normal to this triangle. */
	public Vector getNormal() {
		return p1.vectorTo(p2).crossThis(p1.vectorTo(p3)).normalizeThis();
	}

	/**
	 * Return the circumradius of the triangle. If one side has zero length this
	 * method returns the length of the two remaining sides.
	 */
	public double circumradius() {
		double a = p1.distance(p2);
		double b = p1.distance(p3);
		double c = p2.distance(p3);
		double s = (a + b + c) / 2;// Semiperemiter
		return a * b * c
				/ (4 * Math.sqrt(s * (a + b - s) * (a + c - s) * (b + c - s)));
	}

	/**
	 * Return the circumcenter of the triangle. TODO: Test TODO: Make more
	 * efficient (transform to origo with n as z and use 2D formula)
	 */
	public Point circumcenter() {
		Vector n = getNormal();
		Point m1 = Point.getMidpoint(p1, p2);
		Point m2 = Point.getMidpoint(p1, p3);
		Line l1 = new Line(m1, p1.vectorTo(p2).crossThis(n));
		Line l2 = new Line(m2, p1.vectorTo(p3).crossThis(n));
		return l1.getIntersection(l2);
	}

	public double inradius() {
		double a = p1.distance(p2);
		double b = p1.distance(p3);
		double c = p2.distance(p3);
		double s = (a + b + c) / 2;// Semiperemiter
		return Math.sqrt(((s - a) * (s - b) * (s - c)) / s);
	}

	public Point incenter() {
		double a = p1.distance(p2);
		double b = p1.distance(p3);
		double c = p2.distance(p3);
		double P = a + b + c;
		Vector C = p3.toVector().multiplyThis(a);
		C.addThis(p2.toVector().multiplyThis(b));
		C.addThis(p1.toVector().multiplyThis(c));
		C.divideThis(P);
		return C.toPoint();
	}

	/**
	 * Returns a string-representation of this triangle formatted with two
	 * decimals precision.
	 */
	@Override
	public String toString() {
		return toString(2);
	}

	/**
	 * Returns a string-representation of this triangle formatted with
	 * <code>dec</code> decimals precision.
	 */
	public String toString(int dec) {
		return String.format("Triangle[p1=%s,p2=%s,p3=%s]", p1.toString(dec),
				p2.toString(dec), p3.toString(dec));
	}

	/**
	 * Writes this triangle to <code>System.out</code> with 2 decimals
	 * precision.
	 */
	public void toConsole() {
		toConsole(2);
	}

	/**
	 * Writes this triangle to <code>System.out</code> with <code>dec</code>
	 * decimals precision.
	 */
	public void toConsole(int dec) {
		System.out.println(toString(dec));
	}

	@Override
	public Triangle clone() {
		return new Triangle(p1.clone(), p2.clone(), p3.clone());
	}

}
