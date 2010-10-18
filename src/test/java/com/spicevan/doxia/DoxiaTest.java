package com.spicevan.doxia;

import java.io.IOException;

import org.apache.maven.doxia.book.BookDoxiaException;
import org.junit.Before;
import org.junit.Test;

public class DoxiaTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDoDoxia() throws IOException, BookDoxiaException {
		Doxia d = new Doxia();
		d.doDoxia2();
	}

}
