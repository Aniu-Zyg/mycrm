package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {

    int saveCreateClue(Clue clue);

    List<Clue> queryClueByConditionForPage(Map<String, Object> map);

    int queryCountOfClueByCondition(Map<String, Object> map);

    Clue queryClueById(String id);

    int saveEditClue(Clue clue);

    int deleteClueByIds(String[] id);

    Clue queryClueForDetailById(String id);

    void saveConvertClue(Map<String, Object> map);

    List<String> queryClueStageOfClueGroupByClueStage();

    List<Integer> queryCountOfClueGroupByClueStage();
}
