package com.bookingBirthday.bookingbirthdayforkids.controller;


import com.bookingBirthday.bookingbirthdayforkids.dto.request.ServicesRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.TransactionRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.service.ServicesService;
import com.bookingBirthday.bookingbirthdayforkids.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @GetMapping("/getAll-transaction")
    public ResponseEntity<ResponseObj> getAll(){
        return transactionService.getAll();
    }

    @GetMapping("/getId-transaction/{id}")
    public ResponseEntity<ResponseObj> getByid(@PathVariable Long id){
        return transactionService.getById(id);
    }

    @PostMapping("/create-transaction")
    public ResponseEntity<ResponseObj> create(@RequestBody TransactionRequest transactionRequest){
        return  transactionService.create(transactionRequest);
    }

    @PutMapping("/update-transaction/{id}")
    public ResponseEntity<ResponseObj> update(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest){
        return transactionService.update(id, transactionRequest);
    }
    @DeleteMapping("/delete-transaction/{id}")
    public ResponseEntity<ResponseObj> delete(@PathVariable Long id){
        return transactionService.delete(id);
    }


}
