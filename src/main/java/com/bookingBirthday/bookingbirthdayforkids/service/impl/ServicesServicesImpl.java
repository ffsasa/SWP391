package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.request.ServicesRequest;
import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Account;
import com.bookingBirthday.bookingbirthdayforkids.model.Services;
import com.bookingBirthday.bookingbirthdayforkids.repository.ServicesRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServicesServicesImpl implements ServicesService {
    @Autowired
    ServicesRepository servicesRepository;
    @Autowired
    FirebaseService firebaseService;

    @Override
    public ResponseEntity<ResponseObj> getAll(){
        try{
            List<Services> servicesList = servicesRepository.findAll();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, servicesList));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "List is empty", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getById(Long id){
        try {
            Optional<Services> services = servicesRepository.findById(id);
            if(services.isPresent())
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, services));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Service does not exist", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String serviceName, String description, float pricing){
        if(servicesRepository.existsServiceByServiceName(serviceName)){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(),"Service name has already exist", null));
        }
        Services services = new Services();
        try {
            if (imgFile != null) {
                String img = firebaseService.uploadImage(imgFile);
                services.setServiceName(serviceName);
                services.setServiceDescription(description);
                services.setPricing(pricing);
                services.setServiceImgUrl(img);
                services.setActive(true);
                services.setCreateAt(LocalDateTime.now());
                services.setUpdateAt(LocalDateTime.now());
                servicesRepository.save(services);
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));

        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", services));
    }

    public ResponseEntity<ResponseObj> update(Long id, ServicesRequest servicesRequest){
        Optional<Services> existServices = servicesRepository.findById(id);

        if (existServices.isPresent()){
            existServices.get().setServiceName(servicesRequest.getServiceName() == null ? existServices.get().getServiceName() : servicesRequest.getServiceName());
            existServices.get().setServiceDescription(servicesRequest.getDescription() == null ? existServices.get().getServiceDescription(): servicesRequest.getDescription());
            existServices.get().setPricing(servicesRequest.getPricing() == 0 ? existServices.get().getPricing() : servicesRequest.getPricing());
            existServices.get().setUpdateAt(LocalDateTime.now());
            servicesRepository.save(existServices.get());

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", existServices));

        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Service does not exist", null));

    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Optional<Services> services = servicesRepository.findById(id);
        if (services.isPresent()){
            servicesRepository.delete(services.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Service does not exist", null));
    }

}
