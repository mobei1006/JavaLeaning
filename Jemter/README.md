### Jmter操作手册

#### 整体结构

我们首先看一下测试项目的整体架构

![image.png](https://cdn.nlark.com/yuque/0/2024/png/40783336/1716272096113-52d58c6a-20a6-4eb4-9212-09ee92794cea.png#averageHue=%233e454d&clientId=u1d21fe56-6951-4&from=paste&height=217&id=ue00cd8b1&originHeight=217&originWidth=368&originalType=binary&ratio=1&rotation=0&showTitle=false&size=14341&status=done&style=none&taskId=ud8fc6277-4962-470a-93ad-d5f056755e3&title=&width=368)

主要包含以下几个部分：

- HTTP 请求默认值
- HTTP 请求
   - JSR233 预处理程序
   - HTTP 信息头管理器
   - JSR233 后置处理器
- 查看结果树
- 聚合报告
- CSV 数据文件设置
- JSON 断言

#### 线程组
首先设置线程组，主要设置以下信息

- 线程数：我们需要测试的线程总数，比如 30.那么就是 30 个线程并发跑
- 循环次数：主要针对数据，如果数据量不够，可以循环的跑
- 调度器：针对循环时永远的，可以使用持续时间，比如压测 30 分钟

![image.png](https://cdn.nlark.com/yuque/0/2024/png/40783336/1716272654201-a2f4fb7f-568c-44bb-b189-8f7f418a4c2d.png#averageHue=%233e4143&clientId=u1d21fe56-6951-4&from=paste&height=427&id=u6c8ea212&originHeight=427&originWidth=504&originalType=binary&ratio=1&rotation=0&showTitle=false&size=11590&status=done&style=none&taskId=u815d4f5a-f3d7-42e7-9b2c-e8e7bcbed9c&title=&width=504)

#### HTTP 请求默认值
HTTP 请求发送的默认值，只需要将我们准备测试接口填写号即可

![image.png](https://cdn.nlark.com/yuque/0/2024/png/40783336/1716272721831-c8e1f55b-ac2b-4c2a-95ff-c9915c1b45ae.png#averageHue=%233f4344&clientId=u1d21fe56-6951-4&from=paste&height=249&id=u857637aa&originHeight=249&originWidth=675&originalType=binary&ratio=1&rotation=0&showTitle=false&size=10312&status=done&style=none&taskId=u3e4c294e-6c78-4047-a719-353297236ee&title=&width=675)

#### HTTP 请求
HTTP 请求主要设置请求发送的地址。注意我这里的时从文件构建的，所以下面的参数没有填写。如果是正常的参数，可以直接填写。

![image.png](https://cdn.nlark.com/yuque/0/2024/png/40783336/1716272776178-314b7c52-7d95-4419-93d7-bcd7f2e97e31.png#averageHue=%233e4244&clientId=u1d21fe56-6951-4&from=paste&height=310&id=ud7d59d92&originHeight=310&originWidth=615&originalType=binary&ratio=1&rotation=0&showTitle=false&size=15095&status=done&style=none&taskId=ud50a4908-b9cd-45f0-8264-7b9c65a2669&title=&width=615)

##### JSR233 预处理程序
这个主要是前置处理器，来完成我们对请求体的构建【从文件读取数据构建】

![image.png](https://cdn.nlark.com/yuque/0/2024/png/40783336/1716272885447-615bd666-9765-4b93-a556-148a645a0bf8.png#averageHue=%233e4244&clientId=u1d21fe56-6951-4&from=paste&height=305&id=u91e3a687&originHeight=305&originWidth=464&originalType=binary&ratio=1&rotation=0&showTitle=false&size=13019&status=done&style=none&taskId=ue06ce0c4-2e4c-481e-a0ca-135592e0701&title=&width=464)

处理的脚本代码如下：
```java
可自行编辑脚本
```
##### HTTP 信息头管理器
信息头的主要工作是发送请求为 post 的时候，设置格式为 json

![image.png](https://cdn.nlark.com/yuque/0/2024/png/40783336/1716272948866-049b3a67-4b50-49ad-a4e6-a490d1586cf6.png#averageHue=%233f4345&clientId=u1d21fe56-6951-4&from=paste&height=169&id=ube2b1854&originHeight=169&originWidth=701&originalType=binary&ratio=1&rotation=0&showTitle=false&size=5167&status=done&style=none&taskId=u9c129498-72fb-47a2-9d06-c83f72627f6&title=&width=701)

##### JSR233 后置处理器
这个是后置处理器，主要是完成我们对失败结果的记录

![image.png](https://cdn.nlark.com/yuque/0/2024/png/40783336/1716272981016-10914827-9ebf-4b6d-ae30-0c97b15355fa.png#averageHue=%233f4244&clientId=u1d21fe56-6951-4&from=paste&height=288&id=u4880f8d2&originHeight=288&originWidth=439&originalType=binary&ratio=1&rotation=0&showTitle=false&size=12134&status=done&style=none&taskId=ub3c2819a-6355-400b-85df-b255fd2aca8&title=&width=439)

脚本如下：

```java
可自行编辑脚本
```
#### CSV 数据文件设置
这个是我们配置数据源的，从 csv 文件中获取数据源，并设置好每一列参数的名称，方便我们拿到

![image.png](https://cdn.nlark.com/yuque/0/2024/png/40783336/1716273028931-409a215b-46ec-480c-8cca-01d0635d2cd9.png#averageHue=%233e4143&clientId=u1d21fe56-6951-4&from=paste&height=382&id=u22691c9f&originHeight=382&originWidth=432&originalType=binary&ratio=1&rotation=0&showTitle=false&size=16077&status=done&style=none&taskId=ue06abe1f-024b-47ed-af39-0309dc9462b&title=&width=432)

#### JSON 断言
因为我们返回的数据是 json 格式的，如下：
```java
{
	"code": 200,
	"message": "success",
	"result": [
		{
			"id": 5,
			"createTs": 1714451451672,
			"index": 1,
		}
	]
}
```
所以我们需要设置断言，code 为 200 的是正常，code 为其他不正常

![image.png](https://cdn.nlark.com/yuque/0/2024/png/40783336/1716273237679-a068fb3b-d233-4bf1-aea5-3681840df7c6.png#averageHue=%233d4143&clientId=u1d21fe56-6951-4&from=paste&height=355&id=uc3fdaccb&originHeight=355&originWidth=446&originalType=binary&ratio=1&rotation=0&showTitle=false&size=13026&status=done&style=none&taskId=u42ef27dd-47f5-4f82-bfb9-b0448a04494&title=&width=446)

#### 查看结果树
这个是查看我们的测试结果，可以看每一个请求的具体信息

![image.png](https://cdn.nlark.com/yuque/0/2024/png/40783336/1716273268955-93411aef-8ab0-4c61-82ae-dc725f03a081.png#averageHue=%232c732c&clientId=u1d21fe56-6951-4&from=paste&height=431&id=u4db97a30&originHeight=431&originWidth=596&originalType=binary&ratio=1&rotation=0&showTitle=false&size=25663&status=done&style=none&taskId=ua6263a57-a09c-40a1-bd38-02151dd8a3e&title=&width=596)

#### 聚合报告
聚合报告主要是接口测试的一些性能信息

![image.png](https://cdn.nlark.com/yuque/0/2024/png/40783336/1716273295426-f34798dc-54cb-4196-baa5-a541081db8e0.png#averageHue=%233f4245&clientId=u1d21fe56-6951-4&from=paste&height=123&id=u2b70908b&originHeight=123&originWidth=1134&originalType=binary&ratio=1&rotation=0&showTitle=false&size=11136&status=done&style=none&taskId=u4f062326-d704-48ae-b5a3-aaf50b36ece&title=&width=1134)
