# AnomalyDetection-Java

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=socialflat-square&)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Total Lines](https://img.shields.io/github/stars/algorithm-tools/ad4j?style=socialflat-square&label=stars)](https://github.com/algorithm-tools/ad4j/stargazers)
[![Release](https://jitpack.io/v/algorithm-tools/ad4j.svg)](https://jitpack.io/#algorithm-tools/ad4j)
[![CN doc](https://img.shields.io/badge/文档-中文版-blue.svg?style=socialflat-square&)](README_zh_CN.md)
[![EN doc](https://img.shields.io/badge/document-English-blue.svg?style=socialflat-square&)](README.md)


## Introduction
> Java implemented anomaly monitoring algorithm library.

AnomalyDetection Java is an anomaly detection algorithm library based on statistical and machine learning approaches, implemented in Java. It can be easily embedded in actual business to monitor various types of anomalies in data sequences, including outlier anomalies, fluctuation anomalies, trend anomalies, and so on.

## Feature

Its main feature are as follows:

- Depends on JDK environment and common-math3 library, and conducts graphical testing samples through JFreeChart.
- High scalability: The architecture is very simple. Developers can flexibly and efficiently add business domain anomaly detection rules, or add other types of algorithms.
 The following monitoring algorithms are currently supported:

  |Exception type | Algorithm               | Principle | Limitation|
  |-------------------------|----|----|----|
  |Absolute value anomaly | GESD                    | Calculate generalized extreme student bias statistic to find outliers | Apply normal distribution|
  |Absolute value anomaly | Z-score                 | Z-Score based detection | Apply normal distribution|
  |Absolute value anomaly | Quantile                | Quantile based statistical algorithm | Applicable to all distributions|
  |Abnormal fluctuations | 2nd derivative+distance | Finding MBP based on second derivative and distance | Normal and non normal distributions of inflection points|
  |Trend anomaly | Mann Kendall            | Based on Mann Kendall test | Applicable to all distributions|
  |Threshold anomaly | Threshold rule engine   | Based on business threshold rules | Applicable to all distributions|

# Demos
- [ad4j-Demos](https://github.com/algorithm-tools/ad4j/tree/main/src/test/java/org/algorithmtools/example)

# For Developers
## Building ad4j
you can build ad4j using Maven by issuing the following command from the root directory of the project:
```text
mvn clean install -Dmaven.test.skip=true
```
The build requires JDK 8 or later.

## Using ad4j
- You can bring in the source code or by maven. Add to maven pom:
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.algorithm-tools</groupId>
    <artifactId>ad4j</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```
- business use:
  `BizUseExample`
```java
public class BizUseExample {

    public static void main(String[] args) {
        indicatorDetect();
    }

    public static void indicatorDetect(){
        // 1. Transfer biz data to indicator series info
        long currentTime = System.currentTimeMillis();
        List<IndicatorSeries> indicatorSeries = new ArrayList<>();
        indicatorSeries.add(new IndicatorSeries(currentTime + 1, 1d, "logicalIndex-1"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 2, 2d, "logicalIndex-2"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 3, 3d, "logicalIndex-3"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 4, 4d, "logicalIndex-4"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 5, 40d, "logicalIndex-5"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 6, 6d, "logicalIndex-6"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 7, 7d, "logicalIndex-7"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 8, 8d, "logicalIndex-8"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 9, 9d, "logicalIndex-9"));
        indicatorSeries.add(new IndicatorSeries(currentTime + 10, 10d, "logicalIndex-10"));

        IndicatorInfo info = new IndicatorInfo("Example", "Example-Name", indicatorSeries);

        // 2. New AnomalyDetectionEngine to detect
        AnomalyDetectionEngine engine = new AnomalyDetectionEngine();
        AnomalyDetectionResult detectionResult = engine.detect(info);

        // 3. Business process detect result. Like Records,Alarms,Print
        IndicatorSeriesUtil.print(detectionResult);
    }

}
```

# Test case
> ADEngineTest.java

- Indicator original data:`10.0, 12.0, 12.5, 133.0, 13.0, 10.5, 100.0, 14.0, 15.0, 14.5, 15.5`
- This is Line chart: 
  ![TestAD_Engin_LineChart](docs/pic/TestAD_Engin.png "TestAD_Engin_LineChart")
- `ADEngineTest`print result:
  ```text
  ==============Anomaly Detection Result=============
  1.Anomaly detection original data:[[0, 10.0, 0], [1, 12.0, 1], [2, 12.5, 2], [3, 133.0, 3], [4, 13.0, 4], [5, 10.5, 5], [6, 100.0, 6], [7, 14.0, 7], [8, 15.0, 8], [9, 14.5, 9], [10, 15.5, 10]]
  2.Overview: has 3 types anomaly detected.
  3.1.[Anomaly: Fluctuation Anomaly] [{"anomalyDetectionModel":"MODEL_ADM_2ndDerivationMBP","anomalyType":"TYPE_FLUCTUATION","hasAnomaly":true,"normalRangeMax":19.75,"normalRangeMin":7.75,"seriesList":[{"logicalIndex":"3","time":3,"value":133.0}]}]
  3.2.[Anomaly: Outliers Anomaly] [{"anomalyDetectionModel":"MODEL_ADM_GESD","anomalyType":"TYPE_OUTLIERS_VALUE","hasAnomaly":true,"normalRangeMax":0.0,"normalRangeMin":0.0,"seriesList":[{"logicalIndex":"3","time":3,"value":133.0},{"logicalIndex":"6","time":6,"value":100.0}]},{"anomalyDetectionModel":"MODEL_ADM_Quantile","anomalyType":"TYPE_OUTLIERS_VALUE","hasAnomaly":true,"normalRangeMax":19.75,"normalRangeMin":7.75,"seriesList":[{"$ref":"$[0].seriesList[0]"},{"$ref":"$[0].seriesList[1]"}]}]
  3.3.[Anomaly: Trend Anomaly] [{"anomalyDetectionModel":"MODEL_ADM_ManKendall","anomalyTrend":"TREND_UP","anomalyType":"TYPE_TREND","hasAnomaly":true,"normalRangeMax":0.0,"normalRangeMin":0.0}]
  ==============Anomaly Detection Result End=========
  click Enter to close window...
  ```
More monitoring algorithms are being added, welcome to co build.

# Participate in Contributions

[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](https://github.com/algorithm-tools/ad4j/pulls)

Welcome to join the community, build a win-win situation, please refer to the contribution process： [How to contribute](https://github.com/algorithm-tools/ad4j/blob/main/docs/developer_guide/Contribution_Guide.md).

Thank you to all the people who already contributed to AnomalyDetection-Java!

[![Contributors](https://contrib.rocks/image?repo=algorithm-tools/AnomalyDetection-Java)](https://github.com/algorithm-tools/ad4j/graphs/contributors)


# Get Help

- Create an issue and describe it clearly.
