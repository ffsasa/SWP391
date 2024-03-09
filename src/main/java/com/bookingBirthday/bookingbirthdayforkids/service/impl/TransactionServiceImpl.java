package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.PaymentRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.TransactionRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.model.Package;
import com.bookingBirthday.bookingbirthdayforkids.repository.PaymentRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.TransactionRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    PaymentRepository paymentRepository;
    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try{
            List<Transaction> transactionList = transactionRepository.findAllByIsActiveIsTrue();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, transactionList));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "List is empty", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<Transaction> transaction = transactionRepository.findById(id);
            if(transaction.isPresent())
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, transaction));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Transaction does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        Payment payment = paymentRepository.findById(transactionRequest.getPaymentID()).get();
        transaction.setPayment(payment);
        transaction.setCreateAt(LocalDateTime.now());
        transaction.setStatus(StatusEnum.PENDING);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setActive(true);
        transactionRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(),"Create successful", transaction));
    }


    @Override
    public ResponseEntity<ResponseObj> update(Long id, TransactionRequest transactionRequest) {
        Payment payment = paymentRepository.findById(transactionRequest.getPaymentID()).get();
        Optional<Transaction> existTransaction  = transactionRepository.findById(id);
        if (existTransaction.isPresent()){
            existTransaction.get().setPayment(payment == null ? existTransaction.get().getPayment() : payment);
            existTransaction.get().setUpdateAt(LocalDateTime.now());
            existTransaction.get().setTransactionDate(LocalDateTime.now());
            existTransaction.get().setStatus(StatusEnum.PENDING);
            transactionRepository.save(existTransaction.get());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existTransaction));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Transaction does not exist", null));

    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()){
            transaction.get().setActive(false);
            transaction.get().setDeleteAt(LocalDateTime.now());
            transactionRepository.save(transaction.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Package Services does not exist", null));
    }
}
