package com.financialtools.controller;

import com.financialtools.model.Transaction;
import com.financialtools.repository.TransactionRepository;
import com.financialtools.service.CsvExportService;
import com.financialtools.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CsvExportService csvExportService;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Example: Send an email notification for transactions above a certain amount
        if (transaction.getAmount() > 1000) {
            emailService.sendSimpleMessage(
                "user@example.com",
                "High Value Transaction Alert",
                "A transaction of amount " + transaction.getAmount() + " has been made."
            );
        }

        return savedTransaction;
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable Long id, @RequestBody Transaction updatedTransaction) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transaction.setDate(updatedTransaction.getDate());
                    transaction.setType(updatedTransaction.getType());
                    transaction.setCategory(updatedTransaction.getCategory());
                    transaction.setAmount(updatedTransaction.getAmount());
                    transaction.setDescription(updatedTransaction.getDescription());
                    return transactionRepository.save(transaction);
                }).orElseGet(() -> {
                    updatedTransaction.setId(id);
                    return transactionRepository.save(updatedTransaction);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionRepository.deleteById(id);
    }

    @GetMapping("/export")
    public void exportTransactionsToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=transactions.csv");
        List<Transaction> transactions = transactionRepository.findAll();
        csvExportService.writeTransactionsToCsv(transactions, response.getWriter());
    }

    @GetMapping("/summary")
    public String getSummaryReport() {
        double totalIncome = 0;
        double totalExpense = 0;

        List<Transaction> transactions = transactionRepository.findAll();

        for (Transaction transaction : transactions) {
            if ("Income".equalsIgnoreCase(transaction.getType())) {
                totalIncome += transaction.getAmount();
            } else if ("Expense".equalsIgnoreCase(transaction.getType())) {
                totalExpense += transaction.getAmount();
            }
        }

        return "Summary Report:\nTotal Income: " + totalIncome + "\nTotal Expense: " + totalExpense + "\nNet Balance: " + (totalIncome - totalExpense);
    }
}
