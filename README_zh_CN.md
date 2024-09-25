# AnomalyDetection-Java

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=socialflat-square&)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Total Lines](https://img.shields.io/github/stars/algorithm-tools/ad4j?style=socialflat-square&label=stars)](https://github.com/algorithm-tools/ad4j/stargazers)
[![CN doc](https://img.shields.io/badge/文档-中文版-blue.svg?style=socialflat-square&)](README_zh_CN.md)
[![EN doc](https://img.shields.io/badge/document-English-blue.svg?style=socialflat-square&)](README.md)


## 简介
> Java 实现的异常监测算法库。

AnomalyDetection-Java是一个基于统计学、机器学习等思路实现的异常检测算法库，使用java实现。可以很方便的嵌入实际业务中，对数据序列进行多种异常类型的监测包括：离群值异常、波动异常、趋势异常等类型。

## 特性

主要包含如下特性:

- 依赖JDK环境和common-math3库，并通过JFreeChart进行图形化测试样例。
- 高扩展性：架构十分简单。开发者可以灵活高效的增加业务域异常检测规则，或者增加其他类型的算法。
 当前支持如下监测算法：

  | 异常类型  | 算法           | 原理                  | 限制             |
  |--------------|---------------------|----------------|----|
  | 绝对值异常 | GESD         | 计算广义极端学生化偏差统计量寻找异常点 | 适用正态分布         |
  | 绝对值异常 | Z-score      | 基于Z-score算法         | 适用正态分布、异常点较少情况 |
  | 绝对值异常 | Quantile     | 基于分位统计算法            | 适用所有分布         |
  | 波动异常  | 2阶导+距离       | 基于二阶导数和距离寻找MBP      | 拐点存在的正态、非正态分布  |
  | 趋势异常  | Mann-Kendall | 基于MannKendall检验     | 适用所有分布         |
  | 阈值异常   | 阈值规则引擎       | 基于业务阈值规则            | 适用所有分布         |

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
- add to maven pom:
```xml
<dependency>
    <groupId>org.algorithmtools</groupId>
    <artifactId>ad4j</artifactId>
    <version>${version}</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/ad4j-${version}.jar</systemPath>
</dependency>

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

更多监测算法加入中，欢迎加入共建！

# 参与贡献

[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](https://github.com/algorithm-tools/ad4j/pulls)

欢迎加入共建共赢， 贡献流程请参考：[参与贡献](https://github.com/algorithm-tools/ad4j/blob/main/docs/developer_guide/Contribution_Guide_zh_CN.md).

感谢所有做出贡献的人！

[![Contributors](https://contrib.rocks/image?repo=algorithm-tools/AnomalyDetection-Java)](https://github.com/algorithm-tools/ad4j/graphs/contributors)


# 获得帮助

- 创建 issue，并描述清晰