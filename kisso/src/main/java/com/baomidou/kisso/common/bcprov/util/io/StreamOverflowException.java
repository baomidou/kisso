package com.baomidou.kisso.common.bcprov.util.io;

import java.io.IOException;

public class StreamOverflowException extends IOException {

	private static final long serialVersionUID = 1L;


	public StreamOverflowException( String msg ) {
		super(msg);
	}
}
