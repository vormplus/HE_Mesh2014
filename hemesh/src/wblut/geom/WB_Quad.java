/*
 * Copyright (c) 2010, Frederik Vanhoutte This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * http://creativecommons.org/licenses/LGPL/2.1/ This library is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 */
package wblut.geom;

/**
 * Placeholder for quad..
 */
public class WB_Quad {

	/** First point. */
	public WB_Point p1;

	/** Second point. */
	public WB_Point p2;

	/** Third point. */
	public WB_Point p3;

	/** Fourth point. */
	public WB_Point p4;

	/**
	 * Instantiates a new WB_Quad. No copies are made.
	 * 
	 * @param p1
	 *            first point
	 * @param p2
	 *            second point
	 * @param p3
	 *            third point
	 * @param p4
	 *            fourth point
	 */
	public WB_Quad(final WB_Point p1, final WB_Point p2, final WB_Point p3,
			final WB_Point p4) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
	}

}