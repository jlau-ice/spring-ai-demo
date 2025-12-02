## 前期准备

- 本文所需的环境和平台
>jdk 21
>node 22.21.1
>ollama server (本地服务器均可)
>阿里百炼平台api-key (避免明文使用，建议加入到环境变量)
>向量数据库 redis-stack (拥有pgsql所有功能 拓展了向量存储功能)
>向量数据库 pgvector (拥有redis所有功能 拓展了向量存储功能)


- pgvector docker 快速安装
```bash
docker run -d \
  --name pgvector \
  --restart unless-stopped \
  -p 5432:5432 \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -v ./data/postgresql:/var/lib/postgresql/data \
  pgvector/pgvector:pg16
```
或者 docker compose
```yml
services:
  pgvector:
    image: pgvector/pgvector:pg16
    container_name: pgvector
    restart: unless-stopped
    ports:
      - "5432:5432"
    volumes:
      - ./data/postgresql:/var/lib/postgresql/data
      #- ./conf/postgresql.conf:/var/lib/postgresql/data/postgresql.conf
      #- ./conf/pg_hba.conf:/var/lib/postgresql/data/pg_hba.conf
    environment:
      # POSTGRES_DB: postgres
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
```

- redis-stack docker 快速安装
```bash
docker run -d \
  --name redis-stack \
  --restart unless-stopped \
  -p 6380:6379 \
  -p 18001:8001 \
  -v ./data/redis:/data \
  redis/redis-stack:latest
```
或者docker compose
```yml
services:
  redis-stack:
    image: redis/redis-stack:latest
    container_name: redis-stack
    restart: unless-stopped
    ports:
      - "6380:6379" 
      - "18001:8001" 
    volumes:
      - ./data/redis:/data
      # - ./config/redis-stack.conf:/redis-stack.conf:ro
    # command: ["redis-server", "/redis-stack.conf"]
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 10s
      timeout: 5s
      retries: 3
```


## 基础对话

### 依赖

基础对话所需依赖项目
```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--spring-ai-alibaba dashscope-->
        <dependency>
            <groupId>com.alibaba.cloud.ai</groupId>
            <artifactId>spring-ai-alibaba-starter-dashscope</artifactId>
        </dependency>
        <!--ollama-->
        <!--
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-starter-model-ollama</artifactId>
            <version>1.0.0</version>
        </dependency>
        -->
        <!--lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!--hutool-->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.8.22</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```


