package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.config.PaymentConfig;
import com.bookingBirthday.bookingbirthdayforkids.dto.request.PaymentRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.*;
import com.bookingBirthday.bookingbirthdayforkids.service.PaymentService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.text.SimpleDateFormat;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    private PaymentConfig paymentConfig;

    @Autowired
    PartyBookingRepository partyBookingRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public ResponseEntity<ResponseObj> getAll() {
        try {
            List<Payment> paymentList = paymentRepository.findAll();
            if (paymentList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", paymentList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public void paymentSuccess(Long id) {
        Optional <PartyBooking> partyBooking = partyBookingRepository.findById(id);

        partyBooking.get().setStatus(StatusEnum.CONFIRMED);
        partyBookingRepository.save(partyBooking.get());
    }

    public String payWithVNPAYOnline(Long id, Long paymentMethodId,HttpServletRequest request) throws UnsupportedEncodingException{
        Optional<PartyBooking> partyBookingOptional = partyBookingRepository.findById(id);



        if(partyBookingOptional.isPresent()){
            if(partyBookingOptional.get().getStatus().equals(StatusEnum.PENDING)){

                for(Payment payment : partyBookingOptional.get().getPaymentList()){
                    if(payment.getStatus().equals("SUCCESS")){
                        return "success";
                    }
                }

                Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

                Optional<PartyBooking> partyBooking = partyBookingRepository.findById(id);
                int vnp_Amount = (int) partyBooking.get().getPackageInVenue().getApackage().getPricing();

                for(UpgradeService upgradeService : partyBooking.get().getUpgradeServices()){
                    vnp_Amount += (int) (upgradeService.getServices().getPricing() * upgradeService.getCount());
                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                String vnp_CreateDate = formatter.format(cld.getTime());
                cld.add(Calendar.MINUTE, 10);

                String vnp_ExpireDate = formatter.format(cld.getTime());

                Map<String, String> vnp_Params = new HashMap<>();
                vnp_Params.put("vnp_Version", PaymentConfig.vnp_Version);
                vnp_Params.put("vnp_Command", PaymentConfig.vnp_Command);
                vnp_Params.put("vnp_TmnCode", PaymentConfig.vnp_TmnCode);
                vnp_Params.put("vnp_Amount", String.valueOf(String.valueOf(vnp_Amount) + "00"));
                vnp_Params.put("vnp_BankCode",  PaymentConfig.vnp_BankCode);
                vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
                vnp_Params.put("vnp_CurrCode", PaymentConfig.vnp_CurrCode);
                vnp_Params.put("vnp_IpAddr", PaymentConfig.getIpAddress(request));
                vnp_Params.put("vnp_Locale", PaymentConfig.vnp_Locale);
                vnp_Params.put("vnp_OrderInfo", String.valueOf(id));
                vnp_Params.put("vnp_OrderType", String.valueOf(paymentMethodId));
                vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);
                vnp_Params.put("vnp_TxnRef", "HD" + RandomStringUtils.randomNumeric(6) + "-" + vnp_CreateDate);
                vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

                List fieldList = new ArrayList(vnp_Params.keySet());
                Collections.sort(fieldList);

                StringBuilder hashData = new StringBuilder();
                StringBuilder query = new StringBuilder();

                Iterator itr = fieldList.iterator();
                while (itr.hasNext()) {
                    String fieldName = (String) itr.next();
                    String fieldValue = vnp_Params.get(fieldName);
                    if (fieldValue != null && (fieldValue.length() > 0)) {
                        hashData.append(fieldName);
                        hashData.append("=");
                        hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                        query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                        query.append("=");
                        query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                        if (itr.hasNext()) {
                            query.append("&");
                            hashData.append("&");
                        }
                    }
                }
                String queryUrl = query.toString();
                String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
                queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
                String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;

                return paymentUrl;
            }

        }
        return "success";

    }



    @Override
    public ResponseEntity<ResponseObj> getById(Long id) {
        try {
            Optional<Payment> payment = paymentRepository.findById(id);
            if(payment.isPresent())
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, payment));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Payment does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

//    @Override
//    public ResponseEntity<ResponseObj> create(PaymentRequest paymentRequest) {
//        Payment payment = new Payment();
//        Account account = accountRepository.findById(paymentRequest.getAccountID()).get();
//        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentRequest.getPaymentMethodID()).get();
//        PartyBooking partyBooking = partyBookingRepository.findById(paymentRequest.getBookingID()).get();
//        payment.setAccount(account);
//        payment.setPaymentMethod(paymentMethod);
//        payment.setPartyBooking(partyBooking);
//        payment.setCreateAt(LocalDateTime.now());
//        payment.setStatus(StatusEnum.PENDING);
//        payment.setActive(true);
//
//        LocalDateTime expireDate =  payment.getCreateAt().plus(30, ChronoUnit.DAYS);
//        payment.setExpireDate(expireDate);
//        paymentRepository.save(payment);
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(),"Create successful", payment));
//    }
//
//    @Override
//    public ResponseEntity<ResponseObj> update(Long id, PaymentRequest paymentRequest) {
//        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentRequest.getPaymentMethodID()).get();
//        Account account = accountRepository.findById(paymentRequest.getAccountID()).get();
//        PartyBooking partyBooking = partyBookingRepository.findById(paymentRequest.getBookingID()).get();
//        Optional<Payment> existPayment  = paymentRepository.findById(id);
//        if (existPayment.isPresent()){
//            existPayment.get().setPaymentMethod(paymentMethod == null ? existPayment.get().getPaymentMethod() : paymentMethod);
//            existPayment.get().setAccount(account == null ? existPayment.get().getAccount() : account);
//            existPayment.get().setPartyBooking(partyBooking == null ? existPayment.get().getPartyBooking() : partyBooking);
//            existPayment.get().setUpdateAt(LocalDateTime.now());
//            existPayment.get().setStatus(StatusEnum.PENDING);
//            LocalDateTime expireDate =  existPayment.get().getUpdateAt().plus(30, ChronoUnit.DAYS);
//            existPayment.get().setExpireDate(expireDate);
//            paymentRepository.save(existPayment.get());
//            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existPayment));
//        }
//        else
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Payment does not exist", null));
//
//    }
//
//    @Override
//    public ResponseEntity<ResponseObj> delete(Long id) {
//        Optional<Payment> payment = paymentRepository.findById(id);
//        if (payment.isPresent()){
//            payment.get().setActive(false);
//            payment.get().setDeleteAt(LocalDateTime.now());
//            paymentRepository.save(payment.get());
//            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
//        }
//        else
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Payment does not exist", null));
//    }
}
