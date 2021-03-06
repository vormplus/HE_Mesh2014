package wblut.external.ProGAL;

import wblut.math.WB_Epsilon;

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
 * A plane in (x,y,z)-space represented by a point and a normal.
 * 
 * Assuming that <i>p</i> is a point on the plane and <i>n</i> is the normal
 * vector, the half-space <i>{q|pq·n>0}</i> is called the 'upper halfspace'
 * wrt. the plane and vice versa for the 'lower halfspace'. If the unit normal
 * vector is n = (nx,ny,nz) and the point on the plane is p = (px,py,pz), then
 * the plane has equation ax + bx + cx = d where a = nx, b = ny, c = nz, and d =
 * px*nx + py*ny + pz*nz
 */
public class Plane implements Shape {
	/** Normal vector of the plane. */
	protected Vector normal;
	/** Point in the plane. */
	protected Point point;

	/** Constructs a plane with the normal vector n containing point p. */
	public Plane(Point p, Vector n) {
		this.normal = n.normalizeThis();
		this.point = p;
	}

	/**
	 * Constructs a plane with the normal vector n containing the point (0,0,0).
	 */
	public Plane(Vector n) {
		this.normal = n.normalizeThis();
		this.point = new Point(0, 0, 0);
	}

	/**
	 * Constructs a plane through three points using the first point as defining
	 * point. The normal of the plane will be decided by the order of p, q and r
	 * such that if the right hand follows the rotation from p to q to r the
	 * thumb points in the normals direction. TODO: Test this
	 * 
	 * An error is thrown if the points are collinear.
	 */
	public Plane(Point p, Point q, Point r) {
		if (Point.collinear(p, q, r))
			throw new Error("Cant construct plane: Points are collinear");
		normal = p.vectorTo(q).crossThis(p.vectorTo(r)).normalizeThis();
		this.point = p;
	}

	private double getD() {
		return -normal.x() * point.x() - normal.y() * point.y() - normal.z()
				* point.z();
	}

	/** Get the point defining this plane. */
	public Point getPoint() {
		return point;
	}

	/** Return the normal defining this plane. */
	public Vector getNormal() {
		return normal;
	}

	/** Set the normal to n. */
	public void setNormal(Vector n) {
		this.normal = n;
	}

	/** Get the projection of p onto this plane. */
	public Point projectPoint(Point p) {
		double t = normal.x() * p.x() + normal.y() * p.y() + normal.z() * p.z()
				+ getD();
		return new Point(p.x() - normal.x() * t, p.y() - normal.y() * t, p.z()
				- normal.z() * t);
	}

	/** Returns 1/0/-1 if point p is above/on/below this plane. */
	public int above(Point p) {
		double dotP = normal.dot(p.toVector());
		double d = getD();
		if (dotP > -d)
			return 1;
		if (dotP < -d)
			return -1;
		return 0;
	}

	/** Returns 1/0/-1 if point p is below/on/above this plane */
	public int below(Point p) {
		return -above(p);
	}

	/** Gets perpendicular distance of point to this plane */
	public double getDistance(Point p) {
		double d = getD();
		return Math.abs(normal.dot(p.toVector()) + d) / normal.length();
	}

	/** Get the unsigned angle between this plane and p. */
	public double getUnsignedDihedralAngle(Plane p) {
		return Math.acos(normal.dot(p.normal));
	}

	/**
	 * Get the intersection of a line with the plane. Returns null if line is
	 * parallel to plane.
	 */
	public Point getIntersection(Line line) {
		double denom = normal.dot(line.getDir());
		if (denom == 0)
			return null;
		else {
			Point a = line.getP();
			Vector pa = point.vectorTo(a);
			double u = normal.dot(pa) / denom;
			return new Point(a.x() + u * line.dir.x(),
					a.y() + u * line.dir.y(), a.z() + u * line.dir.z());
		}
	}

	/**
	 * Get the line-parameter of the intersection between a plane and a line.
	 * Returns infinity if line is parallel to plane. TODO: Consider moving to
	 * Line3d
	 */
	public double getIntersectionParameter(Line line) {
		double denom = normal.dot(line.getDir());
		if (denom == 0)
			return Double.POSITIVE_INFINITY;
		else {
			Point a = line.getP();
			Vector pa = point.vectorTo(a);
			double u = normal.dot(pa) / denom;
			return u;
		}
	}

	/**
	 * Get the intersection of a segment with the plane. Returns null if line is
	 * parallel to plane.
	 */
	public Point getIntersection(LineSegment sgm) {
		Vector dir = sgm.getAToB();
		double denom = normal.dot(dir);
		if (denom == 0)
			return null;
		else {
			Vector pa = point.vectorTo(sgm.a);
			double u = normal.dot(pa) / denom;
			if ((u < 0) || (u > 1))
				return null;
			else
				return new Point(sgm.a.x() + u * dir.x(), sgm.a.y() + u
						* dir.y(), sgm.a.z() + u * dir.z());
		}
	}

