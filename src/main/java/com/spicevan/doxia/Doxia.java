package com.spicevan.doxia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.apache.maven.doxia.ConverterException;
import org.apache.maven.doxia.DefaultConverter;
import org.apache.maven.doxia.UnsupportedFormatException;
import org.apache.maven.doxia.wrapper.InputFileWrapper;
import org.apache.maven.doxia.wrapper.OutputFileWrapper;
import org.apache.maven.doxia.book.BookDoxiaException;
import org.apache.maven.doxia.book.DefaultBookDoxia;
import org.apache.maven.doxia.book.context.BookContext;
import org.apache.maven.doxia.book.model.BookModel;
import org.apache.maven.doxia.book.services.io.BookIo;
import org.apache.maven.doxia.book.services.io.DefaultBookIo;

public class Doxia {
	public void doDoxia2() throws UnsupportedEncodingException, FileNotFoundException, BookDoxiaException {
		String in = "src/site/apt/sections/firewall_iptables.apt";
		String from = "apt";
		String out = "firewall_iptables.xhtml";
		String to = "xhtml";
		
		DefaultBookDoxia dbd = new DefaultBookDoxia();
		File file = new File("src/site/ovm.xml");
		boolean exists = file.exists();
		BookModel bookModel = dbd.loadBook(file);
		BookIo io = new DefaultBookIo();
		
		BookContext context = new BookContext();
		
		
		
		DefaultConverter converter = new DefaultConverter();
		try
		{
		    String[] inputFormats = converter.getInputFormats();
			InputFileWrapper input = InputFileWrapper.valueOf( in, from, "ISO-8859-1", inputFormats );
		    String[] outputFormats = converter.getOutputFormats();
			OutputFileWrapper output = OutputFileWrapper.valueOf( out, to, "UTF-8", outputFormats );

		    converter.convert( input, output );
		}
		catch ( UnsupportedFormatException e )
		{
		    e.printStackTrace();
		}
		catch ( ConverterException e )
		{
		    e.printStackTrace();
		}
	}
	/*
	public void doDoxia() throws IOException, BookDoxiaException, XmlPullParserException {
        BookDoxia doxia = new DefaultBookDoxia();
        
        // load the book descriptor
        File book1 = new File( "src/site/ovm.xml" );
        Reader reader = ReaderFactory.newXmlReader( book1 );
        BookModel read = new BookModelXpp3Reader().read( reader, true );
        boolean exists = book1.exists();

        BookModel book = doxia.loadBook( book1 );
*/
        // files to include
       //  List files = FileUtils.getFiles( new File( "src/resources/book/" ), "**/*.apt, **/*.xml", "" );
/*
        // render books in different formats
     //   doxia.renderBook( book, "pdf", files, new File( "target/itext/" ) );
        doxia.renderBook( book, "xhtml", files, new File( "target/xhtml/" ) );
     //   doxia.renderBook( book, "xdoc", files, new File( "target/xdoc/" ) );
     //   doxia.renderBook( book, "latex", files, new File( "target/latex/" ) );
      //  doxia.renderBook( book, "doc-book", files, new File( "target/doc-book/" ) );
       // doxia.renderBook( book, "rtf", files, new File( "target/rtf/" ) );
        
        
	}
	*/
}
