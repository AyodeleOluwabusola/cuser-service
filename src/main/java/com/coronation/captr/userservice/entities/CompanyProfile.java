package com.coronation.captr.userservice.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author toyewole
 */

@Getter
@Setter
@Entity
@Table(name = "COMPANY_PROFILE")
public class CompanyProfile extends BaseEntity {

    @Column(name = "COMPANY_NAME", nullable = false)
    private String companyName;

    @Column(name = "COMPANY_TYPE", nullable = false)
    private String companyType;

    @Column(name = "INCORPORATION_DATE", nullable = false)
    private LocalDate incorporationDate;

    @Column(name = "CURRENCY", nullable = false)
    private String countryIncorporated;

    @Column(name = "COUNTRY_INCORPORATEDE", nullable = false)
    private String currency;

    @Column(name = "STAGE")
    private String stage;

    @Column(name = "TOTAL_SHARES")
    private Long totalAuthorisedShares;

    @Column(name = "PAR_VALUE")
    private Long parValue;

    @ManyToOne
    @JoinColumn(name = "USER_FK", nullable = false)
    private CTUser user;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "COMPANY_PROFILE_FOUNDER", joinColumns = @JoinColumn(name = "COMPANY_FK", referencedColumnName = "ID"),
            inverseJoinColumns= @JoinColumn(name = "FOUNDER_FK", referencedColumnName = "ID"))
    @Cascade({CascadeType.ALL})
    private Set<Founder> founders;

    public boolean addFounder(Founder founder) {
        if(founders == null){
            founders = new HashSet<Founder>();
        }
        if(!founders.contains(founder)){
            return founders.add(founder);
        }
        return false;
    }

    public Set<Founder> getFounders() {
        if(founders == null){
            founders = new HashSet<Founder>();
        }
        return founders;
    }

}
