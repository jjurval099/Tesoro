package ies.jandula.tesoroExceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Juan Jurado
 */
public class TesoroExceptions extends Exception
{
	/** Atributo - Código */
	private int code ;
	
	/** Atributo - Mensaje */
	private String message ;

	/**
	 * @param code	  posee el código del error
	 * @param message contiene un mensaje más completo asociado al código del error
	 */
	public TesoroExceptions(int code, String message)
	{
		super(message);
		
		this.code    = code ;
		this.message = message ;
	}

	/**
	 * @param code	    posee el código del error
	 * @param message   contiene un mensaje más completo asociado al código del error
	 * @param exception contiene la traza de excepción encontrada
	 */
	public TesoroExceptions(int code, String message, Throwable exception)
	{
		super(message, exception);

		this.code    = code ;
		this.message = message ;
	}

	/**
	 * Este método devolverá un mapa con el código y el mensaje que Spring Boot convertirá a JSON
	 * @return un objeto mapa que posteriormente Spring Boot entenderá como un JSON
	 */
	public Object getBodyExceptionMessage()
	{
		Map<String, Object> mapBodyException = new HashMap<String, Object>() ;
		
		mapBodyException.put("code", this.code) ;
		mapBodyException.put("message", this.message) ;
		
		return mapBodyException ;
	}
}
