# AnomalyDetection-Java

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=socialflat-square&)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Total Lines](https://img.shields.io/github/stars/algorithm-tools/AnomalyDetection-Java?style=socialflat-square&label=stars)](https://github.com/DataLinkDC/dinky/stargazers)
[![CN doc](https://img.shields.io/badge/文档-中文版-blue.svg?style=socialflat-square&)](README_zh_CN.md)
[![EN doc](https://img.shields.io/badge/document-English-blue.svg?style=socialflat-square&)](README.md)


## 简介
> Java 实现的异常监测算法库。

AnomalyDetection-Java是一个基于统计学、机器学习等思路实现的异常检测算法库，使用java实现。可以很方便的嵌入实际业务中，对数据序列进行多种异常类型的监测包括：离群值异常、波动异常、趋势异常等类型。

## 特性

主要包含如下特性:

- 依赖JDK环境和common-math3库，并通过JFreeChart进行图形化测试样例。
- 高扩展性：架构十分简单。开发者可以灵活高效的增加业务域异常检测规则，或者增加其他类型的算法。
- 当前支持如下监测算法：

  | 异常类型  |算法|原理| 限制      |
  |----|----|----|----|
  | 绝对值异常 |GESD|计算广义极端学生化偏差统计量寻找异常点| 适用正态分布 |
  | 绝对值异常 | Quantile |  基于分位统计算法   | 适用所有分布  |
  | 波动异常  | 2阶导+距离 |  基于二阶导数和距离寻找MBP | 拐点存在的正态、非正态分布 |
  | 趋势异常  | Mann-Kendall |  基于MannKendall检验 | 适用所有分布 |
  | 阈值异常   | 阈值规则引擎 |  基于业务阈值规则 | 适用所有分布 |


# Use case

`ADEngineTest`print result:
```text
==============Anomaly Detection Result=============
1.Anomaly detection original data:[[0, 10.0, 0], [5, 10.5, 5], [1, 12.0, 1], [2, 12.5, 2], [4, 13.0, 4], [7, 14.0, 7], [9, 14.5, 9], [8, 15.0, 8], [10, 15.5, 10], [6, 100.0, 6], [3, 133.0, 3]]
2.Overview: has 4 types anomaly detected.
3.[Anomaly: 1] [{"anomalyDetectionModel":"ADM_GESD","anomalyInfluence":0,"anomalyTrend":0,"anomalyType":1,"hasAnomaly":true,"normalRangeMax":0.0,"normalRangeMin":0.0,"seriesList":[{"logicalIndex":"3","time":3,"value":133.0},{"logicalIndex":"6","time":6,"value":100.0}]}]
3.[Anomaly: 2] [{"anomalyDetectionModel":"ADM_2ndDerivationMBP","anomalyInfluence":0,"anomalyTrend":0,"anomalyType":2,"hasAnomaly":true,"normalRangeMax":0.0,"normalRangeMin":0.0,"seriesList":[{"logicalIndex":"3","time":3,"value":133.0}]}]
3.[Anomaly: 3] [{"anomalyDetectionModel":"ADM_ManKendall","anomalyInfluence":0,"anomalyTrend":0,"anomalyType":3,"hasAnomaly":false}]
3.[Anomaly: 10] [{"anomalyDetectionModel":"ADM_Threshold","anomalyInfluence":0,"anomalyTrend":0,"anomalyType":10,"hasAnomaly":true,"normalRangeMax":19.75,"normalRangeMin":7.75,"seriesList":[{"logicalIndex":"6","time":6,"value":100.0},{"logicalIndex":"3","time":3,"value":133.0}]}]
==============Anomaly Detection Result End=========
click Enter to close window...
```

更多监测算法加入中，欢迎加入共建！