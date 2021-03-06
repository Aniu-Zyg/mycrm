package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Mon Jun 13 22:06:07 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Mon Jun 13 22:06:07 CST 2022
     */
    int insert(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Mon Jun 13 22:06:07 CST 2022
     */
    int insertSelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Mon Jun 13 22:06:07 CST 2022
     */
    ClueRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Mon Jun 13 22:06:07 CST 2022
     */
    int updateByPrimaryKeySelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Mon Jun 13 22:06:07 CST 2022
     */
    int updateByPrimaryKey(ClueRemark record);

    /**
     * 根据clueId查询该线索下所有的备注
     */
    List<ClueRemark> selectClueRemarkForDetailByClueId(String clueId);

    /**
     * 保存创建的线索备注
     */
    int insertClueRemark(ClueRemark remark);

    /**
     * 根据id删除线索备注
     */
    int deleteClueRemarkById(String id);

    /**
     * 保存修改的线索备注
     */
    int updateClueRemark(ClueRemark remark);

    /**
     * 根据clueId查询该线索下所有的备注信息，以用于转换线索
     */
    List<ClueRemark> selectClueRemarkByClueId(String clueId);

    /**
     * 根据clueId删除该线索下所有的备注信息
     */
    int deleteClueRemarkByClueId(String clueId);
}