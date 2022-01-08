package utilities;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

class StatementCounter extends AbstractRDFHandler {

	  private int countedStatements = 0;

	  @Override
	  public void handleStatement(Statement st) {
	     countedStatements++;
	  }

	 public int getCountedStatements() {
	   return countedStatements;
	 }
	}