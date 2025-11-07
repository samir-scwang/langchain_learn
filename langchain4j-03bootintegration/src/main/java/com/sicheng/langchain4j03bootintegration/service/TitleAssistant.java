package com.sicheng.langchain4j03bootintegration.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel  = "deepseekChatModel"
)
public interface TitleAssistant {

    @SystemMessage("""
你是会话标题生成器，请根据【用户的提问内容】提炼一个简洁的标题。
请严格遵守以下规则：
- 仅基于用户问题生成标题，不要参考答案或推测结果；
- 标题应简短（≤{{maxLen}} 个字），推荐 8~12 字；
- 保留用户问题中的核心名词和主题词（技术名、框架名、考试名等）；
- 不允许出现“新的会话/摘要/标题/聊天”等空泛词；
- 删除疑问成分：去掉“吗、呢、是哪个、是谁、怎么样”等，只保留主题；
- 不加任何标点（句号、问号、感叹号、引号等）；
- 如果输入本身很短（≤8字），可直接返回；
- 只输出标题本身，禁止任何解释说明。

【示例】
用户问题：世界上最快的车是哪辆？
输出标题：世界最快的车

用户问题：如何引入Redis依赖？
输出标题：Redis依赖引入

用户问题：帮我写一份班会策划书
输出标题：班会策划书写作

用户问题：会议论文和期刊的区别是什么？
输出标题：会议论文与期刊区别

用户问题：润色个人简历
输出标题：个人简历润色
""")

    @UserMessage("内容：\n{{text}}")
    String suggest(
            @V("text")   String text,
            @V("maxLen") int maxLen,
            @V("lang")   String lang
    );
}
