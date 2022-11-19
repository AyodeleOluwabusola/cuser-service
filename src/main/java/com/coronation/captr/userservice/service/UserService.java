package com.coronation.captr.userservice.service;

import com.coronation.captr.userservice.entities.CTUser;
import com.coronation.captr.userservice.entities.CompanyProfile;
import com.coronation.captr.userservice.entities.Founder;
import com.coronation.captr.userservice.enums.IResponseEnum;
import com.coronation.captr.userservice.interfaces.IResponse;
import com.coronation.captr.userservice.interfaces.IResponseData;
import com.coronation.captr.userservice.pojo.MessagePojo;
import com.coronation.captr.userservice.pojo.ResponseData;
import com.coronation.captr.userservice.pojo.UserPojo;
import com.coronation.captr.userservice.pojo.request.CompanyProfileRequest;
import com.coronation.captr.userservice.pojo.request.FounderRequest;
import com.coronation.captr.userservice.pojo.request.UserRequest;
import com.coronation.captr.userservice.pojo.response.CompanyProfileResponse;
import com.coronation.captr.userservice.respositories.ICompanyProfileRepository;
import com.coronation.captr.userservice.respositories.IFounderRepository;
import com.coronation.captr.userservice.respositories.IUserRepository;
import com.coronation.captr.userservice.util.AppProperties;
import com.coronation.captr.userservice.util.CommonLogic;
import com.coronation.captr.userservice.util.Constants;
import com.coronation.captr.userservice.util.ProxyTransformer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author toyewole
 */

@Transactional
@Service
@Slf4j
public class UserService {

    @Autowired
    IUserRepository iUserRepository;

    @Autowired
    IFounderRepository iFounderRepository;

    @Autowired
    ICompanyProfileRepository iCompanyProfileRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CommonLogic commonLogic;

    @Autowired
    AppProperties appProperties;

    public IResponseData<UserPojo> createUser(UserRequest request) {

        IResponseData<UserPojo> response = isRequestValid(request);

        if (IResponseEnum.SUCCESS.getCode() != response.getCode()) {
            return response;
        }

        if (iUserRepository.existsUserByEmail(request.getEmail())) {
            response.setResponse(IResponseEnum.EMAIL_EXIST);
            return response;
        }

        CTUser user = new CTUser();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(commonLogic.hashPassword(request.getPassword()));
        String token = UUID.randomUUID().toString();
        user.setConfirmationCode(commonLogic.hashPassword(token));

        iUserRepository.save(user);

        sendEmailConfirmation(user, token);

        response.setData(ProxyTransformer.transformUserToUserPojo(user));
        response.setResponse(IResponseEnum.SUCCESS);
        return response;
    }

    private void sendEmailConfirmation(CTUser user, String token) {
        MessagePojo message = new MessagePojo();


        var expTime = LocalDateTime.now().plusMinutes(appProperties.getEmailConfirmationExpTime());
        String link = String.format(appProperties.getEmailConfirmationLink(), user.getEmail(), token);
        message.setMessageBody(String.format(appProperties.getEmailConfirmationMessage(), user.getFirstName(),
                link, expTime.format(Constants.DATE_TIME_FORMATTER)));
        message.setRecipient(user.getEmail());
        message.setSource("user-service");
        message.setSubject("Email Confirmation");
        message.setRequestTime(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));

