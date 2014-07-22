package wblut.hemesh;

import wblut.geom.WB_Vector;

/**
 * Cone.
 * 
 * @author Frederik Vanhoutte (W:Blut)
 * 
 */

public class HEC_Cone extends HEC_Creator {

	/** Base radius. */
	private double R;

	/** Height. */
	private double H;

	/** Height segments. */
	private int steps;

	/** Facets. */
	private int facets;

	/** The cap. */
	private boolean cap;

	/** The reverse. */
	private boolean reverse;

	/** The taper. */
	private double taper;

	/**
	 * Instantiates a new cone.
	 * 
	 */
	public HEC_Cone() {
		super();
		R = 0;
		H = 0;
		facets = 6;
		steps = 1;
		Z = new WB_Vector(WB_Vector.Y());
		cap = true;
		taper = 1.0;
	}

	/**
	 * Instantiates a new cone.
	 * 
	 * @param R
	 *            radius
	 * @param H
	 *            heights
	 * @param facets
	 *            number of facets
	 * @param steps
	 *            number of height divisions
	 */
	public HEC_Cone(final double R, final double H, final int facets,
			final int steps) {
		this();
		this.R = R;
		this.H = H;
		this.facets = facets;
		this.steps = steps;
		taper = 1.0;
	}

	/**
	 * Set base radius.
	 * 
	 * @param R
	 *            base radius
	 * @return self
	 */
	public HEC_Cone setRadius(final double R) {
		this.R = R;
		return this;
	}

	/**
	 * Set height.
	 * 
	 * @param H
	 *            height
	 * @return self
	 */
	public HEC_Cone setHeight(final double H) {
		this.H = H;
		return this;
	}

	/**
	 * Set number of sides.
	 * 
	 * @param facets
	 *            number of sides
	 * @return self
	 */
	public HEC_Cone setFacets(final int facets) {
		this.facets = facets;
		return this;
	}

	/**
	 * Set number of vertical divisions.
	 * 
	 * @param steps
	 *            vertical divisions
	 * @return self
	 */
	public HEC_Cone setSteps(final int steps) {
		this.steps = steps;
		return this;
	}

	/**
	 * Set capping options.
	 * 
	 * @param cap
	 *            create cap?
	 * @return self
	 */
	public HEC_Cone setCap(final boolean cap) {
		this.cap = cap;
		return this;
	}

	/**
	 * Reverse cone.
	 * 
	 * @param rev
	 *            the rev
	 * @return self
	 */
	public HEC_Cone setReverse(final boolean rev) {
		reverse = rev;
		return this;
	}

	/**
	 * Sets the taper.
	 * 
	 * @param t
	 *            the t
	 * @return the hE c_ cone
	 */
	public HEC_Cone setTaper(final double t) {
		taper = t;
		return this;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		final double[][] vertices = new double[facets * steps + 1][3];
		final int[][] faces = new int[(cap) ? facets * steps + 1 : facets
				* steps][];

		double Ri;
		double Hj;
		final double invs = 1.0 / steps;
		for (int i = 0; i < steps; i++) {
			Ri = R - Math.pow(i * invs, taper) * R;
			Hj = (reverse) ? H - i * H / steps : i * H / steps;
			for (int j = 0; j < facets; j++) {
				vertices[j + i * facets][0] = Ri
						* Math.cos(2 * Math.PI / facets * j);
				vertices[j + i * facets][2] = Ri
						* Math.sin(2 * Math.PI / facets * j);
				vertices[j + i * facets][1] = Hj;
			}
		}
		vertices[facets * steps][0] = 0;
		vertices[facets * steps][2] = 0;
		vertices[facets * steps][1] = (reverse) ? 0 : H;

		if (cap) {
			faces[steps * facets] = new int[facets];
		}

		for (int j = 0; j < facets; j++) {
			if (cap) {
				faces[steps * facets][j] = j;
			}
			for (int i = 0; i < steps - 1; i++) {
				faces[j + i * facets] = new int[4];
				faces[j + i * facets][0] = j + i * facets;
				faces[j + i * facets][1] = j + i * facets + facets;
				faces[j + i * facets][2] = ((j + 1) % facets) + facets + i
						* facets;
				faces[j + i * facets][3] = (j + 1) % facets + i * facets;
			}
			faces[j + (steps - 1) * facets] = new int[3];
			faces[j + (steps - 1) * facets][0] = facets * steps;
			faces[j + (steps - 1) * facets][2] = j + (steps - 1) * facets;
			faces[j + (steps - 1) * facets][1] = (j + 1) % facets + (steps - 1)
					* facets;

		}

		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setFaces(faces);
		return fl.createBase();
	}

}