# 项目简介
实现北京地铁查询功能

## 北京地铁实例

![baidu_subway_map](/res/baidu_subway_map.png)

# 项目地址

[apk及算法实现](https://github.com/klchen0112/subway)

# 项目目录
<!-- TOC -->

- [项目简介](#项目简介)
    - [北京地铁实例](#北京地铁实例)
- [项目地址](#项目地址)
- [项目目录](#项目目录)
- [预期计划](#预期计划)
- [需求分析与实现](#需求分析与实现)
    - [需求分析](#需求分析)
    - [需求实现](#需求实现)
        - [Graph](#graph)
        - [ui](#ui)
    - [输入](#输入)
- [错误与反思与经验](#错误与反思与经验)

<!-- /TOC -->
# 预期计划

| PSP 2.1                               | Personal Software Process Stages      | Time   | 实际 |
| ------------------------------------- | ------------------------------------- | ------ | ---- |
| Planning                              | 计划                                  | 1h     | 1h   |
| Estimate                              | 估计这个任务需要多少时间              | 2h     | 2h   |
| Development                           | 开发                                  | 5h     | 6h   |
| Analysis                              | ·需求分析 (包括学习新技术)            | 2-3h   | 2h   |
| Design Spec                           | 生成设计文档                          | 1h     | 1h   |
| Design Review                         | 设计复审 (和同事审核设计文档)         | 1h     | 1h   |
| Coding Standard                       | 代码规范 (为目前的开发制定合适的规范) | 1h     | 1h   |
| Design                                | 具体设计                              | 1h     | 1h   |
| Coding                                | 具体编码                              | 2h     | 1h   |
| Code Review                           | 代码复审                              | 1h     | 1h   |
| Test                                  | 测试（自我测试，修改代码，提交修改）  | 2-3h   | 1h   |
| Reporting                             | 报告                                  | 1h     | 1h   |
| Test Report                           | 测试报告                              | 1h     | 1h   |
| Size Measurement                      | 计算工作量                            | 1h     | 1h   |
| Postmortem & Process Improvement Plan | 事后总结, 并提出过程改进计划          | 1h     | 1h   |
|                                       | 合计                                  | 23-25h | 22h  |

# 需求分析与实现

## 需求分析

1. 计算地铁线路最短路径
2. 支持显示地铁线路与计算换乘的程序
3. 查询指定地铁线经过的站点
4. 最少的站数从出发点到达目的地

## 需求实现

### Graph

1. 通过InputStream,InputStreamReader,HttpURLConnection;实现从云端读取json文件功能
    ```java
    final URL url = new URL("http://host:port/map.json");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setReadTimeout(5000);
    conn.setConnectTimeout(5000);
    conn.connect();
    InputStream inputStream = null;
    BufferedReader reader = null;
    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
        //获得连接的输入流
        inputStream = conn.getInputStream();
        //转换成一个加强型的buffered流
        reader = new BufferedReader(new InputStreamReader(inputStream));
        //把读到的内容赋值给result
        JSONReader jsonReader = new JSONReader(reader);
        file = ((JSONObject) jsonReader.readObject());
        reader.close();
        inputStream.close();
        conn.disconnect();
    }   
    ```
2. 实现json文件到java对象处理
    1. 将站点名与实际顶点通过hashmap对应，建立双向映射
        ```java
        private HashMap < String, Integer > stationToVertical;
        private HashMap < Integer, String > verticalToStation;
        ```
    2. 将线路与站点名与通过hashmap对应
        ```java
        private HashMap<String,List<String>> lineStations;
        ```
    3. 接口
        ```java
        //得到线路列表
        public String[] getLines();
        //得到对应线路所有站点
        public ArrayList<String> getStationByLine(String linename);
        //路线预处理
        public void dijstra(int s)；
        ```
3. 算法实现
   1. 实现了WeightedEdge类，通过tag标注所属线路
        ```java
        package com.klchen.subway.Graph;
        public class WeightedEdge {
            private int to; private String tag; private int weight;

            public int getTo() {
                return to;
            }

            public String getTag() {
                return tag;
            }

            public int getWeight() {
                return weight;
            }

            public WeightedEdge(int to, String tag, int weight) {
                this.to = to;
                this.tag = tag;
                this.weight = weight;
            }
        }
        ```
    1. 实现WeightedGraph类
       1. 接口说明
            ```java
            //加入单向边
            public void addedge(int from,int to,String tag,int weight);
            //创建有N个订点的图
            public WeightedGraph(int N);
            //路线预处理
            public void dijstra(int s);
            //得到前向边，以及点
            public Pair<ArrayList<WeightedEdge>,ArrayList<Integer> > getPreEdge(int S,int T);
            ```
        2. 变量说明
           ```java
            private int N;
            //邻接表图
            private List<List<WeightedEdge>> Graph;
            // 前向边
            private WeightedEdge[] preEdge;
            // 前向点
            private Integer[] preV;
            // 距离·
            private Integer[] dist; 
           ```
         

### ui

1. 预览
   
![view1][view1]

2. 调用dealgraph
3. 绑定对应控件并实现级联
4. 实际效果

![view2][view2]



## 输入
1. 地图格式

```json

//
{
    {
        "subway-line-name" : "一号线",
        "stations" : ["苹果园","古城"]
    },
    {
        "subway-line-name" : "二号线",
        "stations" : ["积水潭","鼓楼大街"]
    }
}
```


2. 输出格式

```
站点 -> 站点（几号线到几号线） -> 站点

```

# 错误与反思与经验

1. android apk 高版本不支持使用http请求
2. 计划不周全修改了之前所打算用的计划
3. 代码没有设计后在编写，重复修改了代码的结构
4. 技术栈不足，缺少一些技术导致花了较多的时间

[view1]:/res/view1.png
[view2]:/res/view2.png