	public Double getIntersectionAngle(Circle circle, Point p, Vector dir) {
		Vector nC = circle.getNormal();
		if (nC.isParallel(normal))
			return null;
		Plane circlePlane = new Plane(circle.getCenter(), nC);
		Line line = getIntersection(circlePlane); // line.toScene(scene, 0.01,
													// Color.blue);
		double dist = line.getDistance(circle.getCenter());
		if (dist > circle.getRadius() - WB_Epsilon.EPSILON)
			return null;
		return circle.getFirstIntersection(line, p, dir);
	}

	/**
	 * Get intersection of a circle with the plane. Returns null if the plane
	 * does not intersect the circle or if the circle is in the plane. Returns
	 * the touch point if the plane is tangent to the circle. Otherwise returns
	 * 2 points
	 */
	public Point[] getIntersection(Circle circle) {
		Vector u = circle.getNormal().getOrthonormal()
				.multiplyNew(circle.getRadius());
		return getIntersection(circle, u);
	}

	public Point[] getIntersection(Circle circle, Vector u) {
		Vector nC = circle.getNormal();
		if (nC.isParallel(normal))
			return null;
		Plane circlePlane = new Plane(circle.getCenter(), nC);
		Line line = getIntersection(circlePlane);
		double dist = line.getDistance(circle.getCenter());
		if (dist > circle.getRadius() + WB_Epsilon.EPSILON)
			return null;
		if (dist > circle.getRadius() - WB_Epsilon.EPSILON) {
			Point intPoints[] = new Point[1];
			intPoints[0] = line.orthogonalProjection(circle.getCenter());
			return intPoints;
		}
		Vector v = u.clone();
		nC.rotateIn(v, Math.PI / 2);
		Vector cp = new Vector(circle.getCenter(), point);
		double a = u.dot(normal);
		double b = v.dot(normal);
		double c = cp.dot(normal);
		double r = Math.sqrt(a * a + b * b);
		double x = Math.atan2(b / r, a / r); // atan2(sin x, cos x)
		double alpha1 = Math.acos(c / r);
		double alpha2 = 2 * Math.PI - alpha1;
		double t1 = alpha1 + x;
		double t2 = alpha2 + x;
		Vector[] intVectors = new Vector[2];
		intVectors[0] = u.clone();
		nC.rotateIn(intVectors[0], t1);
		intVectors[1] = u.clone();
		nC.rotateIn(intVectors[1], t2);
		Point intPoints[] = new Point[2];
		intPoints[0] = circle.getCenter().clone().add(intVectors[0]);
		intPoints[1] = circle.getCenter().clone().add(intVectors[1]);
		return intPoints;

	}

	/** returns the intersection line with another plane */
	public Line getIntersection(Plane pl) {
		Vector dir = normal.crossNew(pl.getNormal());
		if (dir.isZeroVector())
			return null;
		double h1 = normal.dot(new Vector(point));
		double h2 = pl.getNormal().dot(new Vector(pl.getPoint()));
		double dd = normal.dot(pl.getNormal());
		double denom = 1 - dd * dd;
		double c1 = (h1 - h2 * dd) / denom;
		double c2 = (h2 - h1 * dd) / denom;
		Point q = new Point(c1 * normal.x() + c2 * pl.getNormal().x(), c1
				* normal.y() + c2 * pl.getNormal().y(), c1 * normal.z() + c2
				* pl.getNormal().z());
		return new Line(q, dir);
	}

	/**
	 * Get intersection of a sphere with the plane. Returns null if the plane
	 * does not intersect the sphere. Returns a circle with radius 0 if the
	 * plane is tangent to the sphere. Otherwise returns circle
	 */
	public Circle getIntersection(Sphere sphere) {
		double dist = this.getDistance(sphere.getCenter());
		double rad = sphere.getRadius();
		if (dist - rad > WB_Epsilon.EPSILON)
			return null;
		else {
			Point center = projectPoint(sphere.getCenter());
			if (dist - rad > -WB_Epsilon.EPSILON) {
				return new Circle(center, 0, null);
			} else {
				return new Circle(center, Math.sqrt(rad * rad
						- center.distanceSquared(sphere.getCenter())), normal);
			}
		}
	}

	/**
	 * Returns the defining point for this plane. The center of a plane is not
	 * well-defined, so to implement the shape interface the defining point is
	 * simply used.
	 */
	@Override
	public Point getCenter() {
		return point.clone();
	}

}
