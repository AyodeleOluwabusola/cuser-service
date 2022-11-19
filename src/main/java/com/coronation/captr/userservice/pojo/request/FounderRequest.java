package com.coronation.captr.userservice.pojo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class FounderRequest implements IRequest{

    private Long founderId;

    @NotBlank(message = "First Name of the founder is mandatory")
    private String firstName;

    @NotBlank(message = "Last Name of the founder is mandatory")
    private String lastName;

    @NotBlank(message = "Email address of the founder is required")
    private String emailAddress;

    @NotNull(message = "Please specify total shares issued to the founder")
    private Long totalShares;

    @NotNull(message = "Please specify the price per share issued to the founder")
    private Long pricePerShare;

    @NotNull(message = "Please specify date the share was issued to the founder")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateIssued;

    @NotBlank(message = "Please specify the equity class of the founder")
    private String equityClass;
}
