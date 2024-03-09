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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServicesServicesImpl implements ServicesService {
    @Autowired
    ServicesRepository servicesRepository;

    @Override
    public ResponseEntity<ResponseObj> getAll(){
        try{
            List<Services> servicesList = servicesRepository.findAllByIsActiveIsTrue();
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
    public ResponseEntity<ResponseObj> create(ServicesRequest servicesRequest){
        if(servicesRepository.existsServiceByServiceName(servicesRequest.getServiceName())){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ResponseObj(HttpStatus.ALREADY_REPORTED.toString(),"Service name has already exist", null));
        }
        Services services = new Services();
        services.setServiceName(servicesRequest.getServiceName());
        services.setDescription(servicesRequest.getDescription());
        services.setPricing(servicesRequest.getPricing());
        services.setActive(true);
        services.setCreateAt(LocalDateTime.now());
        servicesRepository.save(services);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", services));

    }

    public ResponseEntity<ResponseObj> update(Long id, ServicesRequest servicesRequest){
        Optional<Services> existServices = servicesRepository.findById(id);

        if (existServices.isPresent()){
            existServices.get().setServiceName(servicesRequest.getServiceName() == null ? existServices.get().getServiceName() : servicesRequest.getServiceName());
            existServices.get().setDescription(servicesRequest.getDescription() == null ? existServices.get().getDescription(): servicesRequest.getDescription());
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
            services.get().setDeleteAt(LocalDateTime.now());
            services.get().setActive(false);
            servicesRepository.save(services.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Service does not exist", null));
    }

}
