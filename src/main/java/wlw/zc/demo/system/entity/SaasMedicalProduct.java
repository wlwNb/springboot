package wlw.zc.demo.system.entity;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

public class SaasMedicalProduct implements java.io.Serializable {
    /** 版本号 */
    private static final long serialVersionUID = 7849314541913684046L;

    /** 主键 */
    private Long id;

    /** 机构标识 */
    private String organSign;

    /**
     * 商品内码
     */
    private String productPref;

    /** 商品id */
    private Long productId;

    /** 标准库id */
    private Long standardLibraryId;

    /** 医保机构商品名称 */
    private String medicalCommonName;

    /** 医药机构商品编码 */
    private String medicalProductCode;

    /** 医保状态：（诊所： 0 未匹配 1 非医保/无需匹配 2 医保/已匹配）（药店： 0 未匹配 1 医保/已匹配  2 非医保/无需匹配） */
    private Integer medicalStatus;

    /** 医保信息类型，1医保药品 2诊疗项目 */
    private Integer medicalType;

    private String extend;

    /** 平台类型 saas:药店系统 clinic:门诊系统 */
    private String platformType;

    /** 数据来源：1.pos手动匹配；2.云端自动匹配；3.云端导入 */
    private Integer dataSource;

    /** 审核状态：1.未提交；2.已提交待审核；3.已提交已审核；4.拒绝通过  5.已提交无需审核*/
    private Integer approveStatus;

    /** 创建人 */
    private String createUser;

    /** 创建时间 */
    private Date createTime;

    /** 更新人 */
    private String updateUser;

    /** 更新时间 */
    private Date updateTime;

    /** 删除标志，1:有效；0删除 */
    private Integer yn;

    /** 操作版本号 */
    private Integer version;

    private String pharmacyPref;
    private String commonName;
    private String productName;
    private String unit;
    private String attributeSpecification;
    private String dosageForm;
    private String approvalNumber;
    private String manufacturer;
    private String mnemonicCode;
    private Long channelId;
    private String smallUnit;
    private BigDecimal unitConversion;

    public String getSmallUnit() {
        if(smallUnit == null){
            return "";
        }
        return smallUnit;
    }

    public void setSmallUnit(String smallUnit) {
        this.smallUnit = smallUnit;
    }

    public BigDecimal getUnitConversion() {
        return unitConversion;
    }

    public void setUnitConversion(BigDecimal unitConversion) {
        this.unitConversion = unitConversion;
    }

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
        this.organSign = organSign;
    }

    public String getProductPref() {
        return productPref;
    }

    public void setProductPref(String productPref) {
        this.productPref = productPref;
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
        if(medicalCommonName == null){
            return "";
        }
        return medicalCommonName;
    }

    public void setMedicalCommonName(String medicalCommonName) {
        this.medicalCommonName = medicalCommonName;
    }

    public String getMedicalProductCode() {
        return medicalProductCode;
    }

    public void setMedicalProductCode(String medicalProductCode) {
        this.medicalProductCode = medicalProductCode;
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
        this.extend = extend;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
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
        this.createUser = createUser;
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
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPharmacyPref() {
        if(pharmacyPref == null){
            return "";
        }
        return pharmacyPref;
    }

    public void setPharmacyPref(String pharmacyPref) {
        this.pharmacyPref = pharmacyPref;
    }

    public String getCommonName() {
        if(commonName == null){
            return "";
        }
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getProductName() {
        if(productName == null){
            return "";
        }
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnit() {
        if(unit == null){
            return "";
        }
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAttributeSpecification() {
        if(attributeSpecification == null){
            return "";
        }
        return attributeSpecification;
    }

    public void setAttributeSpecification(String attributeSpecification) {
        this.attributeSpecification = attributeSpecification;
    }

    public String getDosageForm() {
        if(dosageForm == null){
            return "";
        }
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getApprovalNumber() {
        if(approvalNumber == null){
            return "";
        }
        return approvalNumber;
    }

    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }

    public String getManufacturer() {
        if(manufacturer == null){
            return "";
        }
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMnemonicCode() {
        if(mnemonicCode == null){
            return "";
        }
        return mnemonicCode;
    }

    public void setMnemonicCode(String mnemonicCode) {
        this.mnemonicCode = mnemonicCode;
    }

    public Long getChannelId() {
        if(channelId==null){
            return -1L;
        }
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
}