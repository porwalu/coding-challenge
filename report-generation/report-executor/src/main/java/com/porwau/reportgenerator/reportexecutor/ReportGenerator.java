package com.porwau.reportgenerator.reportexecutor;

import java.io.BufferedWriter;
import java.io.FileWriter;
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

import com.porwau.reportgenerator.common.model.Transaction;
import com.porwau.reportgenerator.common.utilities.EncryptDecrypt;
import com.porwau.reportgenerator.reportexecutor.remote.KeysForDecryptionFetcher;
import com.porwau.reportgenerator.reportexecutor.remote.TransactionsFetcher;

/**
 * @author Utkarsh Porwal<br>
 *         This is the main Report application which does the follow : <br>
 *         1)Gets the transaction from DB1 (the data comes as encrypted)<br>
 *         2)Parses the data to get the bank id and get the key corresponding to
 *         this bank id from DB2<br>
 *         3)It decrypts the transaction data and creates it in a csv file.<br>
 *         4)There is a schedule which runs to generated reports multiple times.
 *         I have made it 1 minute as of now for the purpose of testing.
 */
public class ReportGenerator {

	private static Logger logger = LoggerFactory.getLogger("ReportGenerator.class");
	private KeysForDecryptionFetcher keysForDecryptionFetcher = new KeysForDecryptionFetcher();
	private TransactionsFetcher transactionFetcher = new TransactionsFetcher();

	public static void main(String[] args) {
		ReportGenerator reportGen = new ReportGenerator();
		reportGen.launchScheduler();

	}

	private void launchScheduler() {
		logger.info("Report Generation scheduler launched at " + ZonedDateTime.now());
		Runnable task = new Runnable() {
			@Override
			public void run() {
				String dateTimeLabel = OffsetDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MINUTES)
						.format(DateTimeFormatter.ofPattern("uuuuMMdd'T'HHmmX", Locale.US));
				String fileNamePath = "BankingReport_" + dateTimeLabel + ".csv";
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileNamePath));
						CSVPrinter csvPrinter = new CSVPrinter(writer,
								CSVFormat.DEFAULT.withHeader("TransactionID", "BankID", "Transaction", "Timestamp"));) {
					List<Transaction> transactions = transactionFetcher.getAllTransactions();
					transactions.forEach(transaction -> {
						String symmKey = keysForDecryptionFetcher.getDecryptionKey(transaction.getBankId());
						try {
							csvPrinter.printRecord(transaction.getTransactionId(), transaction.getBankId(),
									EncryptDecrypt.decrypt(transaction.getEncryptedData(),
											EncryptDecrypt.createSecretKey(symmKey.toCharArray())),
									transaction.getUnixtime());

						} catch (Exception e) {
							logger.error("Error printing the csv", e);
						}
					});

					csvPrinter.flush();
				} catch (Exception e) {
					logger.error("Error running the scheduler", e);
				}
				logger.info("Report generation ending its run at " + ZonedDateTime.now());
			}
		};

		// Schedule this task every 1 minute for running. This ends after 2 minutes.
		// Adjust as desired.
		// Using a single thread here, as we have only a single series of tasks to be
		// executed.
		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		try {
			scheduledExecutorService.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
			try {
				Thread.sleep(TimeUnit.MINUTES.toMillis(2)); // Sleep this main thread to let our scheduled task complete
															// few iterations.
			} catch (InterruptedException e) {
				logger.error("Main thread is interrupted. ");
				e.printStackTrace();
			}
		} finally {
			logger.info("Shutting down the scheduledExecutorService at " + ZonedDateTime.now());
			scheduledExecutorService.shutdown();
		}
	}
}