/**
 *                     Dandelion (version 0.70.0)      
 *          Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive
 * frame-based, 2d and 3d scenes.
 * 
 * This source file is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * 
 * A copy of the GNU General Public License is available on the World Wide Web
 * at <http://www.gnu.org/copyleft/gpl.html>. You can also obtain it by
 * writing to the Free Software Foundation, 51 Franklin Street, Suite 500
 * Boston, MA 02110-1335, USA.
 */

package remixlab.dandelion.core;

import java.util.List;

import remixlab.dandelion.geom.*;

public interface Depictable {
  //drawing

  // 2D-3D
  
  /**
	 * Draws an axis of length {@code length} which origin correspond to the
	 * world coordinate system origin.
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawAxis(float length);
	
	/**
	 * Draws a grid in the XY plane, centered on (0,0,0) (defined in the current
	 * coordinate system).
	 * <p>
	 * {@code size} and {@code nbSubdivisions} define its geometry.
	 * 
	 * @see #drawAxis(float)
	 */
	public void drawGrid(float size, int nbSubdivisions);
	
	public void drawDottedGrid(float size, int nbSubdivisions);
	
	public void drawWindow(Window window, float scale);	
	
	/**
	 * Draws a rectangle on the screen showing the region where a zoom operation
	 * is taking place.
	 */	
	public void drawZoomWindowHint();
	
	/**
	 * Draws visual hint (a line on the screen) when a screen rotation is taking
	 * place.
	 */
	public void drawScreenRotateLineHint();
	
	/**
	 * Draws visual hint (a cross on the screen) when the
	 * {@link #arcballReferencePoint()} is being set.
	 * <p>
	 * Simply calls {@link #drawCross(float, float)} on {@code
	 * camera().projectedCoordinatesOf(arcballReferencePoint())} {@code x} and
	 * {@code y} coordinates.
	 * 
	 * @see #drawCross(float, float)
	 */	
	public void drawArcballReferencePointHint();
	
	/**
	 * Draws a cross on the screen centered under pixel {@code (px, py)}, and edge
	 * of size {@code size}.
	 * 
	 * @see #drawArcballReferencePointHint()
	 */
	public void drawCross(float px, float py, float size);
	
	/**
	 * Draws a filled circle using screen coordinates.
	 * 
	 * @param subdivisions
	 *          Number of triangles approximating the circle. 
	 * @param center
	 *          Circle screen center.
	 * @param radius
	 *          Circle screen radius.
	 */	
	public void drawFilledCircle(int subdivisions, Vec center, float radius);
	
	/**
	 * Draws a filled square using screen coordinates.
	 * 
	 * @param center
	 *          Square screen center.
	 * @param edge
	 *          Square edge length.
	 */
	public void drawFilledSquare(Vec center, float edge);
	
	/**
	 * Draws the classical shooter target on the screen.
	 * 
	 * @param center
	 *          Center of the target on the screen
	 * @param length
	 *          Length of the target in pixels
	 */
	public void drawShooterTarget(Vec center, float length);
		
	public void drawPath(List<RefFrame> path, int mask, int nbFrames, int nbSteps, float scale);	
	
	// 3D
	/**
	 * Draws a cylinder of width {@code w} and height {@code h}, along the 
	 * positive {@code z} axis. 
	 */
 public void cylinder(float w, float h);
 
 /**
	 * Draws a cylinder whose bases are formed by two cutting planes ({@code m}
	 * and {@code n}), along the {@link #renderer()} positive {@code z} axis.
	 * 
	 * @param detail
	 * @param w radius of the cylinder and h is its height
	 * @param h height of the cylinder
	 * @param m normal of the plane that intersects the cylinder at z=0
	 * @param n normal of the plane that intersects the cylinder at z=h
	 * 
	 * @see #cylinder(float, float)
	 */
	public void hollowCylinder(int detail, float w, float h, Vec m, Vec n);
 
 /**
	 * Draws a cone along the positive {@code z} axis, with its base centered
	 * at {@code (x,y)}, height {@code h}, and radius {@code r}. 
	 * 
	 * @see #cone(int, float, float, float, float, float)
	 */
 public void cone(int detail, float x, float y, float r, float h);
 
 /**
	 * Draws a truncated cone along the positive {@code z} axis,
	 * with its base centered at {@code (x,y)}, height {@code h}, and radii
	 * {@code r1} and {@code r2} (basis and height respectively).
	 * 
	 * @see #cone(int, float, float, float, float)
	 */
 public void cone(int detail, float x, float y, float r1, float r2, float h);
 
//TODO pend
	//public void drawViewPort(ViewPort camera, float scale);
	
	/**
	 * Draws a representation of the {@code camera} in the 3D virtual world.
	 * <p>
	 * The near and far planes are drawn as quads, the frustum is drawn using
	 * lines and the camera up vector is represented by an arrow to disambiguate
	 * the drawing.
	 * <p>
	 * When {@code drawFarPlane} is {@code false}, only the near plane is drawn.
	 * {@code scale} can be used to scale the drawing: a value of 1.0 (default)
	 * will draw the Camera's frustum at its actual size.
	 * <p>
	 * <b>Note:</b> The drawing of a Scene's own Scene.camera() should not be
	 * visible, but may create artifacts due to numerical imprecisions.
	 */
 public void drawCamera(Camera camera, boolean drawFarPlane, float scale);

 public void drawKFIViewport(float scale);
}