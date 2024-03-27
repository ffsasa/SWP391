package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.Review;
import com.bookingBirthday.bookingbirthdayforkids.model.Services;
import com.bookingBirthday.bookingbirthdayforkids.model.TypeEnum;
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
        try {
            List<Services> servicesList = servicesRepository.findAllByIsActiveIsTrue();
            if (servicesList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", servicesList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    //thêm

    @Override
    public ResponseEntity<ResponseObj> getAllServiceByType(TypeEnum typeEnum){
        try {
            List<Services> servicesList = servicesRepository.findAllByServiceTypeAndIsActiveIsTrue(typeEnum);
            if (servicesList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", servicesList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }



    @Override
    public ResponseEntity<ResponseObj> getAllForHost(){
        try {
            List<Services> servicesList = servicesRepository.findAll();
            if (servicesList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "List is empty", null));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", servicesList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public  ResponseEntity<ResponseObj> getById_ForCustomer(Long id){
        try{
            Optional<Services> services = servicesRepository.findById(id);
            if(services.isPresent() && services.get().isActive() == true){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", services));
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This service not exist", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
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

    //sửa

    @Override
    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String serviceName, String description, float pricing, TypeEnum typeEnum){
        if(servicesRepository.existsServiceByServiceName(serviceName)){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(),"Service name has already exist", null));
        }
        Services services = new Services();
        try {
            if (imgFile != null) {
                String img = "";
                switch (typeEnum){
                    case FOOD:
                        img = firebaseService.uploadImage(imgFile);
                        services.setServiceName(serviceName);
                        services.setServiceDescription(description);
                        services.setPricing(pricing);
                        services.setServiceImgUrl(img);
                        services.setActive(true);
                        services.setCreateAt(LocalDateTime.now());
                        services.setUpdateAt(LocalDateTime.now());
                        servicesRepository.save(services);
                        break;
                    case DECORATION:
                        img = firebaseService.uploadImage(imgFile);
                        services.setServiceName(serviceName);
                        services.setServiceDescription(description);
                        services.setPricing(pricing);
                        services.setServiceImgUrl(img);
                        services.setActive(true);
                        services.setCreateAt(LocalDateTime.now());
                        services.setUpdateAt(LocalDateTime.now());
                        servicesRepository.save(services);
                        break;
                }
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));

        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", services));
    }


    //sửa
    @Override
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String serviceName, String description, float pricing,TypeEnum typeEnum){
        Optional<Services> services = servicesRepository.findById(id);
        if (!services.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This service does not exist", null));
        }
        try {

            switch (typeEnum) {
                case FOOD:
                    services.get().setServiceName(serviceName == null ? services.get().getServiceName() : serviceName);
                    services.get().setServiceDescription(description == null ? services.get().getServiceDescription() : description);
                    services.get().setServiceImgUrl(imgFile == null ? services.get().getServiceImgUrl() : firebaseService.uploadImage(imgFile));
                    services.get().setPricing(pricing == 0 ? services.get().getPricing() : pricing);
                    services.get().setUpdateAt(LocalDateTime.now());
                    servicesRepository.save(services.get());
                    break;
                case DECORATION:
                    services.get().setServiceName(serviceName == null ? services.get().getServiceName() : serviceName);
                    services.get().setServiceDescription(description == null ? services.get().getServiceDescription() : description);
                    services.get().setServiceImgUrl(imgFile == null ? services.get().getServiceImgUrl() : firebaseService.uploadImage(imgFile));
                    services.get().setPricing(pricing == 0 ? services.get().getPricing() : pricing);
                    services.get().setUpdateAt(LocalDateTime.now());
                    servicesRepository.save(services.get());
                   break;
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", services));

    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        try {
            Optional<Services> services = servicesRepository.findById(id);
            if (services.isPresent()) {
                services.get().setActive(false);
                services.get().setDeleteAt(LocalDateTime.now());
                servicesRepository.save(services.get());
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This services does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

}