### 配置api key
获取api key ： [获取地址](https://bailian.console.aliyun.com/?apiKey=1&tab=api#/api)
```yml

spring:
  datasource:
    url: jdbc:postgresql://127.0.0.1:5432/postgres
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  application:
    name: chat-memory
  data:
    redis:
      host: localhost
      port: 6379
      password:
      database: 1
      connect-timeout: 3
      timeout: 3
  ai:
    # ====SpringAIAlibaba Config=============
    dashscope:
	  # 必选 配置环境变量 DASH_SCOPE_API_KEY=你的apiky
      api-key: ${DASH_SCOPE_API_KEY}
      # 可选 
      base-url: "https://dashscope.aliyuncs.com/compatible-mode/v1"
      # 可选，可在config中配置多个模型
      chat:
        options:
          model: "qwen-plus-2025-09-11"
     # ====SpringAI Config=============
    #openai:
      #api-key: ${OPENAI_API_KEY}
      #base-url: "https://api.openai.com/v1"
      #chat:
        #ptions:
	      #model: "gpt-3.5-turbo"
```


### ChatModel 方式
#### 创建ChatModel

```java
    @Bean
    public DashScopeApi dashScopeApi() {
        return DashScopeApi.builder()
                .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                .build();
    }
```

#### 基础使用
流式返回和阻塞返回 (使用可见模块 hello-server)
```java
@RestController
@RequestMapping("/chat")
public class ChatHelloController {
    @Resource
    private ChatModel chatModel;

    @GetMapping(value = "/block")
    public String doChat(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return chatModel.call(question);
    }
    @GetMapping(value = "/stream")
    public Flux<String> stream(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        return chatModel.stream(question);
    }
}
```


### ChatClient

#### 官方解释
`ChatClient` 提供了与 AI 模型通信的 Fluent API，它支持同步和反应式（Reactive）编程模型。与 `ChatModel`、`Message`、`ChatMemory` 等原子 API 相比，使用 `ChatClient` 可以将与 LLM 及其他组件交互的复杂性隐藏在背后，因为基于 LLM 的应用程序通常要多个组件协同工作（例如，提示词模板、聊天记忆、LLM Model、输出解析器、RAG 组件：嵌入模型和存储），并且通常涉及多个交互，因此协调它们会让编码变得繁琐。当然使用 `ChatModel` 等原子 API 可以为应用程序带来更多的灵活性，成本就是您需要编写大量样板代码。

ChatClient 类似于应用程序开发中的服务层，它为应用程序直接提供 `AI 服务`，开发者可以使用 ChatClient Fluent API 快速完成一整套 AI 交互流程的组装。

包括一些基础功能，如：
- 定制和组装模型的输入（Prompt）
- 格式化解析模型的输出（Structured Output）
- 调整模型交互参数（ChatOptions）

还支持更多高级功能：
- 聊天记忆（Chat Memory）
- 工具/函数调用（Function Calling）
- RAG

#### 创建ChatClinet
ChatClinet 不能被`@Resource`和`@Autowired`自动注入.
有两种注入方式
1. 构造方法注入
```java
    private final ChatClient dashScopeChatClient;
    public ChatClientController(ChatModel dashScopeChatModel) {
        this.dashScopeChatClient = ChatClient.builder(dashScopeChatModel).build();
    }
```
2. 自定义`Bean`
```java
    @Bean
    public ChatClient chatClient(ChatModel dashscopeChatModel) {
        return ChatClient.builder(dashscopeChatModel).build();
    }
```


#### 基础使用
流式返回和阻塞返回
```java
    @Resource
    private ChatClient dashScopeChatClient;
    @GetMapping("/client")
    public String chatClientBlock(@RequestParam(name = "question", defaultValue = "2加9等于几") String question) {
        return dashScopeChatClient.prompt().user(question).call().content();
    }
    @GetMapping("/client")
    public Flux<String> chatClientStream(@RequestParam(name = "question", defaultValue = "2加9等于几") String question) {
        return dashScopeChatClient.prompt().user(question).stream().content();
    }
```

### 使用本地模型(ollama)对话

#### 引入依赖

```xml
<!--ollama-->
<dependency>
	<groupId>org.springframework.ai</groupId>
	<artifactId>spring-ai-starter-model-ollama</artifactId>
	<version>1.0.0</version>
</dependency>
```

#### 编辑配置
```yml
spring:
  application:
    name: ollama
  ai:
    ollama:
      base-url: "http://192.168.187.166:11434"
      chat:
        model: "qwen3:8b"
```

#### 基础使用
ChatModel&&ChatClient 流试阻塞方式调用。
```java
    @Resource  
    private ChatModel chatModel;  
      
    public final ChatClient chatClient;  
    public OllamaController(ChatClient.Builder builder) {  
        this.chatClient = builder.build();  
    }

	// ChatModel 方式
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        // 阻塞方式
        //return chatModel.call(question);
        // 流式方式
        return chatModel.stream(question);
    }
    
    // ChatClient方式
    @GetMapping("/client")
    public Flux<String> client(@RequestParam(name = "question", defaultValue = "你是谁") String question) {
        // 阻塞方式
        //return chatClient.prompt().user(question).call().content();
        // 流式方式
        return chatClient.prompt().user(question).stream().content();
    }
```


## 记忆存储


## 提示词


## 格式化输出


## 文本向量化极其存储


## RAG增加检索


## 工具调用


## MCP




