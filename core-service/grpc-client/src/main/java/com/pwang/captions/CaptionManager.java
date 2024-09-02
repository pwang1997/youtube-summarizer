package com.pwang.captions;

import com.pwang.proto.MyRequest;
import com.pwang.proto.MyResponse;
import com.pwang.proto.MyServiceGrpc;
import com.pwang.proto.YoutubeCaption;
import io.grpc.StatusRuntimeException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * @author Puck Wang
 * @project core-service
 * @created 9/2/2024
 */

@Service
@Slf4j
public class CaptionManager {

  @GrpcClient("local-grpc-server")
  MyServiceGrpc.MyServiceBlockingStub blockingStub;

  public List<YoutubeCaptionDTO> getCaption(String videoId) {
    try {
      MyResponse response = blockingStub.myEndpoint(
          MyRequest.newBuilder().setVideoId(videoId).build());

      return mapToYoutubeCaptionDTO(response);
    } catch (final StatusRuntimeException e) {
      log.error(String.valueOf(e));
      return List.of();
    }
  }

  private List<YoutubeCaptionDTO> mapToYoutubeCaptionDTO(MyResponse response) {
    List<YoutubeCaptionDTO> list = new ArrayList<>();
    for (YoutubeCaption caption : response.getDataList()) {
      list.add(new YoutubeCaptionDTO(caption.getText(), caption.getStart(), caption.getDuration()));
    }
    return list;
  }
}
