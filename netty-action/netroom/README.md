# 学习《Netty In Action》

## 章节12 使用netty制作一个在线聊天室服务端程序

#### 从标准的HTTP或HTTPS切换到WebSocket是通过一种 `升级握手` 的机制，因此，使用WebSocket将始终以HTTP/S作为开始，然后再执行升级。

#### 在本DEMO中，约定访问 /ws 结尾的url时，升级协议到WebSocket 之后通过WebSocket协议进行消息发送


## WebSocket帧：websocket以帧的方式传输数据，每一帧代表消息的一部分 

> IETF发布的WebSocket RFC, 定义了6种帧 Netty为他们都提供了POJO实现

```
BinaryWebSocketFrame : 包含了二进制数据
TextWebSocketFrame: 包含了文本数据
ContinuationWebSocketFrame: 包含了属于上一个Text或者Binary的文本数据或二进制数据
CloseWebSocketFrame: 标识一个CLOSE请求 包括关闭状态码和原因
PingWebSocketFrame: 请求一个PongWebSocketFrame
PongWebSocketFrama: 作为ping的回应
```


