package wlw.zc.demo.system.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Product {
    private Long id;

    private String organSign;

    private String productPref;

    private Long productId;

    private Long standardLibraryId;

    private String medicalCommonName;

    private String medicalProductCode;

    private Integer medicalStatus;

    private Integer medicalType;

    private String extend;

    private String platformType;

    private Integer dataSource;

    private Integer approveStatus;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;

    private Byte yn;

    private Integer version;

    private String mnemonicCode;

    private String pharmacyPref;

    private String commonName;

    private String productName;

    private String unit;

    private String attributeSpecification;

    private String dosageForm;

    private String approvalNumber;

    private String manufacturer;

    private Long channelId;

    private String smallUnit;

    private BigDecimal unitConversion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganSign() {
        return organSign;
    }

    public void setOrganSign(String organSign) {
        this.organSign = organSign == null ? null : organSign.trim();
    }

    public String getProductPref() {
        return productPref;
    }

    public void setProductPref(String productPref) {
        this.productPref = productPref == null ? null : productPref.trim();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getStandardLibraryId() {
        return standardLibraryId;
    }

    public void setStandardLibraryId(Long standardLibraryId) {
        this.standardLibraryId = standardLibraryId;
    }

    public String getMedicalCommonName() {
        return medicalCommonName;
    }

    public void setMedicalCommonName(String medicalCommonName) {
        this.medicalCommonName = medicalCommonName == null ? null : medicalCommonName.trim();
    }

    public String getMedicalProductCode() {
        return medicalProductCode;
    }

    public void setMedicalProductCode(String medicalProductCode) {
        this.medicalProductCode = medicalProductCode == null ? null : medicalProductCode.trim();
    }

    public Integer getMedicalStatus() {
        return medicalStatus;
    }

    public void setMedicalStatus(Integer medicalStatus) {
        this.medicalStatus = medicalStatus;
    }

    public Integer getMedicalType() {
        return medicalType;
    }

    public void setMedicalType(Integer medicalType) {
        this.medicalType = medicalType;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend == null ? null : extend.trim();
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType == null ? null : platformType.trim();
    }

    public Integer getDataSource() {
        return dataSource;
    }

    public void setDataSource(Integer dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Integer approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getYn() {
        return yn;
    }

    public void setYn(Byte yn) {
        this.yn = yn;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getMnemonicCode() {
        return mnemonicCode;
    }

    public void setMnemonicCode(String mnemonicCode) {
        this.mnemonicCode = mnemonicCode == null ? null : mnemonicCode.trim();
    }

    public String getPharmacyPref() {
        return pharmacyPref;
    }

    public void setPharmacyPref(String pharmacyPref) {
        this.pharmacyPref = pharmacyPref == null ? null : pharmacyPref.trim();
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName == null ? null : commonName.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getAttributeSpecification() {
        return attributeSpecification;
    }

    public void setAttributeSpecification(String attributeSpecification) {
        this.attributeSpecification = attributeSpecification == null ? null : attributeSpecification.trim();
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm == null ? null : dosageForm.trim();
    }

    public String getApprovalNumber() {
        return approvalNumber;
    }

    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber == null ? null : approvalNumber.trim();
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer == null ? null : manufacturer.trim();
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getSmallUnit() {
        return smallUnit;
    }

    public void setSmallUnit(String smallUnit) {
        this.smallUnit = smallUnit == null ? null : smallUnit.trim();
    }

    public BigDecimal getUnitConversion() {
        return unitConversion;
    }

    public void setUnitConversion(BigDecimal unitConversion) {
        this.unitConversion = unitConversion;
    }
}