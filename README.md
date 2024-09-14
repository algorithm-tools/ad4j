# AnomalyDetection-Java

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=socialflat-square&)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Total Lines](https://img.shields.io/github/stars/algorithm-tools/AnomalyDetection-Java?style=socialflat-square&label=stars)](https://github.com/DataLinkDC/dinky/stargazers)
[![CN doc](https://img.shields.io/badge/文档-中文版-blue.svg?style=socialflat-square&)](README_zh_CN.md)
[![EN doc](https://img.shields.io/badge/document-English-blue.svg?style=socialflat-square&)](README.md)


## Introduction
> Java implemented anomaly monitoring algorithm library.

AnomalyDetection Java is an anomaly detection algorithm library based on statistical and machine learning approaches, implemented in Java. It can be easily embedded in actual business to monitor various types of anomalies in data sequences, including outlier anomalies, fluctuation anomalies, trend anomalies, and so on.

## Feature

Its main feature are as follows:

- Depends on JDK environment and common-math3 library, and conducts graphical testing samples through JFreeChart.
- High scalability: The architecture is very simple. Developers can flexibly and efficiently add business domain anomaly detection rules, or add other types of algorithms.
- The following monitoring algorithms are currently supported:

  |Exception type | Algorithm | Principle | Limitation|
  |----|----|----|----|
  |Absolute value anomaly | GESD | Calculate generalized extreme student bias statistic to find outliers | Apply normal distribution|
  |Absolute value anomaly | Quantile | Quantile based statistical algorithm | Applicable to all distributions|
  |Abnormal fluctuations | 2nd derivative+distance | Finding MBP based on second derivative and distance | Normal and non normal distributions of inflection points|
  |Trend anomaly | Mann Kendall | Based on Mann Kendall test | Applicable to all distributions|
  |Threshold anomaly | Threshold rule engine | Based on business threshold rules | Applicable to all distributions|


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
More monitoring algorithms are being added, welcome to co build.

# Participate in Contributions

[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](https://github.com/algorithm-tools/AnomalyDetection-Java/pulls)

Welcome to join the group, build a win-win situation.

Thank you to all the people who already contributed to AnomalyDetection-Java!

[![Contributors](https://contrib.rocks/image?repo=algorithm-tools/AnomalyDetection-Java)](https://github.com/algorithm-tools/AnomalyDetection-Java/graphs/contributors)


# Get Help

- Create an issue and describe it clearly.