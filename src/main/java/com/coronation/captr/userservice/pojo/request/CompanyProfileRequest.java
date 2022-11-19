package com.coronation.captr.userservice.pojo.request;

import com.coronation.captr.userservice.validator.NotNullIfAnotherFieldCertainHasValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @author AyodeleOluwabusola
 */
@Getter
@Setter
@NotNullIfAnotherFieldCertainHasValue(fieldName = "stage",  fieldValue = "2, FINAL", dependFieldName = "totalAuthorisedShares", message = "Total authorised shares issued to the company is mandatory")
@NotNullIfAnotherFieldCertainHasValue(fieldName = "stage",  fieldValue = "2, FINAL", dependFieldName = "parValue", message = "Par value is mandatory")
@NotNullIfAnotherFieldCertainHasValue(fieldName = "stage",  fieldValue = "FINAL", dependFieldName = "founders", message =  "Founder(s) must be provided")
public class CompanyProfileRequest implements IRequest{

    private Long companyProfileId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Company type is required")
    private String companyType;

    @NotNull(message = "Company Incorporation date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate incorporationDate;

    @NotBlank(message = "Please specify country where the company was incorporated")
    private String countryIncorporated;

    @NotBlank(message = "Specify currency the shares were issued in")
    private String currency;

    private Long totalAuthorisedShares;

    private Long parValue;

    @NotNull(message = "Requesting user is required")
    private Long requestingUser;

    @NotBlank(message = "Stage is required")
    private String stage;

    private List<@Valid FounderRequest> founders;
}
