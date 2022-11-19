package com.coronation.captr.userservice.pojo.response;

import com.coronation.captr.userservice.pojo.request.FounderRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyProfileResponse {

    private Long companyProfileId;

    private String companyName;

    private String companyType;

    private String incorporationDate;

    private String countryIncorporated;

    private String currency;

    private String totalAuthorisedShares;

    private Long parValue;

    private Long requestingUser;

    private String stage;

    private List<FounderRequest> founders;

}
