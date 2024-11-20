# CommonUtils

通用工具

# 引入

## Step 1. Add the JitPack repository to your build file

* maven { url '<https://jitpack.io>' }

## Step 2. Add the dependency

* api 'com.github.shengwang520:CommonUtils:1.1.08'

## Changelog

### Version 1.1.08

* 优化权限拒绝提示显示位置适配

### Version 1.1.07

* 新增安卓15+适配通用工具
* 优化权限拒绝弹窗显示位置

### Version 1.1.06

* 修复权限库偶现内存泄露

### Version 1.1.05

* 新增bannerView生命周期绑定方法
* 新增文本添加画部分文字圆角背景

### Version 1.1.04

* 新增权限拒绝后自动显示去设置界面重新申请

### Version 1.1.03

* 新增单个文字反显，提供点击事件

### Version 1.1.02

* 新增判断是否拥有权限方法
* 新增权限拒绝后可自行实现拦截逻辑

### Version 1.1.01

* 代码结构优化，采用Ktx实现
* 新增基于BaseActivity权限请求框架
* 新增BaseFragment initListener方法
* 删除基于其他权限请求库代码

### Version 1.0.29

* 新增自动滚动banner view自定义指示器

### Version 1.0.28

* 升级编译版本为8.0+，适配安卓14+ jdk17

### Version 1.0.25

* 优化富文本显示图片逻辑，新增可传入图片大小

### Version 1.0.24

* 新增DialogFragment显示唯一弹窗

### version 1.0.23

* 修复BannerViewPager实现多点触控崩溃问题

### Version 1.0.22

* 优化沉浸式状态栏实现

### Version 1.0.21

* 调整引入库权限

### Version 1.0.20

* 新增Banner布局，在数据为1时，隐藏指示器

### Version 1.0.19

* 新增Banner布局
* 修复DialogFragment显示和隐藏偶现崩溃

### Version 1.0.18

* 新增多个字符串内容数据颜色和大小变化

### Version 1.0.17

* 新增TabLayout长按屏蔽方法
* 调整相关包引入权限

### Version 1.0.16

* 新增ViewPager2灵敏度修改方法

### Version 1.0.15

* 新增安卓10+文件转换处理

### Version 1.0.14

* 新增权限拒绝，弹出去设置界面进行开启弹窗

### Version 1.0.13

* 新增设备信息工具类

### Version 1.0.11

* 新增权限请求库
* 新增BaseFragment事件方法

### Version 1.0.10

* 优化文件命名

### Version 1.0.08

* 新增基础BaseApplication

### Version 1.0.07

* 修复DialogFragment宽度问题

### Version 1.0.06

* 新增判断activity是否存在
* DialogFragment新增方法

### Version 1.0.05

* 优化顶部代码
* 限制pop基类

### Version 1.0.04

* 添加上层框架

### Version 1.0.03

* 新增界面跳转工具

### Version 1.0.02

* 通用工具

## License

    Copyright 2021 shengwang520

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
