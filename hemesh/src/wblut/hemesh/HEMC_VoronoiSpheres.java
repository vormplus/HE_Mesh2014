package wblut.hemesh;

import java.util.ArrayList;

import wblut.geom.WB_Point;

/**
 * Creates the Voronoi cell of a collection of points, constrained by a maximum
 * radius.
 * 
 * @author Frederik Vanhoutte (W:Blut)
 * 
 */

public class HEMC_VoronoiSpheres extends HEMC_MultiCreator {

	/** Points. */
	private WB_Point[] points;

	/** Number of points. */
	private int numberOfPoints;

	/** Level of geodesic sphere in exact mode. */
	private int level;

	/** Number of tracer points in approximate mode. */
	private int numTracers;

	/** Starting trace step in approximate mode?. */
	private double traceStep;

	/** Maximum radius. */
	private double cutoff;

	/** Approximate mode?. */
	private boolean approx;

	/**
	 * Instantiates a new HEMC_VoronoiSpheres.
	 * 
	 */
	public HEMC_VoronoiSpheres() {
		super();
		level = 1;
		traceStep = 10;
		numTracers = 100;

	}

	/**
	 * Set points that define cell centers.
	 * 
	 * @param points
	 *            array of vertex positions
	 * @return self
	 */
	public HEMC_VoronoiSpheres setPoints(final WB_Point[] points) {
		this.points = points;
		return this;
	}

	/**
	 * Set points that define cell centers.
	 * 
	 * @param points
	 *            2D array of double of vertex positions
	 * @return self
	 */
	public HEMC_VoronoiSpheres setPoints(final double[][] points) {
		final int n = points.length;
		this.points = new WB_Point[n];

		for (int i = 0; i < n; i++) {
			this.points[i] = new WB_Point(points[i][0], points[i][1],
					points[i][2]);
		}

		return this;
	}

	/**
	 * Set points that define cell centers.
	 * 
	 * @param points
	 *            2D array of float of vertex positions
	 * @return self
	 */
	public HEMC_VoronoiSpheres setPoints(final float[][] points) {
		final int n = points.length;
		this.points = new WB_Point[n];

		for (int i = 0; i < n; i++) {
			this.points[i] = new WB_Point(points[i][0], points[i][1],
					points[i][2]);
		}

		return this;
	}

	/**
	 * Set number of points.
	 * 
	 * @param N
	 *            number of points
	 * @return self
	 */
	public HEMC_VoronoiSpheres setN(final int N) {
		numberOfPoints = N;
		return this;
	}

	/**
	 * Set level of geodesic sphere in each cell.
	 * 
	 * @param l
	 *            recursive level
	 * @return self
	 */
	public HEMC_VoronoiSpheres setLevel(final int l) {
		level = l;
		return this;
	}

	/**
	 * Set number of tracer points to use in approximate model.
	 * 
	 * @param n
	 *            number of tracer points
	 * @return self
	 */
	public HEMC_VoronoiSpheres setNumTracers(final int n) {
		numTracers = n;
		return this;
	}

	/**
	 * Set initial trace step size.
	 * 
	 * @param d
	 *            trace step
	 * @return self
	 */
	public HEMC_VoronoiSpheres setTraceStep(final double d) {
		traceStep = d;
		return this;
	}

	/**
	 * Set maximum radius of cell.
	 * 
	 * @param c
	 *            cutoff radius
	 * @return self
	 */
	public HEMC_VoronoiSpheres setCutoff(final double c) {
		cutoff = Math.abs(c);
		return this;
	}

	/**
	 * Set approximate mode.
	 * 
	 * @param a
	 *            true, false
	 * @return self
	 */
	public HEMC_VoronoiSpheres setApprox(final boolean a) {
		approx = a;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.hemesh.HE_MultiCreator#create()
	 */
	@Override
	public HE_Mesh[] create() {

		HE_Mesh[] result;

		if (points == null) {
			result = new HE_Mesh[1];
			result[0] = new HE_Mesh();
			_numberOfMeshes = 0;
			return result;
		}
		if (numberOfPoints == 0) {
			numberOfPoints = points.length;
		}
		final ArrayList<HE_Mesh> lresult = new ArrayList<HE_Mesh>();
		final HEC_VoronoiSphere cvc = new HEC_VoronoiSphere();

		cvc.setPoints(points).setN(numberOfPoints).setLevel(level)
				.setCutoff(cutoff).setApprox(approx).setNumTracers(numTracers)
				.setTraceStep(traceStep);
		for (int i = 0; i < numberOfPoints; i++) {
			System.out.println("HEMC_VoronoiSpheres: creating cell " + (i + 1)
					+ " of " + numberOfPoints + ".");
			cvc.setCellIndex(i);
			final HE_Mesh mesh = cvc.createBase();
			if (mesh.getNumberOfVertices() > 0) {
				lresult.add(mesh);
			}

		}
		result = new HE_Mesh[lresult.size()];
		_numberOfMeshes = lresult.size();
		return lresult.toArray(result);
	}
}
