package com.examen.springintegrationapplication.services;

import com.examen.springintegrationapplication.domain.Transaction;
import com.examen.springintegrationapplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public void processFile(File file) throws IOException {
        // Implement the logic to read the file and save transactions to the database
        // For example, you can read the file line by line, parse each line into a Transaction object,
        // and then save it using transactionRepository.save(transaction);

        List<String> lines = Files.readAllLines(file.toPath());
        for (String line : lines) {
            String[] data = line.split(",");

            String transactionId = data[0];
            String accountNumber = data[1];
            double amount = Double.parseDouble(data[2]);
            String currency = data[3];
            // Simple validation
            if (amount <= 0) {
                System.out.println(
                        "Invalid transaction: " + transactionId);
                continue;
            }
            Transaction transaction = new Transaction();

            transaction.setTransactionId(transactionId);
            transaction.setAccountNumber(accountNumber);
            transaction.setAmount(amount);
            transaction.setCurrency(currency);

            transactionRepository.save(transaction);

            System.out.println(
                    "Transaction saved: " + transactionId);
        }
    }


}
