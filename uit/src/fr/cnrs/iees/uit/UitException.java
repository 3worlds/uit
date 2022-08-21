/**************************************************************************
 *  UIT - a Universal Indexing Tree                                       *
 *                                                                        *
 *  Copyright 2018: Jacques Gignoux & Ian D. Davies                       *
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  UIT is a generalisation and re-implementation of QuadTree and Octree  *
 *  implementations by Paavo Toivanen as downloaded on 27/8/2018 on       *
 *  <https://dev.solita.fi/2015/08/06/quad-tree.html>                     *
 *                                                                        *
 **************************************************************************                                       
 *  This file is part of UIT (Universal Indexing Tree).                   *
 *                                                                        *
 *  UIT is free software: you can redistribute it and/or modify           *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  UIT is distributed in the hope that it will be useful,                *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with UIT.  If not, see <https://www.gnu.org/licenses/gpl.html>. *
 *                                                                        *
 **************************************************************************/
package fr.cnrs.iees.uit;

/**
 * A class for {@link Exception}s originating from the <strong>uit</strong>
 * library.
 * 
 * @author Jacques Gignoux - 04-10-2018
 *
 */
//NB: this was previously AotException
//Policy is to make an exception at least for each library
//The general advice for exceptions is to throw early and catch late.

public class UitException extends RuntimeException {

	private static final long serialVersionUID = 5911525855011647174L;

	/**
	 * Instantiate an exception with a message
	 * 
	 * @param message the error message
	 */
	public UitException(String message) {
		super("[" + message + "]");
	}

	/**
	 * Exception wrapper.
	 * @param e the exception to wrap
	 */
	public UitException(Exception e) {
		super(e);
	}

	/**
	 * Exception wrapper with additional information
	 * @param message the error message
	 * @param e the exception to wrap
	 */
	public UitException(String message, Exception e) {
		super("[" + message + "]\n[original exception: " + e.getMessage() + "]");
	}

}
