package wblut.hemesh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class HET_HemeshWriter {

	protected OutputStream hemeshStream;

	protected PrintWriter hemeshWriter;

	public void beginSave(final OutputStream stream) {
		try {
			hemeshStream = stream;
			handleBeginSave();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void beginSave(final String fn, final String name) {
		try {
			hemeshStream = new FileOutputStream(new File(fn, name));
			handleBeginSave();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void endSave() {
		try {
			hemeshWriter.flush();
			hemeshWriter.close();
			hemeshStream.flush();
			hemeshStream.close();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
	}

	protected void handleBeginSave() {
		hemeshWriter = new PrintWriter(hemeshStream);
	}

	public void vertex(final HE_Vertex v, final int heid) {
		hemeshWriter.println(v.xd() + " " + v.yd() + " " + v.zd() + " " + heid);
	}

	public void halfedge(final int vid, final int henextid, final int hepairid,
			final int edgeid, final int faceid) {
		hemeshWriter.println(vid + " " + henextid + " " + hepairid + " "
				+ edgeid + " " + faceid);
	}

	public void edge(final int heid) {
		hemeshWriter.println(heid);
	}

	public void face(final int heid) {
		hemeshWriter.println(heid);
	}

	public void sizes(final int v1, final int v2, final int v3, final int v4) {
		hemeshWriter.println(v1 + " " + v2 + " " + v3 + " " + v4);
	}
}
