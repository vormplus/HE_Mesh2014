package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_Distance3D;
import wblut.geom.WB_Line;

/**
 * Twist a mesh. Determined by a twist axis and an angle factor.
 * 
 * @author Frederik Vanhoutte (W:Blut)
 * 
 */
public class HEM_Twist extends HEM_Modifier {

	/** Twist axis. */
	private WB_Line twistAxis;

	/** Angle factor. */
	private double angleFactor;

	/**
	 * Instantiates a new HEM_Twist.
	 */
	public HEM_Twist() {
		super();
	}

	/**
	 * Set twist axis.
	 * 
	 * @param a
	 *            twist axis
	 * @return self
	 */
	public HEM_Twist setTwistAxis(final WB_Line a) {
		twistAxis = a;
		return this;
	}

	/**
	 * Set angle factor, ratio of twist angle in degrees to distance to twist
	 * axis.
	 * 
	 * @param f
	 *            direction
	 * @return self
	 */
	public HEM_Twist setAngleFactor(final double f) {
		angleFactor = f * (Math.PI / 180);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.hemesh.modifiers.HEB_Modifier#modify(wblut.hemesh.HE_Mesh)
	 */
	@Override
	public HE_Mesh apply(final HE_Mesh mesh) {
		if ((twistAxis != null) && (angleFactor != 0)) {
			HE_Vertex v;
			final Iterator<HE_Vertex> vItr = mesh.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = Math.sqrt(WB_Distance3D.sqDistance(v,
						twistAxis));
				v.pos.rotateAboutAxis(d * angleFactor, twistAxis.getOrigin(),
						twistAxis.getOrigin().add(twistAxis.getDirection()));
			}
		}
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	public HE_Mesh apply(final HE_Selection selection) {
		if ((twistAxis != null) && (angleFactor != 0)) {
			selection.collectVertices();
			HE_Vertex v;
			final Iterator<HE_Vertex> vItr = selection.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = Math.sqrt(WB_Distance3D.sqDistance(v,
						twistAxis));
				v.pos.rotateAboutAxis(d * angleFactor, twistAxis.getOrigin(),
						twistAxis.getOrigin().add(twistAxis.getDirection()));
			}
		}
		return selection.parent;
	}

}