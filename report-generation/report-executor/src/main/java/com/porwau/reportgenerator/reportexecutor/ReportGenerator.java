package com.porwau.reportgenerator.reportexecutor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.porwau.reportgenerator.common.model.Transaction;
import com.porwau.reportgenerator.common.utilities.EncryptDecrypt;

public class ReportGenerator {
	Logger logger = LoggerFactory.getLogger("ReportGenerator.class");
    public static void main ( String[] args ) {

		List<Transaction> transactions = getTransactions();

        ReportGenerator reportGen = new ReportGenerator();
        reportGen.generateReport(transactions);
        
    }
	private static List<Transaction> getTransactions() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<Transaction>> responseEntity = restTemplate.exchange("http://localhost:8111/transactions",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Transaction>>() {
				});
		List<Transaction> transactions = responseEntity.getBody();
		return transactions;
	}
    private void generateReport (List<Transaction> transactions) {
        logger.info("Report Generation started at " + ZonedDateTime.now());
        Runnable task = new Runnable() {
            @Override
            public void run () {
                Instant start = Instant.now();
                String dateTimeLabel = OffsetDateTime.now(ZoneId.systemDefault()).truncatedTo( ChronoUnit.MINUTES ).format( DateTimeFormatter.ofPattern( "uuuuMMdd'T'HHmmX" , Locale.US ) );
                String fileNamePath = "BankingReport_" + dateTimeLabel + ".csv";
                try ( 
                BufferedWriter writer = new BufferedWriter( new FileWriter( fileNamePath ) ) ;
                CSVPrinter csvPrinter = new CSVPrinter( writer , CSVFormat.DEFAULT.withHeader( "TransactionID" , "BankID" , "Transaction" , "Timestamp" ) ) ;
                ) {
            		transactions.forEach(transaction -> {
            			String url = "http://localhost:8112/keys/" + transaction.getBankId();
            			String symmKey = new RestTemplate().getForObject(url, String.class);
            			try {
                            csvPrinter.printRecord( transaction.getTransactionId(), transaction.getBankId() , EncryptDecrypt.decrypt(transaction.getEncryptedData(),
            						EncryptDecrypt.createSecretKey(symmKey.toCharArray())), transaction.getUnixtime());

            			} catch (Exception e) {
            				e.printStackTrace();
            			}
            		});

                    csvPrinter.flush();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
                logger.info( "Report generation ending its run at " + ZonedDateTime.now());
            }
        };

        // Schedule this task every 1 minute for running. This ends after 2 minutes. Adjust as desired.
     // Using a single thread here, as we have only a single series of tasks to be executed, no multi-tasking.
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();   
        try {
            scheduledExecutorService.scheduleAtFixedRate( task , 0 , 1 , TimeUnit.MINUTES );  
            try {
                Thread.sleep( TimeUnit.MINUTES.toMillis( 2 ) );  // Sleep this main thread to let our scheduled task complete few iterations.
            } catch ( InterruptedException e ) {
                logger.error( "Main thread is interrupted. " );
                e.printStackTrace();
            }
        } finally {
            logger.info( "Shutting down the scheduledExecutorService at " + ZonedDateTime.now() );
            scheduledExecutorService.shutdown();  
        }
    }
}