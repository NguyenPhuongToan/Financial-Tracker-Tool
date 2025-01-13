	package com.financialtools.service;
	
	import com.financialtools.model.Transaction;
	import org.apache.commons.csv.CSVFormat;
	import org.apache.commons.csv.CSVPrinter;
	import org.springframework.stereotype.Service;
	
	import java.io.IOException;
	import java.io.Writer;
	import java.util.List;
	
	@Service
	public class CsvExportService {
	
	    public void writeTransactionsToCsv(List<Transaction> transactions, Writer writer) throws IOException {
	        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("ID", "Date", "Type", "Category", "Amount", "Description"))) {
	            for (Transaction transaction : transactions) {
	                csvPrinter.printRecord(transaction.getId(), transaction.getDate(), transaction.getType(), transaction.getCategory(), transaction.getAmount(), transaction.getDescription());
	            }
	        }
	    }
	}
