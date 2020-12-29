package wlw.zc.demo.system.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import wlw.zc.demo.system.entity.SaasMedicalProduct;

import java.util.List;

@Mapper
public interface SaasMedicalProductMapper {
    Integer insertProductMatching(@Param("list") List<SaasMedicalProduct> saasMedicalProducts);

    Integer deleteProductMatching(@Param("ids") List<String> ids, @Param("organSign") String organSign);

    Integer deleteProductMatchingByProductIds(@Param("ids") List<Long> ids, @Param("organSign") String organSign);

    Integer updateProductMatching(@Param("list") List<SaasMedicalProduct> saasMedicalProducts);

    List<SaasMedicalProduct> syncIncrementData(@Param("ids") List<Long> ids, @Param("organSign") String organSign);

    List<SaasMedicalProduct> listDataByIds(@Param("ids") List<Long> ids);

    List<SaasMedicalProduct> syncTotalPageData(String organSign);

    List<SaasMedicalProduct> selectMedicalProductByCondition(SaasMedicalProduct product);

    SaasMedicalProduct selectMedicalProduct(@Param("productId") Long productId, @Param("organSign") String organSign, @Param("channelId") Long channelId);

    Integer updateMedicalProduct(SaasMedicalProduct medicalProduct);

    Integer insertMedicalProduct(SaasMedicalProduct medicalProduct);

    Integer selectMedicalProductCountByCondition(SaasMedicalProduct product);

    int countProductByOrgansign(String organSign);

    List<SaasMedicalProduct> selectTotalPageData(@Param("organSign") String organSign, @Param("pageNum") int pageNum, @Param("pageSize") Integer pageSize);

    Integer deleteByOrganSign(String organSign);

    void deleteProductMatchingByMedicalCode(@Param("deleteCodeList") List<String> deleteCodeList, @Param("organSign") String organSign);

    List<SaasMedicalProduct> selectMedicalProducts(@Param("productIds") List<Long> productIds, @Param("organSign") String organSign, @Param("channelId") Long channelId);

    Integer selectMedicalProductCount(@Param("organSign") String organSign, @Param("channelId") Long channelId);

    void flushMedicalProductChannelId(@Param("organSign") String organSign, @Param("channelId") Long channelId);

    List<SaasMedicalProduct> selectMedicalProductByParam(@Param("organSign") String organSign, @Param("medicalStatus") Integer medicalStatus, @Param("list") List<String> productPref, @Param("channelId") Long channelId);

    List<SaasMedicalProduct> selectMedicalProductByProductPref(@Param("productPrefs") List<String> productPrefs, @Param("organSign") String organSign, @Param("channelId") Long channelId);
}