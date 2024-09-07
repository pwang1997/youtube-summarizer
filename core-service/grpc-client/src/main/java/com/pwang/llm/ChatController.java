package com.pwang.llm;

import com.pwang.captions.CaptionManager;
import com.pwang.captions.YoutubeCaptionDTO;
import com.pwang.config.PropertyConfig;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Puck Wang
 * @project core-service
 * @created 9/6/2024
 */

@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
public class ChatController {

  private final ChatClient chatClient;
  private final CaptionManager captionManager;

  public ChatController(ChatClient.Builder chatClient, CaptionManager captionManager) {
    this.chatClient = chatClient.build();
    this.captionManager = captionManager;
  }


  @GetMapping("/test")
  public String generate(@RequestParam(value = "message") String message) {
    return chatClient.prompt(new Prompt(message)).call().content();
  }

  @GetMapping("/{videoId}")
  public List<SummaryDTO> generateVideoSummary(@PathVariable(value = "videoId") String videoId) {
    List<YoutubeCaptionDTO> caption = captionManager.getCaption(videoId);

    StringBuffer messageBuffer = new StringBuffer();
    for (YoutubeCaptionDTO captionDTO : caption) {
      messageBuffer.append(captionDTO.toString());
    }

    String systemPrompt = """
        You are provided with a list of captions, each containing a text segment along with its starting time and duration.
        Your task is to generate a concise summary of the video content. For each key point in the summary,
        reference the relevant starting time.
        Ensure the summary captures the main ideas and flow of the video while omitting less important details. Format the result as a coherent paragraph,
        integrating the starting times where appropriate.
                """;
    String userPrompt = """
        Now generate video summary based on the following caption:
        """ + messageBuffer;

    return chatClient.prompt()
        .system(systemPrompt)
        .user(u -> u.text(userPrompt))
        .call()
        .entity(new ParameterizedTypeReference<List<SummaryDTO>>() {
        });
  }
}
