package org.apache.accumulo.examples.simple.client;

import org.apache.accumulo.core.cli.ClientOnDefaultTable;
import org.apache.accumulo.core.cli.ClientOpts;
import org.apache.accumulo.core.cli.ScannerOpts;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.trace.TraceDump;
import org.apache.accumulo.core.trace.TraceDump.Printer;
import org.apache.hadoop.io.Text;

import com.beust.jcommander.Parameter;

/**
 * Example of using the TraceDump class to print a formatted view of a Trace
 *
 */
public class TraceDumpExample {
	
	static class Opts extends ClientOnDefaultTable {
		public Opts() {
			super("trace");
		}

		@Parameter(names = {"--traceid"}, description = "The hex string id of a given trace, for example 16cfbbd7beec4ae3")
		public String traceId = "";
	}
	
	public void dump(Opts opts) throws TableNotFoundException, AccumuloException, AccumuloSecurityException {
	
		if (opts.traceId.isEmpty()) {
			throw new IllegalArgumentException("--traceid option is required");
		}
		
		Scanner scanner = opts.getConnector().createScanner(opts.getTableName(), opts.auths);
		scanner.setRange(new Range(new Text(opts.traceId)));
		TraceDump.printTrace(scanner, new Printer() {
			public void print(String line) {
				System.out.println(line);
			}
		});
	}
	
	/**
	 * @param args
	 * @throws AccumuloSecurityException 
	 * @throws AccumuloException 
	 * @throws TableNotFoundException 
	 */
	public static void main(String[] args) throws TableNotFoundException, AccumuloException, AccumuloSecurityException {
		TraceDumpExample traceDumpExample = new TraceDumpExample();
		Opts opts = new Opts();
		ScannerOpts scannerOpts = new ScannerOpts();
		opts.parseArgs(TraceDumpExample.class.getName(), args, scannerOpts);

		traceDumpExample.dump(opts);
	}

}
