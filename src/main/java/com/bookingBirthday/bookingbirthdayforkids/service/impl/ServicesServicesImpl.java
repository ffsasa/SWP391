package com.bookingBirthday.bookingbirthdayforkids.service.impl;

import com.bookingBirthday.bookingbirthdayforkids.dto.response.ResponseObj;
import com.bookingBirthday.bookingbirthdayforkids.model.*;
import com.bookingBirthday.bookingbirthdayforkids.repository.AccountRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.RoleRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.ServicesRepository;
import com.bookingBirthday.bookingbirthdayforkids.repository.VenueRepository;
import com.bookingBirthday.bookingbirthdayforkids.service.ServicesService;
import com.bookingBirthday.bookingbirthdayforkids.util.AuthenUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicesServicesImpl implements ServicesService {
    @Autowired
    ServicesRepository servicesRepository;

    @Autowired
    VenueRepository venueRepository;
    @Autowired
    FirebaseService firebaseService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public ResponseEntity<ResponseObj> getAllServiceByVenue(Long venueId) {
        try {
            Optional<Venue> venue = venueRepository.findById(venueId);
            if (!venue.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
            }
            List<Services> servicesList = servicesRepository.findAllByIsActiveIsTrue();
            List<Services> servicesListAccount = new ArrayList<>();
            for (Services services : servicesList) {
                if (services.getAccount().getId().equals(venue.get().getAccount().getId())) {
                    servicesListAccount.add(services);
                }
            }
            if (servicesListAccount.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", servicesListAccount));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", servicesListAccount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    //thêm

    @Override
    public ResponseEntity<ResponseObj> getAllServiceByTypeByVenue(TypeEnum typeEnum, Long venueId) {
        try {
            Optional<Venue> venue = venueRepository.findById(venueId);
            if (!venue.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
            }
            List<Services> servicesListAccountByType = new ArrayList<>();
            List<Services> servicesList = servicesRepository.findAllByServiceTypeAndIsActiveIsTrue(typeEnum);
            for (Services services : servicesList) {
                if (services.getAccount().getId().equals(venue.get().getAccount().getId())) {
                    servicesListAccountByType.add(services);
                }
            }
            if (servicesListAccountByType.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", servicesListAccountByType));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", servicesListAccountByType));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> enable(Long serviceId){
        Long userId = AuthenUtil.getCurrentUserId();
        Optional<Account> account = accountRepository.findById(userId);
        List<Services> servicesList = account.get().getServicesList();
        for(Services services : servicesList){
            if(services.getId().equals(serviceId)){
                services.setActive(true);
                servicesRepository.save(services);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Enable successfully", services));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This service dose not exist", null));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllServiceTypeByHost(TypeEnum typeEnum) {
        try {
            Long useId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(useId);
            List<Services> servicesListType = servicesRepository.findAllByServiceType(typeEnum);
            List<Services> servicesListTypeByHost = new ArrayList<>();
            for (Services services : servicesListType) {
                if (services.getAccount().getId().equals(account.get().getId())) {
                    servicesListTypeByHost.add(services);
                }
            }
            if (servicesListTypeByHost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", servicesListTypeByHost));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", servicesListTypeByHost));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllServiceTypeIsTrueByHost(TypeEnum typeEnum) {
        try {
            Long useId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(useId);
            List<Services> servicesListType = servicesRepository.findAllByServiceType(typeEnum);
            List<Services> servicesListTypeByHost = new ArrayList<>();
            List<Services> servicesListTypeIstrueByHost = new ArrayList<>();
            for (Services services : servicesListType) {
                if (services.getAccount().getId().equals(account.get().getId())) {
                    servicesListTypeByHost.add(services);
                }
            }
            if (servicesListTypeByHost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", servicesListTypeByHost));
            }
            for(Services services : servicesListTypeByHost){
                if(services.isActive()){
                    servicesListTypeIstrueByHost.add(services);
                }
            }
            if(servicesListTypeIstrueByHost.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", servicesListTypeIstrueByHost));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", servicesListTypeIstrueByHost));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getAllServiceTypeIsFalseByHost(TypeEnum typeEnum) {
        try {
            Long useId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(useId);
            List<Services> servicesListType = servicesRepository.findAllByServiceType(typeEnum);
            List<Services> servicesListTypeByHost = new ArrayList<>();
            List<Services> servicesListTypeIstrueByHost = new ArrayList<>();
            for (Services services : servicesListType) {
                if (services.getAccount().getId().equals(account.get().getId())) {
                    servicesListTypeByHost.add(services);
                }
            }
            if (servicesListTypeByHost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", servicesListTypeByHost));
            }
            for(Services services : servicesListTypeByHost){
                if(!services.isActive()){
                    servicesListTypeIstrueByHost.add(services);
                }
            }
            if(servicesListTypeIstrueByHost.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", servicesListTypeIstrueByHost));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", servicesListTypeIstrueByHost));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }


    @Override
    public ResponseEntity<ResponseObj> getAllServiceIsActiveTrueForHost(){
        Long userId = AuthenUtil.getCurrentUserId();
        Optional <Account> account = accountRepository.findById(userId);
        List<Services> servicesList = account.get().getServicesList();
        List<Services> servicesListActive = new ArrayList<>();
        for(Services services : servicesList){
            if(services.isActive()){
                servicesListActive.add(services);
            }
        }
        if(servicesListActive.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", servicesListActive));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", servicesListActive));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllServiceIsActiveFalseForHost(){
        Long userId = AuthenUtil.getCurrentUserId();
        Optional <Account> account = accountRepository.findById(userId);
        List<Services> servicesList = account.get().getServicesList();
        List<Services> servicesListActive = new ArrayList<>();
        for(Services services : servicesList){
            if(!services.isActive()){
                servicesListActive.add(services);
            }
        }
        if(servicesListActive.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", servicesListActive));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", servicesListActive));
    }

    @Override
    public ResponseEntity<ResponseObj> getAllForHost() {
        try {
            Long userId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(userId);
            List<Services> servicesListByHost = new ArrayList<>();
            List<Services> servicesList = servicesRepository.findAll();
            for (Services services : servicesList) {
                if (services.getAccount().getId().equals(account.get().getId())) {
                    servicesListByHost.add(services);
                }
            }
            if (servicesListByHost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "List is empty", servicesListByHost));
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Ok", servicesListByHost));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getServiceByIdForCustomerByVenue(Long venueId, Long serviceId) {
        try {
            Optional<Venue> venue = venueRepository.findById(venueId);
            Optional<Services> services = servicesRepository.findById(serviceId);
            if (!venue.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "Venue does not exist", null));
            }
            if(services.get().getAccount().getId().equals(venue.get().getAccount().getId())){
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Ok", services.get()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Service does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    @Override
    public ResponseEntity<ResponseObj> getByIdByHost(Long id) {
        try {
            Long useId = AuthenUtil.getCurrentUserId();
            Optional<Account> account = accountRepository.findById(useId);
            List<Services> servicesList = account.get().getServicesList();
            for(Services services : servicesList){
                if(services.getId().equals(id)){
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), null, services));
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "Service does not exist", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

    //sửa

    @Override
    public ResponseEntity<ResponseObj> create(MultipartFile imgFile, String serviceName, String description, float pricing, TypeEnum typeEnum) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Role role = roleRepository.findByName(RoleEnum.HOST);
        if (!role.equals(account.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObj(HttpStatus.FORBIDDEN.toString(), "User not permission create service", null));
        }

        Services services = new Services();
        try {
            if (imgFile != null) {
                String img = "";
                switch (typeEnum) {
                    case FOOD, DECORATION:
                        img = firebaseService.uploadImage(imgFile);
                        services.setServiceName(serviceName);
                        services.setServiceDescription(description);
                        services.setPricing(pricing);
                        services.setServiceImgUrl(img);
                        services.setActive(true);
                        services.setCreateAt(LocalDateTime.now());
                        services.setUpdateAt(LocalDateTime.now());
                        services.setAccount(account);
                        services.setServiceType(typeEnum);
                        servicesRepository.save(services);
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Image is invalid", null));

        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Create successful", services));
    }


    //sửa
    @Override
    public ResponseEntity<ResponseObj> update(Long id, MultipartFile imgFile, String serviceName, String description, float pricing, TypeEnum typeEnum) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Optional<Services> services = servicesRepository.findById(id);
        if (!services.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This service does not exist", null));
        } else if (!services.get().getAccount().getId().equals(account.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "This service is not yours so you cannot update it", null));
        }
        try {
            switch (typeEnum) {
                case FOOD, DECORATION:
                    services.get().setServiceName(serviceName == null ? services.get().getServiceName() : serviceName);
                    services.get().setServiceDescription(description == null ? services.get().getServiceDescription() : description);
                    services.get().setServiceImgUrl(imgFile == null ? services.get().getServiceImgUrl() : firebaseService.uploadImage(imgFile));
                    services.get().setPricing(pricing == 0 ? services.get().getPricing() : pricing);
                    services.get().setUpdateAt(LocalDateTime.now());
                    services.get().setAccount(account);
                    services.get().setServiceType(typeEnum);
                    servicesRepository.save(services.get());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObj(HttpStatus.ACCEPTED.toString(), "Update successful", services));


    }

    @Override
    public ResponseEntity<ResponseObj> delete(Long id) {
        Long userId = AuthenUtil.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObj(HttpStatus.UNAUTHORIZED.toString(), "400", null));
        }
        Account account = accountRepository.findById(userId).get();
        Optional<Services> services = servicesRepository.findById(id);
        if (!services.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObj(HttpStatus.NOT_FOUND.toString(), "This service does not exist", null));
        } else if (!services.get().getAccount().getId().equals(account.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "This service is not yours so you cannot update it", null));
        }
        try {
            services.get().setActive(false);
            services.get().setDeleteAt(LocalDateTime.now());
            services.get().setAccount(account);
            servicesRepository.save(services.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj(HttpStatus.OK.toString(), "Delete successful", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObj(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Internal Server Error", null));
        }
    }

}