        rabbitTemplate.convertAndSend(appProperties.getNotificationExchange(), appProperties.getRoutingKey(), message);

    }

    private IResponseData<UserPojo> isRequestValid(UserRequest request) {
        ResponseData<UserPojo> response = new ResponseData<>();
        if (StringUtils.isBlank(request.getEmail())) {
            response.setDescription("Kindly provide an Email Address ");
            return response;
        }

        if (StringUtils.isBlank(request.getFirstName())) {
            response.setDescription("Kindly provide a First name ");
            return response;
        }

        if (StringUtils.isBlank(request.getLastName())) {
            response.setDescription("Kindly provide a Last name");
            return response;
        }

        if (StringUtils.isBlank(request.getPassword())) {
            response.setDescription("Kindly provide a password");
            return response;
        }

        if (StringUtils.equalsIgnoreCase(request.getPassword(), request.getConfirmPassword())) {
            response.setDescription("Password and confirm password must the same ");
            return response;
        }

        return response;
    }

    public IResponse createCompanyProfile(CompanyProfileRequest request) {

        CompanyProfile companyProfile = new CompanyProfile();
        if (request.getCompanyProfileId() != null) {
            Optional<CompanyProfile> existingCompanyProfile = iCompanyProfileRepository.findById(request.getCompanyProfileId());
            if (existingCompanyProfile.isPresent()) {
                companyProfile = existingCompanyProfile.get();
            }
        }

        companyProfile.setCompanyName(request.getCompanyName());
        companyProfile.setCompanyType(request.getCompanyType());
        companyProfile.setIncorporationDate(request.getIncorporationDate());
        companyProfile.setCountryIncorporated(request.getCountryIncorporated());
        companyProfile.setCurrency(request.getCurrency());
        companyProfile.setTotalAuthorisedShares(request.getTotalAuthorisedShares());
        companyProfile.setParValue(request.getParValue());
        companyProfile.setUser(iUserRepository.getReferenceById(request.getRequestingUser()));
        companyProfile.setStage(request.getStage());

        if (request.getFounders() != null && !request.getFounders().isEmpty()) {
            for (FounderRequest founderRequest : request.getFounders()) {
                Founder founder = new Founder();
                if (founderRequest.getFounderId() != null) {
                    Optional<Founder> existingFounder = iFounderRepository.findById(founderRequest.getFounderId());
                    if (existingFounder.isPresent()) {
                        founder = existingFounder.get();
                    }
                }

                Optional<CompanyProfile> existingCompanyProfile = iCompanyProfileRepository.findById(request.getCompanyProfileId());
                if (existingCompanyProfile.isPresent()) {
                    companyProfile = iCompanyProfileRepository.getReferenceById(request.getCompanyProfileId());
                }
                founder.setFirstName(founderRequest.getFirstName());
                founder.setLastName(founderRequest.getLastName());
                founder.setEmailAddress(founderRequest.getEmailAddress());
                founder.setTotalShares(founderRequest.getTotalShares());
                founder.setParValue(founderRequest.getPricePerShare());
                founder.setDateIssued(founderRequest.getDateIssued());
                founder.setEquityClass(founderRequest.getEquityClass());
                founder.setCompanyProfile(companyProfile);

                companyProfile.addFounder(founder);
            }
        }

        iCompanyProfileRepository.save(companyProfile);
        if (!StringUtils.equalsIgnoreCase(request.getStage(), Constants.FINAL)) {
            iUserRepository.setPendingRequestPk(companyProfile.getId(), request.getRequestingUser());
        }

        ResponseData response = new ResponseData();
        response.setResponse(IResponseEnum.SUCCESS);
        return response;
    }

    public IResponse retrieveCompanyProfile(Long userId) {

        CompanyProfileResponse response = new CompanyProfileResponse();
        ResponseData<CompanyProfileResponse> responseData = new ResponseData<>();
        Optional<CTUser> ctUser = iUserRepository.findById(userId);
        if (ctUser.isEmpty()) {
            responseData.setResponse(IResponseEnum.NO_USER_FOUND);
            return responseData;
        }

        Long pendingRequestPk = ctUser.get().getPendingRequestPk();
        if (pendingRequestPk == null) {
            responseData.setResponse(IResponseEnum.NO_PENDING_REQUEST);
            return responseData;
        }

        Optional<CompanyProfile> companyProfile = iCompanyProfileRepository.findById(pendingRequestPk);
        if (companyProfile.isEmpty()) {
            responseData.setResponse(IResponseEnum.NO_PENDING_REQUEST);
            return responseData;
        }

        CompanyProfile profile = companyProfile.get();

        response.setCompanyProfileId(profile.getId());
        response.setCompanyName(profile.getCompanyName());
        response.setCompanyType(profile.getCompanyType());
        response.setIncorporationDate(profile.getIncorporationDate().format(Constants.DATE_FORMATTER));
        response.setCountryIncorporated(profile.getCountryIncorporated());
        response.setCurrency(profile.getCurrency());
        response.setTotalAuthorisedShares(String.valueOf(profile.getTotalAuthorisedShares()));
        response.setParValue(profile.getParValue());
        response.setStage(profile.getStage());
        response.setRequestingUser(profile.getUser().getId());

        List<FounderRequest> founders = new ArrayList<>();
        for (Founder founder : profile.getFounders()) {
            FounderRequest founderRequest = new FounderRequest();
            founderRequest.setFounderId(founder.getId());
            founderRequest.setFirstName(founder.getFirstName());
            founderRequest.setLastName(founder.getLastName());
            founderRequest.setEmailAddress(founder.getEmailAddress());
            founderRequest.setTotalShares(founder.getTotalShares());
            founderRequest.setPricePerShare(founder.getParValue());
            founderRequest.setDateIssued(founder.getDateIssued());
            founderRequest.setEquityClass(founder.getEquityClass());

            founders.add(founderRequest);
        }

        response.setFounders(founders);

        responseData.setResponse(IResponseEnum.SUCCESS);
        responseData.setData(response);
        return responseData;
    }
}
