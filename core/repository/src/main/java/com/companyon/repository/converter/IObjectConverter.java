/**
 * 
 */
package com.companyon.repository.converter;

/**
 * @author mayank
 *
 */
public interface IObjectConverter<B, E> {

	public B convertEOToBO(E eo) throws Exception;

	public E convertBOToEO(B bo) throws Exception;

}
