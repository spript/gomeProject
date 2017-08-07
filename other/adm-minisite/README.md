广告频道
===================

## 对接文档

### H5内嵌

联系人：贾时龙

##### H5内嵌sdk

http://opg-pre.intra.gomeplus.com/jsbridge/schema.html

https://js-pre.meixincdn.com/CDN8107/src/js/mods/Gomeplus/gomeplus.js?timestamp=0.1.002

##### H5内嵌协议

http://wiki.intra.gomeplus.com/pages/viewpage.action?pageId=13305054

##### APP cookie公参说明

http://wiki.intra.gomeplus.com/pages/viewpage.action?pageId=15437458

### router

联系人：罗超

http://wiki.intra.gomeplus.com/pages/viewpage.action?pageId=13304171

#### 列表接口

开发环境：http://flight.dev.gomeplus.com/adrequest?slotId=50187&requestType=2&count=10

预生产环境：http://flight-pre.gomeplus.com/adrequest?slotId=50187&requestType=2&count=10

#### ids查询接口

开发环境：http://flight.dev.gomeplus.com/rebate?slotId=50187&requestType=2&ids=111,222

预生产环境：http://flight-pre.gomeplus.com/rebate?slotId=50187&requestType=2&ids=111,222

### shareserver

联系人：林凯

http://wiki.intra.gomeplus.com/pages/viewpage.action?pageId=22675797

##### 分享接口：

开发环境：http://flight.dev.gomeplus.com:8087/share?userId=1111&requestId=1482921762318-3128751637234825330&deviceId=111&flightJobId=1349&adId=1495&shareId=111&slotId=50186

预生产环境：http://shareserver-pre.gomeplus.com/share?userId=1111&requestId=1482921762318-3128751637234825330&deviceId=111&flightJobId=1349&adId=1495&shareId=111&slotId=50186

##### 分享曝光接口：

开发环境：http://flight.dev.gomeplus.com:8087/shareImpression?userId=1111&requestId=1482921762318-3128751637234825330&deviceId=111&flightJobId=1349&adId=1495&shareId=111&slotId=50186

预生产环境：http://shareserver-pre.gomeplus.com/shareImpression?userId=1111&requestId=1482921762318-3128751637234825330&deviceId=111&flightJobId=1349&adId=1495&shareId=111&slotId=50186

##### 返利预算状态查询接口：

开发环境：http://flight.dev.gomeplus.com:8087/rebateBudget?flightJobId=1349

预生产环境：http://shareserver-pre.gomeplus.com/rebateBudget?flightJobId=1349

### 曝光、点击

联系人：高克孟

http://wiki.intra.gomeplus.com/pages/viewpage.action?pageId=22677268

## 环境

PHP版本：7.0.8

修改php.ini文件，添加预加载文件

auto_prepend_file = "/usr/local/php/etc/prepend.php"

##### 开发环境

10.69.205.18	awall.dev.gomeplus.com

10.69.205.18	h5-awall.dev.gomeplus.com

10.69.205.18	m-awall.dev.gomeplus.com

```php
<?php
$_SERVER['ENVIRONMENT'] = 'DEVELOPMENT';
```

##### 预生产环境

10.125.192.153	awall.pre.gomeplus.com

10.125.192.153	h5-awall.pre.gomeplus.com

10.125.192.153	m-awall.pre.gomeplus.com

```php
<?php
$_SERVER['ENVIRONMENT'] = 'STAGING';
```

##### 生产环境

10.125.157.57	awall.gomeplus.com

10.125.157.58	awall.gomeplus.com


10.125.157.57	h5-awall.gomeplus.com

10.125.157.58	h5-awall.gomeplus.com


10.125.157.57	m-awall.gomeplus.com

10.125.157.58	m-awall.gomeplus.com

```php
<?php
$_SERVER['ENVIRONMENT'] = 'PRODUCTION';
```
