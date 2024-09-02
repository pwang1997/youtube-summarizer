package com.pwang.captions;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Puck Wang
 * @project core-service
 * @created 9/2/2024
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/captions")
@Slf4j
public class CaptionController {

  private final CaptionManager captionManager;

  @GetMapping("/test")
  public List<YoutubeCaptionDTO> getCaptionTest() {
    return captionManager.getCaption("ky5ZB-mqZKM");
  }

  @GetMapping("/{videoId}")
  public List<YoutubeCaptionDTO> getCaption(@PathVariable String videoId) {
    return captionManager.getCaption(videoId);
  }
}

