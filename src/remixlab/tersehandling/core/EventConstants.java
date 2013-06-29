/**
 *                  TerseHandling (version 0.70.0)      
 *           Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive scenes.
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

package remixlab.tersehandling.core;

public interface EventConstants {
  //modifier keys
  static public final int TH_NOMODIFIER_MASK    = 0;
  static public final int TH_SHIFT              = 1 << 0;
  static public final int TH_CTRL               = 1 << 1;
  static public final int TH_META               = 1 << 2;
  static public final int TH_ALT                = 1 << 3;
  static public final int TH_ALT_GRAPH          = 1 << 4;
  
  static public final int TH_SHIFT_DOWN         = 64;
  static public final int TH_CTRL_DOWN          = 128;
  static public final int TH_META_DOWN          = 256;
  static public final int TH_ALT_DOWN           = 512;
  static public final int TH_ALT_GRAPH_DOWN     = 8192;
  
  static final int TH_NOBUTTON	= 0;
  
  static final int TH_CENTER = 3;
  
  //Arrows  
  static final int TH_LEFT  = 37;
  static final int TH_UP    = 38;
  static final int TH_RIGHT = 39;
  static final int TH_DOWN  = 40; 
}
